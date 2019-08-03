package com.ls.design_pattern.ChainOfResponsibility;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-04
 * Handler：定义处理请求的接口，并且实现后继链（successor）
 */
public abstract class Handler {
    protected Handler successor;//继任者，下一个处理对象
    public Handler(Handler successor) {
        this.successor = successor;
    }
    protected abstract void handleRequest(Request request);
}
