package com.myf.wind.base.rpcdemo.rpc.transport.handler;

import com.myf.wind.base.rpcdemo.enums.FlagEnum;
import com.myf.wind.base.rpcdemo.rpc.Dispatcher;
import com.myf.wind.base.rpcdemo.rpc.model.PackageMsg;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyContent;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyHeader;
import com.myf.wind.base.rpcdemo.util.SerDerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 处理器
 * @author wind_myf
 */
public class ServerRequestHandler extends ChannelInboundHandlerAdapter {

    private Dispatcher dispatcher;

    public ServerRequestHandler(Dispatcher dispatcher){
        this.dispatcher = dispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        PackageMsg requestMsg = (PackageMsg)msg;

        // 处理完给客户端返回:新的header + content
        ctx.executor().parent().next().execute(()->{

            String interfaceName = requestMsg.getContent().getInterfaceName();
            String methodName = requestMsg.getContent().getMethodName();
            Object invoke = dispatcher.getInvoke(interfaceName);
            Class<?> invokeClass = invoke.getClass();
            Object result = null;
            try {
                Method method = invokeClass.getMethod(methodName, requestMsg.getContent().getParameterTypes());
                result = method.invoke(invoke, requestMsg.getContent().getArgs());
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }


            MyContent responseContent = new MyContent();
            responseContent.setRes((String) result);
            byte[] contentBytes = SerDerUtil.ser(responseContent);

            MyHeader responseHeader = new MyHeader();
            responseHeader.setRequestId(requestMsg.getHeader().getRequestId());
            responseHeader.setFlag(FlagEnum.ACCEPT_FLAG.getCode());
            responseHeader.setDataLen(contentBytes.length);
            byte[] headerBytes = SerDerUtil.ser(responseHeader);

            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerBytes.length + contentBytes.length);
            byteBuf.writeBytes(headerBytes);
            byteBuf.writeBytes(contentBytes);
            ctx.writeAndFlush(byteBuf);
        });
    }
}
