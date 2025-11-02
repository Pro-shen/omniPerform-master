-- 创建测试用户hhh，密码为asdasdasd，属于导购管理组
-- 密码加密后的值（asdasdasd的BCrypt加密）
INSERT INTO sys_user VALUES(
    10, 
    103, 
    'hhh', 
    'hhh', 
    '00', 
    'hhh@example.com', 
    '15888888888', 
    '1', 
    '', 
    '$2a$10$mE.qmcV0mFObQz/ksEKBCu.j/jjXAVfQxCtOjkXMBG4.nDqinR1JO', 
    '111111', 
    '0', 
    '0', 
    '127.0.0.1', 
    NOW(), 
    NOW(), 
    'admin', 
    NOW(), 
    'admin', 
    NOW(), 
    '测试用户hhh，属于导购管理组'
);

-- 将用户hhh分配到导购管理组（角色ID=3）
INSERT INTO sys_user_role VALUES(10, 3);