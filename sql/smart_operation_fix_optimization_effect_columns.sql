-- =============================================
-- 修复：为optimization_effect_data表补充mom_rate、yoy_rate列
-- 说明：后端Mapper与领域模型已使用mom_rate、yoy_rate，现有库缺列导致SQLSyntaxError
-- 执行环境：MySQL
-- 使用方法：在目标数据库执行此脚本
-- =============================================

ALTER TABLE optimization_effect_data
    ADD COLUMN mom_rate DECIMAL(5,2) COMMENT '环比(%)' AFTER metric_value,
    ADD COLUMN yoy_rate DECIMAL(5,2) COMMENT '同比(%)' AFTER mom_rate;

-- 可选：如果历史上improvement_rate即为环比，可按需迁移（谨慎执行）
-- UPDATE optimization_effect_data SET mom_rate = improvement_rate WHERE mom_rate IS NULL AND improvement_rate IS NOT NULL;

-- 验证列是否存在
SHOW COLUMNS FROM optimization_effect_data LIKE 'mom_rate';
SHOW COLUMNS FROM optimization_effect_data LIKE 'yoy_rate';