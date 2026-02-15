package scope;

import scope.internal.facade.Access.ScopeEngineAccess;
import scope.sideScroll.Camera;
import scope.sideScroll.Entity;
import scope.sideScroll.HudEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public abstract class SideScrollBase extends Base {
    final int WORLD_WIDTH;
    final int WORLD_HEIGHT;

    public SideScrollBase(String title, int worldWidth, int worldHeight) {
        super(title);
        this.WORLD_WIDTH = worldWidth;
        this.WORLD_HEIGHT = worldHeight;
        WORLD_HALF_WIDTH = WORLD_WIDTH / 2.0;
        MIN_X = -WORLD_HALF_WIDTH;
        MAN_X = WORLD_HALF_WIDTH;

        WORLD_HALF_HEIGHT = WORLD_HEIGHT / 2.0;
        MIN_Y = -WORLD_HALF_HEIGHT;
        MAX_Y = WORLD_HALF_HEIGHT;
    }

    final double cullingDistanceWidth = 2000;
    final double cullingDistanceHeight = 1000;

    final int VIRTUAL_X_SCREEN_CENTER = 960;
    final int VIRTUAL_Y_SCREEN_CENTER = 540;

    final double WORLD_HALF_WIDTH;
    public final double MIN_X;
    public final double MAN_X;

    final double WORLD_HALF_HEIGHT;
    public final double MIN_Y;
    public final double MAX_Y;

    protected abstract void update(double deltaTime);
    protected abstract void init();
    protected abstract void render(Graphics g);
    protected void clickEvent() {}
    protected final void exit() { internalExit(); }

    private boolean isEntitiesNeedSort = false;
    private boolean isHudEntityNeedSort = false;
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<HudEntity> hudEntities = new ArrayList<>();

    private final Camera camera = new Camera();

    private boolean hitBoxRender = false;

    protected void addEntity(Entity e) {
        entities.add(e);
        isEntitiesNeedSort = true;
    }
    protected void addHudEntity(HudEntity e) {
        hudEntities.add(e);
        isHudEntityNeedSort = true;
    }

    @Override
    protected final void internalInit() {
        init();
    }

    @Override
    protected final void internalUpdate(double dt) {
        update(dt);
        //internal update
        for (int i = entities.size() - 1; i >= 0; i--) {
            Entity e = entities.get(i);
            e.update(dt);
            e.checkBound(this);
            if (e.isRemove()) {
                entities.remove(i);
            }
        }
        for (int i = hudEntities.size() - 1; i >= 0; i--) {
            HudEntity e = hudEntities.get(i);
            e.update(dt);
            if (e.isRemove()) {
                hudEntities.remove(i);
            }
        }
        if (isEntitiesNeedSort) { entities.sort(Comparator.comparingInt(Entity::getLayer)); isEntitiesNeedSort = false; }
        if (isHudEntityNeedSort) { hudEntities.sort(Comparator.comparingInt(HudEntity::getLayer)); isHudEntityNeedSort = false; }
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (!e.isCollisionEnabled()) continue;

            for (int j = i + 1; j < entities.size(); j++) { // i + 1부터 시작하는 게 핵심!
                Entity e2 = entities.get(j);
                if (e2.isCollisionEnabled()) {
                    resolveCollision(e, e2);
                }
            }
        }
    }

    @Override
    protected final void internalRender(Graphics g) {
        for (Entity e : entities) {
            double relativeX = e.getX() - camera.getX();
            double relativeY = e.getY() - camera.getY();

            if (cullingDistanceWidth > relativeX) {
                if (-1.0 * cullingDistanceWidth < relativeX) {
                    if (cullingDistanceHeight > relativeY) {
                        if (-1.0 * cullingDistanceHeight < relativeY) {
                            e.render(g,e.getX() - camera.getX() + VIRTUAL_X_SCREEN_CENTER,e.getY() - camera.getY() + VIRTUAL_Y_SCREEN_CENTER);
                            e.renderHitbox(g);
                        }
                    }
                }
            }
        }
        render(g);
        for (HudEntity e : hudEntities) {
            e.render(g);
        }
    }

    @Override
    protected void internalClick() {
        clickEvent();
    }

    public ScopeEngineAccess scopeEngine() {
        return super.scopeEngine();
    }

    public void resolveCollision(Entity a, Entity b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double distSq = dx * dx + dy * dy;
        double radiusSum = a.getRadius() + b.getRadius();

        // 제곱 비교로 먼저 충돌 여부 확인 (루트 연산 아끼기)
        if (distSq < radiusSum * radiusSum) {
            double distance = Math.sqrt(distSq); // 여기서만 어쩔 수 없이 루트 한 번!
            if (distance == 0) {
                distance = 0.01; // 임의의 작은 값으로 보정하여 튕겨내기
            }
            double overlap = radiusSum - distance;

            // 밀어낼 방향 벡터 (단위 벡터)
            double nx = dx / distance;
            double ny = dy / distance;

            // 서로 반대 방향으로 밀어내기 (0.5씩)
            // 만약 한쪽이 고정된 벽이라면 0.5 대신 1.0을 곱하면 됨!
            a.addX(-nx * overlap * 0.5);
            a.addY(-ny * overlap * 0.5);
            b.addX(+nx * overlap * 0.5);
            b.addY(+ny * overlap * 0.5);
        }
    }

    public void setHitBoxRender(boolean b) {hitBoxRender = b;}

    public Camera getCamera() {return camera;}
}
