-- 更新会员价值分层数据，让5-7月数据有明显变化趋势
-- 删除现有数据
DELETE FROM member_crfme_distribution;

-- 重置自增ID
ALTER TABLE member_crfme_distribution AUTO_INCREMENT = 1;

-- 插入2025年5月数据（基础数据）
INSERT INTO member_crfme_distribution (data_month, score_range, count, percentage, avg_score, tier, create_by, create_time, remark) VALUES
('2025-05', '81-100', 850, 6.80, 89.5, '高价值会员', 'admin', NOW(), '高价值会员分层'),
('2025-05', '61-80', 2100, 16.80, 70.2, '潜力会员', 'admin', NOW(), '潜力会员分层'),
('2025-05', '41-60', 3500, 28.00, 50.8, '普通会员', 'admin', NOW(), '普通会员分层'),
('2025-05', '21-40', 4200, 33.60, 30.5, '低价值会员', 'admin', NOW(), '低价值会员分层'),
('2025-05', '0-20', 1850, 14.80, 12.3, '沉默会员', 'admin', NOW(), '沉默会员分层');

-- 插入2025年6月数据（显示增长趋势）
INSERT INTO member_crfme_distribution (data_month, score_range, count, percentage, avg_score, tier, create_by, create_time, remark) VALUES
('2025-06', '81-100', 920, 7.20, 91.2, '高价值会员', 'admin', NOW(), '高价值会员分层'),
('2025-06', '61-80', 2280, 17.85, 72.8, '潜力会员', 'admin', NOW(), '潜力会员分层'),
('2025-06', '41-60', 3680, 28.80, 52.1, '普通会员', 'admin', NOW(), '普通会员分层'),
('2025-06', '21-40', 4400, 34.40, 31.2, '低价值会员', 'admin', NOW(), '低价值会员分层'),
('2025-06', '0-20', 1520, 11.90, 13.8, '沉默会员', 'admin', NOW(), '沉默会员分层');

-- 插入2025年7月数据（继续优化趋势）
INSERT INTO member_crfme_distribution (data_month, score_range, count, percentage, avg_score, tier, create_by, create_time, remark) VALUES
('2025-07', '81-100', 1050, 8.00, 92.8, '高价值会员', 'admin', NOW(), '高价值会员分层'),
('2025-07', '61-80', 2520, 19.20, 74.5, '潜力会员', 'admin', NOW(), '潜力会员分层'),
('2025-07', '41-60', 3850, 29.30, 53.6, '普通会员', 'admin', NOW(), '普通会员分层'),
('2025-07', '21-40', 4380, 33.40, 32.1, '低价值会员', 'admin', NOW(), '低价值会员分层'),
('2025-07', '0-20', 1320, 10.10, 15.2, '沉默会员', 'admin', NOW(), '沉默会员分层');

-- 插入2025年8月数据（进一步优化）
INSERT INTO member_crfme_distribution (data_month, score_range, count, percentage, avg_score, tier, create_by, create_time, remark) VALUES
('2025-08', '81-100', 1180, 8.70, 94.1, '高价值会员', 'admin', NOW(), '高价值会员分层'),
('2025-08', '61-80', 2750, 20.30, 76.2, '潜力会员', 'admin', NOW(), '潜力会员分层'),
('2025-08', '41-60', 4020, 29.70, 54.8, '普通会员', 'admin', NOW(), '普通会员分层'),
('2025-08', '21-40', 4350, 32.10, 33.5, '低价值会员', 'admin', NOW(), '低价值会员分层'),
('2025-08', '0-20', 1250, 9.20, 16.8, '沉默会员', 'admin', NOW(), '沉默会员分层');

-- 插入2025年9月数据（持续改善）
INSERT INTO member_crfme_distribution (data_month, score_range, count, percentage, avg_score, tier, create_by, create_time, remark) VALUES
('2025-09', '81-100', 1320, 9.40, 95.3, '高价值会员', 'admin', NOW(), '高价值会员分层'),
('2025-09', '61-80', 2980, 21.20, 77.8, '潜力会员', 'admin', NOW(), '潜力会员分层'),
('2025-09', '41-60', 4200, 29.90, 56.1, '普通会员', 'admin', NOW(), '普通会员分层'),
('2025-09', '21-40', 4280, 30.50, 34.8, '低价值会员', 'admin', NOW(), '低价值会员分层'),
('2025-09', '0-20', 1270, 9.00, 18.1, '沉默会员', 'admin', NOW(), '沉默会员分层');