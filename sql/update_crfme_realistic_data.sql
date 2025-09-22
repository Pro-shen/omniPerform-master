-- 更新会员价值分层(CRFM-E)数据，让5-7月数据有明显差异且避免整百数据
-- 删除现有数据
DELETE FROM member_crfme_distribution;

-- 重置自增ID
ALTER TABLE member_crfme_distribution AUTO_INCREMENT = 1;

-- 插入2025年5月数据（起始阶段，数据相对分散）
INSERT INTO member_crfme_distribution (data_month, score_range, count, percentage, avg_score, tier, create_by, create_time, remark) VALUES
('2025-05', '81-100', 743, 5.94, 88.7, '高价值会员', 'admin', NOW(), '高价值会员分层'),
('2025-05', '61-80', 1876, 15.01, 69.3, '潜力会员', 'admin', NOW(), '潜力会员分层'),
('2025-05', '41-60', 3247, 25.98, 49.8, '普通会员', 'admin', NOW(), '普通会员分层'),
('2025-05', '21-40', 4683, 37.46, 29.7, '低价值会员', 'admin', NOW(), '低价值会员分层'),
('2025-05', '0-20', 1951, 15.61, 11.4, '沉默会员', 'admin', NOW(), '沉默会员分层');

-- 插入2025年6月数据（显示改善趋势）
INSERT INTO member_crfme_distribution (data_month, score_range, count, percentage, avg_score, tier, create_by, create_time, remark) VALUES
('2025-06', '81-100', 891, 7.13, 90.2, '高价值会员', 'admin', NOW(), '高价值会员分层'),
('2025-06', '61-80', 2234, 17.87, 71.6, '潜力会员', 'admin', NOW(), '潜力会员分层'),
('2025-06', '41-60', 3456, 27.65, 51.4, '普通会员', 'admin', NOW(), '普通会员分层'),
('2025-06', '21-40', 4327, 34.62, 30.9, '低价值会员', 'admin', NOW(), '低价值会员分层'),
('2025-06', '0-20', 1592, 12.74, 12.8, '沉默会员', 'admin', NOW(), '沉默会员分层');

-- 插入2025年7月数据（继续优化，高价值会员增长明显）
INSERT INTO member_crfme_distribution (data_month, score_range, count, percentage, avg_score, tier, create_by, create_time, remark) VALUES
('2025-07', '81-100', 1127, 9.02, 91.8, '高价值会员', 'admin', NOW(), '高价值会员分层'),
('2025-07', '61-80', 2689, 21.51, 73.4, '潜力会员', 'admin', NOW(), '潜力会员分层'),
('2025-07', '41-60', 3734, 29.87, 52.9, '普通会员', 'admin', NOW(), '普通会员分层'),
('2025-07', '21-40', 3856, 30.85, 31.7, '低价值会员', 'admin', NOW(), '低价值会员分层'),
('2025-07', '0-20', 1094, 8.75, 14.2, '沉默会员', 'admin', NOW(), '沉默会员分层');

-- 验证数据完整性
SELECT 
    data_month,
    SUM(count) as total_members,
    ROUND(SUM(percentage), 2) as total_percentage
FROM member_crfme_distribution 
GROUP BY data_month 
ORDER BY data_month;

-- 查看各月份高价值会员变化趋势
SELECT 
    data_month,
    tier,
    count,
    percentage,
    avg_score
FROM member_crfme_distribution 
WHERE tier = '高价值会员'
ORDER BY data_month;