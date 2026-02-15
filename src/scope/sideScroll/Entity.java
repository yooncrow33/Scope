package scope.sideScroll;

import scope.SideScrollBase;

import java.awt.*;

public abstract class Entity {
    boolean isRemove = false;
    final int radius;
    double x = 0;
    double y = 0;
    final int layer;
    final boolean isCollisionEnabled;
    final boolean isUpdateEnabled;
    final Color hitBoxColor = new Color(130,225,255,155);
    public Entity(int radius,double x,double y, boolean isCollisionEnabled,boolean isUpdateEnabled, int layer) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.isCollisionEnabled = isCollisionEnabled;
        this.isUpdateEnabled = isUpdateEnabled;
        this.layer = layer;
    }

    public boolean isCollisionEnabled() {
        return isCollisionEnabled;
    }

    public abstract void render(Graphics g,double x,double y);
    public abstract void update(double dt);

    public int getLayer() {
        return layer;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getRadius() {
        return radius;
    }
    public boolean isRemove() {
        return isRemove;
    }
    public void addX(double value) { x += value;}
    public void addY(double value) { y += value;}

    public void checkBound(SideScrollBase ssBase) {
        if (x > ssBase.MAX_X) {
            x = ssBase.MAX_X;
        } else if (x < ssBase.MIN_X) {
            x = ssBase.MIN_X;
        }

        if (y > ssBase.MAX_Y) {
            y = ssBase.MAX_Y;
        } else if (y < ssBase.MIN_Y) {
            y = ssBase.MIN_Y;
        }
    }

    protected void remove() {
        isRemove = true;
    }

    public void renderHitbox(Graphics g,double x,double y) {
        g.setColor(hitBoxColor);
        g.fillOval((int)x, (int)y, radius * 2, radius * 2); // 왼쪽 위 기준으로 원을 그림
    }
}
