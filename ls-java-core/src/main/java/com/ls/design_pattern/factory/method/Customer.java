package com.ls.design_pattern.factory.method;


import com.ls.design_pattern.factory.method.factory.FactoryBMW;
import com.ls.design_pattern.factory.method.factory.FactoryBMW320;
import com.ls.design_pattern.factory.method.factory.FactoryBMW523;

/**
 * 工厂方法模式客户端
 * Created by cipher on 2017/9/6.
 */
public class Customer {

    public static void main(String[] args) {
        FactoryBMW factoryBMW320 = new FactoryBMW320();
        factoryBMW320.createBMW();

        FactoryBMW factoryBMW523 = new FactoryBMW523();
        factoryBMW523.createBMW();
    }

}
