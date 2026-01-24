-- 创建账户表
CREATE TABLE IF NOT EXISTS account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    money DOUBLE NOT NULL
);

-- 插入测试数据
INSERT INTO account (name, money) VALUES ('张三', 1000.0);
INSERT INTO account (name, money) VALUES ('李四', 2000.0);
