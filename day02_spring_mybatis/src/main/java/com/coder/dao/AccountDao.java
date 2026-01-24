package com.coder.dao;

import com.coder.domain.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 账户数据访问接口
 * MyBatis会自动创建此接口的代理实现类
 */
public interface AccountDao {
    
    /**
     * 保存账户
     */
    @Insert("insert into account(name, money) values(#{name}, #{money})")
    void save(Account account);
    
    /**
     * 查询所有账户
     */
    @Select("select * from account")
    List<Account> findAll();
    
    /**
     * 根据id查询账户
     */
    @Select("select * from account where id = #{id}")
    Account findById(Integer id);
}
