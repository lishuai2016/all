package com.ls.design_pattern.factory.simple;


import com.ls.design_pattern.factory.simple.factory.Factory;
import com.ls.design_pattern.factory.simple.product.BMW;

/**
 * 简单工厂方法模式客户端
 * Created by cipher on 2017/9/6.
 */
public class Customer {

    public static void main(String[] args) {
        Factory factory = new Factory();
        BMW bmw320 = factory.createBMW(320);
        BMW bmw523 = factory.createBMW(523);
    }

}
