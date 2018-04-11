package com.msg.android.lib.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.msg.android.lib.core.annotation.template.AnnotationContextUtils;
import com.msg.android.lib.core.annotation.ui.ContextUI;
import com.msg.android.lib.core.intf.ITitleViewProxy;
import com.msg.android.lib.core.intf.IUIControllerInterface;
import com.msg.android.lib.core.ioc.kernel.KernelClass;
import com.msg.android.lib.core.ioc.kernel.KernelReflect;
import com.msg.android.lib.net.http.ui.INetPostAsyncProgress;
import com.msg.android.lib.utils.ConstUtils;

import java.lang.reflect.Method;

/**
 * Created by msg on 16/11/4.
 */
public abstract class BaseActivity extends FragmentActivity implements IUIControllerInterface,INetPostAsyncProgress {

    protected ITitleViewProxy titleViewProxy;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        preInitView();
    }

    protected void preInitView(){
        ContextUI contextUI = KernelClass.getAnnotation(this.getClass(),ContextUI.class);
        String titleViewIdName = null;
        int contextLayout = 0;
        String initViewMethodName = ConstUtils.DEFAULT_INIT_VIEW_METHOD_NAME;
        String initListenerMethodName = ConstUtils.DEFAULT_INIT_LISTENER_METHOD_NAME;
        String initDataMethodName = ConstUtils.DEFAULT_INIT_DATA_METHOD_NAME;
        if(contextUI != null){
            titleViewIdName = contextUI.titleViewIdName();
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
                setContentView(contextLayout);
            }else{
                if(getContextLayout() != 0){
                    setContentView(getContextLayout());
                }
            }
        }else{
            if(getContextLayout() != 0){
                setContentView(getContextLayout());
            }
        }
        AnnotationContextUtils.initActivityContextViewWithAnnotation(this);
        if(!TextUtils.isEmpty(titleViewIdName)){
            int titleViewId = getResources().getIdentifier(titleViewIdName,"id",getApplication().getPackageName());
            if (titleViewId != 0){
                View topTitleView = findViewById(titleViewId);
                if(topTitleView != null && topTitleView instanceof ITitleViewProxy){
                    titleViewProxy = (ITitleViewProxy) topTitleView;
                }
            }
        }

        if(!TextUtils.isEmpty(initViewMethodName)){
            Method initViewMethod = KernelReflect.declaredMethod(this.getClass(),initViewMethodName);
            if(initViewMethod != null){
                KernelReflect.invoke(this,initViewMethod);
            }
        }

        if(!TextUtils.isEmpty(initListenerMethodName)){
            Method initListenerMethod = KernelReflect.declaredMethod(this.getClass(),initListenerMethodName);
            if (initListenerMethod != null){
                KernelReflect.invoke(this,initListenerMethod);
            }
        }

        if(!TextUtils.isEmpty(initDataMethodName)){
            Method initDataMethod = KernelReflect.declaredMethod(this.getClass(),initDataMethodName);
            if (initDataMethod!= null){
                KernelReflect.invoke(this,initDataMethod);
            }
        }
    }

    @Override
    public int getContextLayout(){
        return 0;
    }

    @Override
    public void initView(){
        showLeftButton();
    }

    @Override
    public void initData(){

    }

    @Override
    public  void initListener(){
        setLeftButtonUseSpaceViewOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.finish();
            }
        });
    }

    protected void goneLeftButton(){
        if(titleViewProxy != null){
            View leftButtonUseSpaceView = titleViewProxy.getLeftButtonUseSpaceView();
            if(leftButtonUseSpaceView != null){
                leftButtonUseSpaceView.setVisibility(View.GONE);
            }
        }
    }

    protected void goneRightButton(){
        if (titleViewProxy != null){
            View rightButtonUseSpaceView = titleViewProxy.getRightButtonUseSpaceView();
            if (rightButtonUseSpaceView != null){
                rightButtonUseSpaceView.setVisibility(View.GONE);
            }
        }
    }

    protected void showLeftButton(){
        if(titleViewProxy != null){
            View leftButtonUseSpaceView = titleViewProxy.getLeftButtonUseSpaceView();
            if(leftButtonUseSpaceView != null){
                leftButtonUseSpaceView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void showRightButton(){
        if (titleViewProxy != null){
            View rightButtonUseSpaceView = titleViewProxy.getRightButtonUseSpaceView();
            if (rightButtonUseSpaceView != null){
                rightButtonUseSpaceView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected TextView getTitleTextView(){
        if (titleViewProxy != null){
            return titleViewProxy.getTitleTextView();
        }else{
            return null;
        }
    }

    protected <T extends View> T getTitleView(){
        if(titleViewProxy != null){
            return (T)titleViewProxy.getTitleView();
        }else{
            return null;
        }
    }

    protected <T extends View> T getLeftButton(){
        if(titleViewProxy != null){
            return (T)titleViewProxy.getLeftButton();
        }else{
            return null;
        }
    }

    protected <T extends View> T getRightButton(){
        if(titleViewProxy != null){
            return (T)titleViewProxy.getRightButton();
        }else{
            return null;
        }
    }

    protected <T extends View> T getTitleShowView(){
        if(titleViewProxy != null){
            return (T)titleViewProxy.getTitleShowView();
        }else{
            return null;
        }
    }

    protected void setLeftButtonUseSpaceViewOnclick(View.OnClickListener onclick){
        if (titleViewProxy != null){
            View leftButtonUseSpaceView = titleViewProxy.getLeftButtonUseSpaceView();
            if(leftButtonUseSpaceView != null){
                leftButtonUseSpaceView.setOnClickListener(onclick);
            }
        }
    }

    protected void setRightButtonUseSpaceViewOnclick(View.OnClickListener onclick){
        if (titleViewProxy != null){
            View rightButtonUseSpaceView = titleViewProxy.getRightButtonUseSpaceView();
            if(rightButtonUseSpaceView != null){
                rightButtonUseSpaceView.setOnClickListener(onclick);
            }
        }
    }

    @Override
    public void startProgress() {
        if(progressDialog != null && progressDialog.isShowing()){
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("请求处理中");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
