package com.coder.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * JDBC数据源配置类
 * 负责配置数据库连接池和事务管理器
 *
 * 注意：此类不需要加@Configuration注解，因为它会被SpringConfig通过@Import导入
 */
public class JdbcConfig {

    /**
     * 创建数据源对象（Druid连接池）
     *
     * @Bean - 将方法返回值注册为Spring容器中的Bean
     *         方法名默认为Bean的名称（dataSource）
     *
     * @Value("${jdbc.driver}") - 从配置文件中读取属性值并注入到参数
     *                            ${...} 是Spring的占位符语法，用于引用配置文件中的属性
     *
     * 参数说明：
     * - driver: 数据库驱动类名
     * - url: 数据库连接地址
     * - username: 数据库用户名
     * - password: 数据库密码
     */
    @Bean
    public DataSource dataSource(@Value("${jdbc.driver}") String driver,
                                 @Value("${jdbc.url}") String url,
                                 @Value("${jdbc.username}") String username,
                                 @Value("${jdbc.password}") String password) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    /**
     * 创建事务管理器
     *
     * Spring事务管理的核心组件，负责管理数据库事务的提交和回滚
     *
     * PlatformTransactionManager是Spring事务管理的顶层接口，常用实现类：
     * - DataSourceTransactionManager: 用于单一数据源的事务管理（最常用）
     * - JtaTransactionManager: 用于分布式事务管理（多个数据源）
     *
     * 参数 DataSource dataSource 的注入原理：
     * 1. Spring容器中存在DataSource类型的Bean（上面定义的dataSource方法）
     * 2. Spring会自动按类型查找并注入到方法参数中
     * 3. 这是Spring的方法参数依赖注入特性，不需要额外注解
     *
     * 事务管理器与数据源的关系：
     * - 事务管理器需要知道使用哪个数据源来管理事务
     * - 它通过数据源获取数据库连接，然后在这个连接上开启、提交或回滚事务
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}