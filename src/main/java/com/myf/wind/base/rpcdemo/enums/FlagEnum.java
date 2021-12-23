package com.myf.wind.base.rpcdemo.enums;
/**
 * @author : wind-myf
 * @desc : 标志位枚举
 */
public enum FlagEnum {
    SEND_FLAG(0x14141414,"发送端"),
    ACCEPT_FLAG(0x14141424,"接收端");

    private Integer code;
    private String desc;
    FlagEnum(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
