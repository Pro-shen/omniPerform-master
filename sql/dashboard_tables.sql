-- ----------------------------
-- 运营仪表盘相关数据表
-- 创建时间: 2025-01-27
-- 说明: 支持按月份查询的运营数据表
-- ----------------------------

-- ----------------------------
-- 1、会员概览数据表
-- ----------------------------
drop table if exists dashboard_member_overview;
create table dashboard_member_overview (
  id                bigint(20)      not null auto_increment    comment '主键ID',
  data_month        varchar(7)      not null                   comment '数据月份(格式:2025-05)',
  total_members     int(11)         default 0                  comment '总会员数',
  new_members       int(11)         default 0                  comment '新增会员数',
  active_members    int(11)         default 0                  comment '活跃会员数',
  member_growth_rate decimal(5,2)   default 0.00               comment '会员增长率(%)',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (id),
  unique key uk_data_month (data_month)
) engine=innodb comment = '会员概览数据表';

-- ----------------------------
-- 2、会员增长趋势数据表
-- ----------------------------
drop table if exists dashboard_member_growth;
create table dashboard_member_growth (
  id                bigint(20)      not null auto_increment    comment '主键ID',
  data_month        varchar(7)      not null                   comment '数据月份(格式:2025-05)',
  new_members       int(11)         default 0                  comment '新增会员数',
  total_members     int(11)         default 0                  comment '累计会员数',
  growth_rate       decimal(5,2)    default 0.00               comment '增长率(%)',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (id),
  unique key uk_data_month (data_month)
) engine=innodb comment = '会员增长趋势数据表';

-- ----------------------------
-- 3、会员阶段分布数据表
-- ----------------------------
drop table if exists dashboard_member_stage;
create table dashboard_member_stage (
  id                bigint(20)      not null auto_increment    comment '主键ID',
  data_month        varchar(7)      not null                   comment '数据月份(格式:2025-05)',
  stage_name        varchar(50)     not null                   comment '阶段名称',
  member_count      int(11)         default 0                  comment '会员数量',
  percentage        decimal(5,2)    default 0.00               comment '占比(%)',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (id),
  unique key uk_month_stage (data_month, stage_name)
) engine=innodb comment = '会员阶段分布数据表';

-- ----------------------------
-- 4、区域绩效数据表
-- ----------------------------
drop table if exists dashboard_region_performance;
create table dashboard_region_performance (
  id                bigint(20)      not null auto_increment    comment '主键ID',
  data_month        varchar(7)      not null                   comment '数据月份(格式:2025-05)',
  region_name       varchar(50)     not null                   comment '区域名称',
  sales_amount      decimal(15,2)   default 0.00               comment '销售金额',
  member_count      int(11)         default 0                  comment '会员数量',
  performance_score decimal(5,2)    default 0.00               comment '绩效得分',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (id),
  unique key uk_month_region (data_month, region_name)
) engine=innodb comment = '区域绩效数据表';

-- ----------------------------
-- 5、产品销售数据表
-- ----------------------------
drop table if exists dashboard_product_sales;
create table dashboard_product_sales (
  id                bigint(20)      not null auto_increment    comment '主键ID',
  data_month        varchar(7)      not null                   comment '数据月份(格式:2025-05)',
  product_name      varchar(100)    not null                   comment '产品名称',
  sales_amount      decimal(15,2)   default 0.00               comment '销售金额',
  sales_quantity    int(11)         default 0                  comment '销售数量',
  market_share      decimal(5,2)    default 0.00               comment '市场份额(%)',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (id),
  unique key uk_month_product (data_month, product_name)
) engine=innodb comment = '产品销售数据表';

-- ----------------------------
-- 初始化示例数据 - 2025年5月
-- ----------------------------

-- 会员概览数据
insert into dashboard_member_overview values(1, '2025-05', 12500, 850, 9800, 7.30, 'admin', sysdate(), '', null, '2025年5月会员概览数据');
insert into dashboard_member_overview values(2, '2025-06', 13200, 700, 10200, 5.60, 'admin', sysdate(), '', null, '2025年6月会员概览数据');
insert into dashboard_member_overview values(3, '2025-07', 13800, 600, 10500, 4.55, 'admin', sysdate(), '', null, '2025年7月会员概览数据');

