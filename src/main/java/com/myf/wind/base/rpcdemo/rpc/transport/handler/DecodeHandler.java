package com.myf.wind.base.rpcdemo.rpc.transport.handler;

import com.myf.wind.base.rpcdemo.enums.FlagEnum;
import com.myf.wind.base.rpcdemo.rpc.model.PackageMsg;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyContent;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
/**
 * @author : wind-myf
 * @desc : 解码器
 */
public class DecodeHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {
        // decode start， 113 为请求头长度 msgHeader.length
        while (buf.readableBytes() >= 113){
            byte[] bytes = new byte[113];
            // 从哪里读取，但不移动指针
            buf.getBytes(buf.readerIndex(),bytes);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream stream = new ObjectInputStream(inputStream);
            MyHeader header = (MyHeader) stream.readObject();

            // Decode在两个方向都处理，通信协议
            if (buf.readableBytes() - 113>= header.getDataLen()){
                // 处理指针,移动指针到开始位置
                buf.readBytes(113);
                byte[] data = new byte[header.getDataLen()];
                buf.readBytes(data);
                ByteArrayInputStream din = new ByteArrayInputStream(data);
                ObjectInputStream objectInputStream = new ObjectInputStream(din);

                if (FlagEnum.SEND_FLAG.getCode() == header.getFlag()){
                    MyContent content = (MyContent) objectInputStream.readObject();
                    list.add(new PackageMsg(header,content));
                }else if (FlagEnum.ACCEPT_FLAG.getCode() == header.getFlag()){
                    MyContent content = (MyContent) objectInputStream.readObject();
                    list.add(new PackageMsg(header,content));
                }
            }else {
                System.out.println("read else。。。。" + buf.readableBytes());
                break;
            }

        }
    }
}