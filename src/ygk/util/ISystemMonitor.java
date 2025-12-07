package ygk.util;

interface ISystemMonitor {
    long getTotalMemory();
    long getFreeMemory();
    long getUsedMemory();
    int getCpuPercentage();
}
