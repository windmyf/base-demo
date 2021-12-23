package com.myf.wind.base.rpcdemo.rpc.model;

import com.myf.wind.base.rpcdemo.rpc.protocol.MyContent;
import com.myf.wind.base.rpcdemo.rpc.protocol.MyHeader;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wind_myf
 */
@Data
public class PackageMsg implements Serializable {
    private MyHeader header;
    private MyContent content;
    public PackageMsg(MyHeader header, MyContent content) {
        this.header = header;
        this.content = content;
    }
}
