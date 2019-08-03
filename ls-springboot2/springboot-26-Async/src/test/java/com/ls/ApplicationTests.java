package com.ls;

import com.ls.async.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Future;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-16
默认的输出日志：
开始做任务三SimpleAsyncTaskExecutor-3
开始做任务一SimpleAsyncTaskExecutor-1
开始做任务二SimpleAsyncTaskExecutor-2
完成任务一，耗时：2434毫秒SimpleAsyncTaskExecutor-1
完成任务二，耗时：4018毫秒SimpleAsyncTaskExecutor-2
完成任务三，耗时：5474毫秒SimpleAsyncTaskExecutor-3
任务全部完成，总耗时：6005毫秒

自定义线程池日志：
开始做任务一taskExecutor-1
开始做任务三taskExecutor-3
开始做任务二taskExecutor-2
完成任务一，耗时：2439毫秒taskExecutor-1
完成任务二，耗时：4079毫秒taskExecutor-2
完成任务三，耗时：5320毫秒taskExecutor-3
任务全部完成，总耗时：6003毫秒
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

        @Autowired
        private Task task;

        @Test
        public void test() throws Exception {

            long start = System.currentTimeMillis();

            Future<String> task1 = task.doTaskOne();
            Future<String> task2 = task.doTaskTwo();
            Future<String> task3 = task.doTaskThree();

            while(true) {
                if(task1.isDone() && task2.isDone() && task3.isDone()) {
                    // 三个任务都调用完成，退出循环等待
                    break;
                }
                Thread.sleep(1000);
            }

            long end = System.currentTimeMillis();

            System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");

        }

    }

