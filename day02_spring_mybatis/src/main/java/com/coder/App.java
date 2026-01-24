package com.coder;

import com.coder.config.SpringConfig;
import com.coder.domain.Account;
import com.coder.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * MyBatis整合Spring演示
 */
public class App {
    public static void main(String[] args) {
        // 创建IOC容器
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        
        // 获取AccountService
        AccountService accountService = ctx.getBean(AccountService.class);
        
        System.out.println("========== MyBatis整合Spring演示 ==========");
        
        // 保存账户
        Account account = new Account();
        account.setName("张三");
        account.setMoney(1000.0);
        accountService.save(account);
        
        // 查询所有账户
        List<Account> accounts = accountService.findAll();
        accounts.forEach(System.out::println);
        
        System.out.println("==========================================");
    }
}
