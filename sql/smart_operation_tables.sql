-- =============================================
-- 智能运营模块数据表创建脚本
-- 创建时间: 2025-01-09
-- 说明: 支持智能运营模块的完整功能
-- =============================================

-- 1. 智能运营概览统计表
CREATE TABLE smart_operation_overview (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    stat_date DATE NOT NULL COMMENT '统计日期',
    today_alerts INT DEFAULT 0 COMMENT '今日待处理预警数',
    ai_recommended_tasks INT DEFAULT 0 COMMENT 'AI推荐任务数',
    mot_execution_rate DECIMAL(5,2) DEFAULT 0 COMMENT 'MOT执行率(%)',
    member_activity_rate DECIMAL(5,2) DEFAULT 0 COMMENT '会员活跃度(%)',
    region_code VARCHAR(20) COMMENT '区域代码，NULL表示全国统计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_date_region (stat_date, region_code),
    INDEX idx_stat_date (stat_date),
    INDEX idx_region_code (region_code)
) COMMENT='智能运营概览统计表';

-- 2. 智能运营预警表
CREATE TABLE smart_operation_alerts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    alert_id VARCHAR(50) UNIQUE NOT NULL COMMENT '预警编号',
    alert_type VARCHAR(50) NOT NULL COMMENT '预警类型：会员流失风险、销售异常、库存预警、服务质量、系统异常',
    alert_content TEXT NOT NULL COMMENT '预警内容描述',
    severity ENUM('高', '中', '低') NOT NULL COMMENT '严重程度',
    status ENUM('待处理', '处理中', '已处理') DEFAULT '待处理' COMMENT '处理状态',
    region VARCHAR(50) COMMENT '所属区域',
    member_id BIGINT COMMENT '关联会员ID（如果适用）',
    guide_id BIGINT COMMENT '关联导购ID（如果适用）',
    trigger_data JSON COMMENT '触发预警的数据',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    process_time DATETIME COMMENT '处理时间',
    process_user VARCHAR(100) COMMENT '处理人',
    process_note TEXT COMMENT '处理备注',
    INDEX idx_alert_id (alert_id),
    INDEX idx_alert_type (alert_type),
    INDEX idx_severity (severity),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    INDEX idx_region (region)
) COMMENT='智能运营预警表';

-- 3. AI推荐营销任务表
CREATE TABLE smart_marketing_tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    task_id VARCHAR(50) UNIQUE NOT NULL COMMENT '任务编号',
    task_name VARCHAR(200) NOT NULL COMMENT '任务名称',
    task_type VARCHAR(50) NOT NULL COMMENT '任务类型：个性化推荐、触达优化、内容推送、活动邀请、关怀提醒',
    target_group VARCHAR(100) NOT NULL COMMENT '目标群体',
    member_count INT DEFAULT 0 COMMENT '目标会员数量',
    expected_effect VARCHAR(100) COMMENT '预期效果',
    recommend_time DATETIME COMMENT '推荐执行时间',
    status ENUM('待执行', '执行中', '已完成', '已取消') DEFAULT '待执行' COMMENT '任务状态',
    priority ENUM('高', '中', '低') DEFAULT '中' COMMENT '优先级',
    ai_confidence DECIMAL(5,2) COMMENT 'AI推荐置信度(%)',
    target_members JSON COMMENT '目标会员ID列表',
    task_config JSON COMMENT '任务配置参数',
    execution_result JSON COMMENT '执行结果数据',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    execute_time DATETIME COMMENT '执行时间',
    complete_time DATETIME COMMENT '完成时间',
    INDEX idx_task_id (task_id),
    INDEX idx_task_type (task_type),
    INDEX idx_target_group (target_group),
    INDEX idx_status (status),
    INDEX idx_recommend_time (recommend_time),
    INDEX idx_create_time (create_time)
) COMMENT='AI推荐营销任务表';

-- 4. 会员画像分析表
CREATE TABLE member_profile_analysis (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    analysis_date DATE NOT NULL COMMENT '分析日期',
    profile_type VARCHAR(50) NOT NULL COMMENT '画像类型：成长探索型、品质追求型、价格敏感型、社交分享型、忠诚依赖型',
    member_count INT DEFAULT 0 COMMENT '该类型会员数量',
    percentage DECIMAL(5,2) DEFAULT 0 COMMENT '占比(%)',
    avg_purchase_amount DECIMAL(10,2) DEFAULT 0 COMMENT '平均购买金额',
    avg_interaction_frequency DECIMAL(5,2) DEFAULT 0 COMMENT '平均互动频次',
    region_code VARCHAR(20) COMMENT '区域代码，NULL表示全国统计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_date_type_region (analysis_date, profile_type, region_code),
    INDEX idx_analysis_date (analysis_date),
    INDEX idx_profile_type (profile_type),
    INDEX idx_region_code (region_code)
) COMMENT='会员画像分析表';

-- 5. 优化效果数据表
CREATE TABLE optimization_effect_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    stat_date DATE NOT NULL COMMENT '统计日期',
    metric_name VARCHAR(100) NOT NULL COMMENT '指标名称：MOT执行率、会员活跃度、复购率等',
    metric_value DECIMAL(10,2) NOT NULL COMMENT '指标值',
    previous_value DECIMAL(10,2) COMMENT '前期值',
    improvement_rate DECIMAL(5,2) COMMENT '提升率(%)',
    region_code VARCHAR(20) COMMENT '区域代码，NULL表示全国统计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_date_metric_region (stat_date, metric_name, region_code),
    INDEX idx_stat_date (stat_date),
    INDEX idx_metric_name (metric_name),
    INDEX idx_region_code (region_code)
) COMMENT='优化效果数据表';

-- 6. 最佳触达时间分析表
CREATE TABLE best_touch_time_analysis (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    analysis_date DATE NOT NULL COMMENT '分析日期',
    time_slot VARCHAR(20) NOT NULL COMMENT '时间段：09:00-10:00、10:00-11:00等',
    response_rate DECIMAL(5,2) DEFAULT 0 COMMENT '响应率(%)',
    conversion_rate DECIMAL(5,2) DEFAULT 0 COMMENT '转化率(%)',
    total_touches INT DEFAULT 0 COMMENT '总触达次数',
    successful_touches INT DEFAULT 0 COMMENT '成功触达次数',
    region_code VARCHAR(20) COMMENT '区域代码，NULL表示全国统计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_date_slot_region (analysis_date, time_slot, region_code),
    INDEX idx_analysis_date (analysis_date),
    INDEX idx_time_slot (time_slot),
    INDEX idx_region_code (region_code)
) COMMENT='最佳触达时间分析表';