package com.msg.android.lib.core.ioc.app;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;

import com.msg.android.lib.app.activity.BaseActivity;
import com.msg.android.lib.core.annotation.template.AnnotationContextUtils;
import com.msg.android.lib.core.annotation.ui.ContextUI;
import com.msg.android.lib.core.ioc.kernel.KernelClass;
import com.msg.android.lib.core.ioc.kernel.KernelReflect;
import com.msg.android.lib.core.ioc.template.annotation.Autowired;
import com.msg.android.lib.core.ioc.util.IOCContextUtils;
import com.msg.android.lib.utils.ConstUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by msg on 16/10/16.
 */
public class LfInstrumentation extends Instrumentation {

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        List<Field> fields = KernelReflect.declaredFields(activity.getClass());
        for(Field field : fields){
            if(field.getAnnotation(Autowired.class) != null){
                try {
                    field.set(activity, IOCContextUtils.getBean(field.getType()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.callActivityOnCreate(activity, icicle);
        if (!(activity instanceof BaseActivity)){
            ContextUI contextUI = KernelClass.getAnnotation(activity.getClass(),ContextUI.class);
            int contextLayout = 0;
            String initViewMethodName = ConstUtils.DEFAULT_INIT_VIEW_METHOD_NAME;
            String initListenerMethodName = ConstUtils.DEFAULT_INIT_LISTENER_METHOD_NAME;
            String initDataMethodName = ConstUtils.DEFAULT_INIT_DATA_METHOD_NAME;
            if(contextUI != null){
                contextLayout = contextUI.contextLayout();
                if(!TextUtils.isEmpty(contextUI.initView())){
                    initViewMethodName = contextUI.initView();
                }

                if(!TextUtils.isEmpty(contextUI.initListener())){
                    initListenerMethodName = contextUI.initListener();
                }

                if(!TextUtils.isEmpty(contextUI.initData())){
                    initDataMethodName = contextUI.initData();
                }

                if (contextLayout != 0){
                    activity.setContentView(contextLayout);
                }
            }
            AnnotationContextUtils.initActivityContextViewWithAnnotation(activity);

            if(!TextUtils.isEmpty(initViewMethodName)){
                Method initViewMethod = KernelReflect.declaredMethod(activity.getClass(),initViewMethodName);
                if(initViewMethod != null){
                    KernelReflect.invoke(activity,initViewMethod);
                }
            }

            if(!TextUtils.isEmpty(initListenerMethodName)){
                Method initListenerMethod = KernelReflect.declaredMethod(activity.getClass(),initListenerMethodName);
                if (initListenerMethod != null){
                    KernelReflect.invoke(activity,initListenerMethod);
                }
            }

            if(!TextUtils.isEmpty(initDataMethodName)){
                Method initDataMethod = KernelReflect.declaredMethod(activity.getClass(),initDataMethodName);
                if (initDataMethod!= null){
                    KernelReflect.invoke(activity,initDataMethod);
                }
            }
        }
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        super.callActivityOnCreate(activity, icicle, persistentState);
    }

    @Override
    public void callActivityOnDestroy(Activity activity) {
        super.callActivityOnDestroy(activity);
    }

    @Override
    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
        super.callActivityOnRestoreInstanceState(activity, savedInstanceState);
    }

    @Override
    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState, PersistableBundle persistentState) {
        super.callActivityOnRestoreInstanceState(activity, savedInstanceState, persistentState);
    }

    @Override
    public void callActivityOnPostCreate(Activity activity, Bundle icicle) {
        super.callActivityOnPostCreate(activity, icicle);
    }

    @Override
    public void callActivityOnPostCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        super.callActivityOnPostCreate(activity, icicle, persistentState);
    }

    @Override
    public void callActivityOnNewIntent(Activity activity, Intent intent) {
        super.callActivityOnNewIntent(activity, intent);
    }

    @Override
    public void callActivityOnStart(Activity activity) {
        super.callActivityOnStart(activity);
    }

    @Override
    public void callActivityOnRestart(Activity activity) {
        super.callActivityOnRestart(activity);
    }

    @Override
    public void callActivityOnResume(Activity activity) {
        super.callActivityOnResume(activity);
    }

    @Override
    public void callActivityOnStop(Activity activity) {
        super.callActivityOnStop(activity);
    }

    @Override
    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState) {
        super.callActivityOnSaveInstanceState(activity, outState);
    }

    @Override
    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState, PersistableBundle outPersistentState) {
        super.callActivityOnSaveInstanceState(activity, outState, outPersistentState);
    }

    @Override
    public void callActivityOnPause(Activity activity) {
        super.callActivityOnPause(activity);
    }

    @Override
    public void callActivityOnUserLeaving(Activity activity) {
        super.callActivityOnUserLeaving(activity);
    }
}
