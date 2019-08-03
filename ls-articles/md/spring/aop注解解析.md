---
title: aop注解解析
categories: 
- spring
tags:
---

aop切面相关

测试代码：
com.ls.demo.controller.HelloWorldController
com.ls.demo.aop.Aspect1
com.ls.demo.aop.LimitAspect
com.ls.demo.aop.ServiceLimit

总结：
1、单个切面的执行顺序：
1.1、正常：@Around方法执行前--->@Before--->method--->@Around方法执行后--->@After--->@AfterReturning
[Aspect1] around advise 1
[Aspect1] before advise
test OK
[Aspect1] around advise2
[Aspect1] after advise
[Aspect1] afterReturning advise

1.2、异常：@Around方法执行前--->@Before--->method--->@Around方法异常--->@After--->@AfterReturning
[Aspect1] around advise 1
[Aspect1] before advise
throw an exception
要是再around有catch会执行
[Aspect1] after advise
[Aspect1] afterThrowing advise


2、多个切面：
2.1、正常
[Aspect2] around advise 1
[Aspect2] before advise
[Aspect1] around advise 1
[Aspect1] before advise
test OK
[Aspect1] around advise2
[Aspect1] after advise
[Aspect1] afterReturning advise
[Aspect2] around advise2
[Aspect2] after advise
[Aspect2] afterReturning advise


2.2、异常
[Aspect2] around advise 1
[Aspect2] before advise
[Aspect1] around advise 1
[Aspect1] before advise
throw an exception
[Aspect1] after advise
[Aspect1] afterThrowing advise
[Aspect2] after advise
[Aspect2] afterThrowing advise




一、两个切面正常的执行情况

[Aspect1] around advise before
[Aspect1] before advise
【环绕通知中的--->前置通知】：the method 【test】 begins with [false]
【前置通知】the method 【test】 begins with [false]
test OK
【环绕通知中的--->返回通知】：the method 【test】 ends with null
【环绕通知中的--->后置通知】：-----------------end.----------------------
【后置通知】this is a afterMethod advice...
【返回通知】the method 【test】 ends with 【helloworld1】
[Aspect1] around advise after
[Aspect1] after advise
[Aspect1] afterReturning advise



二、两个切面，在controller中抛异常
[Aspect1] around advise before
[Aspect1] before advise
【环绕通知中的--->前置通知】：the method 【test】 begins with [true]
【前置通知】the method 【test】 begins with [true]

throw an exception
java.lang.Exception: mock a server exception
	
【环绕通知中的--->异常通知】：the method 【test】 occurs exception java.lang.Exception: mock a server exception
【环绕通知中的--->后置通知】：-----------------end.----------------------
【后置通知】this is a afterMethod advice...
【返回通知】the method 【test】 ends with 【null】
[Aspect1] around advise after
[Aspect1] after advise
[Aspect1] afterReturning advise




@Aspect   修饰类的切面
@Pointcut  定义一个公用的切入点
@Around  环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码（jp.proceed()在它的执行前后执行一些逻辑）
@Before  前置通知：目标方法执行之前执行以下方法体的内容
@After   后置通知：目标方法执行之后执行以下方法体的内容，不管是否发生异常
@AfterReturning  返回通知：目标方法正常执行完毕时执行以下代码
@AfterThrowing   异常通知：目标方法发生异常的时候执行以下代码




@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Aspect {
    String value() default "";
}





@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Pointcut {
    String value() default "";

    String argNames() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Around {
    String value();

    String argNames() default "";
}







参考：
https://blog.csdn.net/rainbow702/article/details/52185827