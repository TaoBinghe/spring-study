-- ============================================
-- 银行账户转账业务 - 数据库初始化脚本
-- ============================================

-- 创建账户表
-- id: 主键，自增
-- name: 账户名称（唯一，用于标识账户）
-- money: 账户余额
CREATE TABLE IF NOT EXISTS account (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '账户ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '账户名称',
    money DOUBLE NOT NULL DEFAULT 0 COMMENT '账户余额'
) COMMENT='账户表';

-- 清空表数据（如果存在）
TRUNCATE TABLE account;

-- 插入测试数据
-- 张三初始金额1000元
INSERT INTO account (name, money) VALUES ('张三', 1000.0);

-- 李四初始金额2000元
INSERT INTO account (name, money) VALUES ('李四', 2000.0);

-- 验证数据
SELECT * FROM account;