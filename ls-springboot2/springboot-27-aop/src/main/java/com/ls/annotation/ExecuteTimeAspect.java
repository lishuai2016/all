package com.ls.annotation;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-18
 */
@Component
@Aspect
@Slf4j
public class ExecuteTimeAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("@annotation(com.ls.annotation.executeTime)")
    public void piontCut() {

    }

    @Around("piontCut()")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable{
        startTime.set(System.currentTimeMillis());
        String serviceName = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
        try {
            Object returnVal = pjp.proceed();
            log.info("Service: {}, Args: {}, Return: {}, Cost: {}.", serviceName, Arrays.toString(pjp.getArgs()),
                    JSON.toJSONString(returnVal), (System.currentTimeMillis() - startTime.get()) + "ms");
            return returnVal;
        } catch (Throwable throwable) {
            String info = String.format("Service: %s, Args: %s, Exception: %s.",
                    serviceName, Arrays.toString(pjp.getArgs()), ExceptionUtils.getStackTrace(throwable));
            log.error(info);
            throw throwable;
        }
    }


}
