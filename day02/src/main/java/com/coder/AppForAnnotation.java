package com.coder;

import com.coder.config.SpringConfig;
import com.coder.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 纯注解开发演示
 * 
 * XML配置方式：
 * <beans>
 *     <context:component-scan base-package="com.coder"/>
 * </beans>
 * 
 * 注解配置方式：
 * @Configuration
 * @ComponentScan("com.coder")
 * public class SpringConfig {
 * }
 * 
 * 使用方式：
 * - XML方式：new ClassPathXmlApplicationContext("applicationContext.xml")
 * - 注解方式：new AnnotationConfigApplicationContext(SpringConfig.class)
 * 
 * 优点：
 * - 使用Java类替代XML配置文件
 * - 类型安全，编译期检查
 * - 简化配置，提高开发效率
 */
public class AppForAnnotation {
    public static void main(String[] args) {
        System.out.println("========== 纯注解开发演示 ==========");
        
        // 使用注解配置类创建IOC容器
        // 替代：ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        
        // 获取Bean
        BookService bookService = ctx.getBean(BookService.class);
        bookService.save();
        
        System.out.println("\n=== 注解配置说明 ===");
        System.out.println("@Configuration - 标识配置类");
        System.out.println("@ComponentScan - 设置扫描路径");
        System.out.println("@Service - 标识业务层组件");
        System.out.println("@Repository - 标识数据访问层组件");
        System.out.println("@Autowired - 自动注入依赖");
        System.out.println("================================");
    }
}
