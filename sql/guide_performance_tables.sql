-- =============================================
-- 导购绩效管理系统数据库表结构
-- 创建时间: 2025-01-22
-- 描述: 支持绩效导购界面的完整数据库表结构
-- =============================================

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 导购基础信息表
DROP TABLE IF EXISTS guide_info;
CREATE TABLE guide_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '导购ID',
    guide_code VARCHAR(50) NOT NULL UNIQUE COMMENT '导购编号',
    name VARCHAR(100) NOT NULL COMMENT '导购姓名',
    employee_id VARCHAR(50) UNIQUE COMMENT '员工编号',
    region VARCHAR(50) COMMENT '所属区域',
    store_name VARCHAR(200) COMMENT '门店名称',
    department VARCHAR(100) COMMENT '所属部门',
    level VARCHAR(20) COMMENT '导购等级',
    status VARCHAR(20) DEFAULT '在职' COMMENT '在职状态',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱地址',
    manager VARCHAR(100) COMMENT '直属经理',
    join_date DATE COMMENT '入职日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_guide_code (guide_code),
    INDEX idx_region (region),
    INDEX idx_status (status),
    INDEX idx_employee_id (employee_id)
) COMMENT='导购基础信息表';

-- 2. 导购绩效数据表
DROP TABLE IF EXISTS guide_performance;
CREATE TABLE guide_performance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '绩效记录ID',
    guide_id BIGINT NOT NULL COMMENT '导购ID',
    period VARCHAR(20) NOT NULL COMMENT '统计周期(YYYY-MM)',
    cai_score DECIMAL(5,4) DEFAULT 0 COMMENT 'CAI指数',
    rmv_score DECIMAL(5,4) DEFAULT 0 COMMENT 'RMV指数',
    performance_score DECIMAL(5,2) DEFAULT 0 COMMENT '综合绩效分',
    new_members INT DEFAULT 0 COMMENT '新增会员数',
    mot_completion_rate DECIMAL(5,2) DEFAULT 0 COMMENT 'MOT完成率(%)',
    customer_satisfaction DECIMAL(3,2) DEFAULT 0 COMMENT '客户满意度',
    sales_amount DECIMAL(12,2) DEFAULT 0 COMMENT '销售金额',
    mot_tasks_completed INT DEFAULT 0 COMMENT '完成MOT任务数',
    response_time DECIMAL(5,2) DEFAULT 0 COMMENT '响应时间(小时)',
    matrix_position VARCHAR(10) COMMENT '九宫格位置(如:3-3)',
    matrix_type VARCHAR(50) COMMENT '九宫格类型',
    trend VARCHAR(20) COMMENT '趋势(提升/持平/下降)',
    rank_in_region INT COMMENT '区域排名',
    rank_overall INT COMMENT '总体排名',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (guide_id) REFERENCES guide_info(id) ON DELETE CASCADE,
    UNIQUE KEY uk_guide_period (guide_id, period),
    INDEX idx_period (period),
    INDEX idx_cai_rmv (cai_score, rmv_score),
    INDEX idx_performance_score (performance_score),
    INDEX idx_matrix_position (matrix_position)
) COMMENT='导购绩效数据表';

-- 3. 绩效历史记录表
DROP TABLE IF EXISTS guide_performance_history;
CREATE TABLE guide_performance_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '历史记录ID',
    guide_id BIGINT NOT NULL COMMENT '导购ID',
    record_date DATE NOT NULL COMMENT '记录日期',
    cai_score DECIMAL(5,4) DEFAULT 0 COMMENT 'CAI指数',
    rmv_score DECIMAL(5,4) DEFAULT 0 COMMENT 'RMV指数',
    performance_score DECIMAL(5,2) DEFAULT 0 COMMENT '综合绩效分',
    matrix_position VARCHAR(10) COMMENT '九宫格位置',
    matrix_type VARCHAR(50) COMMENT '九宫格类型',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (guide_id) REFERENCES guide_info(id) ON DELETE CASCADE,
    INDEX idx_guide_date (guide_id, record_date),
    INDEX idx_record_date (record_date)
) COMMENT='绩效历史记录表';

