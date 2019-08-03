package com.ls.design_pattern.FactoryMethod;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 23:12
 */
public class ConcreteFactory2 extends Factory {
    @Override
    public Product factoryMethod() {
        return new ConcreteProduct2();
    }
}
