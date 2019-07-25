package com.spring.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Logger class
 * 日志类，用于存放公共代码
 *
 * @author BowenWang
 * @date 2019/07/25
 */
@Component("logger")
/**
 * 表示当前类，是一个切面类
 */
@Aspect
public class Logger {

    /**
     * 设置切点
     */
    @Pointcut("execution(* com.spring.service.impl.*.*(..))")
    private void pt1(){}

    /**
     * 前置通知
     */
    @Before("pt1()")
    public void beforePrintLog() {
        System.out.println("前置通知...");
    }
    /**
     * 后置通知
     */
    @AfterReturning("pt1()")
    public void afterReturningPrintLog() {
        System.out.println("后置通知...");
    }
    /**
     * 异常通知
     */
    @AfterThrowing("pt1()")
    public void afterThrowingPrintLog() {
        System.out.println("异常通知...");
    }
    /**
     * 最终通知
     */
    @After("pt1()")
    public void afterPrintLog() {
        System.out.println("最终通知...");
    }

    /**
     * 环绕通知
     */
    //@Around("pt1()")
    public Object aroundPrintLog(ProceedingJoinPoint pjp) {
        Object rtValue = null;
        try {
            // 得到方法执行所需的参数
            Object[] args = pjp.getArgs();
            System.out.println("环绕通知——前置通知");
            // 明确调用业务层方法（切入点方法）
            rtValue = pjp.proceed(args);
            System.out.println("环绕通知——后置通知");
        } catch (Throwable throwable) {
            System.out.println("环绕通知——异常通知");
            throwable.printStackTrace();
        }finally {
            System.out.println("环绕通知——最终通知");
        }
        return rtValue;
    }
}
