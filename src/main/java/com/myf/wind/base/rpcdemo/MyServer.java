package com.myf.wind.base.rpcdemo;

import com.myf.wind.base.rpcdemo.rpc.transport.handler.DecodeHandler;
import com.myf.wind.base.rpcdemo.rpc.Dispatcher;
import com.myf.wind.base.rpcdemo.rpc.transport.handler.ServerRequestHandler;
import com.myf.wind.base.rpcdemo.service.Car;
import com.myf.wind.base.rpcdemo.service.Plane;
import com.myf.wind.base.rpcdemo.service.impl.CarImpl;
import com.myf.wind.base.rpcdemo.service.impl.PlaneImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author : wind-myf
 * @desc : 服务端
 */
public class MyServer {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        // 收集可用对象
        Car car = new CarImpl();
        Plane plane = new PlaneImpl();
        Dispatcher dispatcher = Dispatcher.getInstance();
        dispatcher.register(Car.class.getName(),car);
        dispatcher.register(Plane.class.getName(),plane);

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(20);
        NioEventLoopGroup workerGroup = bossGroup;

        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture bind = bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        System.out.println("server accept in... " + nioSocketChannel.localAddress());
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addLast(new DecodeHandler());
                        pipeline.addLast(new ServerRequestHandler(dispatcher));
                    }
                }).bind(new InetSocketAddress("localhost", 9999));
        /**
         * 扩展1：ServerBootstrap可以绑定多个端口，多个端口用同一套Handler逻辑，但丰富了暴露接口
         * bootstrap.bind(9998);
         *
         * 扩展2：ServerBootstrap使用多个BootStrap时，使用各自的Handler逻辑
         * ServerBootstrap bootstrap1 = new ServerBootstrap();
         */

        try {
            bind.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

