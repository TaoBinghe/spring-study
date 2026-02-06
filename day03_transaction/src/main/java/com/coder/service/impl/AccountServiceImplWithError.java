package com.coder.service.impl;

import com.coder.dao.AccountDao;
import com.coder.service.AccountService;
import com.coder.service.TransferLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户业务实现类 - 带错误模拟的版本
 * 用于演示事务传播行为
 *
 * 设计目的：
 * - 通过模拟各种错误场景，演示REQUIRED和REQUIRES_NEW的区别
 * - 让学生直观理解事务传播行为的效果
 */
@Service
public class AccountServiceImplWithError implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransferLogService transferLogService;

    /**
     * 基础转账方法（与原版相同）
     */
    @Override
    public void transfer(String from, String to, Double money) {
        accountDao.outMoney(from, money);
        System.out.println("【转账日志】" + from + " 转出 " + money + " 元");
        accountDao.inMoney(to, money);
        System.out.println("【转账日志】" + to + " 转入 " + money + " 元");
        System.out.println("【转账成功】" + from + " 向 " + to + " 转账 " + money + " 元完成！");
    }

    /**
     * 使用REQUIRED传播行为记录日志 - 成功场景
     */
    @Override
    @Transactional
    public void transferWithLogRequired(String from, String to, Double money) {
        System.out.println("\n========== 开始转账（REQUIRED日志模式 - 成功场景） ==========");
        System.out.println("转账信息：" + from + " -> " + to + ", 金额：" + money);

        // 转出
        accountDao.outMoney(from, money);
        System.out.println("【业务】" + from + " 转出 " + money + " 元");

        // 记录日志（加入当前事务）
        transferLogService.logTransferRequired(from, to, money, "SUCCESS", "转账成功-REQUIRED模式");

        // 转入
        accountDao.inMoney(to, money);
        System.out.println("【业务】" + to + " 转入 " + money + " 元");

        System.out.println("【转账成功】" + from + " 向 " + to + " 转账 " + money + " 元完成！");
    }

    /**
     * 使用REQUIRED传播行为记录日志 - 失败场景（模拟异常）
     * 关键演示：日志会随事务回滚
     */
    @Transactional
    public void transferWithLogRequiredAndFail(String from, String to, Double money) {
        System.out.println("\n========== 开始转账（REQUIRED日志模式 - 失败场景） ==========");
        System.out.println("转账信息：" + from + " -> " + to + ", 金额：" + money);

        // 转出
        accountDao.outMoney(from, money);
        System.out.println("【业务】" + from + " 转出 " + money + " 元");

        // 记录日志（加入当前事务）
        transferLogService.logTransferRequired(from, to, money, "SUCCESS", "转出成功-等待转入");
        System.out.println("【REQUIRED】日志已记录（在当前事务中）");

        // 模拟异常：转入前发生错误
        System.out.println("【模拟异常】转入操作前发生网络超时！");
        throw new RuntimeException("网络超时：无法连接到数据库");

        // 这行代码不会执行
        // accountDao.inMoney(to, money);
    }

    /**
     * 使用REQUIRES_NEW传播行为记录日志 - 成功场景
     */
    @Override
    @Transactional
    public void transferWithLogRequiresNew(String from, String to, Double money) {
        System.out.println("\n========== 开始转账（REQUIRES_NEW日志模式 - 成功场景） ==========");
        System.out.println("转账信息：" + from + " -> " + to + ", 金额：" + money);

        // 记录开始日志（独立事务）
        transferLogService.logTransferRequiresNew(from, to, money, "SUCCESS", "转账开始-REQUIRES_NEW模式");

        // 转出
        accountDao.outMoney(from, money);
        System.out.println("【业务】" + from + " 转出 " + money + " 元");

        // 记录中间日志（独立事务）
        transferLogService.logTransferRequiresNew(from, to, money, "SUCCESS", "转出成功-等待转入");

        // 转入
        accountDao.inMoney(to, money);
        System.out.println("【业务】" + to + " 转入 " + money + " 元");

        // 记录完成日志（独立事务）
        transferLogService.logTransferRequiresNew(from, to, money, "SUCCESS", "转账完成-全部成功");

        System.out.println("【转账成功】" + from + " 向 " + to + " 转账 " + money + " 元完成！");
    }

    /**
     * 使用REQUIRES_NEW传播行为记录日志 - 失败场景（模拟异常）
     * 关键演示：日志不会随事务回滚，会保留在数据库中
     */
    @Transactional
    public void transferWithLogRequiresNewAndFail(String from, String to, Double money) {
        System.out.println("\n========== 开始转账（REQUIRES_NEW日志模式 - 失败场景） ==========");
        System.out.println("转账信息：" + from + " -> " + to + ", 金额：" + money);

        // 记录开始日志（独立事务，立即提交）
        transferLogService.logTransferRequiresNew(from, to, money, "SUCCESS", "转账开始-REQUIRES_NEW模式");
        System.out.println("【REQUIRES_NEW】开始日志已在独立事务中提交！");

        // 转出
        accountDao.outMoney(from, money);
        System.out.println("【业务】" + from + " 转出 " + money + " 元");

        // 记录中间日志（独立事务，立即提交）
        transferLogService.logTransferRequiresNew(from, to, money, "SUCCESS", "转出成功-等待转入");
        System.out.println("【REQUIRES_NEW】中间日志已在独立事务中提交！");

        // 模拟异常：转入前发生错误
        System.out.println("【模拟异常】转入操作前发生数据库连接中断！");

        // 记录失败日志（在异常前尝试记录）
        transferLogService.logTransferRequiresNew(from, to, money, "FAILED", "转账失败：数据库连接中断");
        System.out.println("【REQUIRES_NEW】失败日志已在独立事务中提交！");

        throw new RuntimeException("数据库连接中断：无法完成转入操作");

        // 这行代码不会执行
        // accountDao.inMoney(to, money);
    }
}
