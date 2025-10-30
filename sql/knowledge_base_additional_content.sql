-- ----------------------------
-- 知识库内容扩充 - 添加更多实用知识条目
-- ----------------------------

-- 产品知识分类 (category_id = 2)
INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【产品】婴幼儿奶粉储存指南', '正确的奶粉储存方法，确保产品质量和安全', 
'<h6>储存环境要求：</h6><ul><li><strong>温度：</strong> 室温25℃以下，避免高温</li><li><strong>湿度：</strong> 干燥环境，避免潮湿</li><li><strong>光照：</strong> 避免阳光直射</li><li><strong>密封：</strong> 开封后及时密封</li></ul><h6>储存注意事项：</h6><ul><li>开封后30天内使用完毕</li><li>不要放在冰箱内储存</li><li>使用干燥的勺子取奶粉</li><li>避免与有异味的物品放在一起</li></ul>', 
2, '产品知识,储存方法,食品安全', 'admin', '产品部', 145, 32, 18, '0', '1', NOW(), '0', 'admin', NOW());

INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【产品】不同年龄段奶粉选择指南', '根据宝宝年龄选择合适的奶粉产品', 
'<h6>年龄分段：</h6><ul><li><strong>1段奶粉（0-6个月）：</strong> 接近母乳成分，易消化吸收</li><li><strong>2段奶粉（6-12个月）：</strong> 增加铁质和维生素，支持辅食期</li><li><strong>3段奶粉（12-36个月）：</strong> 均衡营养，支持幼儿全面发育</li></ul><h6>选择要点：</h6><ul><li>根据宝宝实际月龄选择</li><li>注意营养成分表</li><li>考虑宝宝的消化能力</li><li>观察宝宝适应情况</li></ul>', 
2, '产品知识,年龄分段,选择指南', 'admin', '产品部', 198, 45, 22, '0', '1', NOW(), '0', 'admin', NOW());

INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【产品】奶粉过敏预防与处理', '如何预防和处理宝宝奶粉过敏问题', 
'<h6>过敏症状识别：</h6><ul><li><strong>皮肤症状：</strong> 湿疹、红疹、瘙痒</li><li><strong>消化症状：</strong> 腹泻、呕吐、腹胀</li><li><strong>呼吸症状：</strong> 咳嗽、喘息（严重情况）</li></ul><h6>预防措施：</h6><ul><li>选择低敏配方奶粉</li><li>逐步转换奶粉品牌</li><li>注意家族过敏史</li><li>及时咨询医生</li></ul><h6>处理方法：</h6><ul><li>立即停止使用可疑奶粉</li><li>咨询儿科医生</li><li>考虑特殊配方奶粉</li><li>记录过敏反应</li></ul>', 
2, '产品知识,过敏预防,健康安全', 'admin', '医学部', 167, 38, 15, '0', '1', NOW(), '0', 'admin', NOW());

-- 销售技巧分类 (category_id = 3)
INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【销售】价格异议处理技巧', '面对客户价格异议时的专业应对方法', 
'<h6>常见价格异议：</h6><ul><li>"太贵了，有便宜的吗？"</li><li>"网上更便宜"</li><li>"其他品牌更实惠"</li></ul><h6>应对策略：</h6><ul><li><strong>价值强调：</strong> 突出产品独特价值和品质保证</li><li><strong>成本分析：</strong> 解释优质原料和工艺成本</li><li><strong>长期效益：</strong> 强调对宝宝健康的长期投资价值</li><li><strong>服务附加：</strong> 提及专业服务和售后保障</li></ul><h6>话术示例：</h6><p>"我理解您对价格的关注。我们的产品虽然价格稍高，但使用的是进口优质奶源，经过严格的质量检测，为宝宝提供最安全的营养保障。从长远来看，这是对宝宝健康最好的投资。"</p>', 
3, '销售技巧,异议处理,价格谈判', 'admin', '销售部', 234, 56, 28, '0', '1', NOW(), '0', 'admin', NOW());

INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【销售】客户需求挖掘技巧', '如何有效挖掘客户的真实需求', 
'<h6>需求挖掘步骤：</h6><ul><li><strong>倾听：</strong> 认真听取客户描述</li><li><strong>提问：</strong> 使用开放式问题深入了解</li><li><strong>确认：</strong> 重复确认客户需求</li><li><strong>分析：</strong> 分析显性和隐性需求</li></ul><h6>关键问题：</h6><ul><li>"宝宝现在多大了？"</li><li>"之前使用过什么奶粉？"</li><li>"宝宝有什么特殊情况吗？"</li><li>"您最关心奶粉的哪个方面？"</li></ul><h6>需求分类：</h6><ul><li><strong>功能需求：</strong> 营养、消化、免疫</li><li><strong>情感需求：</strong> 安全感、品牌信任</li><li><strong>社会需求：</strong> 面子、地位象征</li></ul>', 
3, '销售技巧,需求挖掘,客户沟通', 'admin', '销售部', 189, 41, 19, '0', '1', NOW(), '0', 'admin', NOW());

INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【销售】成交信号识别与把握', '识别客户成交信号并适时推进成交', 
'<h6>成交信号类型：</h6><ul><li><strong>语言信号：</strong> "这个多少钱？"、"有优惠吗？"</li><li><strong>行为信号：</strong> 仔细查看产品、询问细节</li><li><strong>情绪信号：</strong> 表现出兴趣、点头认同</li></ul><h6>成交技巧：</h6><ul><li><strong>假设成交法：</strong> "您是要一罐还是两罐？"</li><li><strong>选择成交法：</strong> "您选择1段还是2段？"</li><li><strong>紧迫成交法：</strong> "今天有特别优惠"</li><li><strong>试用成交法：</strong> "可以先试用小包装"</li></ul><h6>注意事项：</h6><ul><li>不要过于急躁</li><li>给客户思考时间</li><li>保持专业态度</li><li>做好售后服务承诺</li></ul>', 
3, '销售技巧,成交技巧,销售心理', 'admin', '销售部', 156, 33, 16, '0', '1', NOW(), '0', 'admin', NOW());

-- 会员运营分类 (category_id = 4)
INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【会员】会员等级权益设计', '会员等级体系的设计原则和权益配置', 
'<h6>等级设计原则：</h6><ul><li><strong>层次清晰：</strong> 普通、银卡、金卡、钻石</li><li><strong>门槛合理：</strong> 根据消费金额和频次设定</li><li><strong>权益递增：</strong> 等级越高权益越多</li><li><strong>激励明确：</strong> 升级条件透明</li></ul><h6>权益配置：</h6><ul><li><strong>普通会员：</strong> 积分奖励、生日优惠</li><li><strong>银卡会员：</strong> 9.5折优惠、专属客服</li><li><strong>金卡会员：</strong> 9折优惠、免费配送、优先体验</li><li><strong>钻石会员：</strong> 8.5折优惠、专属礼品、VIP活动</li></ul><h6>运营策略：</h6><ul><li>定期权益提醒</li><li>升级激励活动</li><li>个性化服务</li><li>数据分析优化</li></ul>', 
4, '会员运营,等级体系,权益设计', 'admin', '运营部', 178, 42, 21, '0', '1', NOW(), '0', 'admin', NOW());

INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【会员】会员生命周期管理', '不同生命周期阶段的会员运营策略', 
'<h6>生命周期阶段：</h6><ul><li><strong>新会员期：</strong> 注册后0-30天</li><li><strong>成长期：</strong> 31-90天，建立购买习惯</li><li><strong>成熟期：</strong> 91-365天，稳定复购</li><li><strong>衰退期：</strong> 超过365天无购买</li></ul><h6>运营策略：</h6><ul><li><strong>新会员：</strong> 欢迎礼包、新手指导、首单优惠</li><li><strong>成长期：</strong> 产品推荐、使用指导、互动活动</li><li><strong>成熟期：</strong> 个性化服务、高价值产品、忠诚度奖励</li><li><strong>衰退期：</strong> 召回活动、特别优惠、关怀服务</li></ul><h6>关键指标：</h6><ul><li>新会员转化率</li><li>会员活跃度</li><li>复购率和频次</li><li>会员生命周期价值</li></ul>', 
4, '会员运营,生命周期,客户关系', 'admin', '运营部', 145, 35, 18, '0', '1', NOW(), '0', 'admin', NOW());

INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【会员】会员活动策划指南', '如何策划有效的会员营销活动', 
'<h6>活动类型：</h6><ul><li><strong>促销活动：</strong> 满减、折扣、买赠</li><li><strong>互动活动：</strong> 签到、抽奖、游戏</li><li><strong>内容活动：</strong> 育儿讲座、专家问答</li><li><strong>社交活动：</strong> 分享有礼、邀请奖励</li></ul><h6>策划要素：</h6><ul><li><strong>目标明确：</strong> 拉新、促活、留存</li><li><strong>受众精准：</strong> 根据会员标签定向</li><li><strong>时机合适：</strong> 节假日、换季时节</li><li><strong>奖励吸引：</strong> 有价值的奖品或优惠</li></ul><h6>执行要点：</h6><ul><li>多渠道宣传推广</li><li>实时监控数据</li><li>及时调整策略</li><li>做好活动总结</li></ul>', 
4, '会员运营,活动策划,营销推广', 'admin', '运营部', 167, 39, 20, '0', '1', NOW(), '0', 'admin', NOW());

