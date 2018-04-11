package com.msg.android.lib.core.annotation.template;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.msg.android.lib.core.annotation.ui.UIElement;
import com.msg.android.lib.core.annotation.ui.UISet;
import com.msg.android.lib.core.ioc.kernel.KernelReflect;
import com.msg.android.lib.ui.UIContextUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by msg on 16/11/4.
 */
public class AnnotationContextUtils {

    public static void initFragmentContextViewWithAnnotation(Fragment fragment, View view){
        initViewWithAnnotation(fragment.getActivity(),view,fragment);
    }

    public static void initFragmentContextViewHolderWithAnnotation(Fragment fragment,View view,Object viewHoder){
        initViewWithAnnotation(fragment.getActivity(),view,viewHoder);
    }

    public static void initActivityContextViewWithAnnotation(Activity context){
        initViewWithAnnotation(context,null,null);
    }

    public static void initViewWithAnnotation(Activity context, View contextView,Object viewHolder){

        if (context == null){
            return;
        }

        if(contextView == null){
            contextView = ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
        }

        if (viewHolder == null){
            viewHolder = context;
        }

        if (context == null){
            return;
        }

        List<Field> declaredFields = KernelReflect.declaredFields(viewHolder.getClass());

        if(declaredFields != null && declaredFields.size() > 0){
            for(Field field : declaredFields){
                if(field.getAnnotation(UISet.class) != null){
                    UISet uiSet = field.getAnnotation(UISet.class);
                    UIElement uiElementType = uiSet.type();
                    String idName = uiSet.value();
                    if(TextUtils.isEmpty(idName)){
                        idName = field.getName();
                    }
                    try {
                        switch (uiElementType){
                            case UI_VIEW:
                                field.set(viewHolder, UIContextUtils.getView(context,contextView,idName));
                                break;
                            case UI_COLOR:
                                field.set(viewHolder,UIContextUtils.getColor(context,idName));
                                break;
                            case UI_STRING:
                                field.set(viewHolder,UIContextUtils.getString(context,idName));
                                break;
                            case UI_STRING_ARRAY:
                                field.set(viewHolder,UIContextUtils.getStringArray(context,idName));
                                break;
                            case UI_STRING_LIST:
                                field.set(viewHolder,UIContextUtils.getStringList(context,idName));
                                break;
                            default:
                                break;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
