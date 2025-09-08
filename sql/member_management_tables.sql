-- =============================================
-- 会员管理模块数据表创建脚本
-- 创建时间: 2025-01-09
-- 说明: 支持会员管理模块的完整功能，包括月度查询
-- =============================================

-- 1. 会员基础信息表
CREATE TABLE member_info (
    member_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会员ID',
    member_code VARCHAR(50) UNIQUE NOT NULL COMMENT '会员编号',
    member_name VARCHAR(100) NOT NULL COMMENT '会员姓名',
    phone VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(100) COMMENT '邮箱地址',
    gender TINYINT COMMENT '性别：1-男，2-女',
    birth_date DATE COMMENT '出生日期',
    baby_birth_date DATE COMMENT '宝宝出生日期',
    baby_stage VARCHAR(20) COMMENT '宝宝阶段：孕期、0-6月、6-12月、12-24月、24月+',
    registration_date DATETIME NOT NULL COMMENT '注册时间',
    registration_source VARCHAR(50) COMMENT '注册来源：线下门店、线上商城、微信小程序、APP等',
    guide_id BIGINT COMMENT '专属导购ID',
    region_code VARCHAR(20) COMMENT '所属区域代码',
    city VARCHAR(50) COMMENT '所在城市',
    status TINYINT DEFAULT 1 COMMENT '状态：1-正常，2-冻结，3-注销',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_purchase_time DATETIME COMMENT '最后购买时间',
    last_interaction_time DATETIME COMMENT '最后互动时间',
    total_purchase_amount DECIMAL(10,2) DEFAULT 0 COMMENT '累计购买金额',
    total_purchase_count INT DEFAULT 0 COMMENT '累计购买次数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_member_code (member_code),
    INDEX idx_phone (phone),
    INDEX idx_baby_stage (baby_stage),
    INDEX idx_registration_date (registration_date),
    INDEX idx_guide_id (guide_id),
    INDEX idx_region_code (region_code)
) COMMENT='会员基础信息表';

-- 2. 会员月度统计表
CREATE TABLE member_monthly_statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    stat_month VARCHAR(7) NOT NULL COMMENT '统计月份，格式：YYYY-MM',
    total_members INT DEFAULT 0 COMMENT '总会员数',
    active_members INT DEFAULT 0 COMMENT '活跃会员数（当月有互动）',
    new_members INT DEFAULT 0 COMMENT '新增会员数',
    lost_members INT DEFAULT 0 COMMENT '流失会员数',
    repeat_purchase_rate DECIMAL(5,2) DEFAULT 0 COMMENT '复购率（%）',
    avg_purchase_amount DECIMAL(10,2) DEFAULT 0 COMMENT '平均购买金额',
    total_purchase_amount DECIMAL(15,2) DEFAULT 0 COMMENT '总购买金额',
    total_interactions INT DEFAULT 0 COMMENT '总互动次数',
    region_code VARCHAR(20) COMMENT '区域代码，NULL表示全国统计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_month_region (stat_month, region_code),
    INDEX idx_stat_month (stat_month),
    INDEX idx_region_code (region_code)
) COMMENT='会员月度统计表';

-- 3. 会员生命周期记录表
CREATE TABLE member_lifecycle_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    lifecycle_stage VARCHAR(20) NOT NULL COMMENT '生命周期阶段：导入期、成长期、成熟期、衰退期、流失期',
    stage_start_date DATETIME NOT NULL COMMENT '阶段开始时间',
    stage_end_date DATETIME COMMENT '阶段结束时间',
    days_in_stage INT COMMENT '在该阶段停留天数',
    trigger_event VARCHAR(100) COMMENT '触发阶段变化的事件',
    stage_description TEXT COMMENT '阶段描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_member_id (member_id),
    INDEX idx_lifecycle_stage (lifecycle_stage),
    INDEX idx_stage_start_date (stage_start_date),
    FOREIGN KEY (member_id) REFERENCES member_info(member_id)
) COMMENT='会员生命周期记录表';

