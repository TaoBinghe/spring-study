package com.coder.service.impl;

import com.coder.dao.BookDao;
import com.coder.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BookService实现类
 * @Service 注解：标识为业务层组件，会被Spring扫描并注册为Bean
 * @Autowired 注解：自动注入依赖的BookDao对象
 */
@Service
public class BookServiceImpl implements BookService {
    
    @Autowired
    private BookDao bookDao;
    
    @Override
    public void save() {
        System.out.println("BookService save begin");
        bookDao.save();
        System.out.println("BookService save end");
    }
}
