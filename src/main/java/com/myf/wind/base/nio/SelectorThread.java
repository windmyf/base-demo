package com.myf.wind.base.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author : wind-myf
 * @date : 2021/12/17 5:46
 * @desc : 每个线程对应一个selector
 * @version : 1.0
 */
public class SelectorThread implements Runnable{
    Selector selector = null;
    LinkedBlockingQueue<Channel> blockingQueue = new LinkedBlockingQueue();
    SelectorThreadGroup group;

    public SelectorThread(SelectorThreadGroup group){
        try {
            this.group = group;
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        // Loop
        while (true){

            try {
                // 阻塞
                int nums = selector.select();
                // 处理selectKeys
                if (nums > 0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    // 线程处理过程
                    while (iterator.hasNext()){

                        SelectionKey key = iterator.next();
                        iterator.remove();
                        // 接收客户端的过程
                        if (key.isAcceptable()){
                            acceptHandler(key);
                        }else if (key.isReadable()){
                            readHandler(key);
                        }else if (key.isWritable()){

                        }
                    }

                }
                // 处理task
                if (!blockingQueue.isEmpty()){
                    Channel channel = blockingQueue.take();
                    if (channel instanceof ServerSocketChannel){
                        ServerSocketChannel server = (ServerSocketChannel) channel;
                        server.register(selector,SelectionKey.OP_ACCEPT);
                    }else {
                        System.out.println(Thread.currentThread().getName() + " read");
                        SocketChannel client = (SocketChannel) channel;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        client.register(selector,SelectionKey.OP_READ,buffer);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void acceptHandler(SelectionKey key){

        ServerSocketChannel socketChannel = (ServerSocketChannel) key.channel();

        try {
            SocketChannel client = socketChannel.accept();
            client.configureBlocking(false);
            // 选择一个selector注册
            group.nextSelector(client);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void readHandler(SelectionKey key){
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        while (true){
            try {
                int num = client.read(buffer);
                if (num > 0){
                    buffer.flip();
                    while (buffer.hasRemaining()){
                        client.write(buffer);
                    }
                    buffer.clear();
                }else if (num == 0){
                    break;
                }else {
                    // 客户端断开
                    System.out.println("client:" + client.getRemoteAddress() + "closed.....");
                    key.cancel();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
