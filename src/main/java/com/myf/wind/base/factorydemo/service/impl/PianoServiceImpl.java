package com.myf.wind.base.factorydemo.service.impl;

import com.myf.wind.base.factorydemo.annotation.InstrumentAnnotation;
import com.myf.wind.base.factorydemo.enums.InstrumentTypeEnum;
import com.myf.wind.base.factorydemo.service.InstrumentService;
import org.springframework.stereotype.Service;

/**
 * @author : wind-myf
 * @desc : 钢琴实现类
 */
@Service("pianoService")
@InstrumentAnnotation(instrumentType = "PIANO")
public class PianoServiceImpl implements InstrumentService {
    @Override
    public String playInstrument(String userName) {
        // 模拟业务逻辑
        String result = userName + "play the piano";
        System.out.println(userName + "play the piano - " + System.currentTimeMillis());
        return result;
    }
}
