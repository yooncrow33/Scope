package scope.internal.facade;

import scope.internal.facade.Access.ScopeEngineAccess;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ScopeEngine implements ScopeEngineAccess {
    private final List<Runnable> updatables = new ArrayList<>();
    private final List<Consumer<Graphics>> renderables = new ArrayList<>();

    private EffectFacade effectFacade;
    private SystemFacade systemFacade;
    private SoundFacade soundFacade;

    public ScopeEngine() {
        systemFacade = new SystemFacade(this);
        effectFacade = new EffectFacade(this, systemFacade.tm.get());
        soundFacade = new SoundFacade();
    }

    public void renderAll(Graphics g) {
        for (Consumer<Graphics> drawFunction : renderables) {
            drawFunction.accept(g);
        }
    }

    public void updateAll() {
        for (Runnable updatable : updatables) {
            updatable.run();
        }
    }

    public void registerUpdatable(Runnable updateLogic) {
        this.updatables.add(updateLogic);
    }
    public void registerRenderable(Consumer<Graphics> renderLogic) {
        this.renderables.add(renderLogic);
    }

    @Override
    public EffectFacade effect() {
        return effectFacade;
    }

    @Override
    public SystemFacade system() {
        return systemFacade;
    }

    @Override
    public SoundFacade sound() {
        return  soundFacade;
    }
}
