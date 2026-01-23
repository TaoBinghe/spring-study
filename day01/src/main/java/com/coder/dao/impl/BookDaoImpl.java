package com.coder.dao.impl;

import com.coder.dao.BookDao;

/**
 * BookDao实现类 - 用于演示setter注入简单值（String和int类型）
 */
public class BookDaoImpl implements BookDao {
    private String databaseName;
    private int connectionNum;

    /**
     * setter方法 - 用于注入databaseName属性
     * @param databaseName 数据库名称
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        System.out.println("注入databaseName: " + databaseName);
    }

    /**
     * setter方法 - 用于注入connectionNum属性
     * @param connectionNum 连接数
     */
    public void setConnectionNum(int connectionNum) {
        this.connectionNum = connectionNum;
        System.out.println("注入connectionNum: " + connectionNum);
    }

    @Override
    public void save() {
        System.out.println("save book - databaseName: " + databaseName + ", connectionNum: " + connectionNum);
    }
}
