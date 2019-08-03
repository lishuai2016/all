package com.ls.mini.spring.ioc.aop;

import com.ls.mini.spring.ioc.HelloWorldService;
import com.ls.mini.spring.ioc.context.ApplicationContext;
import com.ls.mini.spring.ioc.context.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 17:50
 */

/**
 输出内容如下：

 Invocation of Method helloWorld start!
 Invocation of Method output start!
 Hello World!
 Invocation of Method output end! takes 65314 nanoseconds.
 Invocation of Method helloWorld end! takes 722611 nanoseconds.


 说明通过xml配置的嵌入的内容生效了
 */

public class JdkDynamicAopProxyTest {
    @Test
    public void testInterceptor() throws Exception {
        // --------- helloWorldService without AOP
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("tinyioc.xml");
        HelloWorldService helloWorldService = (HelloWorldService) applicationContext.getBean("helloWorldService");
        helloWorldService.helloWorld();
//
//        // --------- helloWorldService with AOP
//        // 1. 设置被代理对象(Joinpoint)
//        AdvisedSupport advisedSupport = new AdvisedSupport();
//        TargetSource targetSource = new TargetSource(helloWorldService, HelloWorldService.class);
//        advisedSupport.setTargetSource(targetSource);
//
//        // 2. 设置拦截器(Advice)
//        TimerInterceptor timerInterceptor = new TimerInterceptor();
//        advisedSupport.setMethodInterceptor(timerInterceptor);
//
//        // 3. 创建代理(Proxy) 这里返回的也是代理的对象，因为加入了切点的匹配，导致TimerInterceptor内容不会执行
//        JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupport);
//        HelloWorldService helloWorldServiceProxy = (HelloWorldService) jdkDynamicAopProxy.getProxy();
//
//        // 4. 基于AOP的调用
//        helloWorldServiceProxy.helloWorld();

    }
}
