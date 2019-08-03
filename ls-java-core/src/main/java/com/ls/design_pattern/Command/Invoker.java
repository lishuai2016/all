package com.ls.design_pattern.Command;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 14:57
 */
public class Invoker {
    private Command command;
    public void setCommand(Command command) {
        this.command = command;
    }
    public void action(){
        this.command.execute();
    }
}
