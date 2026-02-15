package scope;

import scope.internal.facade.Access.ScopeEngineAccess;

import java.awt.*;

public abstract class EmptyBase extends Base {
    public EmptyBase(String title) { super(title); }

    protected abstract void update(double deltaTime);
    protected abstract void init();
    protected abstract void render(Graphics g);
    protected void clickEvent() {}
    protected final void exit() { internalExit(); }

    @Override
    protected final void internalInit() {
        init();
    }

    @Override
    protected final void internalUpdate(double dt) {
        update(dt);
    }

    @Override
    protected final void internalRender(Graphics g) {
        render(g);
    }

    @Override
    protected void internalClick() {
        clickEvent();
    }

    public ScopeEngineAccess scopeEngine() {
        return super.scopeEngine();
    }
}
