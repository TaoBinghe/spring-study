package com.coder.service;

import com.coder.domain.Account;

import java.util.List;

/**
 * 账户业务接口
 */
public interface AccountService {
    /**
     * 保存账户
     */
    void save(Account account);
    
    /**
     * 查询所有账户
     */
    List<Account> findAll();
    
    /**
     * 根据id查询账户
     */
    Account findById(Integer id);
}
