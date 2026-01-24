package com.coder.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * JDBC配置类 - 第三方bean管理
 * 
 * 将独立的配置类加入核心配置
 * 方式一:导入式
 * 
 * 注意：被@Import导入的配置类可以不需要@Configuration注解
 * Spring仍然会处理其中的@Bean方法
 * 
 * @Bean 注解：
 * - 用于标识方法，该方法返回的对象会被Spring管理为Bean
 * - 方法名默认为Bean的id，也可以通过@Bean("beanName")指定
 * - 适用于管理第三方类库的Bean（无法使用@Component等注解的类）
 */
@PropertySource("jdbc.properties")  // 加载properties文件，以便使用@Value读取
public class JdbcConfig {
    
    /**
     * 创建DataSource Bean
     * 使用@Bean注解将第三方类（DruidDataSource）注册为Spring管理的Bean
     * 
     * 使用@Value注解从properties文件中读取配置
     * 注意：需要在配置类上添加@PropertySource注解
     */
    @Bean
    public DataSource dataSource(@Value("${jdbc.driver}") String driver,
                                 @Value("${jdbc.url}") String url,
                                 @Value("${jdbc.username}") String username,
                                 @Value("${jdbc.password}") String password) {
        DruidDataSource ds = new DruidDataSource();
        // 相关配置
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }
}
