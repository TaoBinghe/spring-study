package com.coder;

import com.coder.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 方式三：使用实例工厂实例化bean
 * Spring会先创建UserDaoFactory实例，然后调用其getUserDao()方法来创建bean实例
 */
public class AppForInstanceUser {
    public static void main(String[] args) {
        // 获取IOC容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 得到Bean
        UserDao userDao = (UserDao) ctx.getBean("userDao");
        userDao.save();
        System.out.println("方式三：实例工厂实例化bean - 完成");
    }
}
