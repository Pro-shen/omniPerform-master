-- =============================================
-- 智能运营模块月份支持数据库修改脚本
-- 创建时间: 2025-01-09
-- 说明: 为智能运营模块的所有表添加月份字段支持，实现按月份查询数据
-- =============================================

-- 1. 为智能运营概览统计表添加月份字段
ALTER TABLE smart_operation_overview 
ADD COLUMN month_year VARCHAR(7) COMMENT '月份(格式: YYYY-MM)' AFTER stat_date,
ADD INDEX idx_month_year (month_year);

-- 2. 为智能运营预警表添加月份字段
ALTER TABLE smart_operation_alerts 
ADD COLUMN month_year VARCHAR(7) COMMENT '月份(格式: YYYY-MM)' AFTER create_time,
ADD INDEX idx_month_year (month_year);

-- 3. 为AI推荐营销任务表添加月份字段
ALTER TABLE smart_marketing_tasks 
ADD COLUMN month_year VARCHAR(7) COMMENT '月份(格式: YYYY-MM)' AFTER create_time,
ADD INDEX idx_month_year (month_year);

-- 4. 为会员画像分析表添加月份字段
ALTER TABLE member_profile_analysis 
ADD COLUMN month_year VARCHAR(7) COMMENT '月份(格式: YYYY-MM)' AFTER analysis_date,
ADD INDEX idx_month_year (month_year);

-- 5. 为优化效果数据表添加月份字段
ALTER TABLE optimization_effect_data 
ADD COLUMN month_year VARCHAR(7) COMMENT '月份(格式: YYYY-MM)' AFTER stat_date,
ADD INDEX idx_month_year (month_year);

-- 6. 为最佳触达时间分析表添加月份字段
ALTER TABLE best_touch_time_analysis 
ADD COLUMN month_year VARCHAR(7) COMMENT '月份(格式: YYYY-MM)' AFTER analysis_date,
ADD INDEX idx_month_year (month_year);

-- =============================================
-- 更新现有数据的月份字段
-- =============================================

-- 1. 更新智能运营概览统计表的月份字段
UPDATE smart_operation_overview 
SET month_year = DATE_FORMAT(stat_date, '%Y-%m') 
WHERE month_year IS NULL;

-- 2. 更新智能运营预警表的月份字段
UPDATE smart_operation_alerts 
SET month_year = DATE_FORMAT(create_time, '%Y-%m') 
WHERE month_year IS NULL;

-- 3. 更新AI推荐营销任务表的月份字段
UPDATE smart_marketing_tasks 
SET month_year = DATE_FORMAT(create_time, '%Y-%m') 
WHERE month_year IS NULL;

-- 4. 更新会员画像分析表的月份字段
UPDATE member_profile_analysis 
SET month_year = DATE_FORMAT(analysis_date, '%Y-%m') 
WHERE month_year IS NULL;

-- 5. 更新优化效果数据表的月份字段
UPDATE optimization_effect_data 
SET month_year = DATE_FORMAT(stat_date, '%Y-%m') 
WHERE month_year IS NULL;

-- 6. 更新最佳触达时间分析表的月份字段
UPDATE best_touch_time_analysis 
SET month_year = DATE_FORMAT(analysis_date, '%Y-%m') 
WHERE month_year IS NULL;

-- =============================================
-- 创建触发器，自动维护月份字段
-- =============================================

-- 1. 智能运营概览统计表触发器
DELIMITER $$
CREATE TRIGGER tr_smart_operation_overview_month_year
BEFORE INSERT ON smart_operation_overview
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.stat_date, '%Y-%m');
END$$

CREATE TRIGGER tr_smart_operation_overview_month_year_update
BEFORE UPDATE ON smart_operation_overview
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.stat_date, '%Y-%m');
END$$

-- 2. 智能运营预警表触发器
CREATE TRIGGER tr_smart_operation_alerts_month_year
BEFORE INSERT ON smart_operation_alerts
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.create_time, '%Y-%m');
END$$

