package com.coder.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * MyBatis配置类 - MyBatis与Spring整合配置
 */
public class MybatisConfig {
    
    /**
     * 创建SqlSessionFactoryBean
     * 用于创建SqlSessionFactory，替代MyBatis的SqlSessionFactoryBuilder
     * 
     * 形参 DataSource dataSource 的作用：
     * - Spring会自动从容器中查找DataSource类型的Bean（在JdbcConfig中定义的dataSource方法返回的）
     * - 自动注入到这个方法参数中（这是Spring的方法参数依赖注入）
     * - 不需要@Autowired注解，Spring会自动识别并注入
     * - 然后在方法内部使用这个数据源来配置SqlSessionFactoryBean
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        // 设置数据源 - 使用注入的dataSource参数
        ssfb.setDataSource(dataSource);
        // 设置类型别名（可选）
        ssfb.setTypeAliasesPackage("com.coder.domain");
        return ssfb;
    }
    
    /**
     * 创建MapperScannerConfigurer
     * 用于扫描Mapper接口，自动创建代理对象
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        // 设置Mapper接口所在的包
        msc.setBasePackage("com.coder.dao");
        return msc;
    }
}
