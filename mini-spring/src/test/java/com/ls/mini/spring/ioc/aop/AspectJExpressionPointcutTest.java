package com.ls.mini.spring.ioc.aop;

import com.ls.mini.spring.ioc.HelloWorldService;
import com.ls.mini.spring.ioc.HelloWorldServiceImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 21:54
 *
 * 拦截器匹配测试
 */
public class AspectJExpressionPointcutTest {

    @Test
    public void testClassFilter() throws Exception {
        String expression = "execution(* com.ls.mini.spring.ioc.*.*(..))";
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);
        boolean matches = aspectJExpressionPointcut.getClassFilter().matches(HelloWorldService.class);
        Assert.assertTrue(matches);
    }

    @Test
    public void testMethodInterceptor() throws Exception {
        String expression = "execution(* com.ls.mini.spring.ioc.*.*(..))";
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);
        boolean matches = aspectJExpressionPointcut.getMethodMatcher().matches(HelloWorldServiceImpl.class.getDeclaredMethod("helloWorld"),HelloWorldServiceImpl.class);
        Assert.assertTrue(matches);
    }

}
