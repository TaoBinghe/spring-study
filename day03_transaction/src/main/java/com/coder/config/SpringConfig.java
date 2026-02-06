package com.coder.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring核心配置类
 * 整合MyBatis配置，开启事务管理
 *
 * @Configuration - 标识这是一个Spring配置类，替代XML配置文件
 * @ComponentScan("com.coder") - 开启组件扫描，扫描com.coder包及其子包下的所有Spring组件
 *                              （包括@Service、@Repository、@Component等注解标记的类）
 * @PropertySource("classpath:jdbc.properties") - 加载类路径下的jdbc.properties配置文件
 *                                               将配置文件中的属性值注入到Spring环境中
 * @Import({JdbcConfig.class, MybatisConfig.class}) - 导入其他配置类
 *                                                    将数据源配置和MyBatis配置整合到主配置中
 * @EnableTransactionManagement - 开启Spring注解式事务管理
 *                                使@Transactional注解生效，支持声明式事务
 */
@Configuration
@ComponentScan("com.coder")
@PropertySource("classpath:jdbc.properties")
@Import({JdbcConfig.class, MybatisConfig.class})
@EnableTransactionManagement
public class SpringConfig {
}