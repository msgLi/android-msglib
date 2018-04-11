package com.msg.android.lib.core.ioc.app;

import android.app.Application;
import android.app.Instrumentation;

import com.msg.android.lib.core.ioc.kernel.KernelObject;
import com.msg.android.lib.core.ioc.kernel.KernelReflect;
import com.msg.android.lib.core.ioc.util.IOCContextUtils;

import java.lang.reflect.Field;

/**
 * Created by msg on 16/10/16.
 */
public final class LfIOC {

    private Application application = null;

    private Instrumentation instrumentation = null;

    private static LfIOC instance = null;

    private LfIOC(){

    }

    public synchronized static LfIOC getInstance(){
        synchronized (LfIOC.class){
            if (instance == null){
                instance = new LfIOC();
            }
            return instance;
        }
    }

    public void initIoc(Application application){

        this.application = application;

        Object mainThread = KernelObject.declaredGet(application.getBaseContext(), "mMainThread");
        // 反射获取mInstrumentation的对象
        Field instrumentationField = KernelReflect.declaredField(mainThread.getClass(), "mInstrumentation");
        instrumentation = new LfInstrumentation();
        // 自定义一个Instrumentation的子类 并把所有的值给copy进去
        KernelObject.copy(KernelReflect.get(mainThread, instrumentationField), instrumentation);
        // 再把替换过的Instrumentation重新放进去
        KernelReflect.set(mainThread, instrumentationField, instrumentation);

        IOCContextUtils.scanIocBeanByXml(application);
    }

}
