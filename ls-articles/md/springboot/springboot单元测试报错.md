---
title: springboot单元测试报错
categories: 
- Markdown
tags:
---


# 1、错误描述
springboot测试时出现：java.lang.IllegalStateException: 
Unable to find a @SpringBootConfiguration, 
you need to use @ContextConfiguration or @SpringBootTest(classes=...) with your test

问题：
如果springboot在主包的路径是com.ls.Application
在测试包下也许一个包路径com.ls ,因为在测试的时候需要在测试类的当前包路径下找到主类Application，
否则就会上面的错误。
一般情况下把主类Application放在项目包的根路径下。

springboot单元测试类的主要形式如下所示：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

}
```
