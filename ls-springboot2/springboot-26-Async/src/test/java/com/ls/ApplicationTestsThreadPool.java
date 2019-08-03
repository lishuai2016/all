package com.ls;

import com.ls.asyncThreadPool.TaskThreadPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: lishuai-notes
 * @author: lishuai
 *
2018-12-16 21:17:40.336  INFO 1940 --- [ taskExecutor-1] com.ls.asyncThreadPool.TaskThreadPool    : 开始做任务一
2018-12-16 21:17:40.337  INFO 1940 --- [ taskExecutor-2] com.ls.asyncThreadPool.TaskThreadPool    : 开始做任务二
2018-12-16 21:17:40.337  INFO 1940 --- [ taskExecutor-3] com.ls.asyncThreadPool.TaskThreadPool    : 开始做任务三
2018-12-16 21:17:41.560  INFO 1940 --- [ taskExecutor-3] com.ls.asyncThreadPool.TaskThreadPool    : 完成任务三，耗时：1223毫秒
2018-12-16 21:17:45.034  INFO 1940 --- [ taskExecutor-2] com.ls.asyncThreadPool.TaskThreadPool    : 完成任务二，耗时：4697毫秒
2018-12-16 21:17:45.906  INFO 1940 --- [ taskExecutor-1] com.ls.asyncThreadPool.TaskThreadPool    : 完成任务一，耗时：5565毫秒
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTestsThreadPool {

    @Autowired
    private TaskThreadPool taskThreadPool;

    @Test
    public void test() throws Exception {

        taskThreadPool.doTaskOne();
        taskThreadPool.doTaskTwo();
        taskThreadPool.doTaskThree();

        Thread.currentThread().join();
    }
}
