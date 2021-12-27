package com.myf.wind.base.factorydemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 乐器类注解
 * @author wind_myf
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InstrumentAnnotation {
    String instrumentType() default "PIANO";
}
