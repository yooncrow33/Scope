package scope;

import scope.internal.effect.afterImage.AfterImageAccess;
import scope.internal.effect.afterImage.AfterImageManager;
import scope.internal.effect.popup.PopupAccess;
import scope.internal.effect.popup.PopupManager;
import scope.internal.systemMonitor.SystemMonitor;
import scope.internal.tick.TickManager;
import scope.internal.viewMetrics.IFrameSize;
import scope.internal.viewMetrics.ViewMetrics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.lang.Runnable;
import java.util.function.Consumer;

public abstract class Base extends JPanel implements IFrameSize {

    protected JFrame frame;

    private boolean isResizing = false;
    private long lastTime;

    protected ViewMetrics a010ViewMetrics;

    private TickManager TickManager = null;
    private SystemMonitor a000SystemMonitor = null;
    private AfterImageManager e000AfterImageManager = null;
    private AfterImageAccess e009AfterImageAccess;
    private PopupManager popupManager = null;
    private PopupAccess popupAccess;

    private final List<Runnable> updatables = new ArrayList<>();
    private final List<Consumer<Graphics>> renderables = new ArrayList<>();

    public Base(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        setFocusable(true);

        frame.setPreferredSize((new Dimension(1280,720)));

        a010ViewMetrics = new ViewMetrics(this);

        frame.add(this);
        frame.setVisible(true);
        frame.setFocusable(false);

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();

        frame.pack();

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                a010ViewMetrics.updateVirtualMouse(e.getX(),e.getY());
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (isResizing) return;

                int currentW = frame.getWidth();
                int currentH = frame.getHeight();

                double ratio = (double) currentW / currentH;
                double targetRatio = 16.0 / 9.0;

                if (Math.abs(ratio - targetRatio) > 0.05) {

                    isResizing = true;

                    int newH = (int) (currentW / targetRatio);
                    int newW = (int) (currentH * targetRatio);

                    if (Math.abs(currentW - newW) > Math.abs(currentH - newH)) {
                        frame.setSize(newW, currentH);
                    } else {
                        frame.setSize(currentW, newH);
                    }

                    a010ViewMetrics.calculateViewMetrics();
                    EventQueue.invokeLater(() -> isResizing = false);
                }
            }
        });

        TickManager = new TickManager();
        registerUpdatable(TickManager::update);

        a010ViewMetrics.calculateViewMetrics();

        SwingUtilities.invokeLater(() -> {
            // 모든 UI 이벤트 처리 후 (가장 안정적일 때)
            this.setFocusable(true);
            this.requestFocusInWindow();
            this.setFocusable(true);
            this.requestFocus();
            this.requestFocusInWindow();

            init();

            startGameLoop();
        });
    }

    private void startGameLoop() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        lastTime = System.nanoTime();

        executor.scheduleAtFixedRate(() -> {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1_000_000_000.0; // 초 단위
            lastTime = now;

            SwingUtilities.invokeLater(this::repaint);

            update(deltaTime);

            for (Runnable updatable : updatables) {
                updatable.run();
            }

        }, 0, 16, TimeUnit.MILLISECONDS);
    }

    protected abstract void update(double deltaTime);
    protected abstract void init();
    protected abstract void render(Graphics g);

    public final int getMouseX() { return a010ViewMetrics.getVirtualMouseX(); }
    public final int getMouseY() { return a010ViewMetrics.getVirtualMouseY(); }
    public final double getScaleX() { return a010ViewMetrics.getScaleX(); }
    public final double getScaleY() { return a010ViewMetrics.getScaleY(); }
    public final int getWindowHeight() { return a010ViewMetrics.getWindowHeight(); }
    public final int getWindowWidth() { return a010ViewMetrics.getWindowWidth(); }

    protected void registerUpdatable(Runnable updateLogic) {
        this.updatables.add(updateLogic);
    }

    protected void registerRenderable(Consumer<Graphics> renderLogic) {
        this.renderables.add(renderLogic);
    }

    protected SystemMonitor getSystemMonitor() {
        if (a000SystemMonitor == null) {
            a000SystemMonitor = new SystemMonitor();
            // Base의 자동 업데이트 루프에 등록
            registerUpdatable(a000SystemMonitor::update);
        }
        return a000SystemMonitor;
    }

    protected PopupAccess getPopupManager() {
        if (popupManager == null) {
            popupManager = new PopupManager(getTickManager());
            // Base의 자동 업데이트/렌더 루프에 등록
            registerUpdatable(popupManager::update);
            registerRenderable(popupManager::draw);
            popupAccess = popupManager;
        }
        return popupAccess;
    }

    protected AfterImageAccess getAfterImageManager() {
        if (e000AfterImageManager == null) {
            e000AfterImageManager = new AfterImageManager();
            // Base의 자동 업데이트/렌더 루프에 등록
            registerUpdatable(e000AfterImageManager::update);
            registerRenderable(e000AfterImageManager::draw);
            e009AfterImageAccess = e000AfterImageManager;
        }
        return e009AfterImageAccess;
    }


    protected TickManager getTickManager() {
        return TickManager;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D d2 = (Graphics2D) g;

        a010ViewMetrics.calculateViewMetrics();

        d2.translate(a010ViewMetrics.getCurrentXOffset(), a010ViewMetrics.getCurrentYOffset());
        d2.scale(a010ViewMetrics.getCurrentScale(), a010ViewMetrics.getCurrentScale());

        for (Consumer<Graphics> drawFunction : renderables) {
            drawFunction.accept(g); // Graphics 객체 'g'를 전달하며 실행!
        }

        render(g);

        g.setColor(Color.black);
        g.fillRect(-500,1060,3920,200);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.white);
        g.drawString("Powered by Scope          Version = Alpha 1.4.0       2026.1.13", 10 , 1075);
    }

    @Override public int getComponentWidth() { return this.getWidth(); }
    @Override public int getComponentHeight() { return this.getHeight(); }
}


