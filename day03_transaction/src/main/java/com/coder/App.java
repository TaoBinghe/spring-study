package com.coder;

import com.coder.config.SpringConfig;
import com.coder.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 银行账户转账业务演示程序
 *
 * 程序说明：
 * - 本程序演示了Spring整合MyBatis实现银行账户转账功能
 * - 使用了Spring的声明式事务管理确保转账的原子性
 *
 * 运行前准备：
 * 1. 确保MySQL数据库已启动
 * 2. 创建test数据库：CREATE DATABASE test;
 * 3. 执行init.sql中的SQL语句创建account表和测试数据
 * 4. 检查jdbc.properties中的数据库连接配置是否正确
 */
public class App {

    public static void main(String[] args) {
        // ============================================================
        // 步骤1：创建Spring IOC容器
        // ============================================================
        // AnnotationConfigApplicationContext是Spring提供的基于注解的配置容器
        // 参数SpringConfig.class指定了Spring配置类
        // 容器创建时会自动：
        // 1. 加载配置类
        // 2. 扫描组件（@Service、@Repository等）
        // 3. 创建Bean实例并管理依赖注入
        // 4. 初始化数据源和MyBatis
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        System.out.println("========== Spring IOC容器初始化完成 ==========\n");

        // ============================================================
        // 步骤2：从容器中获取AccountService Bean
        // ============================================================
        // getBean(Class<T> requiredType)方法：按类型从容器中获取Bean
        // Spring会自动查找类型为AccountService的Bean并返回
        // 由于AccountServiceImpl上标注了@Service，它会被注册为Bean
        AccountService accountService = ctx.getBean(AccountService.class);

        // ============================================================
        // 步骤3：执行转账操作
        // ============================================================
        System.out.println("========== 开始执行转账业务 ==========");
        System.out.println("初始状态：张三有1000元，李四有2000元\n");

        // 定义转账参数
        String fromAccount = "张三";  // 转出方
        String toAccount = "李四";    // 转入方
        Double transferAmount = 100.0; // 转账金额

        System.out.println("转账信息：");
        System.out.println("  转出方：" + fromAccount);
        System.out.println("  转入方：" + toAccount);
        System.out.println("  金额：" + transferAmount + " 元\n");

        try {
            // 调用转账方法
            // 此方法上有@Transactional注解，会在事务中执行
            accountService.transfer(fromAccount, toAccount, transferAmount);

            System.out.println("\n========== 转账业务执行完成 ==========");
            System.out.println("预期结果：张三有900元，李四有2100元");
            System.out.println("请查询数据库验证结果！");

        } catch (Exception e) {
            // 如果转账过程中发生异常（如余额不足、数据库连接失败等）
            // Spring会自动回滚事务，确保数据一致性
            System.err.println("\n========== 转账失败 ==========");
            System.err.println("错误信息：" + e.getMessage());
            System.err.println("事务已回滚，数据保持一致性");
            e.printStackTrace();
        }

        // ============================================================
        // 步骤4：关闭容器（可选）
        // ============================================================
        // 实际应用中，容器通常由框架（如Spring Boot）管理生命周期
        // 在main方法中演示时，可以显式关闭容器释放资源
        // ((AnnotationConfigApplicationContext) ctx).close();
    }
}