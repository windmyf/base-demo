package com.myf.wind.base.rpcdemo.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

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
}
