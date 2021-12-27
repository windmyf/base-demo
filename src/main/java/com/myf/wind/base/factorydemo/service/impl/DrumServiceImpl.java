package com.myf.wind.base.factorydemo.service.impl;

import com.myf.wind.base.factorydemo.annotation.InstrumentAnnotation;
import com.myf.wind.base.factorydemo.service.InstrumentService;
import org.springframework.stereotype.Service;

/**
 * @author : wind-myf
 * @desc : 鼓实现类
 */
@Service("drumService")
@InstrumentAnnotation(instrumentType = "DRUM")
public class DrumServiceImpl implements InstrumentService {
    @Override
    public String playInstrument(String userName) {
        // 模拟业务逻辑
        String result = userName + "play the drum";
        System.out.println(userName + "play the drum - " + System.currentTimeMillis());
        return result;
    }
}
