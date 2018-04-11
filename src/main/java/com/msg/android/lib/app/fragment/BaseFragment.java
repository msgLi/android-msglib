package com.msg.android.lib.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msg.android.lib.core.annotation.template.AnnotationContextUtils;
import com.msg.android.lib.core.annotation.ui.ContextUI;
import com.msg.android.lib.core.intf.ITitleViewProxy;
import com.msg.android.lib.core.intf.IUIControllerInterface;
import com.msg.android.lib.core.ioc.kernel.KernelClass;
import com.msg.android.lib.core.ioc.kernel.KernelReflect;
import com.msg.android.lib.ui.UIContextUtils;
import com.msg.android.lib.utils.ConstUtils;

import java.lang.reflect.Method;

/**
 * Created by msg on 16/11/4.
 */
public class BaseFragment extends Fragment implements IUIControllerInterface{

    protected ITitleViewProxy titleViewProxy;

    protected View contextView;

    protected LayoutInflater inflater;

    protected ViewGroup container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        preInitView();
        return contextView;
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
                contextView = inflater.inflate(contextLayout,container,true);
            }else{
                if(getContextLayout() != 0){
                    contextView = inflater.inflate(getContextLayout(),container,true);
                }
            }
        }else{
            if(getContextLayout() != 0){
                contextView = LayoutInflater.from(getActivity()).inflate(getContextLayout(),container,true);
            }
        }
        if (contextView == null){
            return ;
        }
        AnnotationContextUtils.initFragmentContextViewWithAnnotation(this,contextView);
        if(!TextUtils.isEmpty(titleViewIdName)){
            View topTitleView = UIContextUtils.getView(getActivity(),contextView,titleViewIdName);
            if(topTitleView != null && topTitleView instanceof ITitleViewProxy){
                titleViewProxy = (ITitleViewProxy) topTitleView;
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
    public int getContextLayout() {
        return 0;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }
}
