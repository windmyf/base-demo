package com.myf.wind.base.rpcdemo.rpc.protocol;

import com.myf.wind.base.rpcdemo.enums.FlagEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author : wind-myf
 * @desc : 消息头
 */
@Data
public class MyHeader implements Serializable {

    /**
     * 值
     */
    private int flag;
    /**
     * uuid
     */
    private long requestId;
    /**
     * data_length
     */
    private int dataLen;

    /**
     * 构建header
     * @param msgBody 消息体
     * @return MyHeader
     */
    public static MyHeader createHeader(byte[] msgBody){
        MyHeader header = new MyHeader();
        int size = msgBody.length;
        long requestId = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        header.setFlag(FlagEnum.SEND_FLAG.getCode());
        header.setDataLen(size);
        header.setRequestId(requestId);

        return header;
    }

}
