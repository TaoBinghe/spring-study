package com.coder.service.impl;

import com.coder.dao.AccountDao;
import com.coder.service.AccountService;
import com.coder.service.TransferLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户业务实现类
 *
 * @Service - Spring提供的注解，标识这是一个业务层组件
 *           作用等同于@Component，但语义更明确，表示这是Service层
 *           被标记的类会被Spring扫描并注册为Bean
 */
@Service
public class AccountServiceImpl implements AccountService {

    /**
     * 注入AccountDao
     *
     * @Autowired - Spring提供的自动注入注解
     *             Spring会从容器中查找类型为AccountDao的Bean并注入到这里
     *             这个Bean是由MyBatis-Spring自动创建的代理对象
     *
     * 依赖注入的好处：
     * - 不需要手动创建对象（new AccountDao()）
     * - 解耦：实现类与具体的DAO实现解耦
     * - 便于测试：可以注入Mock对象进行单元测试
     */
    @Autowired
    private AccountDao accountDao;

    /**
     * 注入转账日志服务
     *
     * 教学重点：
     * - 通过注入TransferLogService，我们可以在转账业务中记录日志
     * - 日志服务提供了两种传播行为的方法，用于演示事务传播的效果
     * - 这是实现审计日志功能的关键
     *
     * 两种日志记录方式：
     * 1. logTransferRequired() - 使用REQUIRED传播行为
     *    - 日志与转账业务在同一个事务中
     *    - 转账失败时，日志也会被回滚
     *
     * 2. logTransferRequiresNew() - 使用REQUIRES_NEW传播行为
     *    - 日志在独立的事务中执行
     *    - 转账失败时，日志仍然会保存
     */
    @Autowired
    private TransferLogService transferLogService;

    /**
     * 转账操作实现 - 基础版本（不带日志记录）
     *
     * @Transactional - Spring声明式事务注解，这是实现事务管理的关键
     *
     * 作用说明：
     * - 标识此方法需要在事务中执行
     * - Spring会在方法执行前开启事务
     * - 方法正常执行完毕后提交事务
     * - 方法抛出运行时异常时回滚事务
     *
     * 事务传播行为（默认REQUIRED）：
     * - 如果当前存在事务，则加入该事务
     * - 如果当前没有事务，则创建一个新的事务
     *
     * 转账业务逻辑：
     * 1. 调用accountDao.outMoney(from, money) - 转出方减钱
     * 2. 调用accountDao.inMoney(to, money) - 转入方加钱
     *
     * 事务保障：
     * - 两个操作在同一个数据库连接中执行
     * - 如果outMoney成功但inMoney失败，事务会回滚，outMoney的操作也会被撤销
     * - 确保数据的一致性（ACID特性中的原子性）
     *
     * @param from  转出方账户名称
     * @param to    转入方账户名称
     * @param money 转账金额
     */
    @Override
    public void transfer(String from, String to, Double money) {
        // 步骤1：转出方减钱
        // 调用DAO层的outMoney方法，执行UPDATE account SET money = money - ? WHERE name = ?
        accountDao.outMoney(from, money);
        System.out.println("【转账日志】" + from + " 转出 " + money + " 元");

        // 步骤2：转入方加钱
        // 调用DAO层的inMoney方法，执行UPDATE account SET money = money + ? WHERE name = ?
        accountDao.inMoney(to, money);
        System.out.println("【转账日志】" + to + " 转入 " + money + " 元");

        System.out.println("【转账成功】" + from + " 向 " + to + " 转账 " + money + " 元完成！");
    }

