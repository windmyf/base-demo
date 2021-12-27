package com.myf.wind.base;

import com.myf.wind.base.factorydemo.enums.InstrumentTypeEnum;
import com.myf.wind.base.factorydemo.factory.InstrumentFactory;
import com.myf.wind.base.factorydemo.factory.InstrumentFactoryNew;
import com.myf.wind.base.factorydemo.service.InstrumentService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class BaseDemoApplicationTests {

    @Test
    void contextLoads() {

    }

    @Test
    void testInstrumentFactory(){
        InstrumentService instrumentService = InstrumentFactory.getInstrumentService(InstrumentTypeEnum.PIANO.getCode());
        Assert.isTrue(instrumentService != null,"获取工厂类失败");
        String playInstrument = instrumentService.playInstrument("张三");
        System.out.println("playInstrument = " + playInstrument);
    }

    @Test
    void testInstrumentFactoryNew(){
        InstrumentService instrumentService = InstrumentFactoryNew.getInstrumentService(InstrumentTypeEnum.PIANO.getCode());
        Assert.isTrue(instrumentService != null,"获取工厂类失败");
        String playInstrument = instrumentService.playInstrument("李四");
        System.out.println("playInstrument = " + playInstrument);
    }

}
