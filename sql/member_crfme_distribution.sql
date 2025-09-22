-- ----------------------------
-- 会员价值分层(CRFM-E)分布数据表
-- 创建时间: 2025-01-27
-- 说明: 支持按月份查询的会员价值分层分布数据表
-- ----------------------------

-- ----------------------------
-- 会员价值分层(CRFM-E)分布数据表
-- ----------------------------
drop table if exists member_crfme_distribution;
create table member_crfme_distribution (
  id                bigint(20)      not null auto_increment    comment '主键ID',
  data_month        varchar(7)      not null                   comment '数据月份(格式:2025-05)',
  score_range       varchar(20)     not null                   comment '评分区间',
  count             int(11)         default 0                  comment '会员数量',
  percentage        decimal(5,2)    default 0.00               comment '占比(%)',
  avg_score         decimal(5,2)    default 0.00               comment '平均评分',
  tier              varchar(20)     default null               comment '分层等级',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (id),
  unique key uk_month_range (data_month, score_range)
) engine=innodb comment = '会员价值分层(CRFM-E)分布数据表';

-- ----------------------------
-- 初始化示例数据 - 2025年5月
-- ----------------------------

-- 2025年5月会员价值分层分布数据
insert into member_crfme_distribution values(1, '2025-05', '81-100', 850, 6.8, 89.5, '高价值会员', 'admin', sysdate(), '', null, '高价值会员分层');
insert into member_crfme_distribution values(2, '2025-05', '61-80', 2100, 16.8, 70.2, '潜力会员', 'admin', sysdate(), '', null, '潜力会员分层');
insert into member_crfme_distribution values(3, '2025-05', '41-60', 3500, 28.0, 50.8, '普通会员', 'admin', sysdate(), '', null, '普通会员分层');
insert into member_crfme_distribution values(4, '2025-05', '21-40', 4200, 33.6, 30.5, '低价值会员', 'admin', sysdate(), '', null, '低价值会员分层');
insert into member_crfme_distribution values(5, '2025-05', '0-20', 1850, 14.8, 12.3, '沉默会员', 'admin', sysdate(), '', null, '沉默会员分层');

-- 2025年6月会员价值分层分布数据
insert into member_crfme_distribution values(6, '2025-06', '81-100', 920, 7.0, 90.2, '高价值会员', 'admin', sysdate(), '', null, '高价值会员分层');
insert into member_crfme_distribution values(7, '2025-06', '61-80', 2280, 17.3, 71.1, '潜力会员', 'admin', sysdate(), '', null, '潜力会员分层');
insert into member_crfme_distribution values(8, '2025-06', '41-60', 3680, 27.9, 51.2, '普通会员', 'admin', sysdate(), '', null, '普通会员分层');
insert into member_crfme_distribution values(9, '2025-06', '21-40', 4400, 33.3, 31.0, '低价值会员', 'admin', sysdate(), '', null, '低价值会员分层');
insert into member_crfme_distribution values(10, '2025-06', '0-20', 1920, 14.5, 12.8, '沉默会员', 'admin', sysdate(), '', null, '沉默会员分层');

-- 2025年7月会员价值分层分布数据
insert into member_crfme_distribution values(11, '2025-07', '81-100', 980, 7.1, 90.8, '高价值会员', 'admin', sysdate(), '', null, '高价值会员分层');
insert into member_crfme_distribution values(12, '2025-07', '61-80', 2450, 17.8, 71.8, '潜力会员', 'admin', sysdate(), '', null, '潜力会员分层');
insert into member_crfme_distribution values(13, '2025-07', '41-60', 3850, 27.9, 51.5, '普通会员', 'admin', sysdate(), '', null, '普通会员分层');
insert into member_crfme_distribution values(14, '2025-07', '21-40', 4600, 33.3, 31.2, '低价值会员', 'admin', sysdate(), '', null, '低价值会员分层');
insert into member_crfme_distribution values(15, '2025-07', '0-20', 1920, 13.9, 13.1, '沉默会员', 'admin', sysdate(), '', null, '沉默会员分层');