-- 4. CRFM-E评分表
CREATE TABLE member_crfme_scores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    score_month VARCHAR(7) NOT NULL COMMENT '评分月份，格式：YYYY-MM',
    c_score DECIMAL(5,2) DEFAULT 0 COMMENT 'C维度评分：宝宝成长阶段匹配度',
    r_score DECIMAL(5,2) DEFAULT 0 COMMENT 'R维度评分：关系深度',
    f_score DECIMAL(5,2) DEFAULT 0 COMMENT 'F维度评分：功能参与度',
    m_score DECIMAL(5,2) DEFAULT 0 COMMENT 'M维度评分：多维价值',
    e_score DECIMAL(5,2) DEFAULT 0 COMMENT 'E维度评分：情感连接',
    total_score DECIMAL(5,2) DEFAULT 0 COMMENT '总评分',
    score_level VARCHAR(20) COMMENT '评分等级：高价值、潜力、普通、低价值',
    calculation_date DATETIME NOT NULL COMMENT '计算时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_member_month (member_id, score_month),
    INDEX idx_score_month (score_month),
    INDEX idx_total_score (total_score),
    INDEX idx_score_level (score_level),
    FOREIGN KEY (member_id) REFERENCES member_info(member_id)
) COMMENT='CRFM-E评分表';

-- 5. 会员分层表
CREATE TABLE member_segmentation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    segment_month VARCHAR(7) NOT NULL COMMENT '分层月份，格式：YYYY-MM',
    segment_type VARCHAR(50) NOT NULL COMMENT '分层类型：高价值会员、潜力会员、新会员、沉默会员等',
    segment_criteria TEXT COMMENT '分层标准描述',
    segment_strategy TEXT COMMENT '运营策略',
    entry_date DATETIME NOT NULL COMMENT '进入该分层时间',
    exit_date DATETIME COMMENT '离开该分层时间',
    is_current TINYINT DEFAULT 1 COMMENT '是否当前分层：1-是，0-否',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_member_id (member_id),
    INDEX idx_segment_month (segment_month),
    INDEX idx_segment_type (segment_type),
    INDEX idx_is_current (is_current),
    FOREIGN KEY (member_id) REFERENCES member_info(member_id)
) COMMENT='会员分层表';

-- 6. 会员活动记录表
CREATE TABLE member_activity_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    activity_type VARCHAR(50) NOT NULL COMMENT '活动类型：登录、购买、浏览、互动、签到等',
    activity_detail TEXT COMMENT '活动详情',
    activity_value DECIMAL(10,2) COMMENT '活动价值（如购买金额）',
    activity_date DATETIME NOT NULL COMMENT '活动时间',
    source_channel VARCHAR(50) COMMENT '来源渠道',
    guide_id BIGINT COMMENT '相关导购ID',
    region_code VARCHAR(20) COMMENT '区域代码',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_member_id (member_id),
    INDEX idx_activity_type (activity_type),
    INDEX idx_activity_date (activity_date),
    INDEX idx_guide_id (guide_id),
    INDEX idx_region_code (region_code),
    FOREIGN KEY (member_id) REFERENCES member_info(member_id)
) COMMENT='会员活动记录表';

-- 7. 会员月度阶段统计表
CREATE TABLE member_stage_monthly_stats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    stat_month VARCHAR(7) NOT NULL COMMENT '统计月份，格式：YYYY-MM',
    baby_stage VARCHAR(20) NOT NULL COMMENT '宝宝阶段',
    member_count INT DEFAULT 0 COMMENT '该阶段会员数量',
    new_members INT DEFAULT 0 COMMENT '新增会员数',
    active_members INT DEFAULT 0 COMMENT '活跃会员数',
    purchase_amount DECIMAL(15,2) DEFAULT 0 COMMENT '购买金额',
    avg_crfme_score DECIMAL(5,2) DEFAULT 0 COMMENT '平均CRFM-E评分',
    region_code VARCHAR(20) COMMENT '区域代码，NULL表示全国统计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_month_stage_region (stat_month, baby_stage, region_code),
    INDEX idx_stat_month (stat_month),
    INDEX idx_baby_stage (baby_stage),
    INDEX idx_region_code (region_code)
) COMMENT='会员月度阶段统计表';

-- =============================================
-- 初始化测试数据
-- =============================================

