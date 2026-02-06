package com.coder.dao;

import com.coder.domain.TransferLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 转账日志数据访问层接口（DAO - Data Access Object）
 *
 * 职责说明：
 * - 负责转账日志的持久化操作（增删改查）
 * - 提供日志记录查询功能，用于审计和演示事务传播行为
 *
 * 与事务传播行为的关系：
 * ============================================================================
 * 这个DAO本身不涉及事务传播配置，事务传播行为是在Service层配置的。
 * 但是，通过观察这个DAO操作的数据，可以清楚地看到不同传播行为的效果：
 *
 * 1. 当使用REQUIRED时：
 *    - 日志记录操作会加入转账业务的事务
 *    - 如果转账失败回滚，日志记录也会被回滚（数据库中没有记录）
 *
 * 2. 当使用REQUIRES_NEW时：
 *    - 日志记录操作会在独立的事务中执行
 *    - 即使转账失败回滚，日志记录仍然会保存（数据库中有记录）
 * ============================================================================
 *
 * MyBatis注解说明：
 * - @Insert: 标识插入操作，对应SQL的INSERT语句
 * - @Select: 标识查询操作，对应SQL的SELECT语句
 * - #{...}: 参数占位符，MyBatis会自动进行参数映射
 */
public interface TransferLogDao {

    /**
     * 插入转账日志记录
     *
     * @Insert - MyBatis提供的注解，标识这是一个插入操作
     *
     * SQL语句说明：
     * - INSERT INTO transfer_log: 向transfer_log表插入数据
     * - (from_account, to_account, amount, status, message): 要插入的字段列表
     * - VALUES (...): 对应的值，使用#{...}引用方法参数
     *
     * 字段映射说明：
     * - #{fromAccount}: 对应TransferLog对象的fromAccount属性
     * - #{toAccount}: 对应TransferLog对象的toAccount属性
     * - #{amount}: 对应TransferLog对象的amount属性
     * - #{status}: 对应TransferLog对象的status属性
     * - #{message}: 对应TransferLog对象的message属性
     *
     * 注意：id和create_time字段不需要插入
     * - id是自增主键，数据库自动生成
     * - create_time有默认值CURRENT_TIMESTAMP
     *
     * @param log 转账日志对象，包含转账的详细信息
     */
    @Insert("INSERT INTO transfer_log (from_account, to_account, amount, status, message) " +
            "VALUES (#{fromAccount}, #{toAccount}, #{amount}, #{status}, #{message})")
    void insert(TransferLog log);

    /**
     * 查询所有转账日志记录
     *
     * @Select - MyBatis提供的注解，标识这是一个查询操作
     *
     * SQL语句说明：
     * - SELECT *: 查询所有字段
     * - FROM transfer_log: 从transfer_log表查询
     * - ORDER BY create_time DESC: 按创建时间降序排列（最新的在前）
     *
     * 返回结果：
     * - MyBatis会自动将查询结果映射为TransferLog对象列表
     * - 字段名与属性名的映射是自动的（支持驼峰命名转换）
     *
     * 用途：
     * - 用于演示和验证事务传播行为的效果
     * - 查看转账失败后，日志是否被保存
     *
     * @return 转账日志列表，按时间倒序排列
     */
    @Select("SELECT * FROM transfer_log ORDER BY create_time DESC")
    List<TransferLog> selectAll();

    /**
     * 根据状态查询转账日志
     *
     * 用途：
     * - 查询所有成功或失败的转账记录
     * - 用于统计和分析
     *
     * @param status 状态值（"SUCCESS"或"FAILED"）
     * @return 符合条件的转账日志列表
     */
    @Select("SELECT * FROM transfer_log WHERE status = #{status} ORDER BY create_time DESC")
    List<TransferLog> selectByStatus(String status);
}
