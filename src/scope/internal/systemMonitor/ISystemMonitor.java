package scope.internal.systemMonitor;

public interface ISystemMonitor {
    long getTotalMemory();
    long getFreeMemory();
    long getUsedMemory();
    int getCpuPercentage();
}
