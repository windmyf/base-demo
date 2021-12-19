package com.myf.wind.base.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : wind-myf
 * @date : 2021/12/17 6:06
 * @desc : 组
 * @version : 1.0
 */
public class SelectorThreadGroup {
    SelectorThread[] selectorThreads;
    ServerSocketChannel server;
    AtomicInteger xid = new AtomicInteger(0);
    SelectorThreadGroup workGroup;
    public SelectorThreadGroup(int num){
        // num 线程数
        selectorThreads = new SelectorThread[num];
        for (int i = 0; i < num; i++) {
            selectorThreads[i] = new SelectorThread(this);
            new Thread(selectorThreads[i]).start();
        }

    }

    public void setWorkerGroup(SelectorThreadGroup workerGroup){
        this.workGroup = workerGroup;
    }

    public void bind(int port) {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            // server注册到哪个selector
            nextSelector(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextSelector(Channel socket) {
        if (socket instanceof ServerSocketChannel){
            SelectorThread selectorThread = nextV2(this);
            // 通过队列传递消息
            selectorThread.blockingQueue.add(socket);
            // selector.select()处会被阻塞，wakeup()是让selector的select()方法立即返回不阻塞
            selectorThread.selector.wakeup();
        }else {
            SelectorThread selectorThread = workGroup.nextV2(workGroup);
            selectorThread.blockingQueue.add(socket);
            selectorThread.selector.wakeup();
        }


    }

    /**
     * 在主线程中，取到堆里的SelectorThread对象
     */
    private SelectorThread next() {
        int index = xid.incrementAndGet() % selectorThreads.length;
        return selectorThreads[index];
    }

    /**
     * 在主线程中，取到堆里的SelectorThread对象
     */
    private SelectorThread nextV2(SelectorThreadGroup group) {
        int index = group.xid.incrementAndGet() % group.selectorThreads.length;
        return group.selectorThreads[index];
    }
}