-- 4. 区域配置表
DROP TABLE IF EXISTS region_config;
CREATE TABLE region_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '区域ID',
    region_code VARCHAR(50) NOT NULL UNIQUE COMMENT '区域编码',
    region_name VARCHAR(100) NOT NULL COMMENT '区域名称',
    region_manager VARCHAR(100) COMMENT '区域经理',
    status VARCHAR(20) DEFAULT '启用' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_region_code (region_code),
    INDEX idx_status (status)
) COMMENT='区域配置表';

-- =============================================
-- 初始化基础数据
-- =============================================

-- 插入区域配置数据
INSERT INTO region_config (region_code, region_name, region_manager, status) VALUES
('EAST', '华东区', '张经理', '启用'),
('SOUTH', '华南区', '李经理', '启用'),
('CENTRAL', '华中区', '王经理', '启用'),
('NORTH', '华北区', '赵经理', '启用'),
('SOUTHWEST', '西南区', '陈经理', '启用'),
('NORTHWEST', '西北区', '刘经理', '启用');

-- 插入导购基础信息测试数据
INSERT INTO guide_info (guide_code, name, employee_id, region, store_name, department, level, status, phone, email, manager, join_date) VALUES
('G20250001', '张明', 'EMP001', 'EAST', '杭州西湖店', '销售部', '高级', '在职', '13812345678', 'zhangming@company.com', '张经理', '2023-01-15'),
('G20250015', '李华', 'EMP015', 'EAST', '南京新街口店', '销售部', '中级', '在职', '13823456789', 'lihua@company.com', '张经理', '2023-03-20'),
('G20250023', '王丽', 'EMP023', 'EAST', '合肥滨湖店', '销售部', '中级', '在职', '13834567890', 'wangli@company.com', '张经理', '2023-05-10'),
('G20250042', '陈静', 'EMP042', 'SOUTH', '深圳南山店', '销售部', '中级', '在职', '13845678901', 'chenjing@company.com', '李经理', '2023-02-28'),
('G20250056', '赵薇', 'EMP056', 'NORTH', '北京朝阳店', '销售部', '初级', '在职', '13856789012', 'zhaowei@company.com', '赵经理', '2023-06-15'),
('G20250067', '刘强', 'EMP067', 'EAST', '上海浦东店', '销售部', '高级', '在职', '13867890123', 'liuqiang@company.com', '张经理', '2022-12-01'),
('G20250078', '孙娜', 'EMP078', 'SOUTH', '广州天河店', '销售部', '中级', '在职', '13878901234', 'sunna@company.com', '李经理', '2023-04-18'),
('G20250089', '周杰', 'EMP089', 'SOUTHWEST', '成都春熙店', '销售部', '中级', '在职', '13889012345', 'zhoujie@company.com', '陈经理', '2023-07-22'),
('G20250091', '吴琳', 'EMP091', 'CENTRAL', '武汉江汉店', '销售部', '中级', '在职', '13890123456', 'wulin@company.com', '王经理', '2023-08-05'),
('G20250102', '郑浩', 'EMP102', 'NORTHWEST', '西安雁塔店', '销售部', '中级', '在职', '13901234567', 'zhenghao@company.com', '刘经理', '2023-09-12');

-- 插入绩效数据测试数据 (2024年6月)
INSERT INTO guide_performance (guide_id, period, cai_score, rmv_score, performance_score, new_members, mot_completion_rate, customer_satisfaction, sales_amount, mot_tasks_completed, response_time, matrix_position, matrix_type, trend, rank_in_region, rank_overall) VALUES
(1, '2024-06', 0.92, 0.88, 95.5, 50, 98.5, 4.8, 150000.00, 45, 2.5, '3-3', '超级明星', '提升', 1, 1),
(2, '2024-06', 0.85, 0.75, 88.2, 42, 95.0, 4.6, 135000.00, 40, 3.0, '3-2', '成长之星', '提升', 2, 3),
(3, '2024-06', 0.65, 0.82, 82.5, 35, 92.0, 4.5, 120000.00, 38, 3.2, '2-3', '关系专家', '持平', 3, 8),
(4, '2024-06', 0.72, 0.68, 78.8, 32, 88.5, 4.3, 110000.00, 35, 3.5, '2-2', '骨干力量', '提升', 1, 12),
(5, '2024-06', 0.45, 0.58, 65.2, 25, 82.0, 4.0, 85000.00, 28, 4.0, '1-2', '服务达人', '下降', 1, 25),
(6, '2024-06', 0.88, 0.91, 92.8, 48, 96.8, 4.7, 145000.00, 43, 2.8, '3-3', '超级明星', '提升', 4, 2),
(7, '2024-06', 0.78, 0.73, 85.5, 38, 93.5, 4.4, 125000.00, 37, 3.1, '3-2', '成长之星', '持平', 2, 6),
(8, '2024-06', 0.55, 0.85, 79.2, 30, 89.0, 4.2, 105000.00, 32, 3.8, '2-3', '关系专家', '提升', 1, 11),
(9, '2024-06', 0.69, 0.62, 75.5, 28, 85.5, 4.1, 95000.00, 30, 3.6, '2-2', '骨干力量', '下降', 1, 18),
(10, '2024-06', 0.82, 0.79, 87.3, 40, 94.2, 4.5, 130000.00, 39, 2.9, '3-2', '成长之星', '提升', 1, 5);

