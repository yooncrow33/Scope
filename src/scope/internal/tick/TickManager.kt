package scope.internal.tick

import kotlin.math.ceil

class TickManager : ITick {
    private var tick: Long = 0

    //private int lastPlayTime;
    private var sessionPlayTime : Int = 0

    fun update() {
        tick++
        sessionPlayTime = ceil((tick / 3600f).toDouble()).toInt()
        //totalPlayTime = lastPlayTime + sessionPlayTime;
    }

    override fun getTick(): Long {
        return tick
    }
}
