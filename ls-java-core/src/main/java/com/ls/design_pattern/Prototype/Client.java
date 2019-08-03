package com.ls.design_pattern.Prototype;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 23:45
 */
public class Client {
    public static void main(String[] args) {
        Prototype prototype = new ConcretePrototype("abc");
        Prototype clone = prototype.myclone();
        System.out.println(clone.toString());

    }
}
