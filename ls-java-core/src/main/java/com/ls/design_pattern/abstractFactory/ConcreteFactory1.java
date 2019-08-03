package com.ls.design_pattern.abstractFactory;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03
 */
public class ConcreteFactory1 extends AbstractFactory {
    @Override
    AbstractProductA createProductA() {
        return new ProductA1();
    }

    @Override
    AbstractProductB createProductB() {
        return new ProductB1();
    }
}
