package com.ls.design_pattern.factory.abs.factory;


import com.ls.design_pattern.factory.abs.product.Aircondition;
import com.ls.design_pattern.factory.abs.product.Engine;

/**
 * 抽象工厂类，制造多个产品
 * Created by cipher on 2017/9/6.
 */
public interface FactoryBMW {

    // 制造发动机
    Engine createEngie();

    // 制造空调
    Aircondition createAircondition();

}
