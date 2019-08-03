package com.ls.web.aop;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-09
 */
@Component
@Scope
@Aspect
public class LimitAspect {
    ////每秒只发出0.5个令牌，此处是单进程服务的限流,内部采用令牌捅算法实现
    private static RateLimiter rateLimiter = RateLimiter.create(0.5);

    //Service层切点  限流
    @Pointcut("@annotation(com.ls.web.annotation.ServiceLimit)")
    public void ServiceAspect() {

    }

    @Around("ServiceAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        Boolean flag = rateLimiter.tryAcquire();
        Object obj = null;
        try {
            if(flag){
                obj = joinPoint.proceed();
            } else {
                return "限流了";  //可以返回一些状态码告诉请求方
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }
}
