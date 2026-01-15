package scope.internal.facade.Access;

public interface SoundManagerAccess {
    void setExternalRoot(String dir);
    void play(String path);
    void loopBgm(String path);
    void stopBgm();
    void setBgmVolume(float db);
    void setSfxVolume(float db);
    void dispose();
}
