package com.coder.dao.impl;

import com.coder.dao.BookDao;
import org.springframework.stereotype.Repository;

/**
 * BookDao实现类
 * @Repository 注解：标识为数据访问层组件，会被Spring扫描并注册为Bean
 */
@Repository
public class BookDaoImpl implements BookDao {
    @Override
    public void save() {
        System.out.println("BookDao save...");
    }
}
