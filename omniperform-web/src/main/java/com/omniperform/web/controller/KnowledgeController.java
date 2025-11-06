package com.omniperform.web.controller;

import com.omniperform.common.annotation.Anonymous;
import com.omniperform.web.common.Result;
import com.omniperform.common.utils.poi.ExcelUtil;
import com.omniperform.common.utils.file.FileUtils;
import com.omniperform.web.domain.KnowledgeImportDTO;
import org.springframework.web.multipart.MultipartFile;
import com.omniperform.web.domain.KnowledgeBase;
import com.omniperform.web.domain.KnowledgeCategory;
import com.omniperform.web.service.IKnowledgeBaseService;
import com.omniperform.web.service.IKnowledgeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 知识库管理
 * 
 * @author omniperform
 */
@RestController
@RequestMapping("/knowledge")
@Api(tags = "知识库管理")
@Anonymous
@CrossOrigin(origins = "*")
public class KnowledgeController {
    
    private static final Logger logger = LoggerFactory.getLogger(KnowledgeController.class);
    
    @Autowired
    private IKnowledgeCategoryService knowledgeCategoryService;
    
    @Autowired
    private IKnowledgeBaseService knowledgeBaseService;

    /**
     * 获取知识库分类列表
     */
    @GetMapping("/categories")
    @ApiOperation("获取知识库分类列表")
    public Result categories() {
        try {
            List<Map<String, Object>> categories = knowledgeCategoryService.getCategoriesWithCount();
            
            logger.info("获取知识库分类列表成功，共{}个分类", categories.size());
            return Result.success("获取知识库分类列表成功", categories);
        } catch (Exception e) {
            logger.error("获取知识库分类失败: {}", e.getMessage(), e);
            return Result.error("获取知识库分类失败");
        }
    }

    /**
     * 获取知识列表
     */
    @GetMapping("/list")
    @ApiOperation("获取知识列表")
    public Result list(@RequestParam(defaultValue = "1") int page,
                      @RequestParam(defaultValue = "10") int size,
                      @RequestParam(required = false) String category,
                      @RequestParam(required = false) String search,
                      @RequestParam(defaultValue = "views") String sortBy) {
        try {
            // Convert category parameter (could be ID or code) to categoryCode
            String categoryCode = null;
            if (category != null && !category.isEmpty()) {
                // Try to parse as ID first
                 try {
                     Long categoryId = Long.parseLong(category);
                     KnowledgeCategory categoryObj = knowledgeCategoryService.selectKnowledgeCategoryByCategoryId(categoryId);
                     if (categoryObj != null) {
                         categoryCode = categoryObj.getCategoryCode();
                     }
                 } catch (NumberFormatException e) {
                     // If not a number, treat as category code
                     categoryCode = category;
                 }
            }
            
            // Get knowledge list with conditions
            Map<String, Object> result = knowledgeBaseService.getKnowledgeListWithConditions(page, size, categoryCode, search, sortBy);
            
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取知识库列表失败", e);
            return Result.error("获取知识库列表失败");
        }
    }

