-- =============================================
-- 基于若依框架的角色权限管理配置
-- 创建导购管理组和企微管理组的完整权限体系
-- =============================================

-- 1. 创建新角色
INSERT INTO sys_role VALUES(3, '导购管理组', 'guide_manager', 3, '1', '0', '0', 'admin', NOW(), '', NULL, '导购管理组，负责导购绩效、MOT管理、会员管理等功能');
INSERT INTO sys_role VALUES(4, '企微管理组', 'wechat_manager', 4, '1', '0', '0', 'admin', NOW(), '', NULL, '企微管理组，负责企微运营、智能运营等功能');

-- 2. 创建业务菜单（如果不存在）
-- 导购管理模块
INSERT INTO sys_menu VALUES(2001, '导购管理', 0, 1, '#', '', 'M', '0', '1', '', 'fa fa-users', 'admin', NOW(), '', NULL, '导购管理模块');
INSERT INTO sys_menu VALUES(2002, '导购绩效', 2001, 1, '/guide/performance', 'menuItem', 'C', '0', '1', 'guide:performance:view', 'fa fa-chart-line', 'admin', NOW(), '', NULL, '导购绩效管理');
INSERT INTO sys_menu VALUES(2003, 'MOT管理', 2001, 2, '/guide/mot', 'menuItem', 'C', '0', '1', 'guide:mot:view', 'fa fa-tasks', 'admin', NOW(), '', NULL, 'MOT任务管理');
INSERT INTO sys_menu VALUES(2004, '会员管理', 2001, 3, '/guide/member', 'menuItem', 'C', '0', '1', 'guide:member:view', 'fa fa-user-friends', 'admin', NOW(), '', NULL, '会员管理');

-- 企微运营模块
INSERT INTO sys_menu VALUES(2101, '企微运营', 0, 2, '#', '', 'M', '0', '1', '', 'fa fa-wechat', 'admin', NOW(), '', NULL, '企微运营模块');
INSERT INTO sys_menu VALUES(2102, '企微运营管理', 2101, 1, '/wechat/operation', 'menuItem', 'C', '0', '1', 'wechat:operation:view', 'fa fa-comments', 'admin', NOW(), '', NULL, '企微运营管理');
INSERT INTO sys_menu VALUES(2103, '智能运营', 2101, 2, '/wechat/smart', 'menuItem', 'C', '0', '1', 'wechat:smart:view', 'fa fa-robot', 'admin', NOW(), '', NULL, '智能运营管理');

-- 共享功能模块
INSERT INTO sys_menu VALUES(2201, '数据分析', 0, 3, '/analytics/dashboard', 'menuItem', 'C', '0', '1', 'analytics:dashboard:view', 'fa fa-chart-bar', 'admin', NOW(), '', NULL, '数据分析');
INSERT INTO sys_menu VALUES(2202, '知识库', 0, 4, '/knowledge/base', 'menuItem', 'C', '0', '1', 'knowledge:base:view', 'fa fa-book', 'admin', NOW(), '', NULL, '知识库');
INSERT INTO sys_menu VALUES(2203, 'SOP流程', 0, 5, '/sop/process', 'menuItem', 'C', '0', '1', 'sop:process:view', 'fa fa-list-alt', 'admin', NOW(), '', NULL, 'SOP流程');

