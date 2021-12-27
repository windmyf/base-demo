package com.myf.wind.base.factorydemo.enums;
/**
 * @author : wind-myf
 * @desc : 乐器类型枚举
 */
public enum InstrumentTypeEnum {
    GENERAL("GENERAL","通用乐器"),
    PIANO("PIANO","钢琴"),
    GUITAR("GUITAR","吉他"),
    HORN("HORN","喇叭"),
    DRUM("DRUM","鼓");

    private String code;
    private String desc;

    InstrumentTypeEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
