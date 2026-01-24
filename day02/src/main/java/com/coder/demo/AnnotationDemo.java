package com.coder.demo;

import com.coder.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 注解演示类 - 展示常用注解的使用
 */
@Component
public class AnnotationDemo {
    
    // ========== 1. 自动装配 (Automatic Assembly) ==========
    
    /**
     * @Autowired - 自动注入依赖
     * 按类型匹配，自动注入BookDao的实现类
     */
    @Autowired
    private BookDao bookDao;
    
    /**
     * @Qualifier - 指定Bean名称
     * 当有多个同类型的Bean时，使用@Qualifier指定要注入的Bean名称
     * 需要和@Autowired一起使用
     */
    // @Autowired
    // @Qualifier("bookDaoImpl")
    // private BookDao bookDao2;
    
    /**
     * @Value - 注入简单值
     * 方式一：注入普通值
     */
    @Value("mysql")
    private String databaseName;
    
    /**
     * @Value - 注入简单值
     * 方式二：注入properties文件中的值（需要配合@PropertySource使用）
     * 使用 ${属性名} 格式读取properties文件中的值
     */
    @Value("${jdbc.driver}")
    private String driver;
    
    @Value("${jdbc.url}")
    private String url;
    
    @Value("${jdbc.username}")
    private String username;
    
    @Value("${jdbc.password}")
    private String password;
    
    /**
     * 演示方法 - 打印所有注入的值
     */
    public void show() {
        System.out.println("========== 注解演示 ==========");
        System.out.println("1. @Autowired 自动注入:");
        System.out.println("   bookDao: " + bookDao.getClass().getSimpleName());
        
        System.out.println("\n2. @Value 注入简单值:");
        System.out.println("   databaseName: " + databaseName);
        System.out.println("   driver: " + driver);
        System.out.println("   url: " + url);
        System.out.println("   username: " + username);
        System.out.println("   password: " + password);
        
        System.out.println("\n3. @Qualifier 使用说明:");
        System.out.println("   当有多个同类型Bean时，使用@Qualifier指定Bean名称");
        System.out.println("   示例: @Autowired @Qualifier(\"bookDaoImpl\")");
        
        System.out.println("\n4. @PropertySource 使用说明:");
        System.out.println("   在配置类上使用 @PropertySource(\"jdbc.properties\")");
        System.out.println("   然后使用 @Value(\"${属性名}\") 读取properties文件中的值");
        System.out.println("=================================");
    }
}
