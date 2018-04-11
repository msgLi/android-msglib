package com.msg.android.lib.core.ioc.service;

/**
 * Created by msg on 16/11/7.
 */
public abstract class Intecepter {

    private Intecepter intecepter;

    public Intecepter(Intecepter intecepter){
        this.intecepter = intecepter;
    }

    public Intecepter(){

    }

    public final Intecepter obtainIntecepter(Intecepter intecepter){
        intecepter.setIntecepter(this);
        return this.intecepter;
    }

    private void setIntecepter(Intecepter intecepter){
        this.intecepter = intecepter;
    }

    public final void acceptCallBefore(Object originalParams){
        if(intecepter != null && this.getClass() != intecepter.getClass()){
            intecepter.acceptCallBefore(originalParams);
        }
        before(originalParams);
    }

    public final void acceptCallEnd(Object originalParams,Exception error){
        if (intecepter != null && this.getClass() != intecepter.getClass()){
            intecepter.acceptCallEnd(originalParams,error);
        }
        end(originalParams,error);
    }

    public abstract void before(Object originalParams);

    public abstract void end(Object originalParams,Exception error);

}