CREATE TRIGGER tr_smart_operation_alerts_month_year_update
BEFORE UPDATE ON smart_operation_alerts
FOR EACH ROW
BEGIN
    IF NEW.create_time != OLD.create_time THEN
        SET NEW.month_year = DATE_FORMAT(NEW.create_time, '%Y-%m');
    END IF;
END$$

-- 3. AI推荐营销任务表触发器
CREATE TRIGGER tr_smart_marketing_tasks_month_year
BEFORE INSERT ON smart_marketing_tasks
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.create_time, '%Y-%m');
END$$

CREATE TRIGGER tr_smart_marketing_tasks_month_year_update
BEFORE UPDATE ON smart_marketing_tasks
FOR EACH ROW
BEGIN
    IF NEW.create_time != OLD.create_time THEN
        SET NEW.month_year = DATE_FORMAT(NEW.create_time, '%Y-%m');
    END IF;
END$$

-- 4. 会员画像分析表触发器
CREATE TRIGGER tr_member_profile_analysis_month_year
BEFORE INSERT ON member_profile_analysis
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.analysis_date, '%Y-%m');
END$$

CREATE TRIGGER tr_member_profile_analysis_month_year_update
BEFORE UPDATE ON member_profile_analysis
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.analysis_date, '%Y-%m');
END$$

-- 5. 优化效果数据表触发器
CREATE TRIGGER tr_optimization_effect_data_month_year
BEFORE INSERT ON optimization_effect_data
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.stat_date, '%Y-%m');
END$$

CREATE TRIGGER tr_optimization_effect_data_month_year_update
BEFORE UPDATE ON optimization_effect_data
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.stat_date, '%Y-%m');
END$$

-- 6. 最佳触达时间分析表触发器
CREATE TRIGGER tr_best_touch_time_analysis_month_year
BEFORE INSERT ON best_touch_time_analysis
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.analysis_date, '%Y-%m');
END$$

CREATE TRIGGER tr_best_touch_time_analysis_month_year_update
BEFORE UPDATE ON best_touch_time_analysis
FOR EACH ROW
BEGIN
    SET NEW.month_year = DATE_FORMAT(NEW.analysis_date, '%Y-%m');
END$$

DELIMITER ;

-- =============================================
-- 验证脚本执行结果
-- =============================================

-- 查看表结构变更结果
SHOW COLUMNS FROM smart_operation_overview LIKE 'month_year';
SHOW COLUMNS FROM smart_operation_alerts LIKE 'month_year';
SHOW COLUMNS FROM smart_marketing_tasks LIKE 'month_year';
SHOW COLUMNS FROM member_profile_analysis LIKE 'month_year';
SHOW COLUMNS FROM optimization_effect_data LIKE 'month_year';
SHOW COLUMNS FROM best_touch_time_analysis LIKE 'month_year';

-- 查看索引创建结果
SHOW INDEX FROM smart_operation_overview WHERE Key_name = 'idx_month_year';
SHOW INDEX FROM smart_operation_alerts WHERE Key_name = 'idx_month_year';
SHOW INDEX FROM smart_marketing_tasks WHERE Key_name = 'idx_month_year';
SHOW INDEX FROM member_profile_analysis WHERE Key_name = 'idx_month_year';
SHOW INDEX FROM optimization_effect_data WHERE Key_name = 'idx_month_year';
SHOW INDEX FROM best_touch_time_analysis WHERE Key_name = 'idx_month_year';

-- 查看触发器创建结果
SHOW TRIGGERS LIKE 'tr_%month_year%';

-- 验证数据更新结果（查看每个表的月份字段数据）
SELECT COUNT(*) as total_records, COUNT(month_year) as month_year_records 
FROM smart_operation_overview;

SELECT COUNT(*) as total_records, COUNT(month_year) as month_year_records 
FROM smart_operation_alerts;

SELECT COUNT(*) as total_records, COUNT(month_year) as month_year_records 
FROM smart_marketing_tasks;

SELECT COUNT(*) as total_records, COUNT(month_year) as month_year_records 
FROM member_profile_analysis;

SELECT COUNT(*) as total_records, COUNT(month_year) as month_year_records 
FROM optimization_effect_data;

SELECT COUNT(*) as total_records, COUNT(month_year) as month_year_records 
FROM best_touch_time_analysis;