package scope.internal.sound;

public interface ISoundManager {
    void setExternalRoot(String dir);
    void play(String path);
    void loopBgm(String path);
    void stopBgm();
    void setBgmVolume(float db);
    void setSfxVolume(float db);
    void dispose();
}
