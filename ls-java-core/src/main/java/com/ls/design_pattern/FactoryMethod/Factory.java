package com.ls.design_pattern.FactoryMethod;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 23:05
 */
public abstract class Factory {
    abstract public Product factoryMethod();

    public void doSomething(){
        Product product = factoryMethod();
        // do something with the product
    }
}
