package scope.internal.facade.Access;

public interface SystemFacadeAccess {
    long getTotalMemory();
    long getFreeMemory();
    long getUsedMemory();
    int getCpuPercentage();

    long getTick();
}
