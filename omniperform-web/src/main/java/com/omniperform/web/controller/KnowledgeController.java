package com.omniperform.web.controller;

import com.omniperform.common.annotation.Anonymous;
import com.omniperform.web.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * 知识库管理控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/knowledge")
@CrossOrigin(origins = "*")
@Api(tags = "知识库管理")
public class KnowledgeController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeController.class);

    /**
     * 获取知识分类列表
     */
    @GetMapping("/categories")
    @ApiOperation("获取知识分类列表")
    public Result getKnowledgeCategories() {
        try {
            List<Map<String, Object>> categories = new ArrayList<>();
            
            // 知识分类数据
            categories.add(createCategory("all", "全部知识", 150, true));
            categories.add(createCategory("product", "产品知识", 45, false));
            categories.add(createCategory("sales", "销售技巧", 30, false));
            categories.add(createCategory("member", "会员运营", 25, false));
            categories.add(createCategory("faq", "常见问题解答 (FAQ)", 35, false));
            categories.add(createCategory("competitor", "竞品分析", 15, false));
            
            log.info("获取知识分类列表成功");
            return Result.success("获取知识分类列表成功", categories);
        } catch (Exception e) {
            log.error("获取知识分类列表失败: {}", e.getMessage(), e);
            return Result.error("获取知识分类列表失败");
        }
    }

    /**
     * 获取知识列表
     */
    @GetMapping("/list")
    @ApiOperation("获取知识列表")
    public Result getKnowledgeList(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String category,
                                   @RequestParam(required = false) String search,
                                   @RequestParam(required = false) String sortBy) {
        try {
            List<Map<String, Object>> knowledgeList = new ArrayList<>();
            
            // 创建知识条目数据
            knowledgeList.add(createKnowledgeItem(
                "1", "product", "【产品】0段孕妇奶粉核心卖点解析",
                "针对孕期妈妈营养需求，特别添加DHA、叶酸、钙等关键营养素...",
                "3天前更新", 125, 30,
                "<h6>核心卖点：</h6><ul><li><strong>全面营养支持：</strong> 专为孕期妈妈设计，提供胎儿发育和母体健康所需的关键营养，如高含量DHA、活性叶酸、易吸收钙质、铁、锌等。</li><li><strong>科学配比：</strong> 参照国际权威营养推荐标准，精准配比各种营养素，满足孕早、中、晚期不同阶段的需求。</li><li><strong>低脂配方：</strong> 控制脂肪含量，有助于孕期体重管理，避免过度肥胖。</li><li><strong>口感优化：</strong> 口味清淡，易于入口，减少孕期不适反应。</li><li><strong>品牌优势：</strong> 国际品牌，国内生产，品质有保障，经过严格质量检测。</li></ul><h6>适用人群：</h6><p>备孕期及整个孕期的女性。</p><h6>沟通要点：</h6><p>强调产品对胎儿大脑发育（DHA）、神经管发育（叶酸）及母体骨骼健康（钙）的重要性，突出配方科学性和安全性。</p>"
            ));
            
            knowledgeList.add(createKnowledgeItem(
                "2", "sales", "【技巧】如何应对客户关于价格的疑虑",
                "当客户觉得产品价格偏高时，可以从价值、品质、服务等角度进行沟通...",
                "5天前更新", 210, 55,
                "<h6>应对策略：</h6><ol><li><strong>价值塑造优先：</strong> 不要直接回应价格，先强调产品的独特价值。</li><li><strong>竞品对比：</strong> 如果客户提及其他品牌，可以客观分析我们产品的优势所在。</li><li><strong>理解与共情：</strong> 表示理解客户对价格的关注，然后重申产品的价值。</li><li><strong>促销活动告知：</strong> 如果有合适的促销活动则告知机会。</li></ol><h6>注意事项：</h6><ul><li>保持自信和专业，不要对自己的产品价格表示歉意。</li><li>根据客户的具体情况和关注点灵活运用不同策略。</li><li>最终目标是让客户认同产品的价值，而非仅仅接受价格。</li></ul>"
            ));
            
            knowledgeList.add(createKnowledgeItem(
                "3", "member", "【会员】新会员首次沟通SOP",
                "新会员招募成功后，24小时内进行首次沟通，建立良好关系...",
                "1周前更新", 98, 20,
                "<h6>沟通目标：</h6><ul><li>表示欢迎，建立初步信任</li><li>核对并完善会员信息（特别是宝宝信息）</li><li>了解会员核心需求和痛点</li><li>介绍会员权益和后续服务</li></ul><h6>沟通流程：</h6><ol><li><strong>信息核对与完善（2-3分钟）：</strong> 为了给您和宝宝提供更精准的服务，想跟您核对了解一下信息。请问宝宝现在是多大了？预产期/出生日期是？</li><li><strong>需求探寻（3-5分钟）：</strong> 请问您目前在宝宝喂养/孕期营养方面，有没有遇到什么困惑或者特别关注的问题呢？</li><li><strong>会员权益介绍（1-2分钟）：</strong> 成为我们的会员后，您可以享受到积分兑换、生日礼、育儿讲座等权益。</li><li><strong>引导与结束（1分钟）：</strong> 后续有任何问题都可以随时联系我。</li></ol><h6>注意事项：</h6><ul><li>沟通时间建议控制在10分钟内</li><li>语气亲切、专业、耐心</li><li>沟通后及时在系统内完善会员标签和备注</li><li>24小时内完成首次沟通</li></ul>"
            ));
            
            knowledgeList.add(createKnowledgeItem(
                "4", "faq", "【FAQ】水解奶粉和普通奶粉有什么区别？",
                "水解奶粉通过特殊工艺将牛奶蛋白分解成小分子，降低致敏性...",
                "2周前更新", 350, 80,
                "<h6>核心区别：蛋白质处理方式不同</h6><ul><li><strong>普通配方奶粉：</strong> 以完整的牛奶蛋白质为基础，适合大部分没有过敏风险的宝宝。</li><li><strong>水解配方奶粉：</strong> 通过先进的水解工艺，将大分子的牛奶蛋白质分解成更小的肽段甚至氨基酸。</li></ul><h6>水解程度分类：</h6><ol><li><strong>部分水解(PHF)：</strong> 将蛋白质部分分解成较小的肽段，降低了蛋白质的致敏性，主要用于预防过敏，适合父母或兄弟姐妹有过敏史的宝宝。我们的品牌在此领域技术全球领先。</li><li><strong>深度水解(EHF)：</strong> 将蛋白质分解成更小的肽段和少量氨基酸，显著降低致敏性，主要用于治疗轻中度的牛奶蛋白过敏。</li><li><strong>氨基酸配方(AAF)：</strong> 完全不含蛋白质片段，以氨基酸为基础，用于治疗重度牛奶蛋白过敏。</li></ol><h6>主要作用：</h6><ul><li><strong>普通奶粉：</strong> 提供常规营养。</li><li><strong>水解奶粉：</strong> 主要用于预防或治疗牛奶蛋白过敏引起的各种症状，如湿疹、腹泻、便秘、吐奶、哭闹不安等。</li></ul><h6>如何选择：</h6><ul><li><strong>无过敏风险：</strong> 选择普通配方奶粉。</li><li><strong>有过敏家族史（预防）：</strong> 建议选择部分水解配方奶粉。</li><li><strong>已确诊牛奶蛋白过敏（治疗）：</strong> 根据过敏严重程度，在医生指导下选择深度水解或氨基酸配方奶粉。</li></ul><h6>沟通要点：</h6><p>强调水解技术的科学性和必要性，特别是对于有过敏风险或已出现过敏症状的宝宝。突出我们品牌在部分水解技术上的领先优势和专业性。<strong>切记：</strong>水解奶粉的选择和使用最好在医生或专业人士指导下进行。</p>"
            ));
            
            knowledgeList.add(createKnowledgeItem(
                "5", "product", "【产品】1段奶粉营养成分详解",
                "0-6个月宝宝专用配方，含有DHA、ARA、益生元等关键营养...",
                "1周前更新", 180, 42,
                "<h6>核心营养成分：</h6><ul><li><strong>DHA：</strong> 支持大脑和视力发育</li><li><strong>ARA：</strong> 促进神经系统发育</li><li><strong>益生元：</strong> 维护肠道健康</li><li><strong>乳铁蛋白：</strong> 增强免疫力</li></ul>"
            ));
            
            knowledgeList.add(createKnowledgeItem(
                "6", "sales", "【技巧】新客户开发的5个步骤",
                "从接触到成交，系统化的新客户开发流程...",
                "3天前更新", 156, 38,
                "<h6>开发流程：</h6><ol><li><strong>接触：</strong> 主动问候，建立初步印象</li><li><strong>了解：</strong> 询问需求，收集信息</li><li><strong>推荐：</strong> 针对性产品推荐</li><li><strong>解答：</strong> 处理疑虑和问题</li><li><strong>成交：</strong> 促成购买决定</li></ol>"
            ));
            
            knowledgeList.add(createKnowledgeItem(
                "7", "competitor", "【竞品】主要竞争对手产品对比分析",
                "与主要竞品在配方、价格、渠道等方面的对比分析...",
                "1周前更新", 89, 15,
                "<h6>竞品对比：</h6><ul><li><strong>品牌A：</strong> 价格较低，但营养配方相对简单</li><li><strong>品牌B：</strong> 营养全面，但价格偏高</li><li><strong>我们的优势：</strong> 性价比最优，营养科学配比</li></ul>"
            ));
            
            // 根据分类筛选
            if (category != null && !"all".equals(category)) {
                knowledgeList = knowledgeList.stream()
                    .filter(item -> category.equals(item.get("category")))
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
            }
            
            // 根据搜索关键词筛选
            if (search != null && !search.trim().isEmpty()) {
                String searchLower = search.toLowerCase();
                knowledgeList = knowledgeList.stream()
                    .filter(item -> {
                        String title = ((String) item.get("title")).toLowerCase();
                        String summary = ((String) item.get("summary")).toLowerCase();
                        return title.contains(searchLower) || summary.contains(searchLower);
                    })
                    .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
            }
            
            // 排序
            if ("1".equals(sortBy)) { // 按浏览次数
                knowledgeList.sort((a, b) -> Integer.compare((Integer) b.get("views"), (Integer) a.get("views")));
            } else if ("2".equals(sortBy)) { // 按点赞次数
                knowledgeList.sort((a, b) -> Integer.compare((Integer) b.get("likes"), (Integer) a.get("likes")));
            }
            // 默认按更新时间排序（已经是正确顺序）
            
            // 分页
            int total = knowledgeList.size();
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            
            List<Map<String, Object>> pagedList = new ArrayList<>();
            if (start < total) {
                pagedList = knowledgeList.subList(start, end);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", pagedList);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (int) Math.ceil((double) total / size));
            
            log.info("获取知识列表成功，页码: {}, 大小: {}, 总数: {}", page, size, total);
            return Result.success("获取知识列表成功", result);
        } catch (Exception e) {
            log.error("获取知识列表失败: {}", e.getMessage(), e);
            return Result.error("获取知识列表失败");
        }
    }

    /**
     * 获取知识详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation("获取知识详情")
    public Result getKnowledgeDetail(@PathVariable String id) {
        try {
            // 这里可以根据ID从数据库获取详细信息
            // 目前返回模拟数据
            Map<String, Object> detail = new HashMap<>();
            detail.put("id", id);
            detail.put("title", "知识详情标题");
            detail.put("content", "详细的知识内容...");
            detail.put("category", "product");
            detail.put("updateTime", LocalDate.now().toString());
            detail.put("views", 100);
            detail.put("likes", 20);
            
            log.info("获取知识详情成功，ID: {}", id);
            return Result.success("获取知识详情成功", detail);
        } catch (Exception e) {
            log.error("获取知识详情失败: {}", e.getMessage(), e);
            return Result.error("获取知识详情失败");
        }
    }

    /**
     * 创建知识分类对象
     */
    private Map<String, Object> createCategory(String code, String name, int count, boolean active) {
        Map<String, Object> category = new HashMap<>();
        category.put("code", code);
        category.put("name", name);
        category.put("count", count);
        category.put("active", active);
        return category;
    }

    /**
     * 创建知识条目对象
     */
    private Map<String, Object> createKnowledgeItem(String id, String category, String title, 
                                                     String summary, String updateTime, 
                                                     int views, int likes, String content) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("category", category);
        item.put("title", title);
        item.put("summary", summary);
        item.put("updateTime", updateTime);
        item.put("views", views);
        item.put("likes", likes);
        item.put("content", content);
        
        // 分类名称映射
        Map<String, String> categoryNames = new HashMap<>();
        categoryNames.put("product", "产品知识");
        categoryNames.put("sales", "销售技巧");
        categoryNames.put("member", "会员运营");
        categoryNames.put("faq", "常见问题解答");
        categoryNames.put("competitor", "竞品分析");
        
        item.put("categoryName", categoryNames.get(category));
        
        return item;
    }
}