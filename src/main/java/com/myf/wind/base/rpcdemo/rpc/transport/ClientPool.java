package com.myf.wind.base.rpcdemo.rpc.transport;

import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 连接池
 */
class ClientPool {
    public NioSocketChannel[] clients;
    public Object[] lock;

    public ClientPool(int size){
        // init 连接是空的
        clients = new NioSocketChannel[size];
        // 锁初始化
        lock = new Object[size];
        for (int i = 0; i < size; i++) {
            lock[i] = new Object();
        }
    }
}
