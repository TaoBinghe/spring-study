package com.coder.factory;

import com.coder.dao.UserDao;
import com.coder.dao.impl.UserDaoImpl;
import org.springframework.beans.factory.FactoryBean;

/**
 * FactoryBean方式实例化bean
 * 代替原始实例工厂中创建对象的方法
 */
public class UserDaoFactoryBean implements FactoryBean<UserDao> {
    
    /**
     * 返回要创建的bean实例
     * @return UserDao实例
     * @throws Exception
     */
    @Override
    public UserDao getObject() throws Exception {
        System.out.println("使用FactoryBean创建UserDao实例");
        return new UserDaoImpl();
    }

    /**
     * 返回要创建的bean类型
     * @return UserDao的Class对象
     */
    @Override
    public Class<?> getObjectType() {
        return UserDao.class;
    }
}
