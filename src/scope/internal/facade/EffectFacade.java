package scope.internal.facade;

import scope.internal.effect.afterImage.AfterImageAccess;
import scope.internal.effect.afterImage.AfterImageManager;
import scope.internal.effect.popup.PopupAccess;
import scope.internal.effect.popup.PopupManager;

import scope.internal.Lazy;
import scope.internal.facade.Access.EffectFacadeAccess;
import scope.internal.tick.TickManager;

public class EffectFacade implements EffectFacadeAccess {
    private AfterImageAccess afterImageAccess;
    private PopupAccess popupAccess;

    private Lazy<AfterImageManager> am;
    private Lazy<PopupManager> pm;

    public EffectFacade(ScopeEngine engine, TickManager tm) {
        this.am = new Lazy<>(() -> {
            AfterImageManager am = new AfterImageManager();
            engine.registerUpdatable(am::update);
            engine.registerRenderable(am::draw);
            return am;
        });

        this.pm = new Lazy<>(() -> {
            PopupManager pm = new PopupManager(tm);
            engine.registerUpdatable(pm::update);
            engine.registerRenderable(pm::draw);
            return pm;
        });
    }

    @Override
    public PopupAccess Popup()  {
        return (popupAccess = pm.get());
    }

    @Override
    public AfterImageAccess AfterImage() {
        return (afterImageAccess = am.get());
    }

}
