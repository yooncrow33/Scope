package scope.internal.facade;

import scope.internal.Lazy;
import scope.internal.facade.Access.SoundManagerAccess;
import scope.internal.facade.Access.SystemFacadeAccess;
import scope.internal.sound.SoundManager;

public final class SoundFacade implements SoundManagerAccess {

    private final Lazy<SoundManager> sm;

    public SoundFacade() {
        this.sm = new Lazy<>(() -> {
            SoundManager sm = new SoundManager();
            return sm;
        });
    }

    @Override public void setExternalRoot(String dir) { sm.get().setExternalRoot(dir);}
    @Override public void play(String path) { sm.get().play(path); }
    @Override public void loopBgm(String path) { sm.get().loopBgm(path); }
    @Override public void stopBgm() { sm.get().stopBgm(); }
    @Override public void setBgmVolume(float db) { sm.get().setBgmVolume(db); }
    @Override public void setSfxVolume(float db) { sm.get().setSfxVolume(db); }
    @Override public void dispose() { sm.get().dispose(); }
}
