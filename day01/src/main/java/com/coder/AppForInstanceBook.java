package com.coder;

import com.coder.dao.BookDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 方式一：构造方法实例化bean
 * Spring会调用BookDaoImpl的无参构造方法来创建bean实例
 */
public class AppForInstanceBook {
    public static void main(String[] args) {
        // 获取IOC容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 得到Bean
        BookDao bookDao = (BookDao) ctx.getBean("bookDao");
        bookDao.save();
        System.out.println("方式一：构造方法实例化bean - 完成");
    }
}