-- 插入绩效数据测试数据 (2024年5月)
INSERT INTO guide_performance (guide_id, period, cai_score, rmv_score, performance_score, new_members, mot_completion_rate, customer_satisfaction, sales_amount, mot_tasks_completed, response_time, matrix_position, matrix_type, trend, rank_in_region, rank_overall) VALUES
(1, '2024-05', 0.88, 0.85, 92.2, 45, 96.0, 4.7, 140000.00, 42, 2.8, '3-3', '超级明星', '提升', 1, 2),
(2, '2024-05', 0.82, 0.72, 85.8, 38, 92.5, 4.5, 125000.00, 37, 3.2, '3-2', '成长之星', '持平', 2, 5),
(3, '2024-05', 0.63, 0.80, 81.2, 33, 90.0, 4.4, 115000.00, 36, 3.4, '2-3', '关系专家', '提升', 3, 9),
(4, '2024-05', 0.68, 0.65, 75.5, 28, 85.0, 4.2, 100000.00, 32, 3.8, '2-2', '骨干力量', '持平', 2, 15),
(5, '2024-05', 0.48, 0.62, 68.8, 28, 85.5, 4.1, 90000.00, 30, 3.9, '1-2', '服务达人', '提升', 2, 22),
(6, '2024-05', 0.85, 0.88, 89.5, 44, 94.2, 4.6, 135000.00, 40, 3.0, '3-3', '超级明星', '持平', 4, 3),
(7, '2024-05', 0.75, 0.70, 82.8, 35, 91.0, 4.3, 118000.00, 35, 3.3, '3-2', '成长之星', '下降', 3, 7),
(8, '2024-05', 0.52, 0.82, 76.5, 27, 86.5, 4.1, 98000.00, 29, 4.0, '2-3', '关系专家', '持平', 2, 14),
(9, '2024-05', 0.72, 0.65, 78.2, 30, 88.0, 4.2, 102000.00, 33, 3.5, '2-2', '骨干力量', '提升', 2, 12),
(10, '2024-05', 0.78, 0.76, 84.5, 36, 91.8, 4.4, 122000.00, 36, 3.1, '3-2', '成长之星', '持平', 2, 6);

-- 插入绩效数据测试数据 (2024年4月)
INSERT INTO guide_performance (guide_id, period, cai_score, rmv_score, performance_score, new_members, mot_completion_rate, customer_satisfaction, sales_amount, mot_tasks_completed, response_time, matrix_position, matrix_type, trend, rank_in_region, rank_overall) VALUES
(1, '2024-04', 0.85, 0.82, 89.8, 40, 93.5, 4.6, 130000.00, 38, 3.0, '3-3', '超级明星', '持平', 1, 3),
(2, '2024-04', 0.78, 0.68, 82.5, 34, 89.0, 4.3, 115000.00, 34, 3.5, '3-2', '成长之星', '提升', 3, 8),
(3, '2024-04', 0.60, 0.78, 78.8, 30, 87.5, 4.2, 108000.00, 33, 3.6, '2-3', '关系专家', '下降', 4, 11),
(4, '2024-04', 0.65, 0.62, 72.2, 25, 82.0, 4.0, 92000.00, 28, 4.0, '2-2', '骨干力量', '下降', 3, 18),
(5, '2024-04', 0.45, 0.58, 65.5, 24, 80.5, 3.9, 82000.00, 26, 4.2, '1-2', '服务达人', '持平', 3, 25),
(6, '2024-04', 0.82, 0.85, 86.8, 41, 92.0, 4.5, 128000.00, 37, 3.2, '3-3', '超级明星', '提升', 5, 4),
(7, '2024-04', 0.72, 0.68, 80.2, 32, 88.5, 4.2, 112000.00, 32, 3.4, '2-2', '骨干力量', '提升', 4, 10),
(8, '2024-04', 0.48, 0.80, 73.8, 24, 83.0, 4.0, 90000.00, 26, 4.1, '1-3', '忠诚专家', '下降', 3, 16),
(9, '2024-04', 0.68, 0.60, 74.5, 26, 84.0, 4.1, 95000.00, 29, 3.8, '2-2', '骨干力量', '持平', 3, 15),
(10, '2024-04', 0.75, 0.73, 81.8, 33, 90.2, 4.3, 118000.00, 34, 3.3, '3-2', '成长之星', '下降', 3, 9);

