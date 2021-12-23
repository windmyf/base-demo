package com.myf.wind.base.rpcdemo.service.impl;

import com.myf.wind.base.rpcdemo.service.Car;

/**
 * @author wind_myf
 */
public class CarImpl implements Car {

    @Override
    public String drive(String city) {
        String s = "I want to drive to go " + city;
        System.out.println("CarImpl - drive : " + s);
        return s;
    }
}
