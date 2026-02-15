package scope.sideScroll;

import java.awt.*;

public abstract class HudEntity {
    boolean isRemove = false;
    public double x,y = 0;
    final int layer;
    final boolean isUpdateEnabled;
    public HudEntity(double x,double y,boolean isUpdateEnabled, int layer) {
        this.x = x;
        this.y = y;
        this.isUpdateEnabled = isUpdateEnabled;
        this.layer = layer;
    }

    public abstract void render(Graphics g);
    public abstract void update(double dt);

    public int getLayer() {
        return layer;
    }

    protected void remove() {
        isRemove = true;
    }
    public boolean isRemove() {
        return isRemove;
    }
}
