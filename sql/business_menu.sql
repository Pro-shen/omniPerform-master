-- 业务菜单初始化脚本
-- 为OmniPerform业务页面添加菜单权限配置

-- 删除已存在的业务菜单（如果有）
DELETE FROM sys_role_menu WHERE menu_id >= 2000;
DELETE FROM sys_menu WHERE menu_id >= 2000;

-- 添加业务菜单
-- 一级菜单：业务管理
INSERT INTO sys_menu VALUES('2000', '业务管理', '0', '1', '#', '', 'M', '0', '1', '', 'fa fa-dashboard', 'admin', NOW(), '', NULL, '业务管理目录');

-- 二级菜单：各个业务页面
INSERT INTO sys_menu VALUES('2001', '运营仪表盘', '2000', '1', 'index.html', 'menuItem', 'C', '0', '1', 'business:dashboard:view', 'fa fa-speedometer', 'admin', NOW(), '', NULL, '运营仪表盘菜单');
INSERT INTO sys_menu VALUES('2002', '会员管理', '2000', '2', 'member-management.html', 'menuItem', 'C', '0', '1', 'business:member:view', 'fa fa-users', 'admin', NOW(), '', NULL, '会员管理菜单');
INSERT INTO sys_menu VALUES('2003', '导购绩效', '2000', '3', 'guide-performance.html', 'menuItem', 'C', '0', '1', 'business:guide:view', 'fa fa-line-chart', 'admin', NOW(), '', NULL, '导购绩效菜单');
INSERT INTO sys_menu VALUES('2004', 'SOP流程', '2000', '4', 'sop-process.html', 'menuItem', 'C', '0', '1', 'business:sop:view', 'fa fa-list-alt', 'admin', NOW(), '', NULL, 'SOP流程菜单');
INSERT INTO sys_menu VALUES('2005', 'MOT管理', '2000', '5', 'mot-management.html', 'menuItem', 'C', '0', '1', 'business:mot:view', 'fa fa-comments', 'admin', NOW(), '', NULL, 'MOT管理菜单');
INSERT INTO sys_menu VALUES('2006', '企业微信运营', '2000', '6', 'wechat-operation.html', 'menuItem', 'C', '0', '1', 'business:wechat:view', 'fa fa-wechat', 'admin', NOW(), '', NULL, '企业微信运营菜单');
INSERT INTO sys_menu VALUES('2007', '数据分析', '2000', '7', 'data-analysis.html', 'menuItem', 'C', '0', '1', 'business:analysis:view', 'fa fa-bar-chart', 'admin', NOW(), '', NULL, '数据分析菜单');
INSERT INTO sys_menu VALUES('2008', '数据洞察', '2000', '8', 'data-insights.html', 'menuItem', 'C', '0', '1', 'business:insights:view', 'fa fa-lightbulb-o', 'admin', NOW(), '', NULL, '数据洞察菜单');
INSERT INTO sys_menu VALUES('2009', '智能运营', '2000', '9', 'smart-operation.html', 'menuItem', 'C', '0', '1', 'business:smart:view', 'fa fa-robot', 'admin', NOW(), '', NULL, '智能运营菜单');
INSERT INTO sys_menu VALUES('2010', '知识库', '2000', '10', 'knowledge-base.html', 'menuItem', 'C', '0', '1', 'business:knowledge:view', 'fa fa-book', 'admin', NOW(), '', NULL, '知识库菜单');
INSERT INTO sys_menu VALUES('2011', '系统设置', '2000', '11', 'system-settings.html', 'menuItem', 'C', '0', '1', 'business:system:view', 'fa fa-cog', 'admin', NOW(), '', NULL, '系统设置菜单');

-- 为超级管理员角色分配所有业务菜单权限
INSERT INTO sys_role_menu VALUES ('1', '2000');
INSERT INTO sys_role_menu VALUES ('1', '2001');
INSERT INTO sys_role_menu VALUES ('1', '2002');
INSERT INTO sys_role_menu VALUES ('1', '2003');
INSERT INTO sys_role_menu VALUES ('1', '2004');
INSERT INTO sys_role_menu VALUES ('1', '2005');
INSERT INTO sys_role_menu VALUES ('1', '2006');
INSERT INTO sys_role_menu VALUES ('1', '2007');
INSERT INTO sys_role_menu VALUES ('1', '2008');
INSERT INTO sys_role_menu VALUES ('1', '2009');
INSERT INTO sys_role_menu VALUES ('1', '2010');
INSERT INTO sys_role_menu VALUES ('1', '2011');

-- 为普通角色分配部分业务菜单权限（示例：只能访问仪表盘、会员管理、导购绩效）
INSERT INTO sys_role_menu VALUES ('2', '2000');
INSERT INTO sys_role_menu VALUES ('2', '2001');
INSERT INTO sys_role_menu VALUES ('2', '2002');
INSERT INTO sys_role_menu VALUES ('2', '2003');

-- 创建一个测试角色：数据分析师（只能访问数据相关页面）
INSERT INTO sys_role VALUES('3', '数据分析师', 'analyst', 3, 3, '0', '0', 'admin', NOW(), '', NULL, '数据分析师角色');

-- 为数据分析师角色分配权限
INSERT INTO sys_role_menu VALUES ('3', '2000');
INSERT INTO sys_role_menu VALUES ('3', '2001'); -- 仪表盘
INSERT INTO sys_role_menu VALUES ('3', '2007'); -- 数据分析
INSERT INTO sys_role_menu VALUES ('3', '2008'); -- 数据洞察

-- 创建一个测试用户：数据分析师用户
INSERT INTO sys_user VALUES(3, NULL, 'analyst', '数据分析师', '00', 'analyst@omniperform.com', '15888888888', '1', '', '$2a$10$7JB720yubVSOfvVMe6/YqOPKitxePbmYdFfDWuTTvn5tnzJrjSYZu', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '数据分析师用户');

-- 为数据分析师用户分配角色
INSERT INTO sys_user_role VALUES ('3', '3');

COMMIT;