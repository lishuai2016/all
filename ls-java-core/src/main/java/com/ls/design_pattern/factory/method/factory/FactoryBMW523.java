package com.ls.design_pattern.factory.method.factory;


import com.ls.design_pattern.factory.method.product.BMW;
import com.ls.design_pattern.factory.method.product.BMW523;

/**
 * 具体工厂类
 * Created by cipher on 2017/9/6.
 */
public class FactoryBMW523 implements FactoryBMW {

    public BMW createBMW() {
        return new BMW523();
    }

}
