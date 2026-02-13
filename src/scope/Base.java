package scope;

import scope.internal.facade.Access.ScopeEngineAccess;
import scope.internal.facade.ScopeEngine;
import scope.internal.viewMetrics.IFrameSize;
import scope.internal.viewMetrics.ViewMetrics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Base extends JPanel implements IFrameSize {

    private JFrame frame;

    private boolean isResizing = false;
    private long lastTime;

    private boolean runnung = false;

    protected ViewMetrics ViewMetrics;

    private final ScopeEngine scopeEngine;
    private ScopeEngineAccess scopeEngineAccess;

    public Base(String title) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        setFocusable(true);

        frame.setPreferredSize((new Dimension(1280,720)));

        ViewMetrics = new ViewMetrics(this);

        scopeEngine = new ScopeEngine();

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
                ViewMetrics.updateVirtualMouse(e.getX(),e.getY());
                click();
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

                    ViewMetrics.calculateViewMetrics();
                    EventQueue.invokeLater(() -> isResizing = false);
                }
            }
        });

        ViewMetrics.calculateViewMetrics();

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
        runnung = true;
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        lastTime = System.nanoTime();

        executor.scheduleAtFixedRate(() -> {
            if (!runnung) { executor.shutdown(); return; }
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1_000_000_000.0; // 초 단위
            lastTime = now;

            SwingUtilities.invokeLater(this::repaint);

            scopeEngine.updateAll();

            update(deltaTime);


        }, 0, 16, TimeUnit.MILLISECONDS);
    }

    protected abstract void update(double deltaTime);
    protected abstract void init();
    protected abstract void render(Graphics g);
    protected void click() {}
    protected final void exit() { runnung = false; }

    public final int getMouseX() { return ViewMetrics.getVirtualMouseX(); }
    public final int getMouseY() { return ViewMetrics.getVirtualMouseY(); }
    public final double getScaleX() { return ViewMetrics.getScaleX(); }
    public final double getScaleY() { return ViewMetrics.getScaleY(); }
    public final int getWindowHeight() { return ViewMetrics.getWindowHeight(); }
    public final int getWindowWidth() { return ViewMetrics.getWindowWidth(); }

    protected final String getVersion() { return "Scope v1.5.3-alpha"; }
    protected final String getBuildDate() { return "2026-2-13"; }
    protected final String getDeveloper() { return "yooncrow33"; }
    protected final String getRepository() { return "yooncrow33/Scope"; }
    protected final String getTitle() { return "____________________________________________________\n" +
            "  ____                             \n" +
            " / ___|  ___ ___  _ __   ___       Version: 1.5.3-alpha\n" +
            " \\___ \\ / __/ _ \\| '_ \\ / _ \\      Build: 2026-02-13\n" +
            "  ___) | (_| (_) | |_) |  __/      Dev: yooncrow33\n" +
            " |____/ \\___\\___/| .__/ \\___|      Framework: Scope\n" +
            "                 |_|               \n" +
            "____________________________________________________"; }

    public ScopeEngineAccess scopeEngine() {
        return (scopeEngineAccess = scopeEngine);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D d2 = (Graphics2D) g;

        ViewMetrics.calculateViewMetrics();

        d2.translate(ViewMetrics.getCurrentXOffset(), ViewMetrics.getCurrentYOffset());
        d2.scale(ViewMetrics.getCurrentScale(), ViewMetrics.getCurrentScale());

        scopeEngine.renderAll(g);

        render(g);
    }

    @Override public int getComponentWidth() { return this.getWidth(); }
    @Override public int getComponentHeight() { return this.getHeight(); }
}


