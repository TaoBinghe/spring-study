package com.coder;

import com.coder.service.BookSave;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 获取IOC容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 得到Bean
        BookSave bookService = (BookSave) ctx.getBean("bookService");
        bookService.saveBook();
    }
}
