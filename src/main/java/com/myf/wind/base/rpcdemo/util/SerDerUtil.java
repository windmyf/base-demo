package com.myf.wind.base.rpcdemo.util;

import com.myf.wind.base.rpcdemo.rpc.protocol.MyContent;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author : wind-myf
 * @desc : 序列化工具类
 */
public class SerDerUtil {
    static ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

    public static synchronized byte[] ser(Object msg) {
        arrayOutputStream.reset();
        ObjectOutputStream outputStream;
        byte[] msgBytes = null;
        try {
            outputStream = new ObjectOutputStream(arrayOutputStream);
            outputStream.writeObject(msg);
            msgBytes = arrayOutputStream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return msgBytes;
    }

    public static MyContent getInvokeResult(MyContent myContent, String methodName, Object invoke) {
        Class<?> invokeClass = invoke.getClass();
        Object result = null;
        try {
            Method method = invokeClass.getMethod(methodName, myContent.getParameterTypes());
            result = method.invoke(invoke, myContent.getArgs());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        MyContent responseContent = new MyContent();
        responseContent.setRes(result);
        return responseContent;
    }
}
