package com.ls.design_pattern.SimpleFactory;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03
 */
public class Client {
    public static void main(String[] args) {
        SimpleFactory simpleFactory = new SimpleFactory();
        Product product = simpleFactory.createProduct(1);
        //dosomething
    }
//不正确的示例
//    public static void main(String[] args) {
//        int type = 1;
//        Product product;
//        if (type == 1) {
//            product = new ConcreteProduct1();
//        } else if (type == 2) {
//            product = new ConcreteProduct2();
//        } else {
//            product = new ConcreteProduct();
//        }
//        // do something with the product
//    }
}
