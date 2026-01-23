package com.coder.factory;

import com.coder.dao.UserDao;
import com.coder.dao.impl.UserDaoImpl;

/**
 * 实例工厂类 - 用于方式三：使用实例工厂实例化bean
 */
public class UserDaoFactory {
    /**
     * 实例工厂方法
     * @return UserDao实例
     */
    public UserDao getUserDao() {
        System.out.println("使用实例工厂方法创建UserDao实例");
        return new UserDaoImpl();
    }
}
