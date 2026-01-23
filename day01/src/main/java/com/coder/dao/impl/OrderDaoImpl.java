package com.coder.dao.impl;

import com.coder.dao.OrderDao;

public class OrderDaoImpl implements OrderDao {
    @Override
    public void save() {
        System.out.println("OrderDao save...");
    }
}
