package com.coder.factory;

import com.coder.dao.OrderDao;
import com.coder.dao.impl.OrderDaoImpl;

/**
 * 静态工厂类 - 用于方式二：使用静态工厂实例化bean
 */
public class OrderDaoFactory {
    /**
     * 静态工厂方法
     * @return OrderDao实例
     */
    public static OrderDao getOrderDao() {
        System.out.println("使用静态工厂方法创建OrderDao实例");
        return new OrderDaoImpl();
    }
}
