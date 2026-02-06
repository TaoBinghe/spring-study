package com.coder.aop;

import com.coder.config.SpringConfig;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAdvice {
    @Pointcut("execution(void com.coder.dao.BookDao.update())")
    private void pt(){};

    @Before("pt()")
    public void method(JoinPoint joinPoint) {
        // 获取参数
        Object[] args = joinPoint.getArgs();
        System.out.println("MyAdvice");
    }

    @Around("pt()")
    public Object round(ProceedingJoinPoint pjp) throws Throwable
    {
        System.out.println("before round");
        // 对原始功能的调用
        Object ret = pjp.proceed();
        System.out.println("after round");
        return ret;
    }

    // 获取返回值
    @AfterReturning(value="pt()", returning = "ret")
    public void afterReturning(Object ret) {

    }
}
