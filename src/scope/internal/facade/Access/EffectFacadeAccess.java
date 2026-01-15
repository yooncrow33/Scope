package scope.internal.facade.Access;

import scope.internal.effect.afterImage.AfterImageAccess;
import scope.internal.effect.popup.PopupAccess;

public interface EffectFacadeAccess {
    PopupAccess Popup();
    AfterImageAccess AfterImage();
}
