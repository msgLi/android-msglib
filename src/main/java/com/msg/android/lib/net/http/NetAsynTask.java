package com.msg.android.lib.net.http;

import com.msg.android.lib.core.threadpool.ThreadPoolManager;

/**
 * Created by msg on 16/11/5.
 */
public final class NetAsynTask {

    protected static NetConfig config = new NetConfig();

    static {
        defaultConfig(config);
    }

    private static NetConfig defaultConfig(NetConfig config){
        if(config == null){
            config = new NetConfig();
        }

        if (config.getTimeout() <= 0){
            config.setTimeout(10000);
        }

        if (config.getNetRequestTaskClass() == null){
            config.setNetRequestTaskClass(NetRequestTask.class);
        }
        return config;
    }

    public static void initConfig(NetConfig config){
        NetAsynTask.config = defaultConfig(config);
    }

    public static void doNetGet(String url, NetRequest request,Class<? extends NetResponse> responseClass) {
        doNetRequest(url,request,HttpRequestMethod.GET,responseClass);
    }

    public static void doNetPost(String url, NetRequest request,Class<? extends NetResponse> responseClass) {
        doNetRequest(url,request,HttpRequestMethod.POST,responseClass);
    }

    public static void doNetRequest(String url, NetRequest request,HttpRequestMethod requestMethod,Class<? extends NetResponse> responseClass) {
        if (null == request || null == url
                || "".equals(url.trim())) {
            return;
        }
        NetRequestTask task = new NetRequestTask();
        task.url = url;
        task.request = request;
        task.responseClass = responseClass;
        task.requestMethod = requestMethod;
        task.sendMessage(NetConst.HANDLE_MESSAGE_FLAG_START_TASK, task);
        ThreadPoolManager.executeTask(task);
    }
}
