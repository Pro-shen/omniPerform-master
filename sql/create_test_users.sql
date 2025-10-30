-- 创建测试用户和角色分配
-- 若依权限管理系统 - 测试用户配置

START TRANSACTION;

-- 1. 创建测试用户
INSERT INTO sys_user VALUES(3, 103, 'guide_user', '导购管理员', '00', 'guide@example.com', '15888888888', '1', '', '$2a$10$7JB720yubVSOfvam/CZlye.PjMczlqHdgvmb41Mc/c1Hhc7/UWBD.', '111111', '0', '0', '127.0.0.1', NOW(), NOW(), 'admin', NOW(), 'admin', NOW(), '导购管理组测试用户');
INSERT INTO sys_user VALUES(4, 103, 'wechat_user', '企微管理员', '00', 'wechat@example.com', '15999999999', '1', '', '$2a$10$7JB720yubVSOfvam/CZlye.PjMczlqHdgvmb41Mc/c1Hhc7/UWBD.', '111111', '0', '0', '127.0.0.1', NOW(), NOW(), 'admin', NOW(), 'admin', NOW(), '企微管理组测试用户');

-- 2. 分配用户角色
INSERT INTO sys_user_role VALUES(3, 3); -- 导购管理员 -> 导购管理组
INSERT INTO sys_user_role VALUES(4, 4); -- 企微管理员 -> 企微管理组

-- 3. 为测试用户分配默认岗位
INSERT INTO sys_user_post VALUES(3, 1); -- 导购管理员 -> 董事长岗位
INSERT INTO sys_user_post VALUES(4, 1); -- 企微管理员 -> 董事长岗位

COMMIT;