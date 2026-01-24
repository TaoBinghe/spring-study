package com.coder.service.impl;

import com.coder.dao.BookDao;
import com.coder.dao.UserDao;
import com.coder.service.BookService;

/**
 * BookService实现类 - 用于演示setter注入对象引用（ref）
 */
public class BookServiceImpl implements BookService {
    private BookDao bookDao;
    private UserDao userDao;

    /**
     * setter方法 - 用于注入bookDao对象引用
     * @param bookDao BookDao实例
     */
    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
        System.out.println("注入bookDao对象引用");
    }

    /**
     * setter方法 - 用于注入userDao对象引用
     * @param userDao UserDao实例
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
        System.out.println("注入userDao对象引用");
    }

    @Override
    public void save() {
        System.out.println("BookService save begin");
        if (bookDao != null) {
            bookDao.save();
        }
        if (userDao != null) {
            userDao.save();
        }
        System.out.println("BookService save end");
    }
}
