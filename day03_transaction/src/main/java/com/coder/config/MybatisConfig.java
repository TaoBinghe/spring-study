package com.coder.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * MyBatis配置类 - MyBatis与Spring整合配置
 *
 * 此类负责配置MyBatis与Spring的整合，主要包括：
 * 1. 创建SqlSessionFactoryBean - 用于创建SqlSessionFactory
 * 2. 创建MapperScannerConfigurer - 用于扫描Mapper接口
 *
 * 整合原理：
 * - MyBatis-Spring整合包提供了SqlSessionFactoryBean，替代原生的SqlSessionFactoryBuilder
 * - MapperScannerConfigurer会自动扫描指定包下的Mapper接口，创建代理对象并注册到Spring容器
 * - 这样我们就可以直接注入Mapper接口使用，无需手动获取SqlSession
 */
public class MybatisConfig {

    /**
     * 创建SqlSessionFactoryBean
     *
     * SqlSessionFactoryBean的作用：
     * - 替代MyBatis原生的SqlSessionFactoryBuilder
     * - 创建SqlSessionFactory对象，用于生产SqlSession
     * - 在Spring管理下，SqlSession的生命周期由Spring控制
     *
     * 参数 DataSource dataSource 的注入原理：
     * - Spring会自动从容器中查找DataSource类型的Bean
     * - 在JdbcConfig中定义的dataSource方法返回的Bean会被注入到这里
     * - 这是Spring的方法参数依赖注入，不需要@Autowired注解
     *
     * @param dataSource 数据源对象，由Spring自动注入
     * @return SqlSessionFactoryBean实例
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();

        // 设置数据源 - MyBatis需要通过数据源获取数据库连接
        ssfb.setDataSource(dataSource);

        // 设置类型别名包
        // 作用：在Mapper XML中可以使用简单类名代替全限定名
        // 例如：可以使用Account代替com.coder.domain.Account
        ssfb.setTypeAliasesPackage("com.coder.domain");

        return ssfb;
    }

    /**
     * 创建MapperScannerConfigurer
     *
     * MapperScannerConfigurer的作用：
     * - 自动扫描指定包下的Mapper接口
     * - 为每个Mapper接口创建代理对象（使用JDK动态代理）
     * - 将代理对象注册为Spring容器中的Bean
     *
     * 这样我们就可以在Service中直接@Autowired注入Mapper接口使用
     * 而不需要手动写代码获取SqlSession再获取Mapper
     *
     * @return MapperScannerConfigurer实例
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer msc = new MapperScannerConfigurer();

        // 设置Mapper接口所在的包路径
        // Spring会扫描这个包及其子包下的所有接口
        // 如果这些接口上有@Mapper注解或者在XML中配置了，就会创建代理对象
        msc.setBasePackage("com.coder.dao");

        return msc;
    }
}