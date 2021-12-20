package com.myf.wind.base.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author : wind-myf
 * @date : 2021/12/19 14:23
 * @desc : netty
 * @version : 1.0
 */
public class MyNetty {
    public static void main(String[] args) throws InterruptedException {
        serverMode();
    }

    public static void serverMode() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        NioServerSocketChannel server = new NioServerSocketChannel();

        group.register(server);

        ChannelPipeline pipeline = server.pipeline();
        // accept 接收客户端并注册
        pipeline.addLast(new MyAcceptHandler(group,new ChannelInit()));
        ChannelFuture bind = server.bind(new InetSocketAddress("47.96.167.160", 9090));
        bind.sync().channel().closeFuture().sync();
        System.out.println("server closed ...");

    }
}

class MyAcceptHandler extends ChannelInboundHandlerAdapter {
    private final EventLoopGroup group;
    private final  ChannelHandler handler;

    public MyAcceptHandler(EventLoopGroup group,ChannelHandler handler){
        this.group = group;
        this.handler = handler;
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx){
        System.out.println("server registered ...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        SocketChannel client = (SocketChannel) msg;
        // 响应
        ChannelPipeline pipeline = client.pipeline();
        pipeline.addLast(handler);
        // 注册
        group.register(client);
    }

}

class MyInHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception{
        System.out.println("client register ...");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        System.out.println("client active ...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        ByteBuf buf = (ByteBuf) msg;
        CharSequence charSequence = buf.getCharSequence(0,buf.readableBytes(), CharsetUtil.UTF_8);
        System.out.println(charSequence);
        ctx.writeAndFlush(buf);
    }
}

class ChannelInit extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception{
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new MyInHandler());
    }

}
