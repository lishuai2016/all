package com.ls.design_pattern.Command;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 14:55
 */
public class ConcreteCommand extends Command {

    private Receiver receiver;
    public ConcreteCommand(Receiver receiver) {
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        receiver.doSomething();
    }
}
