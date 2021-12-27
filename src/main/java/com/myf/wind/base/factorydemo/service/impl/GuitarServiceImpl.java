package com.myf.wind.base.factorydemo.service.impl;

import com.myf.wind.base.factorydemo.annotation.InstrumentAnnotation;
import com.myf.wind.base.factorydemo.service.InstrumentService;
import org.springframework.stereotype.Service;

/**
 * @author : wind-myf
 * @desc : 吉他实现类
 */
@Service("guitarService")
@InstrumentAnnotation(instrumentType = "GUITAR")
public class GuitarServiceImpl implements InstrumentService {
    @Override
    public String playInstrument(String userName) {
        // 模拟业务逻辑
        String result = userName + "play the guitar";
        System.out.println(userName + "play the guitar - " + System.currentTimeMillis());
        return result;
    }
}
