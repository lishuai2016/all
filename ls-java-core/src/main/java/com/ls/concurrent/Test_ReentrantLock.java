package com.ls.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-08 19:52
 */
public class Test_ReentrantLock {
    private static ReentrantLock reentrantLock = new ReentrantLock();

    void metnod() {
        reentrantLock.lock();
        //业务逻辑处理
        reentrantLock.unlock();

    }
}
