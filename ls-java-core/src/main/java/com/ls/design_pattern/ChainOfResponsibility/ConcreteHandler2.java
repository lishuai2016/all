package com.ls.design_pattern.ChainOfResponsibility;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-04 09:37
 */
public class ConcreteHandler2 extends Handler {
    public ConcreteHandler2(Handler successor) {
        super(successor);
    }
    @Override
    protected void handleRequest(Request request) {
        if (request.getType() == RequestType.TYPE2) {
            System.out.println(request.getName() + " is handle by ConcreteHandler2");
            return;
        }
        if (successor != null){   //让下一个处理器处理
            successor.handleRequest(request);
        }
    }
}
