package com.coder.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Spring配置类 - 纯注解开发
 * 
 * Spring 3.0开启了纯注解开发模式，使用Java类替代配置文件，简化Spring开发
 * Java类替代Spring核心配置文件
 * 
 * ========== 基础注解 ==========
 * 
 * @Configuration 注解：
 * - 用于标识当前类是一个配置类
 * - 替代XML配置文件
 * 
 * @ComponentScan 注解：
 * - 用于设置扫描路径，等价于XML中的 <context:component-scan base-package="..."/>
 * - 只能添加一次，多个扫描路径使用数组格式
 * 
 * ========== 读取properties文件 ==========
 * 
 * @PropertySource 注解：
 * - 用于加载properties文件
 * - 等价于XML中的 <context:property-placeholder location="..."/>
 * - 可以加载多个文件：@PropertySource({"jdbc.properties", "msg.properties"})
 * 
 * ========== 自动装配注解 ==========
 * 
 * @Autowired 注解：
 * - 自动注入依赖，按类型匹配
 * - 可以标注在字段、构造方法、setter方法上
 * - 示例：@Autowired private BookDao bookDao;
 * 
 * @Qualifier 注解：
 * - 当有多个同类型的Bean时，指定要注入的Bean名称
 * - 需要和@Autowired一起使用
 * - 示例：@Autowired @Qualifier("bookDaoImpl") private BookDao bookDao;
 * 
 * @Value 注解：
 * - 注入简单值（String、int等）
 * - 可以注入properties文件中的值：@Value("${jdbc.url}")
 * - 也可以注入普通值：@Value("mysql")
 * - 示例：@Value("${jdbc.driver}") private String driver;
 * 
 * ========== 第三方bean管理 ==========
 * 
 * @Bean 注解：
 * - 用于标识方法，该方法返回的对象会被Spring管理为Bean
 * - 方法名默认为Bean的id，也可以通过@Bean("beanName")指定
 * - 适用于管理第三方类库的Bean（无法使用@Component等注解的类）
 * 
 * @Import 注解：
 * - 使用@Import注解手动加入配置类到核心配置
 * - 此注解只能添加一次，多个配置类请用数组格式
 * - 示例：@Import(JdbcConfig.class)
 * - 多个配置类：@Import({JdbcConfig.class, MybatisConfig.class})
 */
@Configuration
// 方式一：单个扫描路径
@ComponentScan("com.coder")

// 方式二：多个扫描路径（使用数组格式）
// @ComponentScan({"com.coder.service", "com.coder.dao"})

// 读取properties文件
@PropertySource("jdbc.properties")
// 读取多个properties文件
// @PropertySource({"jdbc.properties", "msg.properties"})

// 将独立的配置类加入核心配置 - 方式一:导入式
@Import(JdbcConfig.class)
// 导入多个配置类（使用数组格式）
// @Import({JdbcConfig.class, MybatisConfig.class})

public class SpringConfig {
}
