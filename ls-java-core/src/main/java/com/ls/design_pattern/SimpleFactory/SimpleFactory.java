package com.ls.design_pattern.SimpleFactory;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03
 */
public class SimpleFactory {
    public Product createProduct(int type) {
        if (type == 1) {
            return new ConcreteProduct1();
        } else if (type == 2) {
            return new ConcreteProduct2();
        }
        return new ConcreteProduct();
    }
}
