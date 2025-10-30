-- ----------------------------
-- 知识库模块数据表
-- ----------------------------

-- ----------------------------
-- 1、知识分类表
-- ----------------------------
DROP TABLE IF EXISTS knowledge_category;
CREATE TABLE knowledge_category (
  category_id       bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '分类ID',
  category_code     varchar(50)     NOT NULL                   COMMENT '分类编码',
  category_name     varchar(100)    NOT NULL                   COMMENT '分类名称',
  parent_id         bigint(20)      DEFAULT 0                  COMMENT '父分类ID',
  sort_order        int(4)          DEFAULT 0                  COMMENT '显示顺序',
  description       varchar(500)    DEFAULT ''                 COMMENT '分类描述',
  status            char(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  del_flag          char(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       datetime                                   COMMENT '创建时间',
  update_by         varchar(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       datetime                                   COMMENT '更新时间',
  PRIMARY KEY (category_id),
  UNIQUE KEY uk_category_code (category_code)
) ENGINE=InnoDB AUTO_INCREMENT=1000 COMMENT = '知识分类表';

-- ----------------------------
-- 2、知识库主表
-- ----------------------------
DROP TABLE IF EXISTS knowledge_base;
CREATE TABLE knowledge_base (
  knowledge_id      bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '知识ID',
  title             varchar(200)    NOT NULL                   COMMENT '知识标题',
  summary           varchar(500)    DEFAULT ''                 COMMENT '知识摘要',
  content           longtext                                   COMMENT '知识内容',
  category_id       bigint(20)      NOT NULL                   COMMENT '分类ID',
  tags              varchar(500)    DEFAULT ''                 COMMENT '标签（逗号分隔）',
  author            varchar(64)     DEFAULT ''                 COMMENT '作者',
  source            varchar(200)    DEFAULT ''                 COMMENT '来源',
  attachment_url    varchar(500)    DEFAULT ''                 COMMENT '附件URL',
  views             int(11)         DEFAULT 0                  COMMENT '浏览次数',
  likes             int(11)         DEFAULT 0                  COMMENT '点赞次数',
  downloads         int(11)         DEFAULT 0                  COMMENT '下载次数',
  status            char(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  is_featured       char(1)         DEFAULT '0'                COMMENT '是否推荐（0否 1是）',
  publish_time      datetime                                   COMMENT '发布时间',
  del_flag          char(1)         DEFAULT '0'                COMMENT '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     DEFAULT ''                 COMMENT '创建者',
  create_time       datetime                                   COMMENT '创建时间',
  update_by         varchar(64)     DEFAULT ''                 COMMENT '更新者',
  update_time       datetime                                   COMMENT '更新时间',
  PRIMARY KEY (knowledge_id),
  KEY idx_category_id (category_id),
  KEY idx_status (status),
  KEY idx_create_time (create_time),
  KEY idx_views (views),
  KEY idx_likes (likes)
) ENGINE=InnoDB AUTO_INCREMENT=10000 COMMENT = '知识库主表';

-- ----------------------------
-- 3、知识库访问记录表
-- ----------------------------
DROP TABLE IF EXISTS knowledge_access_log;
CREATE TABLE knowledge_access_log (
  log_id            bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '日志ID',
  knowledge_id      bigint(20)      NOT NULL                   COMMENT '知识ID',
  user_id           bigint(20)      DEFAULT NULL               COMMENT '用户ID',
  user_name         varchar(64)     DEFAULT ''                 COMMENT '用户名称',
  access_type       varchar(20)     DEFAULT 'view'             COMMENT '访问类型（view浏览 like点赞 download下载）',
  ip_address        varchar(50)     DEFAULT ''                 COMMENT 'IP地址',
  user_agent        varchar(500)    DEFAULT ''                 COMMENT '用户代理',
  access_time       datetime                                   COMMENT '访问时间',
  PRIMARY KEY (log_id),
  KEY idx_knowledge_id (knowledge_id),
  KEY idx_user_id (user_id),
  KEY idx_access_time (access_time)
) ENGINE=InnoDB AUTO_INCREMENT=100000 COMMENT = '知识库访问记录表';

-- ----------------------------
-- 初始化知识分类数据
-- ----------------------------
INSERT INTO knowledge_category VALUES
(1, 'all', '全部知识', 0, 0, '所有知识分类', '0', '0', 'admin', NOW(), '', NULL),
(2, 'product', '产品知识', 0, 1, '产品相关的知识内容', '0', '0', 'admin', NOW(), '', NULL),
(3, 'sales', '销售技巧', 0, 2, '销售技巧和方法', '0', '0', 'admin', NOW(), '', NULL),
(4, 'member', '会员运营', 0, 3, '会员运营相关知识', '0', '0', 'admin', NOW(), '', NULL),
(5, 'faq', '常见问题解答', 0, 4, '常见问题及解答', '0', '0', 'admin', NOW(), '', NULL),
(6, 'competitor', '竞品分析', 0, 5, '竞品分析报告', '0', '0', 'admin', NOW(), '', NULL);

-- ----------------------------
-- 初始化知识库测试数据
-- ----------------------------
INSERT INTO knowledge_base VALUES
(1, '【竞品】主要竞争对手产品对比分析', '与主要竞品在配方、价格、渠道等方面的对比分析...', 
'<h6>竞品对比：</h6><ul><li><strong>品牌A：</strong> 价格较低，但营养配方相对简单</li><li><strong>品牌B：</strong> 营养全面，但价格偏高</li><li><strong>我们的优势：</strong> 性价比最优，营养科学配比</li></ul><h6>市场定位分析：</h6><p>通过深入分析主要竞争对手的产品特点，我们发现在中高端市场存在明显的机会空间。</p>', 
6, '竞品分析,市场调研,产品对比', 'admin', '市场部', '', 89, 15, 5, '0', '1', NOW(), '0', 'admin', NOW(), '', NULL),

(2, '【产品】雀巢奶粉营养成分详解', '详细介绍雀巢奶粉的营养成分及其对婴幼儿发育的作用', 
'<h6>主要营养成分：</h6><ul><li><strong>DHA：</strong> 促进大脑和视力发育</li><li><strong>益生菌：</strong> 增强肠道健康</li><li><strong>乳铁蛋白：</strong> 提高免疫力</li></ul><h6>科学配比：</h6><p>根据中国婴幼儿营养需求特点，科学配比各种营养素，确保宝宝健康成长。</p>', 
2, '产品知识,营养成分,婴幼儿', 'admin', '产品部', '', 156, 28, 12, '0', '1', NOW(), '0', 'admin', NOW(), '', NULL),

(3, '【销售】如何与新手妈妈建立信任关系', '针对新手妈妈的销售沟通技巧和信任建立方法', 
'<h6>沟通要点：</h6><ul><li><strong>倾听需求：</strong> 耐心了解妈妈的担忧和需求</li><li><strong>专业解答：</strong> 用专业知识回答产品相关问题</li><li><strong>情感共鸣：</strong> 理解新手妈妈的焦虑情绪</li></ul><h6>建立信任的步骤：</h6><ol><li>主动关怀，询问宝宝情况</li><li>分享育儿经验和知识</li><li>提供持续的售后服务</li></ol>', 
3, '销售技巧,客户关系,新手妈妈', 'admin', '销售部', '', 203, 42, 8, '0', '1', NOW(), '0', 'admin', NOW(), '', NULL),

(4, '【会员】会员积分体系运营指南', '会员积分体系的设计原理和运营策略', 
'<h6>积分体系设计：</h6><ul><li><strong>获取积分：</strong> 购买、签到、分享、评价</li><li><strong>消费积分：</strong> 兑换商品、优惠券、专属服务</li><li><strong>等级权益：</strong> 不同等级享受不同权益</li></ul><h6>运营策略：</h6><p>通过积分体系激励会员活跃度，提升复购率和忠诚度。</p>', 
4, '会员运营,积分体系,用户留存', 'admin', '运营部', '', 134, 31, 6, '0', '0', NOW(), '0', 'admin', NOW(), '', NULL),

(5, '【FAQ】奶粉冲调常见问题解答', '关于奶粉冲调方法的常见问题及标准答案', 
'<h6>常见问题：</h6><ul><li><strong>Q：水温应该多少度？</strong><br>A：建议使用40-50度的温开水冲调</li><li><strong>Q：先放水还是先放奶粉？</strong><br>A：应该先放水，再加入奶粉</li><li><strong>Q：冲调后可以保存多久？</strong><br>A：建议2小时内饮用完毕</li></ul>', 
5, 'FAQ,奶粉冲调,客户服务', 'admin', '客服部', '', 298, 67, 15, '0', '1', NOW(), '0', 'admin', NOW(), '', NULL),

(6, '【产品】新品上市推广策略', '新产品上市的推广策略和执行方案', 
'<h6>推广策略：</h6><ul><li><strong>预热阶段：</strong> 社交媒体造势，KOL合作</li><li><strong>上市阶段：</strong> 线上线下同步推广</li><li><strong>维护阶段：</strong> 用户反馈收集和产品优化</li></ul><h6>执行要点：</h6><p>确保推广信息的一致性和专业性，重点突出产品优势。</p>', 
2, '新品推广,营销策略,产品上市', 'admin', '市场部', '', 87, 19, 4, '0', '0', NOW(), '0', 'admin', NOW(), '', NULL);

-- ----------------------------
-- 初始化访问记录数据
-- ----------------------------
INSERT INTO knowledge_access_log (knowledge_id, user_id, user_name, access_type, ip_address, access_time) VALUES
(1, 1, 'admin', 'view', '127.0.0.1', NOW()),
(1, 1, 'admin', 'like', '127.0.0.1', NOW()),
(2, 1, 'admin', 'view', '127.0.0.1', NOW()),
(3, 1, 'admin', 'view', '127.0.0.1', NOW()),
(4, 1, 'admin', 'view', '127.0.0.1', NOW()),
(5, 1, 'admin', 'view', '127.0.0.1', NOW());