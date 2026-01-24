package com.coder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 加载properties文件示例
 * 
 * 步骤：
 * 1. 开启context命名空间
 *    - 在beans标签中添加: xmlns:context="http://www.springframework.org/schema/context"
 *    - 在xsi:schemaLocation中添加context的schema地址
 * 
 * 2. 使用context:property-placeholder加载properties文件
 *    <context:property-placeholder location="jdbc.properties"/>
 * 
 * 3. 使用属性占位符${}读取properties文件中的属性
 *    <property name="driverClassName" value="${jdbc.driver}"/>
 * 
 * 优点：
 * - 将配置信息从代码中分离出来
 * - 便于修改和维护
 * - 不同环境可以使用不同的properties文件
 */
public class AppForProperties {
    public static void main(String[] args) {
        // 获取IOC容器
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        System.out.println("========== 加载properties文件示例 ==========");
        System.out.println("1. 开启context命名空间");
        System.out.println("2. 使用context:property-placeholder加载properties文件");
        System.out.println("3. 使用${}占位符读取properties文件中的属性");
        System.out.println("=============================================");
        
        // 注意：数据源配置已注释，如需测试请取消注释
        // DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        // System.out.println("数据源加载成功");
    }
}
