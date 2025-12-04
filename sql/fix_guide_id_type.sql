-- 修复guide_id字段类型问题
-- 将guide_performance表中的guide_id字段从BIGINT改为VARCHAR类型以支持字母数字混合格式

-- 1. 首先备份现有数据（如果有的话）
CREATE TABLE IF NOT EXISTS guide_performance_backup AS SELECT * FROM guide_performance;

-- 2. 删除外键约束（如果存在）
SET FOREIGN_KEY_CHECKS = 0;

-- 3. 修改guide_performance表的guide_id字段类型
ALTER TABLE guide_performance MODIFY COLUMN guide_id VARCHAR(50) NOT NULL COMMENT '导购ID';

-- 4. 如果guide_info表的guide_id也需要修改（保持一致性）
-- 注意：这可能会影响其他表的外键关系，需要谨慎处理
-- ALTER TABLE guide_info MODIFY COLUMN guide_id VARCHAR(50) NOT NULL COMMENT '导购ID';

-- 5. 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 6. 验证修改结果
DESCRIBE guide_performance;

-- 7. 如果有guide_performance_history表，也需要修改
ALTER TABLE guide_performance_history MODIFY COLUMN guide_id VARCHAR(50) NOT NULL COMMENT '导购ID';

-- 注意事项：
-- 1. 执行前请确保数据库已备份
-- 2. 如果guide_info表的主键也是guide_id，可能需要同时修改
-- 3. 检查是否有其他表引用guide_performance.guide_id作为外键
-- 4. 执行后需要重新编译和部署应用程序