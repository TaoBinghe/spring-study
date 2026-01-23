package com.coder;

import com.coder.dao.OrderDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 方式二：使用静态工厂实例化bean
 * Spring会调用OrderDaoFactory的静态方法getOrderDao()来创建bean实例
 */
public class AppForInstanceOrder {
    public static void main(String[] args) {
        // 获取IOC容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 得到Bean
        OrderDao orderDao = (OrderDao) ctx.getBean("orderDao");
        orderDao.save();
        System.out.println("方式二：静态工厂实例化bean - 完成");
    }
}
