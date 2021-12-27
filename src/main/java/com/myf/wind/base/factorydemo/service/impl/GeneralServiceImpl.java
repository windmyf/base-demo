package com.myf.wind.base.factorydemo.service.impl;

import com.myf.wind.base.factorydemo.annotation.InstrumentAnnotation;
import com.myf.wind.base.factorydemo.service.InstrumentService;
import org.springframework.stereotype.Service;

/**
 * @author : wind-myf
 * @desc : 通用实现类
 */
@Service("generalService")
@InstrumentAnnotation(instrumentType = "GENERAL")
public class GeneralServiceImpl implements InstrumentService {
    @Override
    public String playInstrument(String userName) {
        // 模拟业务逻辑
        String result = userName + "play the general instrument";
        System.out.println(userName + "play the general instrument - " + System.currentTimeMillis());
        return result;
    }
}
