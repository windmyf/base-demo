package com.myf.wind.base.rpcdemo.rpc;

import java.util.concurrent.ConcurrentHashMap;
/**
 * @author : wind-myf
 * @desc :
 */
public class Dispatcher {
    private static Dispatcher dispatcher;
    private Dispatcher(){
    }

    static {
        dispatcher = new Dispatcher();
    }
    public static Dispatcher getInstance(){
        return dispatcher;
    }

    public static ConcurrentHashMap<String,Object> invokeMap = new ConcurrentHashMap<>();

    public void register(String key,Object obj){
        invokeMap.put(key,obj);
    }

    public Object getInvoke(String key){
        return invokeMap.get(key);
    }

}
