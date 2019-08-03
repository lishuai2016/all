package com.ls;

import com.ls.domain.User;
import com.ls.domain.UserRepository;
import com.ls.util.ApplicationContextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-16 15:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private CacheManager cacheManager;

    @Before
    public void before() {
        userRepository.save(new User("AAA", 10));
    }

    @Test
    public void test() throws Exception {




        CacheManager bean = ApplicationContextUtils.applicationContext.getBean(CacheManager.class);
        System.out.println("缓存管理器："+bean);
        //获取EhCacheCacheManager类
//        EhCacheCacheManager cacheCacheManager=ApplicationContextUtils.applicationContext.getBean(EhCacheCacheManager.class);
//        //获取CacheManager类
//        net.sf.ehcache.CacheManager cacheManager=  cacheCacheManager.getCacheManager();



        User u1 = userRepository.findByName("AAA");
        System.out.println("第一次查询：" + u1.getAge());

        User u2 = userRepository.findByName("AAA");
        System.out.println("第二次查询：" + u2.getAge());

        u1.setAge(20);
        userRepository.save(u1);
        User u3 = userRepository.findByName("AAA");
        System.out.println("第三次查询：" + u3.getAge());

    }
}
