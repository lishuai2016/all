package com.ls.design_pattern.Command;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 14:57
 */
public class Client {
    public static void main(String[] args){
        Receiver receiver = new Receiver();
        Command command = new ConcreteCommand(receiver);
        //客户端直接执行具体命令方式（此方式与类图相符）
        command.execute();

        //上面的这样的流程很像线程的启动流程




        //客户端通过调用者来执行命令
        Invoker invoker = new Invoker();
        invoker.setCommand(command);
        invoker.action();
    }
}
