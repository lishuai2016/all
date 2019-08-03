package com.ls.mini.spring.ioc;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 22:42
 */
public class OutputServiceImpl implements OutputService {

    @Override
    public void output(String text){
        System.out.println(text);
    }

}
