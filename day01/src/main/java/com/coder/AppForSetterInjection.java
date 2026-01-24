package com.coder;

import com.coder.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Setter注入示例
 * 
 * Setter注入的两种方式：
 * 1. 注入简单值（value）：用于注入String、int等基本类型和包装类型
 *    <property name="databaseName" value="mysql"/>
 *    <property name="connectionNum" value="10"/>
 * 
 * 2. 注入对象引用（ref）：用于注入其他bean对象
 *    <property name="bookDao" ref="bookDao"/>
 *    <property name="userDao" ref="userDao"/>
 * 
 * 工作原理：
 * - Spring会调用对应的setter方法来注入属性值
 * - 对于value，直接传入值
 * - 对于ref，从容器中获取对应的bean并传入
 */
public class AppForSetterInjection {
    public static void main(String[] args) {
        // 获取IOC容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        // 获取bookService bean，观察setter注入的效果
        BookService bookService = (BookService) ctx.getBean("bookService");
        bookService.save();
        
        System.out.println("\n=== Setter注入完成 ===");
    }
}
