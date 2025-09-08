-- =============================================
-- 导购管理模块数据表创建脚本
-- 创建时间: 2025-01-09
-- 说明: 支持导购管理和绩效排行功能
-- =============================================

-- 1. 导购基础信息表
CREATE TABLE guide_info (
    guide_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '导购ID',
    guide_code VARCHAR(50) UNIQUE NOT NULL COMMENT '导购编号',
    guide_name VARCHAR(100) NOT NULL COMMENT '导购姓名',
    phone VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(100) COMMENT '邮箱地址',
    employee_id VARCHAR(50) COMMENT '员工工号',
    department VARCHAR(100) COMMENT '所属部门',
    region_code VARCHAR(20) COMMENT '所属区域代码',
    region_name VARCHAR(50) COMMENT '所属区域名称',
    store_id BIGINT COMMENT '所属门店ID',
    store_name VARCHAR(100) COMMENT '所属门店名称',
    supervisor_id BIGINT COMMENT '督导ID',
    supervisor_name VARCHAR(100) COMMENT '督导姓名',
    level VARCHAR(20) DEFAULT 'Junior' COMMENT '导购等级：Junior,Senior,Expert,Master',
    hire_date DATE COMMENT '入职日期',
    status TINYINT DEFAULT 1 COMMENT '状态：1-在职，2-离职，3-休假',
    monthly_target DECIMAL(12,2) COMMENT '月度销售目标',
    manager_id BIGINT COMMENT '上级管理者ID',
    wechat_id VARCHAR(100) COMMENT '企业微信ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_guide_code (guide_code),
    INDEX idx_region_code (region_code),
    INDEX idx_store_id (store_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导购基础信息表';

-- 2. 导购绩效表
CREATE TABLE guide_performance (
    performance_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '绩效记录ID',
    guide_id BIGINT NOT NULL COMMENT '导购ID',
    data_month VARCHAR(7) NOT NULL COMMENT '数据月份(YYYY-MM)',
    new_members INT DEFAULT 0 COMMENT '新增会员数',
    total_members INT DEFAULT 0 COMMENT '总服务会员数',
    active_members INT DEFAULT 0 COMMENT '活跃会员数',
    sales_amount DECIMAL(15,2) DEFAULT 0 COMMENT '销售金额',
    order_count INT DEFAULT 0 COMMENT '订单数量',
    mot_tasks_completed INT DEFAULT 0 COMMENT 'MOT任务完成数',
    mot_completion_rate DECIMAL(5,2) DEFAULT 0 COMMENT 'MOT完成率(%)',
    customer_satisfaction DECIMAL(3,2) DEFAULT 0 COMMENT '客户满意度(1-5分)',
    response_time DECIMAL(5,2) DEFAULT 0 COMMENT '平均响应时间(小时)',
    conversion_rate DECIMAL(5,2) DEFAULT 0 COMMENT '转化率(%)',
    repeat_purchase_rate DECIMAL(5,2) DEFAULT 0 COMMENT '复购率(%)',
    member_scan_rate DECIMAL(5,2) DEFAULT 0 COMMENT '会员扫码率(%)',
    interaction_count INT DEFAULT 0 COMMENT '互动次数',
    performance_score DECIMAL(5,2) DEFAULT 0 COMMENT '综合绩效评分',
    rank_in_region INT COMMENT '区域内排名',
    rank_overall INT COMMENT '全国排名',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_guide_month (guide_id, data_month),
    INDEX idx_data_month (data_month),
    INDEX idx_guide_id (guide_id),
    INDEX idx_performance_score (performance_score),
    FOREIGN KEY (guide_id) REFERENCES guide_info(guide_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导购绩效表';

-- 3. 导购门店关联表
CREATE TABLE guide_store_relation (
    relation_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    guide_id BIGINT NOT NULL COMMENT '导购ID',
    store_id BIGINT NOT NULL COMMENT '门店ID',
    store_name VARCHAR(100) COMMENT '门店名称',
    is_primary TINYINT DEFAULT 0 COMMENT '是否主门店：1-是，0-否',
    start_date DATE COMMENT '开始服务日期',
    end_date DATE COMMENT '结束服务日期',
    status TINYINT DEFAULT 1 COMMENT '状态：1-有效，0-无效',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_guide_id (guide_id),
    INDEX idx_store_id (store_id),
    INDEX idx_is_primary (is_primary),
    FOREIGN KEY (guide_id) REFERENCES guide_info(guide_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导购门店关联表';

-- 4. 导购任务完成记录表
CREATE TABLE guide_task_records (
    record_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    guide_id BIGINT NOT NULL COMMENT '导购ID',
    task_type VARCHAR(50) NOT NULL COMMENT '任务类型：MOT,FOLLOW_UP,PROMOTION等',
    task_id BIGINT COMMENT '任务ID',
    member_id BIGINT COMMENT '会员ID',
    completion_date DATETIME COMMENT '完成时间',
    result VARCHAR(20) COMMENT '完成结果：SUCCESS,FAILED,PARTIAL',
    notes TEXT COMMENT '备注说明',
    data_month VARCHAR(7) COMMENT '数据月份(YYYY-MM)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_guide_id (guide_id),
    INDEX idx_data_month (data_month),
    INDEX idx_task_type (task_type),
    INDEX idx_completion_date (completion_date),
    FOREIGN KEY (guide_id) REFERENCES guide_info(guide_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导购任务完成记录表';

-- 插入示例数据

-- 插入导购基础信息
INSERT INTO guide_info (guide_code, guide_name, phone, email, employee_id, department, region_code, region_name, store_name, level, hire_date, status, monthly_target) VALUES
('G001', '张小丽', '13812345678', 'zhangli@company.com', 'EMP001', '华东销售部', 'CN-EAST', '华东区', '上海旗舰店', 'Senior', '2023-01-15', 1, 80000.00),
('G002', '李明华', '13823456789', 'liminghua@company.com', 'EMP002', '华南销售部', 'CN-SOUTH', '华南区', '深圳中心店', 'Expert', '2022-08-20', 1, 95000.00),
('G003', '王美玲', '13834567890', 'wangmeiling@company.com', 'EMP003', '华中销售部', 'CN-CENTRAL', '华中区', '武汉购物中心店', 'Senior', '2023-03-10', 1, 75000.00),
('G004', '赵志强', '13845678901', 'zhaozhiqiang@company.com', 'EMP004', '华北销售部', 'CN-NORTH', '华北区', '北京王府井店', 'Master', '2021-12-05', 1, 120000.00),
('G005', '陈雅琪', '13856789012', 'chenyaqi@company.com', 'EMP005', '西南销售部', 'CN-SOUTHWEST', '西南区', '成都春熙路店', 'Junior', '2023-06-01', 1, 60000.00),
('G006', '刘建国', '13867890123', 'liujianguo@company.com', 'EMP006', '华东销售部', 'CN-EAST', '华东区', '杭州西湖店', 'Senior', '2022-11-15', 1, 85000.00),
('G007', '周晓燕', '13878901234', 'zhouxiaoyan@company.com', 'EMP007', '华南销售部', 'CN-SOUTH', '华南区', '广州天河店', 'Expert', '2022-04-18', 1, 90000.00),
('G008', '吴德华', '13889012345', 'wudehua@company.com', 'EMP008', '华中销售部', 'CN-CENTRAL', '华中区', '长沙步行街店', 'Senior', '2023-02-28', 1, 78000.00),
('G009', '孙丽娜', '13890123456', 'sunlina@company.com', 'EMP009', '华北销售部', 'CN-NORTH', '华北区', '天津滨海店', 'Junior', '2023-07-12', 1, 65000.00),
('G010', '马志远', '13901234567', 'mazhiyuan@company.com', 'EMP010', '西南销售部', 'CN-SOUTHWEST', '西南区', '重庆解放碑店', 'Senior', '2022-09-30', 1, 82000.00);

-- 插入导购绩效数据
INSERT INTO guide_performance (guide_id, data_month, new_members, total_members, active_members, sales_amount, order_count, mot_tasks_completed, mot_completion_rate, customer_satisfaction, response_time, conversion_rate, repeat_purchase_rate, member_scan_rate, interaction_count, performance_score, rank_in_region, rank_overall) VALUES
-- 2025-01月数据
(1, '2025-01', 45, 280, 168, 125000.00, 89, 42, 93.33, 4.7, 2.1, 15.89, 68.5, 85.2, 156, 92.5, 1, 3),
(2, '2025-01', 52, 310, 195, 148000.00, 105, 48, 96.00, 4.8, 1.8, 16.77, 72.3, 88.9, 178, 95.2, 1, 1),
(3, '2025-01', 38, 245, 142, 98000.00, 72, 35, 89.74, 4.5, 2.5, 14.69, 65.1, 82.4, 134, 88.7, 2, 6),
(4, '2025-01', 48, 295, 185, 142000.00, 98, 46, 95.83, 4.9, 1.6, 16.61, 70.8, 87.6, 172, 94.8, 1, 2),
(5, '2025-01', 32, 198, 115, 78000.00, 58, 28, 87.50, 4.3, 2.8, 13.64, 62.3, 79.8, 108, 85.3, 3, 8),
(6, '2025-01', 41, 265, 158, 115000.00, 82, 39, 92.86, 4.6, 2.2, 15.47, 67.2, 84.1, 148, 90.8, 2, 4),
(7, '2025-01', 46, 288, 172, 132000.00, 94, 44, 95.65, 4.7, 1.9, 16.32, 69.7, 86.8, 165, 93.6, 2, 3),
(8, '2025-01', 35, 228, 135, 89000.00, 65, 32, 88.89, 4.4, 2.6, 14.25, 64.8, 81.7, 125, 87.4, 3, 7),
(9, '2025-01', 29, 185, 108, 72000.00, 52, 26, 86.67, 4.2, 3.0, 13.24, 60.5, 78.3, 98, 83.9, 4, 9),
(10, '2025-01', 37, 242, 145, 95000.00, 69, 34, 91.89, 4.5, 2.4, 14.88, 66.4, 83.2, 138, 89.2, 4, 5),

-- 2024-12月数据
(1, '2024-12', 42, 275, 162, 118000.00, 85, 40, 90.91, 4.6, 2.3, 15.27, 66.8, 83.5, 148, 90.2, 2, 4),
(2, '2024-12', 49, 305, 188, 140000.00, 98, 45, 93.75, 4.7, 2.0, 16.07, 70.1, 86.2, 168, 92.8, 1, 2),
(3, '2024-12', 36, 240, 138, 92000.00, 68, 33, 86.84, 4.4, 2.7, 14.17, 63.5, 80.8, 128, 86.5, 3, 7),
(4, '2024-12', 46, 290, 180, 135000.00, 92, 44, 93.62, 4.8, 1.8, 15.86, 68.9, 85.7, 165, 93.5, 1, 1),
(5, '2024-12', 30, 195, 112, 75000.00, 55, 26, 84.62, 4.2, 3.1, 12.82, 60.8, 77.5, 102, 83.1, 4, 9),
(6, '2024-12', 39, 260, 152, 108000.00, 78, 37, 90.24, 4.5, 2.4, 14.62, 65.4, 82.3, 142, 88.7, 3, 6),
(7, '2024-12', 44, 283, 168, 125000.00, 88, 42, 92.31, 4.6, 2.1, 15.55, 67.9, 84.6, 158, 91.4, 2, 3),
(8, '2024-12', 33, 225, 130, 85000.00, 62, 30, 85.71, 4.3, 2.8, 13.78, 62.7, 79.9, 118, 85.8, 4, 8),
(9, '2024-12', 27, 180, 105, 68000.00, 48, 24, 82.76, 4.1, 3.2, 12.59, 58.9, 76.1, 92, 81.7, 5, 10),
(10, '2024-12', 35, 238, 140, 90000.00, 65, 32, 88.89, 4.4, 2.6, 13.95, 64.2, 81.4, 132, 87.9, 4, 5);

-- 插入导购门店关联数据
INSERT INTO guide_store_relation (guide_id, store_id, store_name, is_primary, start_date, status) VALUES
(1, 1, '上海旗舰店', 1, '2023-01-15', 1),
(2, 2, '深圳中心店', 1, '2022-08-20', 1),
(3, 3, '武汉购物中心店', 1, '2023-03-10', 1),
(4, 4, '北京王府井店', 1, '2021-12-05', 1),
(5, 5, '成都春熙路店', 1, '2023-06-01', 1),
(6, 6, '杭州西湖店', 1, '2022-11-15', 1),
(7, 7, '广州天河店', 1, '2022-04-18', 1),
(8, 8, '长沙步行街店', 1, '2023-02-28', 1),
(9, 9, '天津滨海店', 1, '2023-07-12', 1),
(10, 10, '重庆解放碑店', 1, '2022-09-30', 1);

-- 插入导购任务完成记录示例数据
INSERT INTO guide_task_records (guide_id, task_type, member_id, completion_date, result, data_month) VALUES
(1, 'MOT', 1, '2025-01-15 10:30:00', 'SUCCESS', '2025-01'),
(1, 'MOT', 2, '2025-01-16 14:20:00', 'SUCCESS', '2025-01'),
(1, 'FOLLOW_UP', 3, '2025-01-17 09:15:00', 'SUCCESS', '2025-01'),
(2, 'MOT', 4, '2025-01-15 11:45:00', 'SUCCESS', '2025-01'),
(2, 'PROMOTION', 5, '2025-01-16 16:30:00', 'SUCCESS', '2025-01'),
(3, 'MOT', 6, '2025-01-17 13:20:00', 'PARTIAL', '2025-01'),
(4, 'MOT', 7, '2025-01-18 10:10:00', 'SUCCESS', '2025-01'),
(5, 'FOLLOW_UP', 8, '2025-01-19 15:45:00', 'SUCCESS', '2025-01');

-- 5. MOT任务表
CREATE TABLE mot_task (
    task_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '任务ID',
    member_id BIGINT COMMENT '会员ID',
    member_name VARCHAR(100) COMMENT '会员姓名',
    member_phone VARCHAR(20) COMMENT '会员手机号',
    mot_type VARCHAR(50) COMMENT 'MOT类型',
    priority VARCHAR(20) COMMENT '任务优先级',
    status VARCHAR(20) COMMENT '任务状态',
    guide_id BIGINT COMMENT '负责导购ID',
    guide_name VARCHAR(100) COMMENT '负责导购姓名',
    due_date DATE COMMENT '计划执行时间',
    execute_date DATETIME COMMENT '实际执行时间',
    execute_result VARCHAR(50) COMMENT '执行结果',
    execute_note TEXT COMMENT '执行备注',
    description TEXT COMMENT '任务描述',
    region_id BIGINT COMMENT '区域ID',
    region_name VARCHAR(100) COMMENT '区域名称',
    store_id BIGINT COMMENT '门店ID',
    store_name VARCHAR(100) COMMENT '门店名称',
    data_month VARCHAR(10) COMMENT '数据月份',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_guide_id (guide_id),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date),
    INDEX idx_data_month (data_month),
    FOREIGN KEY (guide_id) REFERENCES guide_info(guide_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MOT任务表';

-- 插入MOT任务测试数据
INSERT INTO mot_task (member_id, member_name, member_phone, mot_type, priority, status, guide_id, guide_name, due_date, execute_date, execute_result, execute_note, description, region_id, region_name, store_id, store_name, data_month, create_time) VALUES
(1, '张三', '13800138001', '首购后回访', '高', '已完成', 1, '张小丽', '2025-01-10', '2025-01-10 14:30:00', '成功沟通', '客户满意度很高，已推荐相关产品', '针对新会员张三的首购后回访', 1, '华东区', 1, '上海旗舰店', '2025-01', '2025-01-09 10:00:00'),
(2, '李四', '13800138002', '生日关怀', '中', '已完成', 1, '张小丽', '2025-01-11', '2025-01-11 16:20:00', '成功沟通', '发送生日祝福，客户很感动', '会员李四生日关怀', 1, '华东区', 1, '上海旗舰店', '2025-01', '2025-01-10 09:00:00'),
(3, '王五', '13800138003', '复购提醒', '高', '待执行', 2, '李明华', '2025-01-15', NULL, NULL, NULL, '提醒会员王五进行复购', 2, '华南区', 2, '深圳中心店', '2025-01', '2025-01-12 11:00:00'),
(4, '赵六', '13800138004', '购买后指导', '中', '已完成', 2, '李明华', '2025-01-12', '2025-01-12 15:45:00', '成功沟通', '详细指导产品使用方法', '指导赵六正确使用产品', 2, '华南区', 2, '深圳中心店', '2025-01', '2025-01-11 14:00:00'),
(5, '钱七', '13800138005', '节日问候', '低', '已完成', 3, '王美玲', '2025-01-13', '2025-01-13 10:15:00', '成功沟通', '新年问候，维护客户关系', '新年节日问候', 3, '华中区', 3, '武汉购物中心店', '2025-01', '2025-01-12 16:00:00'),
(6, '孙八', '13800138006', '首购后回访', '高', '执行中', 3, '王美玲', '2025-01-16', NULL, NULL, NULL, '新会员孙八的首购回访', 3, '华中区', 3, '武汉购物中心店', '2025-01', '2025-01-14 09:30:00'),
(7, '周九', '13800138007', '产品推荐', '中', '待执行', 4, '赵志强', '2025-01-17', NULL, NULL, NULL, '根据购买历史推荐新产品', 4, '华北区', 4, '北京王府井店', '2025-01', '2025-01-15 13:20:00'),
(8, '吴十', '13800138008', '使用指导', '中', '待执行', 5, '陈雅琪', '2025-01-18', NULL, NULL, NULL, '指导产品正确使用方法', 5, '西南区', 5, '成都春熙路店', '2025-01', '2025-01-16 10:45:00'),
(9, '郑十一', '13800138009', '满意度调研', '低', '待执行', 6, '刘建国', '2025-01-19', NULL, NULL, NULL, '收集客户满意度反馈', 1, '华东区', 6, '杭州西湖店', '2025-01', '2025-01-17 14:15:00'),
(10, '陈十二', '13800138010', '复购提醒', '高', '待执行', 7, '周晓燕', '2025-01-20', NULL, NULL, NULL, '提醒老客户进行复购', 2, '华南区', 7, '广州天河店', '2025-01', '2025-01-18 11:30:00');