-- =============================================
-- 导购绩效管理系统数据库表结构更新 (简化版)
-- 创建时间: 2025-01-22
-- 描述: 基于现有表结构，添加缺失的字段和表，不使用外键约束
-- =============================================

-- 1. 创建导购基础信息表 (如果不存在)
CREATE TABLE IF NOT EXISTS guide_info (
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

-- 2. 为现有guide_performance表添加缺失字段
-- 添加cai_score字段
ALTER TABLE guide_performance ADD COLUMN IF NOT EXISTS cai_score DECIMAL(5,4) DEFAULT 0 COMMENT 'CAI指数';

-- 添加rmv_score字段
ALTER TABLE guide_performance ADD COLUMN IF NOT EXISTS rmv_score DECIMAL(5,4) DEFAULT 0 COMMENT 'RMV指数';

-- 添加matrix_position字段
ALTER TABLE guide_performance ADD COLUMN IF NOT EXISTS matrix_position VARCHAR(10) COMMENT '九宫格位置(如:3-3)';

-- 添加matrix_type字段
ALTER TABLE guide_performance ADD COLUMN IF NOT EXISTS matrix_type VARCHAR(50) COMMENT '九宫格类型';

-- 添加trend字段
ALTER TABLE guide_performance ADD COLUMN IF NOT EXISTS trend VARCHAR(20) COMMENT '趋势(提升/持平/下降)';

-- 添加new_members字段
ALTER TABLE guide_performance ADD COLUMN IF NOT EXISTS new_members INT DEFAULT 0 COMMENT '新增会员数';

-- 3. 创建绩效历史记录表
CREATE TABLE IF NOT EXISTS guide_performance_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '历史记录ID',
    guide_id BIGINT NOT NULL COMMENT '导购ID',
    record_date DATE NOT NULL COMMENT '记录日期',
    cai_score DECIMAL(5,4) DEFAULT 0 COMMENT 'CAI指数',
    rmv_score DECIMAL(5,4) DEFAULT 0 COMMENT 'RMV指数',
    performance_score DECIMAL(5,2) DEFAULT 0 COMMENT '综合绩效分',
    matrix_position VARCHAR(10) COMMENT '九宫格位置',
    matrix_type VARCHAR(50) COMMENT '九宫格类型',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_guide_date (guide_id, record_date),
    INDEX idx_record_date (record_date)
) COMMENT='绩效历史记录表';

-- 4. 创建区域配置表
CREATE TABLE IF NOT EXISTS region_config (
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
INSERT IGNORE INTO region_config (region_code, region_name, region_manager, status) VALUES
('EAST', '华东区', '张经理', '启用'),
('SOUTH', '华南区', '李经理', '启用'),
('CENTRAL', '华中区', '王经理', '启用'),
('NORTH', '华北区', '赵经理', '启用'),
('SOUTHWEST', '西南区', '陈经理', '启用'),
('NORTHWEST', '西北区', '刘经理', '启用');

-- 插入导购基础信息测试数据
INSERT IGNORE INTO guide_info (guide_code, name, employee_id, region, store_name, department, level, status, phone, email, manager, join_date) VALUES
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

-- 更新现有guide_performance表的数据，添加CAI、RMV等字段的测试数据
UPDATE guide_performance SET 
    cai_score = CASE 
        WHEN performance_score >= 90 THEN ROUND(0.85 + RAND() * 0.15, 4)
        WHEN performance_score >= 80 THEN ROUND(0.70 + RAND() * 0.15, 4)
        WHEN performance_score >= 70 THEN ROUND(0.55 + RAND() * 0.15, 4)
        ELSE ROUND(0.40 + RAND() * 0.15, 4)
    END,
    rmv_score = CASE 
        WHEN performance_score >= 90 THEN ROUND(0.80 + RAND() * 0.20, 4)
        WHEN performance_score >= 80 THEN ROUND(0.65 + RAND() * 0.15, 4)
        WHEN performance_score >= 70 THEN ROUND(0.50 + RAND() * 0.15, 4)
        ELSE ROUND(0.35 + RAND() * 0.15, 4)
    END,
    new_members = FLOOR(20 + RAND() * 40),
    trend = CASE 
        WHEN RAND() > 0.6 THEN '提升'
        WHEN RAND() > 0.3 THEN '持平'
        ELSE '下降'
    END
WHERE (cai_score IS NULL OR cai_score = 0) AND performance_score > 0;

-- 根据CAI和RMV分数计算九宫格位置和类型
UPDATE guide_performance SET 
    matrix_position = CONCAT(
        CASE 
            WHEN cai_score >= 0.8 THEN '3'
            WHEN cai_score >= 0.6 THEN '2'
            ELSE '1'
        END,
        '-',
        CASE 
            WHEN rmv_score >= 0.8 THEN '3'
            WHEN rmv_score >= 0.6 THEN '2'
            ELSE '1'
        END
    ),
    matrix_type = CASE 
        WHEN cai_score >= 0.8 AND rmv_score >= 0.8 THEN '超级明星'
        WHEN cai_score >= 0.8 AND rmv_score >= 0.6 THEN '成长之星'
        WHEN cai_score >= 0.8 AND rmv_score < 0.6 THEN '销售专家'
        WHEN cai_score >= 0.6 AND rmv_score >= 0.8 THEN '关系专家'
        WHEN cai_score >= 0.6 AND rmv_score >= 0.6 THEN '骨干力量'
        WHEN cai_score >= 0.6 AND rmv_score < 0.6 THEN '效率达人'
        WHEN cai_score < 0.6 AND rmv_score >= 0.8 THEN '忠诚专家'
        WHEN cai_score < 0.6 AND rmv_score >= 0.6 THEN '服务达人'
        ELSE '新手上路'
    END
WHERE (matrix_position IS NULL OR matrix_position = '') AND cai_score > 0 AND rmv_score > 0;

-- =============================================
-- 添加索引优化
-- =============================================

-- 为guide_performance表添加索引
CREATE INDEX idx_cai_rmv ON guide_performance(cai_score, rmv_score);
CREATE INDEX idx_matrix_position ON guide_performance(matrix_position);

-- =============================================
-- 数据完整性检查
-- =============================================

-- 检查表创建结果
SELECT 'guide_info' as table_name, COUNT(*) as record_count FROM guide_info
UNION ALL
SELECT 'guide_performance', COUNT(*) FROM guide_performance
UNION ALL
SELECT 'guide_performance_history', COUNT(*) FROM guide_performance_history
UNION ALL
SELECT 'region_config', COUNT(*) FROM region_config;

-- 检查新增字段的数据
SELECT 
    COUNT(*) as total_records,
    COUNT(CASE WHEN cai_score > 0 THEN 1 END) as cai_records,
    COUNT(CASE WHEN rmv_score > 0 THEN 1 END) as rmv_records,
    COUNT(CASE WHEN matrix_position IS NOT NULL THEN 1 END) as matrix_records
FROM guide_performance;

COMMIT;