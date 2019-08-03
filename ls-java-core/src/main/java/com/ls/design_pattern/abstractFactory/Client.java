package com.ls.design_pattern.abstractFactory;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03
 */
public class Client {
    public static void main(String[] args) {
        AbstractFactory abstractFactory = new ConcreteFactory1();
        AbstractProductA productA = abstractFactory.createProductA();
        AbstractProductB productB = abstractFactory.createProductB();
        // do something with productA and productB
    }
}
