package ygk.util;

import ygk.IUpdate;

public class TickManager implements ITick{
    private long tick = 0;
    //private int lastPlayTime;
    private int totalPlayTime = 0;
    private int sessionPlayTime = 0;

    public void update() {
        tick++;
        sessionPlayTime = (int)Math.ceil(tick/3600f);
        //totalPlayTime = lastPlayTime + sessionPlayTime;
    }

    @Override
    public long getTick() {
        return tick;
    }
    @Override
    public int getTotalPlayTime() {
        return totalPlayTime;
    }
    @Override
    public int getSessionPlayTime() {
        return sessionPlayTime;
    }
}
