package com.ls.design_pattern.factory.abs.factory;


import com.ls.design_pattern.factory.abs.product.Aircondition;
import com.ls.design_pattern.factory.abs.product.AirconditionA;
import com.ls.design_pattern.factory.abs.product.Engine;
import com.ls.design_pattern.factory.abs.product.EngineA;

/**
 * 为宝马320系列生产配件
 * Created by cipher on 2017/9/6.
 */
public class FactoryBMW320 implements FactoryBMW {

    public Engine createEngie() {
        return new EngineA();
    }

    public Aircondition createAircondition() {
        return new AirconditionA();
    }

}
