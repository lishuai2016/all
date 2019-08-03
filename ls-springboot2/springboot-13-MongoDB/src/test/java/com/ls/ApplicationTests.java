package com.ls;

import com.ls.domain.User;
import com.ls.domain.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-16 13:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void test() throws Exception {

        // 创建三个User，并验证User总数
        userRepository.save(new User(1L, "didi", 30));
        userRepository.save(new User(2L, "mama", 40));
        userRepository.save(new User(3L, "kaka", 50));
        Assert.assertEquals(3, userRepository.findAll().size());

        // 删除一个User，再验证User总数
//        User u = userRepository.findOne(1L);
//        userRepository.delete(u);
//        Assert.assertEquals(2, userRepository.findAll().size());

        // 删除一个User，再验证User总数
        User  u = userRepository.findByUsername("mama");
        userRepository.delete(u);
        Assert.assertEquals(2, userRepository.findAll().size());

    }
}
