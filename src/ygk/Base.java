package ygk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.lang.Runnable;

public abstract class Base extends JPanel implements IFrameSize {

    protected JFrame frame;

    private boolean isResizing = false;
    private long lastTime;

    protected ViewMetrics viewMetrics;

    public long tick;
    int lastPlayTime;
    int totalPlayTime = 0;
    int sessionPlayTime = 0;

    private final List<Runnable> updatables = new ArrayList<>();

    public Base(String title) {
        frame = new JFrame(title + "(Powered by Yooncrow Game Kit)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        frame.setPreferredSize((new Dimension(1280,720)));
        frame.setFocusable(true);

        viewMetrics = new ViewMetrics(this);

        frame.add(this);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocus();
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

        initGame();

        viewMetrics.calculateViewMetrics();

        startGameLoop();
    }

    private void startGameLoop() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        lastTime = System.nanoTime();

        executor.scheduleAtFixedRate(() -> {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1_000_000_000.0; // 초 단위
            lastTime = now;

            update(deltaTime);
            SwingUtilities.invokeLater(this::repaint);

            for (Runnable updatable : updatables) {
                updatable.run();
            }

        }, 0, 16, TimeUnit.MILLISECONDS);
    }

    protected abstract void update(double deltaTime);
    protected abstract void initGame();
    protected abstract void render(Graphics g);

    public final int getMouseX() { return viewMetrics.getVirtualMouseX(); }
    public final int getMouseY() { return viewMetrics.getVirtualMouseY(); }
    public final double getScaleX() { return viewMetrics.getScaleX(); }
    public final double getScaleY() { return viewMetrics.getScaleY(); }
    public final int getWindowHeight() { return viewMetrics.getWindowHeight(); }
    public final int getWindowWidth() { return viewMetrics.getWindowWidth(); }

    protected void registerUpdatable(Runnable updateLogic) {
        this.updatables.add(updateLogic);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D d2 = (Graphics2D) g;

        viewMetrics.calculateViewMetrics();

        d2.translate(viewMetrics.getCurrentXOffset(), viewMetrics.getCurrentYOffset());
        d2.scale(viewMetrics.getCurrentScale(), viewMetrics.getCurrentScale());

        render(g);

        g.setColor(Color.black);
        g.fillRect(-500,1060,3920,200);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.white);
        g.drawString("Powered by Yooncrow Game Kit          Version = Alpha 1.0", 10 , 1075);
    }

    @Override public int getComponentWidth() { return this.getWidth(); }
    @Override public int getComponentHeight() { return this.getHeight(); }
}


