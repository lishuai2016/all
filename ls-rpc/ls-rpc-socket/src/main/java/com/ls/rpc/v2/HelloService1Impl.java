package com.ls.rpc.v2;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-30 10:42
 */
public class HelloService1Impl implements HelloService1 {

    public String hello1(String name) {
        return "HelloService1Impl:Hello " + name;
    }
}