    /**
     * 转账操作实现 - 使用REQUIRED传播行为记录日志
     *
     * 教学演示：REQUIRED传播行为的效果
     * ============================================================================
     *
     * 执行流程：
     * 1. Spring开启事务T1
     * 2. 执行outMoney() - 转出方减钱
     * 3. 调用logTransferRequired() - 尝试记录日志
     *    - 因为传播行为是REQUIRED，所以加入事务T1
     *    - 执行INSERT操作，但还未提交
     * 4. 执行inMoney() - 转入方加钱
     * 5. 如果全部成功，提交事务T1（包括转账和日志）
     * 6. 如果抛出异常，回滚事务T1（转账和日志都回滚）
     *
     * 关键结论：
     * - 日志记录与转账业务在同一个事务中
     * - 转账失败时，日志也会被回滚，数据库中没有记录
     * - 适用于日志与业务强关联的场景
     *
     * @param from  转出方账户名称
     * @param to    转入方账户名称
     * @param money 转账金额
     */
    @Override
    public void transferWithLogRequired(String from, String to, Double money) {
        System.out.println("\n========== 开始转账（REQUIRED日志模式） ==========");
        System.out.println("转账信息：" + from + " -> " + to + ", 金额：" + money);

        try {
            // 步骤1：转出方减钱
            accountDao.outMoney(from, money);
            System.out.println("【业务】" + from + " 转出 " + money + " 元");

            // 步骤2：记录日志（使用REQUIRED传播行为）
            // 此时日志记录会加入当前事务
            transferLogService.logTransferRequired(from, to, money, "SUCCESS", "转账进行中 - 转出成功");

            // 步骤3：转入方加钱
            accountDao.inMoney(to, money);
            System.out.println("【业务】" + to + " 转入 " + money + " 元");

            // 步骤4：更新日志状态为成功
            transferLogService.logTransferRequired(from, to, money, "SUCCESS", "转账完成 - 全部成功");

            System.out.println("【转账成功】" + from + " 向 " + to + " 转账 " + money + " 元完成！");

        } catch (Exception e) {
            System.out.println("【转账失败】发生异常：" + e.getMessage());
            System.out.println("【REQUIRED特性】由于使用REQUIRED传播行为，日志记录会随事务回滚！");
            // 抛出异常，触发事务回滚
            throw new RuntimeException("转账失败：" + e.getMessage(), e);
        }
    }

    /**
     * 转账操作实现 - 使用REQUIRES_NEW传播行为记录日志
     *
     * 教学演示：REQUIRES_NEW传播行为的效果
     * ============================================================================
     *
     * 执行流程：
     * 1. Spring开启事务T1（主业务事务）
     * 2. 执行outMoney() - 转出方减钱
     * 3. 调用logTransferRequiresNew() - 记录日志
     *    - 因为传播行为是REQUIRES_NEW，Spring会：
     *      a. 挂起当前事务T1
     *      b. 创建新事务T2
     *      c. 在T2中执行日志记录INSERT
     *      d. 提交事务T2
     *      e. 恢复事务T1
     * 4. 执行inMoney() - 转入方加钱
     * 5. 如果后续抛出异常，事务T1回滚（转账操作回滚）
     *    - 但事务T2已经提交，日志记录保留！
     *
     * 关键结论：
     * - 日志记录在独立的事务中执行
     * - 即使转账失败回滚，日志仍然会保存到数据库
     * - 这就是审计日志的实现原理！
     *
     * @param from  转出方账户名称
     * @param to    转入方账户名称
     * @param money 转账金额
     */
    @Override
    public void transferWithLogRequiresNew(String from, String to, Double money) {
        System.out.println("\n========== 开始转账（REQUIRES_NEW日志模式） ==========");
        System.out.println("转账信息：" + from + " -> " + to + ", 金额：" + money);

        // 记录转账开始日志（在独立事务中）
        // 这个日志会立即提交，不受后续异常影响
        transferLogService.logTransferRequiresNew(from, to, money, "SUCCESS", "转账开始 - 记录审计日志");

        try {
            // 步骤1：转出方减钱
            accountDao.outMoney(from, money);
            System.out.println("【业务】" + from + " 转出 " + money + " 元");

            // 步骤2：记录中间状态日志（在独立事务中）
            transferLogService.logTransferRequiresNew(from, to, money, "SUCCESS", "转出成功 - 等待转入");

            // 步骤3：转入方加钱
            accountDao.inMoney(to, money);
            System.out.println("【业务】" + to + " 转入 " + money + " 元");

            // 步骤4：记录成功日志（在独立事务中）
            transferLogService.logTransferRequiresNew(from, to, money, "SUCCESS", "转账完成 - 全部成功");

            System.out.println("【转账成功】" + from + " 向 " + to + " 转账 " + money + " 元完成！");

        } catch (Exception e) {
            System.out.println("【转账失败】发生异常：" + e.getMessage());
            System.out.println("【REQUIRES_NEW特性】虽然转账失败，但之前的审计日志已独立提交，不会回滚！");

            // 记录失败日志（在独立事务中）
            transferLogService.logTransferRequiresNew(from, to, money, "FAILED", "转账失败：" + e.getMessage());

            // 抛出异常，触发主事务回滚（转账操作回滚）
            throw new RuntimeException("转账失败：" + e.getMessage(), e);
        }
    }
}