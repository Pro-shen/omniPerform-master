-- =============================================
-- 企业微信运营模块数据表创建脚本
-- 创建时间: 2025-01-24
-- 说明: 支持企业微信运营管理功能，包括绑定管理、群组管理、运营指标、SOP计划等
-- =============================================

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 企业微信绑定表
DROP TABLE IF EXISTS wechat_binding;
CREATE TABLE wechat_binding (
    binding_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '绑定ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(100) NOT NULL COMMENT '用户姓名',
    wechat_id VARCHAR(100) NOT NULL COMMENT '企业微信ID',
    wechat_name VARCHAR(100) COMMENT '企业微信昵称',
    department VARCHAR(100) COMMENT '所属部门',
    position VARCHAR(50) COMMENT '职位',
    phone VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(100) COMMENT '邮箱地址',
    status TINYINT DEFAULT 1 COMMENT '绑定状态：1-已绑定，2-已解绑，3-待审核',
    bind_time DATETIME COMMENT '绑定时间',
    unbind_time DATETIME COMMENT '解绑时间',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    INDEX idx_user_id (user_id),
    INDEX idx_wechat_id (wechat_id),
    INDEX idx_status (status),
    INDEX idx_department (department)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业微信绑定表';

-- 2. 企业微信群组表
DROP TABLE IF EXISTS wechat_group;
CREATE TABLE wechat_group (
    group_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '群组ID',
    group_name VARCHAR(200) NOT NULL COMMENT '群组名称',
    group_type VARCHAR(50) DEFAULT 'normal' COMMENT '群组类型：normal-普通群，work-工作群，customer-客户群',
    group_owner_id BIGINT COMMENT '群主用户ID',
    group_owner_name VARCHAR(100) COMMENT '群主姓名',
    member_count INT DEFAULT 0 COMMENT '群成员数量',
    max_member_count INT DEFAULT 500 COMMENT '最大成员数量',
    description TEXT COMMENT '群组描述',
    avatar_url VARCHAR(500) COMMENT '群头像URL',
    notice TEXT COMMENT '群公告',
    status TINYINT DEFAULT 1 COMMENT '群组状态：1-正常，2-已解散，3-已归档',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    INDEX idx_group_type (group_type),
    INDEX idx_group_owner_id (group_owner_id),
    INDEX idx_status (status),
    INDEX idx_member_count (member_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业微信群组表';

-- 3. 企业微信群成员表
DROP TABLE IF EXISTS wechat_group_member;
CREATE TABLE wechat_group_member (
    member_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成员ID',
    group_id BIGINT NOT NULL COMMENT '群组ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(100) NOT NULL COMMENT '用户姓名',
    wechat_id VARCHAR(100) NOT NULL COMMENT '企业微信ID',
    nickname VARCHAR(100) COMMENT '群内昵称',
    role VARCHAR(20) DEFAULT 'member' COMMENT '群内角色：owner-群主，admin-管理员，member-普通成员',
    join_time DATETIME COMMENT '加入时间',
    leave_time DATETIME COMMENT '离开时间',
    status TINYINT DEFAULT 1 COMMENT '成员状态：1-正常，2-已退群，3-被移除',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    INDEX idx_group_id (group_id),
    INDEX idx_user_id (user_id),
    INDEX idx_wechat_id (wechat_id),
    INDEX idx_role (role),
    INDEX idx_status (status),
    UNIQUE KEY uk_group_user (group_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业微信群成员表';

-- 4. 企业微信运营指标表
DROP TABLE IF EXISTS wechat_operation_metrics;
CREATE TABLE wechat_operation_metrics (
    metric_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '指标ID',
    stat_date DATE NOT NULL COMMENT '统计日期',
    stat_month VARCHAR(7) NOT NULL COMMENT '统计月份(YYYY-MM)',
    user_id BIGINT COMMENT '用户ID（为空表示全局统计）',
    user_name VARCHAR(100) COMMENT '用户姓名',
    department VARCHAR(100) COMMENT '部门',
    
    -- 好友相关指标
    friend_requests INT DEFAULT 0 COMMENT '好友申请数',
    friend_accepts INT DEFAULT 0 COMMENT '好友通过数',
    friend_total INT DEFAULT 0 COMMENT '好友总数',
    friend_active INT DEFAULT 0 COMMENT '活跃好友数',
    
    -- 沟通相关指标
    chat_sessions INT DEFAULT 0 COMMENT '沟通会话数',
    chat_messages INT DEFAULT 0 COMMENT '发送消息数',
    chat_replies INT DEFAULT 0 COMMENT '回复消息数',
    chat_duration INT DEFAULT 0 COMMENT '沟通时长(分钟)',
    
    -- 社群相关指标
    group_messages INT DEFAULT 0 COMMENT '群消息数',
    group_interactions INT DEFAULT 0 COMMENT '群互动数',
    group_activity_score DECIMAL(5,2) DEFAULT 0.00 COMMENT '群活跃度评分',
    
    -- 朋友圈相关指标
    moments_posts INT DEFAULT 0 COMMENT '朋友圈发布数',
    moments_likes INT DEFAULT 0 COMMENT '朋友圈点赞数',
    moments_comments INT DEFAULT 0 COMMENT '朋友圈评论数',
    moments_shares INT DEFAULT 0 COMMENT '朋友圈分享数',
    
    -- 活动推送相关指标
    activity_pushes INT DEFAULT 0 COMMENT '活动推送数',
    activity_views INT DEFAULT 0 COMMENT '活动查看数',
    activity_participations INT DEFAULT 0 COMMENT '活动参与数',
    activity_conversions INT DEFAULT 0 COMMENT '活动转化数',
    
    -- 数据复盘相关指标
    data_reviews INT DEFAULT 0 COMMENT '数据复盘次数',
    report_generates INT DEFAULT 0 COMMENT '报告生成数',
    insights_shared INT DEFAULT 0 COMMENT '洞察分享数',
    
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    
    INDEX idx_stat_date (stat_date),
    INDEX idx_stat_month (stat_month),
    INDEX idx_user_id (user_id),
    INDEX idx_department (department),
    UNIQUE KEY uk_date_user (stat_date, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业微信运营指标表';

-- 5. 企业微信SOP计划表
DROP TABLE IF EXISTS wechat_sop_plan;
CREATE TABLE wechat_sop_plan (
    plan_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '计划ID',
    plan_name VARCHAR(200) NOT NULL COMMENT '计划名称',
    sop_type VARCHAR(50) NOT NULL COMMENT 'SOP类型：add_friend-加好友，chat-一对一沟通，group-社群互动，moments-朋友圈，activity-活动推送，review-数据复盘',
    target_type VARCHAR(50) COMMENT '目标类型：user-用户，group-群组，all-全部',
    target_id BIGINT COMMENT '目标ID',
    target_name VARCHAR(200) COMMENT '目标名称',
    
    -- 计划内容
    title VARCHAR(500) NOT NULL COMMENT '计划标题',
    content TEXT COMMENT '计划内容',
    template_id BIGINT COMMENT '模板ID',
    template_name VARCHAR(200) COMMENT '模板名称',
    
    -- 执行时间
    execute_type VARCHAR(20) DEFAULT 'manual' COMMENT '执行类型：manual-手动，auto-自动，scheduled-定时',
    scheduled_time DATETIME COMMENT '计划执行时间',
    execute_time DATETIME COMMENT '实际执行时间',
    
    -- 执行状态
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending-待执行，executing-执行中，completed-已完成，failed-执行失败，cancelled-已取消',
    execute_result VARCHAR(50) COMMENT '执行结果',
    execute_note TEXT COMMENT '执行备注',
    
    -- 统计数据
    target_count INT DEFAULT 0 COMMENT '目标数量',
    success_count INT DEFAULT 0 COMMENT '成功数量',
    fail_count INT DEFAULT 0 COMMENT '失败数量',
    completion_rate DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成率(%)',
    
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    
    INDEX idx_sop_type (sop_type),
    INDEX idx_target_type (target_type),
    INDEX idx_target_id (target_id),
    INDEX idx_status (status),
    INDEX idx_scheduled_time (scheduled_time),
    INDEX idx_execute_time (execute_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业微信SOP计划表';

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- 插入测试数据 (2025-05 到 2025-07)
-- =============================================

-- 插入企业微信绑定测试数据
INSERT INTO wechat_binding (user_id, user_name, wechat_id, wechat_name, department, position, phone, email, status, bind_time, create_by) VALUES
(1, '张三', 'zhangsan001', '张三-销售', '销售部', '销售经理', '13800138001', 'zhangsan@company.com', 1, '2025-05-01 09:00:00', 'admin'),
(2, '李四', 'lisi002', '李四-客服', '客服部', '客服专员', '13800138002', 'lisi@company.com', 1, '2025-05-02 10:00:00', 'admin'),
(3, '王五', 'wangwu003', '王五-运营', '运营部', '运营主管', '13800138003', 'wangwu@company.com', 1, '2025-05-03 11:00:00', 'admin'),
(4, '赵六', 'zhaoliu004', '赵六-市场', '市场部', '市场专员', '13800138004', 'zhaoliu@company.com', 1, '2025-05-04 12:00:00', 'admin'),
(5, '钱七', 'qianqi005', '钱七-技术', '技术部', '技术经理', '13800138005', 'qianqi@company.com', 1, '2025-05-05 13:00:00', 'admin');

-- 插入企业微信群组测试数据
INSERT INTO wechat_group (group_name, group_type, group_owner_id, group_owner_name, member_count, description, status, create_by) VALUES
('销售团队交流群', 'work', 1, '张三', 25, '销售团队日常工作交流群', 1, 'admin'),
('客户服务群', 'customer', 2, '李四', 150, '为客户提供专业服务的群组', 1, 'admin'),
('产品运营讨论群', 'work', 3, '王五', 35, '产品运营策略讨论和分享', 1, 'admin'),
('市场推广群', 'work', 4, '赵六', 40, '市场推广活动策划和执行', 1, 'admin'),
('技术支持群', 'work', 5, '钱七', 20, '技术问题讨论和解决方案分享', 1, 'admin'),
('VIP客户群', 'customer', 1, '张三', 80, 'VIP客户专属服务群', 1, 'admin'),
('新品发布群', 'customer', 3, '王五', 200, '新产品发布和介绍群', 1, 'admin');

-- 插入企业微信群成员测试数据
INSERT INTO wechat_group_member (group_id, user_id, user_name, wechat_id, nickname, role, join_time, status, create_by) VALUES
(1, 1, '张三', 'zhangsan001', '张三-销售', 'owner', '2025-05-01 09:00:00', 1, 'admin'),
(1, 2, '李四', 'lisi002', '李四-客服', 'member', '2025-05-01 10:00:00', 1, 'admin'),
(1, 3, '王五', 'wangwu003', '王五-运营', 'admin', '2025-05-01 11:00:00', 1, 'admin'),
(2, 2, '李四', 'lisi002', '李四-客服', 'owner', '2025-05-02 09:00:00', 1, 'admin'),
(2, 1, '张三', 'zhangsan001', '张三-销售', 'admin', '2025-05-02 10:00:00', 1, 'admin'),
(3, 3, '王五', 'wangwu003', '王五-运营', 'owner', '2025-05-03 09:00:00', 1, 'admin'),
(3, 4, '赵六', 'zhaoliu004', '赵六-市场', 'member', '2025-05-03 10:00:00', 1, 'admin'),
(4, 4, '赵六', 'zhaoliu004', '赵六-市场', 'owner', '2025-05-04 09:00:00', 1, 'admin'),
(5, 5, '钱七', 'qianqi005', '钱七-技术', 'owner', '2025-05-05 09:00:00', 1, 'admin');

-- 插入企业微信运营指标测试数据 (2025-05 到 2025-07)
INSERT INTO wechat_operation_metrics (stat_date, stat_month, user_id, user_name, department, friend_requests, friend_accepts, friend_total, friend_active, chat_sessions, chat_messages, chat_replies, chat_duration, group_messages, group_interactions, group_activity_score, moments_posts, moments_likes, moments_comments, moments_shares, activity_pushes, activity_views, activity_participations, activity_conversions, data_reviews, report_generates, insights_shared, create_by) VALUES
-- 2025年5月数据
('2025-05-01', '2025-05', 1, '张三', '销售部', 15, 12, 180, 120, 45, 230, 180, 360, 85, 65, 8.5, 3, 25, 8, 5, 2, 150, 45, 12, 1, 2, 3, 'admin'),
('2025-05-01', '2025-05', 2, '李四', '客服部', 8, 6, 95, 70, 60, 320, 280, 480, 120, 90, 9.2, 2, 18, 6, 3, 1, 80, 25, 8, 1, 1, 2, 'admin'),
('2025-05-01', '2025-05', 3, '王五', '运营部', 20, 18, 220, 160, 35, 180, 150, 280, 95, 75, 8.8, 4, 35, 12, 8, 3, 200, 60, 18, 2, 3, 4, 'admin'),
('2025-05-01', '2025-05', 4, '赵六', '市场部', 12, 10, 150, 100, 40, 200, 160, 320, 70, 55, 7.8, 3, 22, 7, 4, 2, 120, 35, 10, 1, 2, 2, 'admin'),
('2025-05-01', '2025-05', 5, '钱七', '技术部', 5, 4, 60, 45, 25, 120, 100, 200, 40, 30, 7.2, 1, 12, 3, 2, 1, 60, 18, 5, 1, 1, 1, 'admin'),

-- 2025年6月数据
('2025-06-01', '2025-06', 1, '张三', '销售部', 18, 15, 195, 135, 50, 280, 220, 400, 95, 75, 9.0, 4, 32, 10, 6, 3, 180, 55, 15, 2, 3, 4, 'admin'),
('2025-06-01', '2025-06', 2, '李四', '客服部', 10, 8, 103, 78, 65, 350, 300, 520, 130, 100, 9.5, 3, 25, 8, 4, 2, 100, 30, 10, 1, 2, 3, 'admin'),
('2025-06-01', '2025-06', 3, '王五', '运营部', 22, 20, 240, 180, 40, 220, 180, 320, 105, 85, 9.2, 5, 42, 15, 10, 4, 250, 75, 22, 2, 4, 5, 'admin'),
('2025-06-01', '2025-06', 4, '赵六', '市场部', 15, 12, 162, 115, 45, 240, 190, 360, 80, 65, 8.2, 4, 28, 9, 5, 3, 150, 45, 12, 2, 3, 3, 'admin'),
('2025-06-01', '2025-06', 5, '钱七', '技术部', 7, 6, 66, 52, 30, 150, 125, 240, 50, 40, 7.8, 2, 18, 5, 3, 1, 80, 25, 7, 1, 2, 2, 'admin'),

-- 2025年7月数据
('2025-07-01', '2025-07', 1, '张三', '销售部', 20, 17, 212, 150, 55, 320, 250, 440, 105, 85, 9.3, 5, 38, 12, 8, 4, 220, 65, 18, 2, 4, 5, 'admin'),
('2025-07-01', '2025-07', 2, '李四', '客服部', 12, 10, 113, 85, 70, 380, 320, 560, 140, 110, 9.8, 4, 30, 10, 5, 3, 120, 35, 12, 2, 3, 4, 'admin'),
('2025-07-01', '2025-07', 3, '王五', '运营部', 25, 22, 262, 200, 45, 260, 210, 360, 115, 95, 9.5, 6, 48, 18, 12, 5, 300, 90, 25, 3, 5, 6, 'admin'),
('2025-07-01', '2025-07', 4, '赵六', '市场部', 18, 15, 177, 130, 50, 280, 220, 400, 90, 75, 8.5, 5, 35, 11, 7, 4, 180, 55, 15, 2, 4, 4, 'admin'),
('2025-07-01', '2025-07', 5, '钱七', '技术部', 9, 8, 74, 60, 35, 180, 150, 280, 60, 50, 8.2, 3, 22, 7, 4, 2, 100, 30, 9, 2, 3, 3, 'admin');

-- 插入企业微信SOP计划测试数据
INSERT INTO wechat_sop_plan (plan_name, sop_type, target_type, target_id, target_name, title, content, execute_type, scheduled_time, status, target_count, success_count, completion_rate, create_by) VALUES
('5月新客户加好友计划', 'add_friend', 'user', 1, '张三', '主动添加潜在客户为好友', '通过客户资料主动添加好友，发送个性化问候消息', 'scheduled', '2025-05-01 09:00:00', 'completed', 50, 45, 90.00, 'admin'),
('客服一对一沟通SOP', 'chat', 'user', 2, '李四', '客户问题解答标准流程', '按照标准话术回复客户问题，确保服务质量', 'auto', '2025-05-01 10:00:00', 'completed', 100, 95, 95.00, 'admin'),
('销售群互动活跃计划', 'group', 'group', 1, '销售团队交流群', '提升群内互动活跃度', '定期发布有价值内容，引导群成员积极参与讨论', 'manual', '2025-05-02 14:00:00', 'completed', 25, 20, 80.00, 'admin'),
('产品推广朋友圈计划', 'moments', 'user', 3, '王五', '新产品朋友圈推广', '制作精美图文内容，在朋友圈推广新产品', 'scheduled', '2025-05-03 16:00:00', 'completed', 1, 1, 100.00, 'admin'),
('6月促销活动推送', 'activity', 'all', NULL, '全体用户', '夏季促销活动推送', '向所有客户推送夏季促销活动信息', 'scheduled', '2025-06-01 10:00:00', 'completed', 500, 450, 90.00, 'admin'),
('月度数据复盘会议', 'review', 'user', 1, '张三', '5月运营数据复盘分析', '分析5月份运营数据，总结经验和改进方向', 'manual', '2025-06-01 15:00:00', 'completed', 1, 1, 100.00, 'admin'),
('7月新品发布预热', 'moments', 'user', 3, '王五', '新品发布预热推广', '通过朋友圈为新品发布造势预热', 'scheduled', '2025-07-01 09:00:00', 'completed', 3, 3, 100.00, 'admin'),
('客户群维护计划', 'group', 'group', 2, '客户服务群', '客户群日常维护', '定期在客户群分享有价值内容，维护客户关系', 'auto', '2025-07-01 11:00:00', 'executing', 150, 120, 80.00, 'admin');

-- =============================================
-- 脚本执行完成
-- =============================================

SELECT '企业微信运营模块数据表创建完成！' AS message;
SELECT '共创建5张数据表，支持完整的企业微信运营功能' AS summary;
SELECT '包含绑定管理、群组管理、运营指标统计、SOP计划执行等功能' AS features;