-- 插入会员月度统计测试数据
INSERT INTO member_monthly_statistics (stat_month, total_members, active_members, new_members, lost_members, repeat_purchase_rate, avg_purchase_amount, total_purchase_amount, total_interactions, region_code) VALUES
('2025-05', 6750, 4523, 1287, 156, 68.5, 285.60, 1928040.00, 15678, NULL),
('2025-06', 7234, 4891, 1456, 189, 71.2, 298.40, 2145680.00, 17234, NULL),
('2025-07', 7856, 5327, 1623, 142, 73.8, 312.80, 2456790.00, 18956, NULL);

-- 插入会员月度阶段统计测试数据
INSERT INTO member_stage_monthly_stats (stat_month, baby_stage, member_count, new_members, active_members, purchase_amount, avg_crfme_score, region_code) VALUES
('2025-05', '0阶段孕妇', 2437, 287, 1634, 698520.00, 72.5, NULL),
('2025-05', '1阶段0-6个月', 4863, 456, 3245, 1389640.00, 78.2, NULL),
('2025-05', '2阶段6-12个月', 3291, 234, 2187, 945780.00, 75.8, NULL),
('2025-05', '3阶段12-24个月', 2156, 189, 1456, 623400.00, 69.4, NULL),
('2025-05', '4阶段24个月+', 1789, 121, 1234, 445680.00, 65.7, NULL),
('2025-06', '0阶段孕妇', 3027, 356, 2134, 867340.00, 74.1, NULL),
('2025-06', '1阶段0-6个月', 5234, 523, 3678, 1567890.00, 79.6, NULL),
('2025-06', '2阶段6-12个月', 3567, 287, 2456, 1034560.00, 76.9, NULL),
('2025-06', '3阶段12-24个月', 2389, 198, 1567, 689450.00, 70.8, NULL),
('2025-06', '4阶段24个月+', 1923, 134, 1345, 498760.00, 67.2, NULL),
('2025-07', '0阶段孕妇', 3316, 398, 2387, 945670.00, 75.3, NULL),
('2025-07', '1阶段0-6个月', 5427, 567, 3892, 1689340.00, 80.4, NULL),
('2025-07', '2阶段6-12个月', 3789, 312, 2678, 1123450.00, 77.8, NULL),
('2025-07', '3阶段12-24个月', 2534, 223, 1734, 734560.00, 71.9, NULL),
('2025-07', '4阶段24个月+', 2067, 156, 1456, 523890.00, 68.5, NULL);

-- =============================================
-- 创建视图用于常用查询
-- =============================================

-- 会员概览视图
CREATE VIEW v_member_overview AS
SELECT 
    stat_month,
    total_members,
    active_members,
    new_members,
    repeat_purchase_rate,
    avg_purchase_amount,
    total_purchase_amount
FROM member_monthly_statistics 
WHERE region_code IS NULL
ORDER BY stat_month DESC;

-- 会员阶段分布视图
CREATE VIEW v_member_stage_distribution AS
SELECT 
    stat_month,
    baby_stage,
    member_count,
    active_members,
    purchase_amount,
    avg_crfme_score
FROM member_stage_monthly_stats 
WHERE region_code IS NULL
ORDER BY stat_month DESC, baby_stage;

-- =============================================
-- 创建存储过程用于数据统计
-- =============================================

-- 获取指定月份的会员概览数据
DELIMITER //
CREATE PROCEDURE GetMemberOverview(IN p_month VARCHAR(7))
BEGIN
    SELECT 
        total_members,
        active_members,
        new_members,
        lost_members,
        repeat_purchase_rate,
        avg_purchase_amount,
        total_purchase_amount,
        total_interactions
    FROM member_monthly_statistics 
    WHERE stat_month = p_month AND region_code IS NULL;
END //
DELIMITER ;

-- 获取指定月份的会员阶段统计
DELIMITER //
CREATE PROCEDURE GetMemberStageStats(IN p_month VARCHAR(7))
BEGIN
    SELECT 
        baby_stage,
        member_count,
        new_members,
        active_members,
        purchase_amount,
        avg_crfme_score
    FROM member_stage_monthly_stats 
    WHERE stat_month = p_month AND region_code IS NULL
    ORDER BY baby_stage;
END //
DELIMITER ;

-- =============================================
-- 脚本执行完成
-- =============================================

SELECT '会员管理模块数据表创建完成！' AS message;
SELECT '共创建7张数据表，支持完整的会员管理功能' AS summary;
SELECT '包含月度查询、生命周期管理、CRFM-E模型、分层运营等功能' AS features;