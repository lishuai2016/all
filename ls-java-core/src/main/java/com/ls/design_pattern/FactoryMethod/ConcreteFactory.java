package com.ls.design_pattern.FactoryMethod;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 23:08
 */
public class ConcreteFactory extends Factory {

    @Override
    public Product factoryMethod() {
        return new ConcreteProduct();
    }
}
