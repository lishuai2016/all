package com.ls;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-15 10:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    @Autowired
    private BlogProperties blogProperties;

    @Test
    public void test() {
        System.out.println(blogProperties);
    }


}
