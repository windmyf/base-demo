package com.myf.wind.base.factorydemo.factory;

import com.myf.wind.base.factorydemo.annotation.InstrumentAnnotation;
import com.myf.wind.base.factorydemo.service.InstrumentService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wind-myf
 * @desc : 乐器工厂 类，实现 ApplicationContextAware方式
 */
@Component
public class InstrumentFactory implements ApplicationContextAware {
    private static HashMap<String, InstrumentService> instrumentServiceMap = new HashMap<>();
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取包含音乐注解的类
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(InstrumentAnnotation.class);
        for (Object bean : beansWithAnnotationMap.values()) {
            InstrumentAnnotation annotation = bean.getClass().getAnnotation(InstrumentAnnotation.class);
            if (annotation == null){
                continue;
            }
            System.out.println(bean);
            instrumentServiceMap.put(annotation.instrumentType(), (InstrumentService) bean);
        }
    }

    public static InstrumentService getInstrumentService(String instrumentType){
        return instrumentServiceMap.get(instrumentType);
    }
}
