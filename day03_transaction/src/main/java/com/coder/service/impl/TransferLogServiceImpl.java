package com.coder.service.impl;

import com.coder.dao.TransferLogDao;
import com.coder.domain.TransferLog;
import com.coder.service.TransferLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 转账日志服务实现类
 *
 * 核心教学点：事务传播行为的实际应用
 *
 * @Service - 标识这是一个Spring服务层组件
 *           会被Spring扫描并注册为Bean
 *
 * 重要说明：
 * ============================================================================
 * 事务传播行为只在通过代理对象调用时才生效！
 *
 * 什么是代理对象调用？
 * - 从Spring容器中获取的Bean（通过@Autowired注入）
 * - 这个Bean是Spring生成的代理对象，不是原始对象
 * - 只有通过代理对象调用方法，事务拦截器才会生效
 *
 * 为什么同一个类内部调用不生效？
 * - 类内部调用是this调用，不经过代理对象
 * - 例如：方法A调用方法B，如果都在同一个类中，@Transactional不会生效
 * - 解决方案：需要注入自身代理对象，或者拆分到不同类中
 *
 * 本类的设计：
 * - 所有方法都通过Spring代理调用（从其他Service注入使用）
 * - 因此事务传播行为会正确生效
 * ============================================================================
 */
@Service
public class TransferLogServiceImpl implements TransferLogService {

    /**
     * 注入转账日志DAO
     *
     * @Autowired - Spring自动注入TransferLogDao的代理对象
     *             这个DAO由MyBatis-Spring自动生成
     */
    @Autowired
    private TransferLogDao transferLogDao;

    /**
     * 记录转账日志 - 使用REQUIRED传播行为
     *
     * @Transactional 注解放在实现类上的效果：
     * - 与放在接口上等效，但Spring官方推荐放在实现类上
     * - 这样更明确，且不受接口限制（JDK动态代理 vs CGLIB）
     *
     * 执行流程分析：
     * ============================================================================
     * 假设转账业务transfer()方法调用了此方法：
     *
     * 1. transfer()方法开始执行，Spring开启事务T1
     * 2. transfer()调用logTransferRequired()
     * 3. Spring检查传播行为：REQUIRED
     * 4. 发现当前存在事务T1，此方法加入T1
     * 5. 执行INSERT操作，但还未提交
     * 6. 如果transfer()正常结束，T1提交，日志也提交
     * 7. 如果transfer()抛出异常，T1回滚，日志也回滚
     *
     * 关键结论：
     * - 日志记录与转账业务在同一个事务中
     * - 同生共死：要么都成功，要么都失败
     * ============================================================================
     *
     * @param from    转出方账户
     * @param to      转入方账户
     * @param amount  转账金额
     * @param status  转账状态
     * @param message 日志信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void logTransferRequired(String from, String to, Double amount, String status, String message) {
        // 创建日志对象
        TransferLog log = new TransferLog();
        log.setFromAccount(from);
        log.setToAccount(to);
        log.setAmount(amount);
        log.setStatus(status);
        log.setMessage(message);

        // 插入日志记录
        transferLogDao.insert(log);

        // 打印控制台日志，方便观察执行顺序
        System.out.println("【REQUIRED日志】已记录转账日志: " + from + " -> " + to + ", 金额: " + amount + ", 状态: " + status);
    }

    /**
     * 记录转账日志 - 使用REQUIRES_NEW传播行为
     *
     * 执行流程分析：
     * ============================================================================
     * 假设转账业务transfer()方法调用了此方法：
     *
     * 1. transfer()方法开始执行，Spring开启事务T1
     * 2. transfer()调用logTransferRequiresNew()
     * 3. Spring检查传播行为：REQUIRES_NEW
     * 4. 发现当前存在事务T1，挂起T1，创建新事务T2
     * 5. 在T2中执行INSERT操作，并立即提交T2
     * 6. 恢复之前挂起的T1事务
     * 7. 如果transfer()后续抛出异常，T1回滚，但T2已提交不受影响
     *
     * 关键结论：
     * - 日志记录在一个独立的事务中执行
     * - 即使转账业务失败，日志也会保留
     * - 这就是审计日志的实现原理
     * ============================================================================
     *
     * @param from    转出方账户
     * @param to      转入方账户
     * @param amount  转账金额
     * @param status  转账状态
     * @param message 日志信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logTransferRequiresNew(String from, String to, Double amount, String status, String message) {
        // 创建日志对象
        TransferLog log = new TransferLog();
        log.setFromAccount(from);
        log.setToAccount(to);
        log.setAmount(amount);
        log.setStatus(status);
        log.setMessage(message);

        // 插入日志记录（在独立事务中）
        transferLogDao.insert(log);

        // 打印控制台日志，方便观察执行顺序
        System.out.println("【REQUIRES_NEW日志】已记录转账日志: " + from + " -> " + to + ", 金额: " + amount + ", 状态: " + status);
        System.out.println("【REQUIRES_NEW日志】此日志在独立事务中已提交，不受主业务事务影响！");
    }

    /**
     * 查询所有转账日志
     *
     * @return 所有日志记录列表
     */
    @Override
    public List<TransferLog> getAllLogs() {
        return transferLogDao.selectAll();
    }

    /**
     * 根据状态查询转账日志
     *
     * @param status 状态（SUCCESS/FAILED）
     * @return 符合条件的日志记录列表
     */
    @Override
    public List<TransferLog> getLogsByStatus(String status) {
        return transferLogDao.selectByStatus(status);
    }
}
