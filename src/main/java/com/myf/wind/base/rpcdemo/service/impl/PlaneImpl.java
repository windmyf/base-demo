package com.myf.wind.base.rpcdemo.service.impl;

import com.myf.wind.base.rpcdemo.service.Plane;

/**
 * @author wind_myf
 */
public class PlaneImpl implements Plane {

    @Override
    public void fly(String city) {
        System.out.println("PlaneImpl fly : I want to fly to " + city);
    }
}
