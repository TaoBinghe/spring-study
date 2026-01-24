package com.coder.domain;

import lombok.Data;

/**
 * 账户实体类
 */
@Data
public class Account {
    private Integer id;
    private String name;
    private Double money;
}
