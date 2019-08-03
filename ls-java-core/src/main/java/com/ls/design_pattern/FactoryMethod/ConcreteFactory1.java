package com.ls.design_pattern.FactoryMethod;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 23:11
 */
public class ConcreteFactory1 extends Factory {
    @Override
    public Product factoryMethod() {
        return new ConcreteProduct1();
    }
}
