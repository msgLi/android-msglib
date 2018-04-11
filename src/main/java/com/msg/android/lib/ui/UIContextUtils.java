package com.msg.android.lib.ui;

import android.app.Activity;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * Created by msg on 16/11/4.
 */
public class UIContextUtils {

    public static int getId(Activity context,String idName,String defType){
        return context.getResources().getIdentifier(idName,defType,context.getPackageName());
    }

    public static int getColor(Activity context, String colorName){
        int colorId = getId(context,colorName,"color");
        return context.getResources().getColor(colorId);
    }

    public static String getString(Activity context, String stringName){
        int stringId = getId(context,stringName,"string");
        return context.getResources().getString(stringId);
    }

    public static List<String> getStringList(Activity context, String stringArrayName){
        String[] stringArray = getStringArray(context,stringArrayName);
        if(stringArray != null && stringArray.length > 0){
            List<String> stringList = Arrays.asList(stringArray);
            return stringList;
        }
        return null;
    }

    public static String[] getStringArray(Activity context,String stringArrayName){
        int stringArrayId = getId(context,stringArrayName,"string");
        return context.getResources().getStringArray(stringArrayId);
    }

    public static View getView(Activity context,String viewIdName){
        return getView(context,getId(context,viewIdName,"id"));
    }

    public static View getView(Activity context,int viewId){
        return context.findViewById(viewId);
    }

    public static View getView(Activity context,View view,String viewIdName){
        return getView(view,getId(context,viewIdName,"id"));
    }

    public static View getView(View view,int viewId){
        return view.findViewById(viewId);
    }

}
