package com.coder;

import com.coder.config.SpringConfig;
import com.coder.demo.AnnotationDemo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 注解演示测试类
 */
public class AppForAnnotationDemo {
    public static void main(String[] args) {
        // 创建IOC容器
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        
        // 获取演示类
        AnnotationDemo demo = ctx.getBean(AnnotationDemo.class);
        
        // 展示所有注解的使用效果
        demo.show();
    }
}
