package com.ls.mini.spring.ioc;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 17:52
 */
public class HelloWorldServiceImpl implements HelloWorldService {
    private String text;

    private OutputService outputService;

    @Override
    public void helloWorld(){
        outputService.output(text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOutputService(OutputService outputService) {
        this.outputService = outputService;
    }
}
