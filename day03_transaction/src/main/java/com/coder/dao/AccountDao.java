package com.coder.dao;

import org.apache.ibatis.annotations.Update;

/**
 * 账户数据访问层接口（DAO - Data Access Object）
 *
 * 职责说明：
 * - 负责与数据库进行交互，执行具体的CRUD操作
 * - 提供基础的账户操作：减钱（outMoney）和加钱（inMoney）
 * - 使用MyBatis注解方式编写SQL语句
 *
 * MyBatis代理机制说明：
 * - 我们只需要定义接口，不需要编写实现类
 * - MyBatis-Spring会自动为这个接口创建代理对象
 * - 代理对象会实现接口方法，执行对应的SQL语句
 * - 这个代理对象会被注册到Spring容器中，可以直接@Autowired注入使用
 */
public interface AccountDao {

    /**
     * 指定账户减钱（转出操作）
     *
     * @Update - MyBatis提供的注解，用于标识这是一个更新操作（UPDATE语句）
     *          MyBatis会根据这个注解自动生成实现代码
     *
     * SQL语句说明：
     * - UPDATE account: 更新account表
     * - SET money = money - #{money}: 将money字段减去传入的金额
     * - WHERE name = #{name}: 根据账户名称定位要更新的记录
     *
     * #{...} 是MyBatis的参数占位符语法：
     * - #{name} 表示方法参数name的值
     * - #{money} 表示方法参数money的值
     * - MyBatis会自动进行参数映射和SQL注入防护（预编译）
     *
     * @param name  账户名称（如"张三"）
     * @param money 转出金额（正数）
     */
    @Update("UPDATE account SET money = money - #{money} WHERE name = #{name}")
    void outMoney(String name, Double money);

    /**
     * 指定账户加钱（转入操作）
     *
     * @Update - MyBatis提供的注解，标识这是一个更新操作
     *
     * SQL语句说明：
     * - UPDATE account: 更新account表
     * - SET money = money + #{money}: 将money字段加上传入的金额
     * - WHERE name = #{name}: 根据账户名称定位要更新的记录
     *
     * 转账业务的核心：
     * - 转出方调用outMoney方法，余额减少
     * - 转入方调用inMoney方法，余额增加
     * - 这两个操作必须在同一个事务中执行，确保数据一致性
     *
     * @param name  账户名称（如"李四"）
     * @param money 转入金额（正数）
     */
    @Update("UPDATE account SET money = money + #{money} WHERE name = #{name}")
    void inMoney(String name, Double money);
}