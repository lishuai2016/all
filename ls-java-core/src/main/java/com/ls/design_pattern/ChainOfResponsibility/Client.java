package com.ls.design_pattern.ChainOfResponsibility;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-04
 */
public class Client {
    public static void main(String[] args) {
        ConcreteHandler1 handler1 = new ConcreteHandler1(null);
        ConcreteHandler2 handler2 = new ConcreteHandler2(handler1);
        Request request = new Request(RequestType.TYPE1,"请求类型1");
        handler2.handleRequest(request);

        Request request2 = new Request(RequestType.TYPE2,"请求类型2");
        handler2.handleRequest(request2);
    }
}
