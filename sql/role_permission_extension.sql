-- 角色权限扩展脚本
-- 为导购管理组和企微管理组创建专门的角色和菜单权限

-- 添加新的角色
INSERT INTO sys_role VALUES('3', '导购管理组', 'guide_manager', 3, 2, '0', '0', 'admin', NOW(), '', NULL, '导购管理组，负责导购绩效管理');
INSERT INTO sys_role VALUES('4', '企微管理组', 'wechat_manager', 4, 2, '0', '0', 'admin', NOW(), '', NULL, '企微管理组，负责企业微信运营管理');

-- 添加新的菜单项（如果不存在）
-- 导购管理相关菜单
INSERT INTO sys_menu VALUES(2001, '导购管理', 0, 1, '#', '', 'M', '0', '1', '', 'fa fa-users', 'admin', NOW(), '', NULL, '导购管理模块');
INSERT INTO sys_menu VALUES(2002, '导购绩效', 2001, 1, '/guide-performance.html', 'menuItem', 'C', '0', '1', 'guide:performance:view', 'fa fa-chart-line', 'admin', NOW(), '', NULL, '导购绩效管理');
INSERT INTO sys_menu VALUES(2003, 'MOT管理', 2001, 2, '/mot-management.html', 'menuItem', 'C', '0', '1', 'mot:management:view', 'fa fa-tasks', 'admin', NOW(), '', NULL, 'MOT任务管理');
INSERT INTO sys_menu VALUES(2004, '会员管理', 2001, 3, '/member-management.html', 'menuItem', 'C', '0', '1', 'member:management:view', 'fa fa-user-friends', 'admin', NOW(), '', NULL, '会员管理');

-- 企微管理相关菜单
INSERT INTO sys_menu VALUES(2101, '企微运营', 0, 2, '#', '', 'M', '0', '1', '', 'fa fa-wechat', 'admin', NOW(), '', NULL, '企微运营模块');
INSERT INTO sys_menu VALUES(2102, '企微运营管理', 2101, 1, '/wechat-operation.html', 'menuItem', 'C', '0', '1', 'wechat:operation:view', 'fa fa-comments', 'admin', NOW(), '', NULL, '企微运营管理');
INSERT INTO sys_menu VALUES(2103, '智能运营', 2101, 2, '/smart-operation.html', 'menuItem', 'C', '0', '1', 'smart:operation:view', 'fa fa-robot', 'admin', NOW(), '', NULL, '智能运营管理');

-- 通用菜单
INSERT INTO sys_menu VALUES(2201, '数据分析', 0, 3, '/data-analysis.html', 'menuItem', 'C', '0', '1', 'data:analysis:view', 'fa fa-chart-bar', 'admin', NOW(), '', NULL, '数据分析');
INSERT INTO sys_menu VALUES(2202, '知识库', 0, 4, '/knowledge-base.html', 'menuItem', 'C', '0', '1', 'knowledge:base:view', 'fa fa-book', 'admin', NOW(), '', NULL, '知识库');
INSERT INTO sys_menu VALUES(2203, 'SOP流程', 0, 5, '/sop-process.html', 'menuItem', 'C', '0', '1', 'sop:process:view', 'fa fa-list-alt', 'admin', NOW(), '', NULL, 'SOP流程');

-- 为导购管理组分配菜单权限
INSERT INTO sys_role_menu VALUES ('3', '2001'); -- 导购管理
INSERT INTO sys_role_menu VALUES ('3', '2002'); -- 导购绩效
INSERT INTO sys_role_menu VALUES ('3', '2003'); -- MOT管理
INSERT INTO sys_role_menu VALUES ('3', '2004'); -- 会员管理
INSERT INTO sys_role_menu VALUES ('3', '2201'); -- 数据分析
INSERT INTO sys_role_menu VALUES ('3', '2202'); -- 知识库
INSERT INTO sys_role_menu VALUES ('3', '2203'); -- SOP流程

-- 为企微管理组分配菜单权限
INSERT INTO sys_role_menu VALUES ('4', '2101'); -- 企微运营
INSERT INTO sys_role_menu VALUES ('4', '2102'); -- 企微运营管理
INSERT INTO sys_role_menu VALUES ('4', '2103'); -- 智能运营
INSERT INTO sys_role_menu VALUES ('4', '2201'); -- 数据分析
INSERT INTO sys_role_menu VALUES ('4', '2202'); -- 知识库

-- 创建测试用户
INSERT INTO sys_user VALUES(3, 103, 'guide_manager', '导购管理员', '00', 'guide@company.com', '13800000001', '0', '', '29c67a30398638269fe600f73a054934', '111111', '0', '0', '127.0.0.1', NULL, NULL, 'admin', NOW(), '', NULL, '导购管理员');
INSERT INTO sys_user VALUES(4, 103, 'wechat_manager', '企微管理员', '00', 'wechat@company.com', '13800000002', '1', '', '29c67a30398638269fe600f73a054934', '111111', '0', '0', '127.0.0.1', NULL, NULL, 'admin', NOW(), '', NULL, '企微管理员');

-- 为测试用户分配角色
INSERT INTO sys_user_role VALUES ('3', '3'); -- 导购管理员 -> 导购管理组
INSERT INTO sys_user_role VALUES ('4', '4'); -- 企微管理员 -> 企微管理组