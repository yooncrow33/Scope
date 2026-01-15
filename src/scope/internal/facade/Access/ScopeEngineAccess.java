package scope.internal.facade.Access;

import scope.internal.facade.EffectFacade;
import scope.internal.facade.SystemFacade;

public interface ScopeEngineAccess {
    EffectFacadeAccess effect();
    SystemFacadeAccess system();
    SoundManagerAccess sound();
}
