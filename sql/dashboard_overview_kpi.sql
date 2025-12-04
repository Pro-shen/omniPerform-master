-- ----------------------------
-- 首页概览KPI专用数据表
-- 创建时间: 2025-11-05
-- 说明: 独立于其他优化效果数据，按月份存储首页KPI及环比
-- ----------------------------

drop table if exists dashboard_overview_kpi;
create table dashboard_overview_kpi (
  id                      bigint(20)      not null auto_increment    comment '主键ID',
  data_month              varchar(7)      not null                   comment '数据月份(格式:YYYY-MM)',
  region_code             varchar(64)     default null               comment '区域代码，可为空表示全国',
  new_members             int(11)         default null               comment '新会员数量',
  new_members_growth      decimal(6,2)    default null               comment '新会员数量环比(%)',
  repeat_purchase_rate    decimal(6,2)    default null               comment '复购率(%)',
  repeat_purchase_growth  decimal(6,2)    default null               comment '复购率环比(%)',
  mot_success_rate        decimal(6,2)    default null               comment 'MOT成功率(%)',
  mot_success_growth      decimal(6,2)    default null               comment 'MOT成功率环比(%)',
  member_activity_rate    decimal(6,2)    default null               comment '会员活跃度(%)',
  member_activity_growth  decimal(6,2)    default null               comment '会员活跃度环比(%)',
  create_by               varchar(64)     default ''                 comment '创建者',
  create_time             datetime                                   comment '创建时间',
  update_by               varchar(64)     default ''                 comment '更新者',
  update_time             datetime                                   comment '更新时间',
  remark                  varchar(500)    default null               comment '备注',
  primary key (id),
  unique key uk_month_region (data_month, region_code)
) engine=innodb comment = '首页概览KPI数据表';

-- 示例数据
insert into dashboard_overview_kpi
  (data_month, region_code, new_members, new_members_growth, repeat_purchase_rate, repeat_purchase_growth,
   mot_success_rate, mot_success_growth, member_activity_rate, member_activity_growth, create_by, create_time)
values
  ('2025-05', null, 1138, 8.20, 34.30, 0.80, 22.60, 1.70, 63.10, 1.90, 'system', sysdate()),
  ('2025-06', null, 1285, 12.80, 38.20, 3.50, 24.50, 2.10, 65.20, 1.80, 'system', sysdate()),
  ('2025-07', null, 1302, 1.30, 38.80, 0.60, 25.20, 0.70, 66.00, 0.80, 'system', sysdate());