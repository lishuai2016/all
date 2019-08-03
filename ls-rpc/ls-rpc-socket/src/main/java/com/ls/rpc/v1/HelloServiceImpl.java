package com.ls.rpc.v1;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-30 09:34
 */
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello " + name;

    }

}
