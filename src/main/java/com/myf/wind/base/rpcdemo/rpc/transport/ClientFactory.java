package com.myf.wind.base.rpcdemo.rpc.transport;

import com.myf.wind.base.rpcdemo.rpc.ResponseMappingCallback;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyContent;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyHeader;
import com.myf.wind.base.rpcdemo.rpc.transport.handler.DecodeHandler;
import com.myf.wind.base.rpcdemo.util.SerDerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wind-myf
 * @desc : 连接池工厂
 */
public class ClientFactory {
    int poolSize = 10;

    private static final ClientFactory FACTORY;
    private Random rand = new Random();
    private NioEventLoopGroup clientWorker;

    private ClientFactory(){}

    static {
        FACTORY = new ClientFactory();
    }

    public static ClientFactory getFactory(){
        return FACTORY;
    }
    private final ConcurrentHashMap<InetSocketAddress, ClientPool> outBoxes = new ConcurrentHashMap<>();

    public NioSocketChannel getClient(InetSocketAddress address){
        ClientPool clientPool = outBoxes.get(address);
        // 并发情况处理
        if (clientPool == null){
            synchronized (outBoxes){
                if (clientPool == null){
                    outBoxes.putIfAbsent(address,new ClientPool(poolSize));
                    clientPool = outBoxes.get(address);
                }
            }
        }
        int i = rand.nextInt(poolSize);
        if (clientPool.clients[i] != null && clientPool.clients[i].isActive()){
            return clientPool.clients[i];
        }
        synchronized (clientPool.lock[i]){
            if (clientPool.clients[i] == null || !clientPool.clients[i].isActive()){
                clientPool.clients[i] = create(address);
            }
        }
        return clientPool.clients[i];
    }

    /**
     * 创建连接
     * @param address 地址
     * @return NioSocketChannel
     */
    private NioSocketChannel create(InetSocketAddress address){
        // 基于netty 的客户端创建方式
        clientWorker = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connect = bootstrap.group(clientWorker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast(new DecodeHandler());
                        pipeline.addLast(new ClientResponse());
                    }
                }).connect(address);
        try {
            NioSocketChannel client = (NioSocketChannel) connect.sync().channel();
            return client;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param content 内容
     * @return 内容处理
     */
    public static CompletableFuture<Object> transport(MyContent content) throws InterruptedException {
        byte[] msgBody = SerDerUtil.ser(content);
        MyHeader header = MyHeader.createHeader(msgBody);
        byte[] msgHeader = SerDerUtil.ser(header);
        System.out.println("client msgHeader.length = " + msgHeader.length);

        // 连接池：取得连接
        ClientFactory factory = ClientFactory.getFactory();
        NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9999));

        // 获取连接过程中：开始-》创建
        // 发送 --> IO out -》 走Netty
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);
        long id = header.getRequestId();
        CompletableFuture<Object> future = new CompletableFuture<>();
        ResponseMappingCallback.addCallBack(id, future);

        buf.writeBytes(msgHeader);
        buf.writeBytes(msgBody);
        ChannelFuture channelFuture = clientChannel.writeAndFlush(buf);
        // TODO 是否需要
        channelFuture.sync();
        return future;
    }

}
