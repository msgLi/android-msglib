package com.msg.android.lib.net.http;

/**
 * Created by msg on 16/11/5.
 */
public class NetConfig {

    private int timeout;

    private Class<? extends NetRequestTask> netRequestTaskClass;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Class<? extends NetRequestTask> getNetRequestTaskClass() {
        return netRequestTaskClass;
    }

    public void setNetRequestTaskClass(Class<? extends NetRequestTask> netRequestTaskClass) {
        this.netRequestTaskClass = netRequestTaskClass;
    }
}