-- 插入绩效历史记录数据
INSERT INTO guide_performance_history (guide_id, record_date, cai_score, rmv_score, performance_score, matrix_position, matrix_type) 
SELECT guide_id, DATE(CONCAT(period, '-01')), cai_score, rmv_score, performance_score, matrix_position, matrix_type 
FROM guide_performance;

-- =============================================
-- 创建视图和存储过程
-- =============================================

-- 创建绩效概览视图
CREATE OR REPLACE VIEW v_performance_overview AS
SELECT 
    COUNT(*) as total_guides,
    COUNT(CASE WHEN gi.status = '在职' THEN 1 END) as active_guides,
    ROUND(AVG(gp.performance_score), 1) as avg_performance_score,
    COUNT(CASE WHEN gp.matrix_position IN ('3-3', '3-2', '2-3') THEN 1 END) as top_performer_count,
    ROUND(AVG(gp.cai_score), 3) as avg_cai,
    ROUND(AVG(gp.rmv_score), 3) as avg_rmv
FROM guide_info gi
LEFT JOIN guide_performance gp ON gi.id = gp.guide_id AND gp.period = DATE_FORMAT(NOW(), '%Y-%m')
WHERE gi.status = '在职';

-- 创建九宫格分布视图
CREATE OR REPLACE VIEW v_matrix_distribution AS
SELECT 
    CASE 
        WHEN cai_score >= 0.8 THEN '高'
        WHEN cai_score >= 0.6 THEN '中'
        ELSE '低'
    END as x,
    CASE 
        WHEN rmv_score >= 0.8 THEN '高'
        WHEN rmv_score >= 0.6 THEN '中'
        ELSE '低'
    END as y,
    COUNT(*) as z,
    matrix_position as position,
    matrix_type as type
FROM guide_performance gp
WHERE period = DATE_FORMAT(NOW(), '%Y-%m')
GROUP BY x, y, matrix_position, matrix_type;

-- =============================================
-- 索引优化
-- =============================================

-- 为常用查询添加复合索引
CREATE INDEX idx_guide_performance_period_region ON guide_performance(period, guide_id);
CREATE INDEX idx_guide_info_region_status ON guide_info(region, status);
CREATE INDEX idx_performance_score_desc ON guide_performance(performance_score DESC);

-- =============================================
-- 数据完整性检查
-- =============================================

-- 检查数据插入结果
SELECT '导购基础信息表' as table_name, COUNT(*) as record_count FROM guide_info
UNION ALL
SELECT '导购绩效数据表', COUNT(*) FROM guide_performance
UNION ALL
SELECT '绩效历史记录表', COUNT(*) FROM guide_performance_history
UNION ALL
SELECT '区域配置表', COUNT(*) FROM region_config;

-- 检查绩效数据分布
SELECT 
    period,
    COUNT(*) as guide_count,
    ROUND(AVG(performance_score), 2) as avg_score,
    MIN(performance_score) as min_score,
    MAX(performance_score) as max_score
FROM guide_performance 
GROUP BY period 
ORDER BY period DESC;

-- 检查九宫格分布
SELECT 
    matrix_position,
    matrix_type,
    COUNT(*) as count
FROM guide_performance 
WHERE period = '2024-06'
GROUP BY matrix_position, matrix_type
ORDER BY matrix_position;

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

COMMIT;