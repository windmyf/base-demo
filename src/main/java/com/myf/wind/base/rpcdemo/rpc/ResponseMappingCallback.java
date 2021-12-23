package com.myf.wind.base.rpcdemo.rpc;

import com.myf.wind.base.rpcdemo.rpc.model.PackageMsg;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author : wind-myf
 * @desc : response处理器
 */
public class ResponseMappingCallback {
    static ConcurrentHashMap<Long,CompletableFuture> mapping = new ConcurrentHashMap<>();

    public static void addCallBack(long requestId, CompletableFuture cb){
        mapping.putIfAbsent(requestId,cb);
    }

    public  static void runCallBack(PackageMsg packageMsg){
        CompletableFuture future = mapping.get(packageMsg.getHeader().getRequestId());
        future.complete(packageMsg.getContent().getRes());
        removeCallBack(packageMsg.getHeader().getRequestId());
    }

    private static void removeCallBack(long requestId) {
        mapping.remove(requestId);
    }
}