    /**
     * 获取知识详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation("获取知识详情")
    public Result getKnowledgeDetail(@PathVariable String id) {
        try {
            Long knowledgeId = Long.parseLong(id);
            KnowledgeBase knowledgeDetail = knowledgeBaseService.getKnowledgeDetail(knowledgeId);
            
            if (knowledgeDetail == null) {
                return Result.error("知识库文章不存在");
            }
            
            logger.info("获取知识详情成功，ID: {}", id);
            return Result.success(knowledgeDetail);
        } catch (NumberFormatException e) {
            logger.error("无效的知识库ID: {}", id);
            return Result.error("无效的知识库ID");
        } catch (Exception e) {
            logger.error("获取知识详情失败", e);
            return Result.error("获取知识详情失败");
        }
    }

    /**
     * 下载知识库Excel导入模板
     */
    @GetMapping("/import/template")
    @ApiOperation("下载知识库Excel导入模板")
    public void downloadKnowledgeImportTemplate(javax.servlet.http.HttpServletResponse response) {
        try {
            ExcelUtil<KnowledgeImportDTO> util = new ExcelUtil<>(KnowledgeImportDTO.class);
            java.util.List<KnowledgeImportDTO> samples = new java.util.ArrayList<>();

            KnowledgeImportDTO s1 = new KnowledgeImportDTO();
            s1.setTitle("产品A介绍与卖点");
            s1.setSummary("产品A的关键卖点、规格与适用场景");
            s1.setContent("产品A主打高保湿与温和，适用于干皮和敏感肌。规格：200ml；核心成分：透明质酸、泛醇。使用建议：早晚洁面后使用。");
            s1.setTags("产品,卖点,规格");
            s1.setAuthor("产品专家");
            s1.setSource("内部知识库");
            s1.setAttachmentUrl("https://example.com/productA/specs.pdf");
            s1.setCategoryCode("product");
            s1.setPublishTime(new java.util.Date());
            s1.setStatus("0");
            s1.setIsFeatured("1");
            samples.add(s1);

            KnowledgeImportDTO s2 = new KnowledgeImportDTO();
            s2.setTitle("销售话术：如何引导顾客试用产品A");
            s2.setSummary("试用环节关键话术与注意事项");
            s2.setContent("开场：您好，最近天气干燥，皮肤容易缺水。不妨试试我们这款高保湿产品A。展示：先试在手背上，感受下吸收速度。强调：无酒精配方，对敏感肌更友好。");
            s2.setTags("销售,话术,试用");
            s2.setAuthor("资深导购");
            s2.setSource("线下经验沉淀");
            s2.setAttachmentUrl("https://example.com/talk/demo.mov");
            s2.setCategoryCode("sales");
            s2.setPublishTime(new java.util.Date());
            s2.setStatus("0");
            s2.setIsFeatured("0");
            samples.add(s2);

            KnowledgeImportDTO s3 = new KnowledgeImportDTO();
            s3.setTitle("会员分层：黄金会员运营策略");
            s3.setSummary("黄金会员激活与复购提升方案");
            s3.setContent("核心动作：定期推送定制化护理方案；专属优惠券（满299-60）；生日礼；专属客服跟进。指标：复购率提升15%，客单价提升10%。");
            s3.setTags("会员,分层,运营策略");
            s3.setAuthor("运营同学");
            s3.setSource("运营手册");
            s3.setAttachmentUrl("https://example.com/member/gold-plan.pdf");
            s3.setCategoryCode("member");
            s3.setPublishTime(new java.util.Date());
            s3.setStatus("0");
            s3.setIsFeatured("0");
            samples.add(s3);

            KnowledgeImportDTO s4 = new KnowledgeImportDTO();
            s4.setTitle("FAQ：产品A是否适合敏感肌？");
            s4.setSummary("敏感肌使用注意事项");
            s4.setContent("回答：产品A为温和配方，不含酒精与香精。建议首次使用先做局部测试，如无不适可按日常护理流程使用。");
            s4.setTags("FAQ,敏感肌,产品使用");
            s4.setAuthor("客服团队");
            s4.setSource("客服知识库");
            s4.setAttachmentUrl("https://example.com/faq/productA-sensitive.html");
            s4.setCategoryCode("faq");
            s4.setPublishTime(new java.util.Date());
            s4.setStatus("0");
            s4.setIsFeatured("0");
            samples.add(s4);

            KnowledgeImportDTO s5 = new KnowledgeImportDTO();
            s5.setTitle("竞品分析：品牌X保湿乳与产品A对比");
            s5.setSummary("成分、价格与用户口碑对比");
            s5.setContent("成分：品牌X以甘油为主，产品A含透明质酸与泛醇，保湿更持久。价格：品牌X 189元/150ml；产品A 199元/200ml。口碑：X平均评分4.3，A平均评分4.6。");
            s5.setTags("竞品,分析,对比");
            s5.setAuthor("市场研究员");
            s5.setSource("市场情报");
            s5.setAttachmentUrl("https://example.com/competitor/x-vs-a.xlsx");
            s5.setCategoryCode("competitor");
            s5.setPublishTime(new java.util.Date());
            s5.setStatus("0");
            s5.setIsFeatured("0");
            samples.add(s5);

            KnowledgeImportDTO s6 = new KnowledgeImportDTO();
            s6.setTitle("产品B使用指南（干皮修护）");
            s6.setSummary("三步法修护：清洁-修护-保湿");
            s6.setContent("第一步：温和清洁；第二步：修护精华（含神经酰胺）；第三步：高保湿面霜锁水。建议晚间护理搭配睡眠面膜。");
            s6.setTags("产品,修护,干皮");
            s6.setAuthor("产品专家");
            s6.setSource("内部知识库");
            s6.setAttachmentUrl("https://example.com/productB/guide.pdf");
            s6.setCategoryCode("product");
            s6.setPublishTime(new java.util.Date());
            s6.setStatus("0");
            s6.setIsFeatured("0");
            samples.add(s6);

            KnowledgeImportDTO s7 = new KnowledgeImportDTO();
            s7.setTitle("门店活动：新品A试用周执行SOP");
            s7.setSummary("物料、动线与人员分工");
            s7.setContent("物料：试用装、展架、海报；动线：入口引导-试用区-收银台；分工：导购负责介绍，店长负责协调，兼职负责引导与补货。");
            s7.setTags("活动,SOP,门店");
            s7.setAuthor("运营同学");
            s7.setSource("线下活动方案");
            s7.setAttachmentUrl("https://example.com/store/event-sop.docx");
            s7.setCategoryCode("sales");
            s7.setPublishTime(new java.util.Date());
            s7.setStatus("0");
            s7.setIsFeatured("1");
            samples.add(s7);

            KnowledgeImportDTO s8 = new KnowledgeImportDTO();
            s8.setTitle("会员关怀：节假日短信模板合集");
            s8.setSummary("覆盖春节、618、双11等节点");
            s8.setContent("示例：亲爱的会员，天气逐渐干燥，A产品助你水润过冬。今日下单享满299减60，限时24小时。");
            s8.setTags("会员,短信模板,节日");
            s8.setAuthor("CRM运营");
            s8.setSource("短信运营手册");
            s8.setAttachmentUrl("https://example.com/crm/sms-templates.zip");
            s8.setCategoryCode("member");
            s8.setPublishTime(new java.util.Date());
            s8.setStatus("0");
            s8.setIsFeatured("0");
            samples.add(s8);

            KnowledgeImportDTO s9 = new KnowledgeImportDTO();
            s9.setTitle("FAQ：如何辨别真假货？");
            s9.setSummary("包装防伪与渠道识别");
            s9.setContent("建议：通过官方防伪码查询；避免通过未授权渠道购买；留意包装印刷质量与封口完整性。");
            s9.setTags("FAQ,防伪,渠道");
            s9.setAuthor("客服团队");
            s9.setSource("客服知识库");
            s9.setAttachmentUrl("https://example.com/faq/anti-counterfeit.png");
            s9.setCategoryCode("faq");
            s9.setPublishTime(new java.util.Date());
            s9.setStatus("0");
            s9.setIsFeatured("0");
            samples.add(s9);

            KnowledgeImportDTO s10 = new KnowledgeImportDTO();
            s10.setTitle("竞品监测：品牌Y社媒口碑月报（2025-10）");
            s10.setSummary("抖音与小红书平台走势");
            s10.setContent("抖音：KOL带货集中在护肤套装，GMV增长12%；小红书：测评类帖子热度提升，核心关键词为‘敏感肌修护’。");
            s10.setTags("竞品,口碑,社媒");
            s10.setAuthor("市场研究员");
            s10.setSource("社媒监测报告");
            s10.setAttachmentUrl("https://example.com/competitor/y-social-report.pdf");
            s10.setCategoryCode("competitor");
            s10.setPublishTime(new java.util.Date());
            s10.setStatus("0");
            s10.setIsFeatured("0");
            samples.add(s10);

            // 显式设置下载文件名，避免浏览器使用URL末尾“template”作为文件名
            FileUtils.setAttachmentResponseHeader(response, "知识库导入模板.xlsx");
            util.exportExcel(response, samples, "知识库导入数据", "知识库导入模板");
            logger.info("下载知识库Excel导入模板成功");
        } catch (Exception e) {
            logger.error("下载知识库Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 导入知识库Excel数据
     */
    @PostMapping("/import")
    @ApiOperation("导入知识库Excel数据")
    public Result<Map<String, Object>> importKnowledge(@RequestParam("file") MultipartFile file,
                                                       @RequestParam(value = "updateSupport", required = false, defaultValue = "false") boolean updateSupport,
                                                       @RequestParam(value = "defaultCategory", required = false, defaultValue = "") String defaultCategory) {
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls"))) {
                return Result.error("请上传Excel文件（.xlsx或.xls）");
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                return Result.error("文件大小不能超过10MB");
            }

            ExcelUtil<KnowledgeImportDTO> util = new ExcelUtil<>(KnowledgeImportDTO.class);
            // 跳过模板的首行标题行，使用第2行作为表头进行字段映射
            java.util.List<KnowledgeImportDTO> dataList = util.importExcel(file.getInputStream(), 1);
            if (dataList == null) {
                dataList = java.util.Collections.emptyList();
            }
            logger.info("知识库Excel读取到 {} 条记录", dataList.size());

            Map<String, Object> result = knowledgeBaseService.importKnowledgeExcel(dataList, updateSupport, defaultCategory);
            return Result.success("导入完成", result);
        } catch (Exception e) {
            logger.error("导入知识库Excel数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }


}