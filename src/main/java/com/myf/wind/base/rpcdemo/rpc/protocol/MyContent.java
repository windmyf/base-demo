package com.myf.wind.base.rpcdemo.rpc.protocol;

import lombok.Data;

import java.io.Serializable;
/**
 * @author : wind-myf
 * @desc : 消息封装类
 */
@Data
public class MyContent implements Serializable {

    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
    private Object res;
}
