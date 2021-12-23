package com.myf.wind.base.rpcdemo.proxy;

import com.myf.wind.base.rpcdemo.rpc.Dispatcher;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyContent;
import com.myf.wind.base.rpcdemo.rpc.transport.ClientFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * @author : wind-myf
 * @desc : 代理类
 */
public class InterfaceProxy {
    public static <T>T proxyGet(Class<T> interfaceInfo){

        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] methodInfo = {interfaceInfo};

        Dispatcher dispatcher = Dispatcher.getInstance();
        return (T) Proxy.newProxyInstance(classLoader, methodInfo, (proxy, method, args) -> {

            Object result = null;
            Object invoke = dispatcher.getInvoke(interfaceInfo.getName());
            // 判断本地还是远程
            if (invoke == null){
                // rpc 调用, 调用的服务、方法、参数 ===》 封装成message [content]
                String name = interfaceInfo.getName();
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                MyContent content = new MyContent();
                content.setInterfaceName(name);
                content.setMethodName(methodName);
                content.setArgs(args);
                content.setParameterTypes(parameterTypes);

                // requestId + message ,本地缓存  协议 [header<>] [msgBody]
                CompletableFuture<Object> future = ClientFactory.transport(content);
                result = future.get();

            }else {
                // 本地调用
                Class<?> invokeClass = invoke.getClass();
                try {
                    Method classMethod = invokeClass.getMethod(method.getName(), method.getParameterTypes());
                    result = classMethod.invoke(invoke, args);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return result;
        });
    }

}