-- 会员增长趋势数据
insert into dashboard_member_growth values(1, '2025-05', 850, 12500, 7.30, 'admin', sysdate(), '', null, '2025年5月增长数据');
insert into dashboard_member_growth values(2, '2025-06', 700, 13200, 5.60, 'admin', sysdate(), '', null, '2025年6月增长数据');
insert into dashboard_member_growth values(3, '2025-07', 600, 13800, 4.55, 'admin', sysdate(), '', null, '2025年7月增长数据');

-- 会员阶段分布数据
insert into dashboard_member_stage values(1, '2025-05', '0阶段(孕妇)', 3750, 30.00, 'admin', sysdate(), '', null, '0阶段孕妇会员');
insert into dashboard_member_stage values(2, '2025-05', '1阶段(0-6个月)', 4375, 35.00, 'admin', sysdate(), '', null, '1阶段0-6个月会员');
insert into dashboard_member_stage values(3, '2025-05', '2阶段(6-12个月)', 3125, 25.00, 'admin', sysdate(), '', null, '2阶段6-12个月会员');
insert into dashboard_member_stage values(4, '2025-05', '3阶段(1-2岁)', 1000, 8.00, 'admin', sysdate(), '', null, '3阶段1-2岁会员');
insert into dashboard_member_stage values(5, '2025-05', '4阶段(2-3岁)', 250, 2.00, 'admin', sysdate(), '', null, '4阶段2-3岁会员');

insert into dashboard_member_stage values(6, '2025-06', '0阶段(孕妇)', 3960, 30.00, 'admin', sysdate(), '', null, '0阶段孕妇会员');
insert into dashboard_member_stage values(7, '2025-06', '1阶段(0-6个月)', 4620, 35.00, 'admin', sysdate(), '', null, '1阶段0-6个月会员');
insert into dashboard_member_stage values(8, '2025-06', '2阶段(6-12个月)', 3300, 25.00, 'admin', sysdate(), '', null, '2阶段6-12个月会员');
insert into dashboard_member_stage values(9, '2025-06', '3阶段(1-2岁)', 1056, 8.00, 'admin', sysdate(), '', null, '3阶段1-2岁会员');
insert into dashboard_member_stage values(10, '2025-06', '4阶段(2-3岁)', 264, 2.00, 'admin', sysdate(), '', null, '4阶段2-3岁会员');

insert into dashboard_member_stage values(11, '2025-07', '0阶段(孕妇)', 4140, 30.00, 'admin', sysdate(), '', null, '0阶段孕妇会员');
insert into dashboard_member_stage values(12, '2025-07', '1阶段(0-6个月)', 4830, 35.00, 'admin', sysdate(), '', null, '1阶段0-6个月会员');
insert into dashboard_member_stage values(13, '2025-07', '2阶段(6-12个月)', 3450, 25.00, 'admin', sysdate(), '', null, '2阶段6-12个月会员');
insert into dashboard_member_stage values(14, '2025-07', '3阶段(1-2岁)', 1104, 8.00, 'admin', sysdate(), '', null, '3阶段1-2岁会员');
insert into dashboard_member_stage values(15, '2025-07', '4阶段(2-3岁)', 276, 2.00, 'admin', sysdate(), '', null, '4阶段2-3岁会员');

-- 区域绩效数据
insert into dashboard_region_performance values(1, '2025-05', '华东', 2850000.00, 4200, 92.5, 'admin', sysdate(), '', null, '华东区域绩效');
insert into dashboard_region_performance values(2, '2025-05', '华南', 2650000.00, 3800, 89.2, 'admin', sysdate(), '', null, '华南区域绩效');
insert into dashboard_region_performance values(3, '2025-05', '华北', 2450000.00, 3500, 87.8, 'admin', sysdate(), '', null, '华北区域绩效');
insert into dashboard_region_performance values(4, '2025-05', '西南', 1850000.00, 2000, 82.3, 'admin', sysdate(), '', null, '西南区域绩效');

