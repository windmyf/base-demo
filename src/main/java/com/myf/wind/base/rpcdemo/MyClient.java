package com.myf.wind.base.rpcdemo;

import com.myf.wind.base.rpcdemo.proxy.InterfaceProxy;
import com.myf.wind.base.rpcdemo.service.Car;

/**
 * @author : wind-myf
 * @desc : 来回通信、连接数量、拆包
 * 动态代理、序列化、协议封装
 * 连接池
 */
public class MyClient {

    public static void main(String[] args) {

        int size = 20;
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(()->{
                Car car = InterfaceProxy.proxyGet(Car.class);
                String drive = car.drive("北京[" + Thread.currentThread().getName() + "]");
                System.out.println(drive);
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

}

