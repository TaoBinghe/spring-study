package com.coder.domain;

import lombok.Data;

/**
 * 账户实体类
 * 对应数据库表 account
 *
 * @Data 是Lombok提供的注解，自动生成以下方法：
 * - getter方法：用于获取属性值
 * - setter方法：用于设置属性值
 * - toString方法：用于打印对象信息
 * - equals和hashCode方法：用于对象比较
 *
 * 使用Lombok可以大大减少样板代码，让实体类更加简洁
 */
@Data
public class Account {

    /**
     * 账户ID
     * 对应数据库字段：id
     * 类型：INT，主键，自增
     */
    private Integer id;

    /**
     * 账户名称
     * 对应数据库字段：name
     * 类型：VARCHAR(50)
     * 用于标识账户持有人，如"张三"、"李四"
     */
    private String name;

    /**
     * 账户余额
     * 对应数据库字段：money
     * 类型：DOUBLE
     * 表示账户中的金额，单位：元
     */
    private Double money;
}