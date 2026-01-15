package scope.internal.facade;

import scope.internal.Lazy;
import scope.internal.facade.Access.SystemFacadeAccess;
import scope.internal.systemMonitor.SystemMonitor;
import scope.internal.tick.TickManager;

public final class SystemFacade implements SystemFacadeAccess {
    private final Lazy<SystemMonitor> sm;
    public final Lazy<TickManager> tm;

    public SystemFacade(ScopeEngine engine) {
        this.sm = new Lazy<>(() -> {
            SystemMonitor sm = new SystemMonitor();
            engine.registerUpdatable(sm::update);
            return sm;
        });

        this.tm = new Lazy<>(() -> {
            TickManager tm = new TickManager();
            engine.registerUpdatable(tm::update);
            return tm;
        });
    }

    @Override public long getTotalMemory() { return sm.get().getTotalMemory(); }
    @Override public long getFreeMemory() { return sm.get().getFreeMemory(); }
    @Override public long getUsedMemory() { return sm.get().getUsedMemory(); }
    @Override public int getCpuPercentage() { return sm.get().getCpuPercentage(); }

    @Override public long getTick() { return tm.get().getTick(); }
}
