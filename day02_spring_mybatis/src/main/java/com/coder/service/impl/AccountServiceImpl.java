package com.coder.service.impl;

import com.coder.dao.AccountDao;
import com.coder.domain.Account;
import com.coder.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户业务实现类
 */
@Service
public class AccountServiceImpl implements AccountService {
    
    @Autowired
    private AccountDao accountDao;
    
    @Override
    public void save(Account account) {
        accountDao.save(account);
        System.out.println("保存账户成功：" + account);
    }
    
    @Override
    public List<Account> findAll() {
        List<Account> accounts = accountDao.findAll();
        System.out.println("查询到 " + accounts.size() + " 个账户");
        return accounts;
    }
    
    @Override
    public Account findById(Integer id) {
        Account account = accountDao.findById(id);
        System.out.println("查询账户：" + account);
        return account;
    }
}
