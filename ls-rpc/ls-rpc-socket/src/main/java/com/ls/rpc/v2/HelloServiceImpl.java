package com.ls.rpc.v2;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-30 09:34
 */
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "HelloServiceImpl:Hello " + name;
    }

}
