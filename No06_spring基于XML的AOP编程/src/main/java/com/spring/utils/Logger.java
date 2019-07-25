package com.spring.utils;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Logger class
 * 日志类，用于存放公共代码
 *
 * @author BowenWang
 * @date 2019/07/25
 */
public class Logger {
    /**
     * 前置通知
     */
    public void beforePrintLog() {
        System.out.println("前置通知...");
    }
    /**
     * 后置通知
     */
    public void afterReturningPrintLog() {
        System.out.println("后置通知...");
    }
    /**
     * 异常通知
     */
    public void afterThrowingPrintLog() {
        System.out.println("异常通知...");
    }
    /**
     * 最终通知
     */
    public void afterPrintLog() {
        System.out.println("最终通知...");
    }

    /**
     * 环绕通知
     * 问题：
     *      当我们配置了环绕通知之后，切入点方法没有执行，而通知方法执行了。
     * 分析：
     *      通过对比动态代理中的环绕通知代码，发现动态代理的环绕通知有明确的切入点方法调用，而我们的代码中没有。
     * 解决：
     *      Spring框架为我们提供了一个接口：ProceedingJoinPoint。该接口有一个方法proceed()，此方法就相当于明确调用切入点方法。
     *      该接口可以作为环绕通知的方法参数，在程序执行时，spring框架会为我们提供该接口的实现类供我们使用。
     *
     * spring中的环绕通知：
     *      它是spring框架为我们提供的一种可以在代码中手动控制增强方法何时执行的方式。
     */
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
