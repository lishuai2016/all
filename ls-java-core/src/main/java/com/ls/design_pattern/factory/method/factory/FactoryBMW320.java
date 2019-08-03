package com.ls.design_pattern.factory.method.factory;


import com.ls.design_pattern.factory.method.product.BMW;
import com.ls.design_pattern.factory.method.product.BMW320;

/**
 * 具体工厂类
 * Created by cipher on 2017/9/6.
 */
public class FactoryBMW320 implements FactoryBMW {

    public BMW createBMW() {
        return new BMW320();
    }

}
