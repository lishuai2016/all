package com.ls.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-08
 */
public class Test_atomic {

    public AtomicInteger data = new AtomicInteger(0);
    //多线程调用  data.incrementAndGet();

    void method() {
        data.incrementAndGet();
    }

}
