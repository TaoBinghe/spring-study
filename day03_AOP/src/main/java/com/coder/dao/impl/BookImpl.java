package com.coder.dao.impl;

import com.coder.dao.BookDao;

public class BookImpl implements BookDao {
    @Override
    public void save() {
        System.out.println("save");
    }

    @Override
    public void update() {
        System.out.println("update");
    }
}
