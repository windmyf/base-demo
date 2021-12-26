package com.myf.wind.base.rpcdemo;

import com.myf.wind.base.rpcdemo.rpc.Dispatcher;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyContent;
import com.myf.wind.base.rpcdemo.rpc.transport.handler.MyHttpRpcHandler;
import com.myf.wind.base.rpcdemo.service.Car;
import com.myf.wind.base.rpcdemo.service.Plane;
import com.myf.wind.base.rpcdemo.service.impl.CarImpl;
import com.myf.wind.base.rpcdemo.service.impl.PlaneImpl;
import com.myf.wind.base.rpcdemo.util.SerDerUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;

/**
 * @author : wind-myf
 * @desc : 服务端
 */
public class MyServer {

    public static void main(String[] args) {
//        startServer();
        startHttpServer();
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

                        // case 1：自定义的rpc
//                        pipeline.addLast(new DecodeHandler());
//                        pipeline.addLast(new ServerRequestHandler(dispatcher));
                        // case 2：http 协议
                        pipeline.addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(1024*512))
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        // http 协议，msg是一个完整的http-request
                                        FullHttpRequest httpRequest = (FullHttpRequest) msg;
                                        System.out.println("httpRequest = " + httpRequest.toString());

                                        // 序列化的myContent
                                        ByteBuf content = httpRequest.content();
                                        byte[] data = new byte[content.readableBytes()];
                                        content.readBytes(data);
                                        ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
                                        MyContent myContent = (MyContent) oin.readObject();


                                        String interfaceName = myContent.getInterfaceName();
                                        String methodName = myContent.getMethodName();
                                        Object invoke = dispatcher.getInvoke(interfaceName);
                                        MyContent responseContent = SerDerUtil.getInvokeResult(myContent, methodName, invoke);
                                        byte[] contentBytes = SerDerUtil.ser(responseContent);

                                        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK,
                                                Unpooled.copiedBuffer(contentBytes));
                                        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,contentBytes.length);
                                        // http协议 ：header + body
                                        ctx.writeAndFlush(response);
                                    }
                                });
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


    public static void startHttpServer(){
        // 收集可用对象
        Car car = new CarImpl();
        Plane plane = new PlaneImpl();
        Dispatcher dispatcher = Dispatcher.getInstance();
        dispatcher.register(Car.class.getName(),car);
        dispatcher.register(Plane.class.getName(),plane);
        // tomcat netty
        Server server = new Server(new InetSocketAddress("localhost", 9999));
        ServletContextHandler contextHandler = new ServletContextHandler(server,"/");
        server.setHandler(contextHandler);
        contextHandler.addServlet(MyHttpRpcHandler.class,"/*");
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

