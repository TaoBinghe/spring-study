package com.coder.service;

import com.coder.domain.TransferLog;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 转账日志服务接口
 *
 * 核心教学点：Spring事务传播行为（Propagation Behavior）
 * ============================================================================
 *
 * 什么是事务传播行为？
 * - 当事务方法被另一个事务方法调用时，事务应该如何传播的规则
 * - 例如：方法A有事务，方法A调用方法B，方法B应该如何处理事务？
 *
 * Spring提供了7种事务传播行为：
 *
 * 1. REQUIRED（默认）
 *    - 如果当前存在事务，则加入该事务
 *    - 如果当前没有事务，则创建一个新事务
 *    - 最常用，适用于大多数业务场景
 *
 * 2. REQUIRES_NEW
 *    - 无论当前是否存在事务，都创建一个新事务
 *    - 如果当前存在事务，则挂起当前事务
 *    - 适用于需要独立事务的场景，如日志记录
 *
 * 3. SUPPORTS
 *    - 如果当前存在事务，则加入该事务
 *    - 如果当前没有事务，则以非事务方式执行
 *
 * 4. NOT_SUPPORTED
 *    - 以非事务方式执行
 *    - 如果当前存在事务，则挂起当前事务
 *
 * 5. MANDATORY
 *    - 如果当前存在事务，则加入该事务
 *    - 如果当前没有事务，则抛出异常
 *
 * 6. NEVER
 *    - 以非事务方式执行
 *    - 如果当前存在事务，则抛出异常
 *
 * 7. NESTED
 *    - 如果当前存在事务，则在嵌套事务中执行
 *    - 如果当前没有事务，则创建一个新事务
 *    - 嵌套事务可以独立回滚
 *
 * ============================================================================
 *
 * 本接口演示两种最重要的传播行为：
 * - logTransferRequired: 使用REQUIRED，演示日志与业务同生共死
 * - logTransferRequiresNew: 使用REQUIRES_NEW，演示日志独立保存
 */
public interface TransferLogService {

    /**
     * 记录转账日志 - 使用REQUIRED传播行为（默认）
     *
     * @Transactional 注解说明：
     * - propagation = Propagation.REQUIRED: 指定事务传播行为为REQUIRED
     * - 这是默认值，即使不写也会生效
     *
     * 行为特征：
     * ============================================================================
     * 场景1：转账成功
     * - 转账业务和日志记录在同一个事务中
     * - 两者都提交，数据库都有记录
     *
     * 场景2：转账失败（抛出异常）
     * - 转账业务回滚，日志记录也回滚
     * - 数据库中：账户余额不变，transfer_log表也没有记录
     * - 结果：就像这次转账从未发生过一样
     *
     * 适用场景：
     * - 日志与业务强关联，必须保持一致性
     * - 例如：订单和订单明细，必须同时成功或同时失败
     * ============================================================================
     *
     * @param from   转出方账户
     * @param to     转入方账户
     * @param amount 转账金额
     * @param status 转账状态（SUCCESS/FAILED）
     * @param message 日志详细信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    void logTransferRequired(String from, String to, Double amount, String status, String message);

    /**
     * 记录转账日志 - 使用REQUIRES_NEW传播行为
     *
     * @Transactional 注解说明：
     * - propagation = Propagation.REQUIRES_NEW: 指定事务传播行为为REQUIRES_NEW
     *
     * 行为特征：
     * ============================================================================
     * 场景1：转账成功
     * - 日志记录在一个独立的新事务中执行
     * - 两者都提交，数据库都有记录
     *
     * 场景2：转账失败（抛出异常）
     * - 转账业务回滚（账户余额不变）
     * - 但日志记录的事务是独立的，会正常提交
     * - 数据库中：transfer_log表有一条FAILED记录
     * - 结果：虽然转账失败了，但我们留下了审计日志
     *
     * 关键点：
     * - 当调用这个方法时，Spring会挂起当前事务（如果有的话）
     * - 创建一个新的事务来执行日志记录
     * - 日志记录完成后，恢复之前挂起的事务
     *
     * 适用场景：
     * - 审计日志、操作记录，必须保留即使业务失败
     * - 例如：银行转账记录，即使失败也要记录以便追踪
     * ============================================================================
     *
     * @param from   转出方账户
     * @param to     转入方账户
     * @param amount 转账金额
     * @param status 转账状态（SUCCESS/FAILED）
     * @param message 日志详细信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void logTransferRequiresNew(String from, String to, Double amount, String status, String message);

    /**
     * 查询所有转账日志
     *
     * @return 转账日志列表
     */
    List<TransferLog> getAllLogs();

    /**
     * 根据状态查询转账日志
     *
     * @param status 状态（SUCCESS/FAILED）
     * @return 符合条件的转账日志列表
     */
    List<TransferLog> getLogsByStatus(String status);
}
