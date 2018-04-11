package com.msg.android.lib.core.threadpool.config;

/**
 * Created by msg on 16/11/5.
 */
public class ThreadPoolConfig {

    private int corePoolSize;

    private int maxPoolSize;

    private int aliveTime;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getAliveTime() {
        return aliveTime;
    }

    public void setAliveTime(int aliveTime) {
        this.aliveTime = aliveTime;
    }
}
