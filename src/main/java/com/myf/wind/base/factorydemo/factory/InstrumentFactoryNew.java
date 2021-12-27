package com.myf.wind.base.factorydemo.factory;

import com.myf.wind.base.factorydemo.annotation.InstrumentAnnotation;
import com.myf.wind.base.factorydemo.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author : wind-myf
 * @desc : 乐器工厂 类 (使用构造器方式)
 */
@Component
public class InstrumentFactoryNew{
    private static HashMap<String, InstrumentService> instrumentServiceMap = new HashMap<>();

    @Autowired
    public InstrumentFactoryNew(List<InstrumentService> instrumentServices){
        for (InstrumentService instrumentService : instrumentServices) {
            InstrumentAnnotation annotation = instrumentService.getClass().getAnnotation(InstrumentAnnotation.class);
            if (annotation == null){
                continue;
            }
            System.out.println(instrumentService);
            instrumentServiceMap.put(annotation.instrumentType(),instrumentService);
        }
    }

    public static InstrumentService getInstrumentService(String instrumentType){
        return instrumentServiceMap.get(instrumentType);
    }
}
