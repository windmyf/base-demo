package com.myf.wind.base.rpcdemo.rpc.transport;

import com.myf.wind.base.rpcdemo.rpc.ResponseMappingCallback;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyContent;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyHeader;
import com.myf.wind.base.rpcdemo.rpc.transport.handler.DecodeHandler;
import com.myf.wind.base.rpcdemo.util.SerDerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
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
                clientPool = outBoxes.get(address);
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
        // 既可以自定义RPC传输（有状态），也可以用http协议（无状态）作为载体传输
//        String type = "rpc";
        String type = "http";

        CompletableFuture<Object> res = new CompletableFuture<>();
        if ("rpc".equals(type)) {


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
            res = new CompletableFuture<>();
            ResponseMappingCallback.addCallBack(id, res);

            buf.writeBytes(msgHeader);
            buf.writeBytes(msgBody);
            ChannelFuture channelFuture = clientChannel.writeAndFlush(buf);
        }else {
            // 使用http协议作为载体
            // 1、用URL的现有工具
//            urlTS(content,res);

            // 2、on netty （io框架）+ 已经提供的http相关编码
            nettyTS(content,res);
        }
        return res;
    }

    /**
     * 基于netty的http client
     * @param content 消息
     * @param res 结果
     */
    private static void nettyTS(MyContent content, CompletableFuture<Object> res) {

        // 通过netty建立IO连接
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        Bootstrap client = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 512))
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                        // 接收
                                        // 客户端的msg：完整的 http response
                                        FullHttpResponse response = (FullHttpResponse) msg;

                                        ByteBuf resContent = response.content();
                                        byte[] data = new byte[resContent.readableBytes()];
                                        resContent.readBytes(data);

                                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                                        MyContent myContent = (MyContent) ois.readObject();

                                        res.complete(myContent.getRes());
                                    }
                                });
                    }
                });
        // 连接后，收到数据处理Handler
        try {
            ChannelFuture channelFuture = client.connect("localhost", 9999).sync();
            Channel channel = channelFuture.channel();

            // 发送
            byte[] data = SerDerUtil.ser(content);
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.POST,
                    "/", Unpooled.copiedBuffer(data));

            request.headers().set(HttpHeaderNames.CONTENT_LENGTH,data.length);
            // 客户端向server端发送
            channel.writeAndFlush(request).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 使用http协议
     * 这种方式每个请求占用一个连接
     * @param content 内容
     * @param res 结果
     */
    private static void urlTS(MyContent content, CompletableFuture<Object> res) {
        Object object = null;
        try {
            URL url = new URL("http://localhost:9999");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(outputStream);
            stream.writeObject(content);

            if (connection.getResponseCode() == 200){
                InputStream inputStream = connection.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(inputStream);
                MyContent myContent = (MyContent)ois.readObject();
                object = myContent.getRes();
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        res.complete(object);
    }

}
