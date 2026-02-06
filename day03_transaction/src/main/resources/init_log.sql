-- ============================================
-- 转账日志表 - 数据库初始化脚本
-- ============================================

-- 创建转账日志表
-- id: 主键，自增
-- from_account: 转出方账户
-- to_account: 转入方账户
-- amount: 转账金额
-- status: 转账状态（SUCCESS-成功，FAILED-失败）
-- message: 日志信息
-- create_time: 记录创建时间
CREATE TABLE IF NOT EXISTS transfer_log (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    from_account VARCHAR(50) NOT NULL COMMENT '转出方账户',
    to_account VARCHAR(50) NOT NULL COMMENT '转入方账户',
    amount DOUBLE NOT NULL COMMENT '转账金额',
    status VARCHAR(20) NOT NULL COMMENT '转账状态：SUCCESS/FAILED',
    message VARCHAR(500) COMMENT '日志详细信息',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间'
) COMMENT='转账日志表';

-- 清空表数据（如果存在）
TRUNCATE TABLE transfer_log;

-- 验证数据
SELECT * FROM transfer_log;
