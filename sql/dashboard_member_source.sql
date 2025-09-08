-- 会员来源分析数据表
CREATE TABLE `dashboard_member_source` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `data_month` varchar(7) NOT NULL COMMENT '数据月份(格式:2025-05)',
  `source_channel` varchar(50) NOT NULL COMMENT '来源渠道',
  `member_count` int(11) NOT NULL DEFAULT '0' COMMENT '会员数量',
  `percentage` decimal(5,2) DEFAULT NULL COMMENT '占比(%)',
  `conversion_rate` decimal(5,2) DEFAULT NULL COMMENT '转化率(%)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_data_month` (`data_month`),
  KEY `idx_source_channel` (`source_channel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员来源分析数据表';

-- 插入示例数据
INSERT INTO `dashboard_member_source` (`data_month`, `source_channel`, `member_count`, `percentage`, `conversion_rate`) VALUES
-- 2025年5月数据
('2025-05', '线下门店', 3420, 38.50, 15.20),
('2025-05', '线上商城', 2180, 24.50, 8.90),
('2025-05', '微信推广', 1560, 17.50, 12.30),
('2025-05', '朋友推荐', 890, 10.00, 25.60),
('2025-05', '广告投放', 650, 7.30, 5.40),
('2025-05', '其他渠道', 340, 3.80, 3.20),
-- 2025年6月数据
('2025-06', '线下门店', 3580, 39.20, 15.80),
('2025-06', '线上商城', 2250, 24.70, 9.20),
('2025-06', '微信推广', 1620, 17.80, 12.80),
('2025-06', '朋友推荐', 920, 10.10, 26.20),
('2025-06', '广告投放', 680, 7.50, 5.70),
('2025-06', '其他渠道', 350, 3.80, 3.50),
-- 2025年7月数据
('2025-07', '线下门店', 3750, 40.10, 16.50),
('2025-07', '线上商城', 2320, 24.80, 9.50),
('2025-07', '微信推广', 1680, 18.00, 13.20),
('2025-07', '朋友推荐', 950, 10.20, 27.10),
('2025-07', '广告投放', 710, 7.60, 6.00),
('2025-07', '其他渠道', 360, 3.90, 3.80);