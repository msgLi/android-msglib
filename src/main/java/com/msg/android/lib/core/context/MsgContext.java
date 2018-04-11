package com.msg.android.lib.core.context;

import android.app.Application;

import com.msg.android.lib.core.ioc.app.LfIOC;
import com.msg.android.lib.core.threadpool.ThreadPoolManager;
import com.msg.android.lib.db.MsgSqliteDatabaseManager;
import com.msg.android.lib.net.http.NetAsynTask;

/**
 * Created by msg on 16/11/5.
 */
public final class MsgContext {

    public synchronized static void initContext(Application application,ContextConfig contextConfig){
        synchronized (MsgContext.class){
            if(contextConfig == null){
                contextConfig = new ContextConfig();
            }
            ThreadPoolManager.initPool(contextConfig.getThreadPoolConfig());
            NetAsynTask.initConfig(contextConfig.getNetConfig());
            if(contextConfig.getIocConfig() != null){
                LfIOC.getInstance().initIoc(application);
            }

            if(contextConfig.getDbConfig() != null){
                MsgSqliteDatabaseManager.init(application,contextConfig.getDbConfig());
            }
        }
    }

}
