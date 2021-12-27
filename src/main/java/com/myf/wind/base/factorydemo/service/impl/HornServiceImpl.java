package com.myf.wind.base.factorydemo.service.impl;

import com.myf.wind.base.factorydemo.annotation.InstrumentAnnotation;
import com.myf.wind.base.factorydemo.service.InstrumentService;
import org.springframework.stereotype.Service;

/**
 * @author : wind-myf
 * @desc : 喇叭实现类
 */
@Service("hornService")
@InstrumentAnnotation(instrumentType = "HORN")
public class HornServiceImpl implements InstrumentService {
    @Override
    public String playInstrument(String userName) {
        // 模拟业务逻辑
        String result = userName + "play the horn";
        System.out.println(userName + "play the horn - " + System.currentTimeMillis());
        return result;
    }
}
