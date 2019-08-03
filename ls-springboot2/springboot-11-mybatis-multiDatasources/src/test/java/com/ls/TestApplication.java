package com.ls;

import com.ls.mapper.primary.UserMapper;
import com.ls.mapper.secondary.MessageMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-15 20:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplication {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void test() throws Exception {
        System.out.println(userMapper.selectUserList().size());
        System.out.println(messageMapper.selectMessageList().size());
    }

}