-- 常见问题解答分类 (category_id = 5)
INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【FAQ】产品质量与安全问题', '关于产品质量和安全的常见问题解答', 
'<h6>质量保证：</h6><ul><li><strong>Q：产品有质量认证吗？</strong><br>A：我们的产品通过了国家食品药品监督管理局认证，符合国家婴幼儿配方奶粉标准。</li><li><strong>Q：奶源来自哪里？</strong><br>A：采用优质进口奶源，来自新西兰和荷兰的天然牧场。</li></ul><h6>安全问题：</h6><ul><li><strong>Q：如何确保产品安全？</strong><br>A：从奶源到成品全程质量控制，每批产品都经过严格检测。</li><li><strong>Q：保质期多长？</strong><br>A：未开封产品保质期24个月，开封后请在30天内使用完毕。</li></ul>', 
5, 'FAQ,产品质量,安全认证', 'admin', '客服部', 289, 68, 32, '0', '1', NOW(), '0', 'admin', NOW());

INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【FAQ】配送与售后服务', '关于配送和售后服务的常见问题', 
'<h6>配送问题：</h6><ul><li><strong>Q：多久能收到货？</strong><br>A：一般情况下，下单后1-3个工作日内发货，3-7天内送达。</li><li><strong>Q：配送费用如何计算？</strong><br>A：单笔订单满299元免配送费，不满299元收取15元配送费。</li></ul><h6>售后服务：</h6><ul><li><strong>Q：不满意可以退货吗？</strong><br>A：未开封产品在收货7天内可以无理由退货，开封产品如有质量问题可以退换。</li><li><strong>Q：如何联系客服？</strong><br>A：可通过官方客服热线400-xxx-xxxx或在线客服联系我们。</li></ul>', 
5, 'FAQ,配送服务,售后支持', 'admin', '客服部', 245, 52, 25, '0', '1', NOW(), '0', 'admin', NOW());

INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【FAQ】营养与喂养指导', '关于营养成分和喂养方法的专业指导', 
'<h6>营养问题：</h6><ul><li><strong>Q：DHA含量是多少？</strong><br>A：每100g奶粉含DHA不少于0.3%，有助于宝宝大脑和视力发育。</li><li><strong>Q：是否添加益生菌？</strong><br>A：添加了双歧杆菌和乳酸菌，有助于维护肠道健康。</li></ul><h6>喂养指导：</h6><ul><li><strong>Q：每次冲调多少合适？</strong><br>A：根据宝宝月龄和体重，一般每次30-60ml，具体请参考包装上的喂养表。</li><li><strong>Q：可以和其他奶粉混合吗？</strong><br>A：不建议混合使用，如需转换奶粉请逐步过渡。</li></ul>', 
5, 'FAQ,营养指导,喂养方法', 'admin', '营养师', 198, 45, 28, '0', '1', NOW(), '0', 'admin', NOW());

-- 竞品分析分类 (category_id = 6)
INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【竞品】国际品牌对比分析', '与国际主要竞争品牌的全面对比', 
'<h6>品牌对比：</h6><ul><li><strong>品牌A：</strong> 价格偏高，营养全面，品牌知名度高</li><li><strong>品牌B：</strong> 性价比适中，口感较好，市场占有率高</li><li><strong>品牌C：</strong> 价格较低，基础营养，适合价格敏感客户</li></ul><h6>我们的优势：</h6><ul><li><strong>营养配方：</strong> 科学配比，更适合中国宝宝</li><li><strong>品质保证：</strong> 严格质控，安全可靠</li><li><strong>性价比：</strong> 合理定价，物超所值</li><li><strong>服务支持：</strong> 专业客服，贴心服务</li></ul><h6>市场定位：</h6><p>高品质、高性价比的中高端产品</p>', 
6, '竞品分析,市场对比,品牌优势', 'admin', '市场部', 156, 34, 17, '0', '1', NOW(), '0', 'admin', NOW());

INSERT INTO knowledge_base (title, summary, content, category_id, tags, author, source, views, likes, downloads, status, is_featured, publish_time, del_flag, create_by, create_time) VALUES
('【竞品】价格策略分析', '竞争对手的价格策略和我们的应对方案', 
'<h6>价格区间分析：</h6><ul><li><strong>高端产品：</strong> 300-500元/罐，主打进口优质</li><li><strong>中端产品：</strong> 200-300元/罐，平衡品质与价格</li><li><strong>低端产品：</strong> 100-200元/罐，主打性价比</li></ul><h6>我们的定价策略：</h6><ul><li><strong>产品定位：</strong> 中高端市场，250-350元/罐</li><li><strong>价值主张：</strong> 优质营养，合理价格</li><li><strong>促销策略：</strong> 定期优惠，会员专享</li></ul><h6>竞争优势：</h6><ul><li>同等品质下价格更优</li><li>同等价格下品质更好</li><li>灵活的促销政策</li><li>完善的服务体系</li></ul>', 
6, '竞品分析,价格策略,市场定位', 'admin', '市场部', 134, 28, 14, '0', '1', NOW(), '0', 'admin', NOW());