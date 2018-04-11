package com.msg.android.lib.core.context;

import com.msg.android.lib.core.ioc.IOCConfig;
import com.msg.android.lib.core.threadpool.config.ThreadPoolConfig;
import com.msg.android.lib.db.config.MsgDbConfig;
import com.msg.android.lib.net.http.NetConfig;

/**
 * Created by msg on 16/11/5.
 */
public class ContextConfig {

    private ThreadPoolConfig threadPoolConfig;

    private NetConfig netConfig;

    private IOCConfig iocConfig;

    private MsgDbConfig dbConfig;

    public ThreadPoolConfig getThreadPoolConfig() {
        return threadPoolConfig;
    }

    public void setThreadPoolConfig(ThreadPoolConfig threadPoolConfig) {
        this.threadPoolConfig = threadPoolConfig;
    }

    public NetConfig getNetConfig() {
        return netConfig;
    }

    public void setNetConfig(NetConfig netConfig) {
        this.netConfig = netConfig;
    }

    public IOCConfig getIocConfig() {
        return iocConfig;
    }

    public void setIocConfig(IOCConfig iocConfig) {
        this.iocConfig = iocConfig;
    }

    public MsgDbConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(MsgDbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }
}