-- 3. 创建功能权限按钮
-- 导购绩效权限
INSERT INTO sys_menu VALUES(2010, '导购绩效查询', 2002, 1, '#', '', 'F', '0', '1', 'guide:performance:list', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2011, '导购绩效新增', 2002, 2, '#', '', 'F', '0', '1', 'guide:performance:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2012, '导购绩效修改', 2002, 3, '#', '', 'F', '0', '1', 'guide:performance:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2013, '导购绩效删除', 2002, 4, '#', '', 'F', '0', '1', 'guide:performance:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2014, '导购绩效导出', 2002, 5, '#', '', 'F', '0', '1', 'guide:performance:export', '#', 'admin', NOW(), '', NULL, '');

-- MOT管理权限
INSERT INTO sys_menu VALUES(2020, 'MOT查询', 2003, 1, '#', '', 'F', '0', '1', 'guide:mot:list', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2021, 'MOT新增', 2003, 2, '#', '', 'F', '0', '1', 'guide:mot:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2022, 'MOT修改', 2003, 3, '#', '', 'F', '0', '1', 'guide:mot:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2023, 'MOT删除', 2003, 4, '#', '', 'F', '0', '1', 'guide:mot:remove', '#', 'admin', NOW(), '', NULL, '');

-- 会员管理权限
INSERT INTO sys_menu VALUES(2030, '会员查询', 2004, 1, '#', '', 'F', '0', '1', 'guide:member:list', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2031, '会员新增', 2004, 2, '#', '', 'F', '0', '1', 'guide:member:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2032, '会员修改', 2004, 3, '#', '', 'F', '0', '1', 'guide:member:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2033, '会员删除', 2004, 4, '#', '', 'F', '0', '1', 'guide:member:remove', '#', 'admin', NOW(), '', NULL, '');

-- 企微运营权限
INSERT INTO sys_menu VALUES(2110, '企微运营查询', 2102, 1, '#', '', 'F', '0', '1', 'wechat:operation:list', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2111, '企微运营新增', 2102, 2, '#', '', 'F', '0', '1', 'wechat:operation:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2112, '企微运营修改', 2102, 3, '#', '', 'F', '0', '1', 'wechat:operation:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2113, '企微运营删除', 2102, 4, '#', '', 'F', '0', '1', 'wechat:operation:remove', '#', 'admin', NOW(), '', NULL, '');

-- 智能运营权限
INSERT INTO sys_menu VALUES(2120, '智能运营查询', 2103, 1, '#', '', 'F', '0', '1', 'wechat:smart:list', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2121, '智能运营新增', 2103, 2, '#', '', 'F', '0', '1', 'wechat:smart:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2122, '智能运营修改', 2103, 3, '#', '', 'F', '0', '1', 'wechat:smart:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_menu VALUES(2123, '智能运营删除', 2103, 4, '#', '', 'F', '0', '1', 'wechat:smart:remove', '#', 'admin', NOW(), '', NULL, '');

-- 4. 配置导购管理组权限（角色ID=3）
-- 导购管理模块完整权限
INSERT INTO sys_role_menu VALUES(3, 2001); -- 导购管理
INSERT INTO sys_role_menu VALUES(3, 2002); -- 导购绩效
INSERT INTO sys_role_menu VALUES(3, 2003); -- MOT管理
INSERT INTO sys_role_menu VALUES(3, 2004); -- 会员管理
INSERT INTO sys_role_menu VALUES(3, 2010); -- 导购绩效查询
INSERT INTO sys_role_menu VALUES(3, 2011); -- 导购绩效新增
INSERT INTO sys_role_menu VALUES(3, 2012); -- 导购绩效修改
INSERT INTO sys_role_menu VALUES(3, 2013); -- 导购绩效删除
INSERT INTO sys_role_menu VALUES(3, 2014); -- 导购绩效导出
INSERT INTO sys_role_menu VALUES(3, 2020); -- MOT查询
INSERT INTO sys_role_menu VALUES(3, 2021); -- MOT新增
INSERT INTO sys_role_menu VALUES(3, 2022); -- MOT修改
INSERT INTO sys_role_menu VALUES(3, 2023); -- MOT删除
INSERT INTO sys_role_menu VALUES(3, 2030); -- 会员查询
INSERT INTO sys_role_menu VALUES(3, 2031); -- 会员新增
INSERT INTO sys_role_menu VALUES(3, 2032); -- 会员修改
INSERT INTO sys_role_menu VALUES(3, 2033); -- 会员删除

-- 共享功能权限
INSERT INTO sys_role_menu VALUES(3, 2201); -- 数据分析
INSERT INTO sys_role_menu VALUES(3, 2202); -- 知识库
INSERT INTO sys_role_menu VALUES(3, 2203); -- SOP流程

-- 5. 配置企微管理组权限（角色ID=4）
-- 企微运营模块完整权限
INSERT INTO sys_role_menu VALUES(4, 2101); -- 企微运营
INSERT INTO sys_role_menu VALUES(4, 2102); -- 企微运营管理
INSERT INTO sys_role_menu VALUES(4, 2103); -- 智能运营
INSERT INTO sys_role_menu VALUES(4, 2110); -- 企微运营查询
INSERT INTO sys_role_menu VALUES(4, 2111); -- 企微运营新增
INSERT INTO sys_role_menu VALUES(4, 2112); -- 企微运营修改
INSERT INTO sys_role_menu VALUES(4, 2113); -- 企微运营删除
INSERT INTO sys_role_menu VALUES(4, 2120); -- 智能运营查询
INSERT INTO sys_role_menu VALUES(4, 2121); -- 智能运营新增
INSERT INTO sys_role_menu VALUES(4, 2122); -- 智能运营修改
INSERT INTO sys_role_menu VALUES(4, 2123); -- 智能运营删除

-- 共享功能权限
INSERT INTO sys_role_menu VALUES(4, 2201); -- 数据分析
INSERT INTO sys_role_menu VALUES(4, 2202); -- 知识库
INSERT INTO sys_role_menu VALUES(4, 2203); -- SOP流程

-- 6. 创建测试用户
INSERT INTO sys_user VALUES(3, 103, 'guide_user', '导购管理员', '00', 'guide@example.com', '15888888888', '1', '', '$2a$10$7JB720yubVSOfvam/CZlye.PjMczlqHdgvmb41Mc/c1Hhc7/UWBD.', '111111', '0', '0', '127.0.0.1', NOW(), NOW(), 'admin', NOW(), 'admin', NOW(), '导购管理组测试用户');
INSERT INTO sys_user VALUES(4, 103, 'wechat_user', '企微管理员', '00', 'wechat@example.com', '15999999999', '1', '', '$2a$10$7JB720yubVSOfvam/CZlye.PjMczlqHdgvmb41Mc/c1Hhc7/UWBD.', '111111', '0', '0', '127.0.0.1', NOW(), NOW(), 'admin', NOW(), 'admin', NOW(), '企微管理组测试用户');

-- 7. 分配用户角色
INSERT INTO sys_user_role VALUES(3, 3); -- 导购管理员 -> 导购管理组
INSERT INTO sys_user_role VALUES(4, 4); -- 企微管理员 -> 企微管理组

-- 8. 为测试用户分配默认岗位
INSERT INTO sys_user_post VALUES(3, 1); -- 导购管理员 -> 董事长岗位
INSERT INTO sys_user_post VALUES(4, 1); -- 企微管理员 -> 董事长岗位

COMMIT;