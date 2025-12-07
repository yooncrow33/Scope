package ygk;

interface ISystemMonitor {
    long getTotalMemory();
    long getFreeMemory();
    long getUsedMemory();
    int getCpuPercentage();
}
