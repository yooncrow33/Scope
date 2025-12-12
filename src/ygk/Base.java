package ygk;

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

    protected ViewMetrics viewMetrics;

    private TickManager tickManager = null;
    private SystemMonitor systemMonitor = null;
    private AfterImageManager afterImageManager = null;

    private final List<Runnable> updatables = new ArrayList<>();
    private final List<Consumer<Graphics>> renderables = new ArrayList<>();

    public Base(String title) {
        frame = new JFrame(title + " (Powered by Yooncrow Game Kit)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        setFocusable(true);

        frame.setPreferredSize((new Dimension(1280,720)));

        viewMetrics = new ViewMetrics(this);

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
                viewMetrics.updateVirtualMouse(e.getX(),e.getY());
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

                    viewMetrics.calculateViewMetrics();
                    EventQueue.invokeLater(() -> isResizing = false);
                }
            }
        });

        tickManager = new TickManager();
        registerUpdatable(tickManager::update);

        viewMetrics.calculateViewMetrics();


        startGameLoop();
        SwingUtilities.invokeLater(() -> {
            // 모든 UI 이벤트 처리 후 (가장 안정적일 때)
            this.setFocusable(true);
            this.requestFocusInWindow();
            this.setFocusable(true);
            this.requestFocus();
            this.requestFocusInWindow();

            // 포커스 확보 (재차 요청)
            this.initGame();             // <-- 여기에 initGame을 호출
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
    protected abstract void initGame();
    protected abstract void render(Graphics g);
    protected void addKeyAdapter(KeyListener kd) { this.addKeyListener(kd);
        System.out.println("qnxdma");}

    public final int getMouseX() { return viewMetrics.getVirtualMouseX(); }
    public final int getMouseY() { return viewMetrics.getVirtualMouseY(); }
    public final double getScaleX() { return viewMetrics.getScaleX(); }
    public final double getScaleY() { return viewMetrics.getScaleY(); }
    public final int getWindowHeight() { return viewMetrics.getWindowHeight(); }
    public final int getWindowWidth() { return viewMetrics.getWindowWidth(); }

    protected void registerUpdatable(Runnable updateLogic) {
        this.updatables.add(updateLogic);
    }

    protected void registerRenderable(Consumer<Graphics> renderLogic) {
        this.renderables.add(renderLogic);
    }

    protected SystemMonitor getSystemMonitor() {
        if (systemMonitor == null) {
            systemMonitor = new SystemMonitor();
            // Base의 자동 업데이트 루프에 등록
            registerUpdatable(systemMonitor::update);
        }
        return systemMonitor;
    }

    protected AfterImageManager getAfterImageManager() {
        if (afterImageManager == null) {
            afterImageManager = new AfterImageManager();
            // Base의 자동 업데이트/렌더 루프에 등록
            registerUpdatable(afterImageManager::update);
            registerRenderable(afterImageManager::draw);
        }
        return afterImageManager;
    }

    protected TickManager getTickManager() {
        return tickManager;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D d2 = (Graphics2D) g;

        viewMetrics.calculateViewMetrics();

        d2.translate(viewMetrics.getCurrentXOffset(), viewMetrics.getCurrentYOffset());
        d2.scale(viewMetrics.getCurrentScale(), viewMetrics.getCurrentScale());

        for (Consumer<Graphics> drawFunction : renderables) {
            drawFunction.accept(g); // Graphics 객체 'g'를 전달하며 실행!
        }

        render(g);

        g.setColor(Color.black);
        g.fillRect(-500,1060,3920,200);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.white);
        g.drawString("Powered by Yooncrow Game Kit          Version = Alpha 1.3.4       2025.12.12", 10 , 1075);
    }

    @Override public int getComponentWidth() { return this.getWidth(); }
    @Override public int getComponentHeight() { return this.getHeight(); }
}


