package com.msg.android.lib.core.ioc.service;

/**
 * Created by msg on 16/11/7.
 */
public class ServiceIntecepterManager {

    public static final <R> R intecepterCall(Intecepter intecepter,IntecepterCall intecepterCall,Object originalParams){

        if (intecepterCall == null){
            return null;
        }

        try {
            if(intecepter != null){
                intecepter.acceptCallBefore(originalParams);
            }

            Object ret = intecepterCall.call();

            if (intecepter != null){
                intecepter.acceptCallEnd(originalParams,null);
            }
            return (R)ret;
        } catch (Exception e) {
            e.printStackTrace();
            intecepter.acceptCallEnd(originalParams,e);
            return null;
        }
    }

    public static final <R> R intecepterCall(Intecepter intecepter,IntecepterCall intecepterCall){
        return intecepterCall(intecepter,intecepterCall,null);
    }

}
