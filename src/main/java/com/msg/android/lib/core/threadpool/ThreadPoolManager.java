package com.msg.android.lib.core.threadpool;

import com.msg.android.lib.core.threadpool.config.ThreadPoolConfig;
import com.msg.android.lib.core.threadpool.intf.IThreadRequest;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by msg on 16/11/5.
 */
public class ThreadPoolManager {

    private static ThreadPoolExecutor threadPoolExecutor;

    private static final int DEFAULT_CORE_POOL_SIZE = 5;

    private static final int DEFAULT_MAX_POOL_SIZE = 15;

    private static final int DEFAULT_ALIVE_TIME = 30;

    public synchronized static void initPool(ThreadPoolConfig config){
        synchronized (ThreadPoolManager.class){
            if(config == null){
                config = new ThreadPoolConfig();
            }
            if(config.getCorePoolSize() <= 0){
                config.setCorePoolSize(DEFAULT_CORE_POOL_SIZE);
            }
            if (config.getMaxPoolSize() <= 0){
                config.setMaxPoolSize(DEFAULT_MAX_POOL_SIZE);
            }
            if(config.getAliveTime() <= 0){
                config.setAliveTime(DEFAULT_ALIVE_TIME);
            }
            threadPoolExecutor = new ThreadPoolExecutor(config.getCorePoolSize(),
                    config.getMaxPoolSize(),
                    config.getAliveTime(),
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<Runnable>()
                    );
        }
    }

    public synchronized static void executeTask(IThreadRequest request){
        synchronized (ThreadPoolManager.class){
            if (request == null){
                return;
            }
            Runnable task = request.getThreadTask();
            if (task == null){
                return;
            }

            if (threadPoolExecutor != null){
                threadPoolExecutor.execute(task);
            }
        }
    }

    public synchronized static void shutDown(){
        synchronized (ThreadPoolManager.class){
            if (threadPoolExecutor != null){
                threadPoolExecutor.shutdown();
            }
        }

    }

}
