package com.coder;

import com.coder.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 方式四：使用FactoryBean实例化bean
 * Spring会创建UserDaoFactoryBean实例，然后调用其getObject()方法来获取实际的bean
 * 注意：从容器中获取的bean是getObject()返回的对象，而不是FactoryBean本身
 */
public class AppForInstanceUserByFactoryBean {
    public static void main(String[] args) {
        // 获取IOC容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 得到Bean（获取的是UserDao实例，不是UserDaoFactoryBean）
        UserDao userDao = (UserDao) ctx.getBean("userDaoByFactoryBean");
        userDao.save();
        System.out.println("方式四：FactoryBean实例化bean - 完成");
        
        // 如果要获取FactoryBean本身，需要在bean id前加&符号
        // Object factoryBean = ctx.getBean("&userDaoByFactoryBean");
        // System.out.println("FactoryBean本身: " + factoryBean.getClass().getName());
    }
}
