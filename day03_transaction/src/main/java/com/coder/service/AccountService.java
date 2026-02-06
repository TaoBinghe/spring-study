package com.coder.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 账户业务层接口（Service）
 *
 * 职责说明：
 * - 负责处理业务逻辑，是Controller层和DAO层之间的桥梁
 * - 转账业务的核心逻辑在此定义
 * - 接口定义了业务操作的契约，具体实现由实现类完成
 *
 * 分层架构的好处：
 * - 解耦：调用者只需要知道接口，不需要关心具体实现
 * - 可替换性：可以轻松切换不同的实现（如更换数据源、更换算法）
 * - 可测试性：便于编写单元测试，可以使用Mock对象
 */
public interface AccountService {

    /**
     * 转账操作
     *
     * 业务逻辑说明：
     * - 从转出方账户扣除指定金额
     * - 向转入方账户增加指定金额
     * - 这两个操作必须作为一个原子操作执行（事务）
     *
     * 事务的重要性：
     * - 如果转出成功但转入失败，会导致资金丢失
     * - 如果转入成功但转出失败，会导致资金凭空产生
     * - 使用Spring的声明式事务可以确保两个操作要么都成功，要么都失败
     *
     * @param from  转出方账户名称
     * @param to    转入方账户名称
     * @param money 转账金额
     */
    @Transactional
    void transfer(String from, String to, Double money);

    /**
     * 转账操作 - 使用REQUIRED传播行为记录日志
     *
     * 教学演示：
     * - 使用REQUIRED传播行为记录转账日志
     * - 日志记录与转账业务在同一个事务中
     * - 转账失败时，日志也会被回滚
     *
     * 适用场景：
     * - 日志与业务强关联，必须保持一致性
     *
     * @param from  转出方账户名称
     * @param to    转入方账户名称
     * @param money 转账金额
     */
    @Transactional
    void transferWithLogRequired(String from, String to, Double money);

    /**
     * 转账操作 - 使用REQUIRES_NEW传播行为记录日志
     *
     * 教学演示：
     * - 使用REQUIRES_NEW传播行为记录转账日志
     * - 日志记录在独立的事务中执行
     * - 即使转账失败，日志仍然会保存
     *
     * 适用场景：
     * - 审计日志，必须保留即使业务失败
     *
     * @param from  转出方账户名称
     * @param to    转入方账户名称
     * @param money 转账金额
     */
    @Transactional
    void transferWithLogRequiresNew(String from, String to, Double money);
}