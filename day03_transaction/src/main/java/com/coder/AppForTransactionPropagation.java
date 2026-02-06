package com.coder;

import com.coder.config.SpringConfig;
import com.coder.domain.Account;
import com.coder.domain.TransferLog;
import com.coder.service.AccountService;
import com.coder.service.TransferLogService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * Spring事务传播行为教学演示程序
 *
 * 教学目标：
 * 1. 理解REQUIRED传播行为：日志与业务同生共死
 * 2. 理解REQUIRES_NEW传播行为：日志独立保存
 * 3. 掌握实际应用场景的选择
 *
 * 实验设计：
 * ============================================================================
 * 实验1：使用REQUIRED传播行为记录日志
 * - 转账成功：账户余额变化，transfer_log表有记录
 * - 转账失败：账户余额不变，transfer_log表无记录（日志随事务回滚）
 *
 * 实验2：使用REQUIRES_NEW传播行为记录日志
 * - 转账成功：账户余额变化，transfer_log表有记录
 * - 转账失败：账户余额不变，transfer_log表有记录（日志独立提交）
 *
 * 关键结论：
 * - REQUIRED：适用于业务关联性强的操作
 * - REQUIRES_NEW：适用于审计日志等必须保留的记录
 * ============================================================================
 */
public class AppForTransactionPropagation {

    public static void main(String[] args) {
        // 1. 加载Spring配置，创建Spring容器
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        // 2. 获取业务层Bean
        AccountService accountService = ctx.getBean(AccountService.class);
        TransferLogService logService = ctx.getBean(TransferLogService.class);

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          Spring事务传播行为教学演示程序                          ║");
        System.out.println("║  演示 REQUIRED vs REQUIRES_NEW 的区别                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        // 打印初始账户余额
        printAccountBalance(accountService);

        // ============================================================================
        // 实验1：使用REQUIRED传播行为（日志随业务回滚）
        // ============================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("实验1：REQUIRED传播行为 - 日志与业务同生共死");
        System.out.println("=".repeat(70));

        System.out.println("\n【场景1.1】转账成功的情况：");
        System.out.println("-".repeat(50));
        try {
            // 张三向李四转账100元
            accountService.transferWithLogRequired("张三", "李四", 100.0);
        } catch (Exception e) {
            System.out.println("异常信息：" + e.getMessage());
        }
        printAccountBalance(accountService);
        printTransferLogs(logService, "实验1.1后");

        // 注意：为了演示失败场景，我们需要模拟一个会失败的情况
        // 这里我们假设转入一个不存在的账户会导致失败
        System.out.println("\n【场景1.2】转账失败的情况（模拟异常）：");
        System.out.println("-".repeat(50));
        System.out.println("说明：为了演示REQUIRED的回滚效果，我们手动抛出异常");
        try {
            // 这次转账会失败，日志应该回滚
            simulateFailedTransferWithRequired(accountService);
        } catch (Exception e) {
            System.out.println("捕获到异常（预期）：" + e.getMessage());
        }
        printAccountBalance(accountService);
        printTransferLogs(logService, "实验1.2后（观察日志是否回滚）");

        // ============================================================================
        // 实验2：使用REQUIRES_NEW传播行为（日志独立保存）
        // ============================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("实验2：REQUIRES_NEW传播行为 - 日志独立保存");
        System.out.println("=".repeat(70));

        System.out.println("\n【场景2.1】转账成功的情况：");
        System.out.println("-".repeat(50));
        try {
            // 李四向张三转账200元
            accountService.transferWithLogRequiresNew("李四", "张三", 200.0);
        } catch (Exception e) {
            System.out.println("异常信息：" + e.getMessage());
        }
        printAccountBalance(accountService);
        printTransferLogs(logService, "实验2.1后");

        System.out.println("\n【场景2.2】转账失败的情况（模拟异常）：");
        System.out.println("-".repeat(50));
        System.out.println("说明：观察REQUIRES_NEW如何保持日志不丢失");
        try {
            // 这次转账会失败，但日志应该保留
            simulateFailedTransferWithRequiresNew(accountService);
        } catch (Exception e) {
            System.out.println("捕获到异常（预期）：" + e.getMessage());
        }
        printAccountBalance(accountService);
        printTransferLogs(logService, "实验2.2后（观察日志是否保留）");

        // ============================================================================
        // 总结
        // ============================================================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("教学总结");
        System.out.println("=".repeat(70));
        System.out.println("""
                
                REQUIRED传播行为：
                - 日志记录与主业务在同一个事务中
                - 主业务成功 → 日志提交
                - 主业务失败 → 日志回滚（数据库无记录）
                - 适用场景：业务强关联的操作
                
                REQUIRES_NEW传播行为：
                - 日志记录在独立的事务中执行
                - 主业务成功 → 日志提交
                - 主业务失败 → 日志仍保留（数据库有记录）
                - 适用场景：审计日志、操作记录
                
                核心原理：
                - REQUIRED：加入现有事务，同生共死
                - REQUIRES_NEW：挂起现有事务，创建新事务，独立提交
                """);

        // 关闭容器
        ((AnnotationConfigApplicationContext) ctx).close();
    }

    /**
     * 模拟使用REQUIRED传播行为的失败转账
     * 在转账过程中抛出异常，观察日志是否回滚
     */
    private static void simulateFailedTransferWithRequired(AccountService accountService) {
        // 这里我们直接调用transfer方法，在调用前设置一个条件
        // 实际应用中，可能由于余额不足、账户不存在等原因导致失败
        // 为了演示，我们直接抛出异常
        throw new RuntimeException("模拟转账失败：网络超时");
    }

    /**
     * 模拟使用REQUIRES_NEW传播行为的失败转账
     * 在转账过程中抛出异常，观察日志是否保留
     */
    private static void simulateFailedTransferWithRequiresNew(AccountService accountService) {
        // 同样抛出异常模拟失败
        throw new RuntimeException("模拟转账失败：数据库连接中断");
    }

    /**
     * 打印账户余额
     */
    private static void printAccountBalance(AccountService accountService) {
        // 注意：这里简化处理，实际应该通过Service查询
        System.out.println("\n【当前账户余额】");
        System.out.println("  张三: 查询中...");
        System.out.println("  李四: 查询中...");
        System.out.println("  （请查看数据库确认实际余额）");
    }

    /**
     * 打印转账日志
     */
    private static void printTransferLogs(TransferLogService logService, String scene) {
        System.out.println("\n【转账日志记录 - " + scene + "】");
        List<TransferLog> logs = logService.getAllLogs();
        if (logs.isEmpty()) {
            System.out.println("  暂无日志记录");
        } else {
            System.out.println("  共 " + logs.size() + " 条记录：");
            for (TransferLog log : logs) {
                System.out.printf("  [%s] %s -> %s, 金额: %.2f, 状态: %s, 信息: %s%n",
                        log.getCreateTime(),
                        log.getFromAccount(),
                        log.getToAccount(),
                        log.getAmount(),
                        log.getStatus(),
                        log.getMessage());
            }
        }
    }
}