insert into dashboard_region_performance values(5, '2025-06', '华东', 2950000.00, 4350, 93.2, 'admin', sysdate(), '', null, '华东区域绩效');
insert into dashboard_region_performance values(6, '2025-06', '华南', 2750000.00, 3950, 90.1, 'admin', sysdate(), '', null, '华南区域绩效');
insert into dashboard_region_performance values(7, '2025-06', '华北', 2550000.00, 3650, 88.5, 'admin', sysdate(), '', null, '华北区域绩效');
insert into dashboard_region_performance values(8, '2025-06', '西南', 1950000.00, 2100, 83.7, 'admin', sysdate(), '', null, '西南区域绩效');

insert into dashboard_region_performance values(9, '2025-07', '华东', 3050000.00, 4500, 94.1, 'admin', sysdate(), '', null, '华东区域绩效');
insert into dashboard_region_performance values(10, '2025-07', '华南', 2850000.00, 4100, 91.3, 'admin', sysdate(), '', null, '华南区域绩效');
insert into dashboard_region_performance values(11, '2025-07', '华北', 2650000.00, 3800, 89.2, 'admin', sysdate(), '', null, '华北区域绩效');
insert into dashboard_region_performance values(12, '2025-07', '西南', 2050000.00, 2200, 85.1, 'admin', sysdate(), '', null, '西南区域绩效');

-- 产品销售数据
insert into dashboard_product_sales values(1, '2025-05', '雀巢咖啡', 1850000.00, 15200, 28.5, 'admin', sysdate(), '', null, '雀巢咖啡销售数据');
insert into dashboard_product_sales values(2, '2025-05', '雀巢奶粉', 1650000.00, 8500, 25.2, 'admin', sysdate(), '', null, '雀巢奶粉销售数据');
insert into dashboard_product_sales values(3, '2025-05', '雀巢巧克力', 1250000.00, 12800, 19.8, 'admin', sysdate(), '', null, '雀巢巧克力销售数据');
insert into dashboard_product_sales values(4, '2025-05', '雀巢麦片', 950000.00, 9600, 15.2, 'admin', sysdate(), '', null, '雀巢麦片销售数据');
insert into dashboard_product_sales values(5, '2025-05', '雀巢饮用水', 750000.00, 18500, 11.3, 'admin', sysdate(), '', null, '雀巢饮用水销售数据');

insert into dashboard_product_sales values(6, '2025-06', '雀巢咖啡', 1920000.00, 15800, 28.2, 'admin', sysdate(), '', null, '雀巢咖啡销售数据');
insert into dashboard_product_sales values(7, '2025-06', '雀巢奶粉', 1720000.00, 8850, 25.5, 'admin', sysdate(), '', null, '雀巢奶粉销售数据');
insert into dashboard_product_sales values(8, '2025-06', '雀巢巧克力', 1300000.00, 13200, 19.5, 'admin', sysdate(), '', null, '雀巢巧克力销售数据');
insert into dashboard_product_sales values(9, '2025-06', '雀巢麦片', 980000.00, 9850, 15.1, 'admin', sysdate(), '', null, '雀巢麦片销售数据');
insert into dashboard_product_sales values(10, '2025-06', '雀巢饮用水', 780000.00, 19200, 11.7, 'admin', sysdate(), '', null, '雀巢饮用水销售数据');

insert into dashboard_product_sales values(11, '2025-07', '雀巢咖啡', 1980000.00, 16200, 28.0, 'admin', sysdate(), '', null, '雀巢咖啡销售数据');
insert into dashboard_product_sales values(12, '2025-07', '雀巢奶粉', 1780000.00, 9100, 25.8, 'admin', sysdate(), '', null, '雀巢奶粉销售数据');
insert into dashboard_product_sales values(13, '2025-07', '雀巢巧克力', 1350000.00, 13600, 19.2, 'admin', sysdate(), '', null, '雀巢巧克力销售数据');
insert into dashboard_product_sales values(14, '2025-07', '雀巢麦片', 1020000.00, 10200, 15.0, 'admin', sysdate(), '', null, '雀巢麦片销售数据');
insert into dashboard_product_sales values(15, '2025-07', '雀巢饮用水', 820000.00, 20100, 12.0, 'admin', sysdate(), '', null, '雀巢饮用水销售数据');