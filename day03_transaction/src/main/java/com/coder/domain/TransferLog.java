package com.coder.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 转账日志实体类
 * 对应数据库表 transfer_log
 *
 * 设计目的：
 * - 记录每一次转账操作的详细信息
 * - 用于审计、追踪和故障排查
 * - 演示Spring事务传播行为的重要工具
 *
 * @Data 是Lombok提供的注解，自动生成getter、setter、toString等方法
 *
 * 事务传播行为说明：
 * ============================================================================
 * 1. REQUIRED（默认）
 *    - 如果当前存在事务，则加入该事务
 *    - 如果当前没有事务，则创建一个新事务
 *    - 特点：日志记录与主业务在同一个事务中，主业务回滚时日志也回滚
 *
 * 2. REQUIRES_NEW
 *    - 无论当前是否存在事务，都创建一个新事务
 *    - 如果当前存在事务，则挂起当前事务，创建新事务执行
 *    - 特点：日志记录独立于主业务事务，主业务回滚时日志仍然保留
 * ============================================================================
 *
 * 实际应用场景：
 * - REQUIRED：适用于业务关联性强的操作，确保数据一致性
 * - REQUIRES_NEW：适用于审计日志、操作记录等，确保记录不丢失
 */
@Data
public class TransferLog {

    /**
     * 日志ID
     * 对应数据库字段：id
     * 类型：INT，主键，自增
     */
    private Integer id;

    /**
     * 转出方账户
     * 对应数据库字段：from_account
     * 类型：VARCHAR(50)
     */
    private String fromAccount;

    /**
     * 转入方账户
     * 对应数据库字段：to_account
     * 类型：VARCHAR(50)
     */
    private String toAccount;

    /**
     * 转账金额
     * 对应数据库字段：amount
     * 类型：DOUBLE
     */
    private Double amount;

    /**
     * 转账状态
     * 对应数据库字段：status
     * 类型：VARCHAR(20)
     * 取值：SUCCESS（成功）、FAILED（失败）
     */
    private String status;

    /**
     * 日志详细信息
     * 对应数据库字段：message
     * 类型：VARCHAR(500)
     * 用于记录转账过程中的详细信息或错误原因
     */
    private String message;

    /**
     * 记录创建时间
     * 对应数据库字段：create_time
     * 类型：TIMESTAMP
     * 默认值：CURRENT_TIMESTAMP
     */
    private LocalDateTime createTime;
}
