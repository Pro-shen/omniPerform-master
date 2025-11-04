package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import com.omniperform.system.domain.SmartOperationAlert;
import com.omniperform.system.domain.MemberProfileAnalysis;
import com.omniperform.system.service.ISmartOperationOverviewService;
import com.omniperform.system.service.ISmartOperationAlertService;
import com.omniperform.system.service.ISmartMarketingTaskService;
import com.omniperform.system.service.IMemberProfileAnalysisService;
import com.omniperform.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

/**
 * 智能运营控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/smart-operation")
@CrossOrigin(origins = "*")
@Api(tags = "智能运营管理")
public class SmartOperationController {

    private static final Logger log = LoggerFactory.getLogger(SmartOperationController.class);

    @Autowired
    private ISmartOperationOverviewService smartOperationOverviewService;

    @Autowired
    private ISmartOperationAlertService smartOperationAlertService;

    @Autowired
    private ISmartMarketingTaskService smartMarketingTaskService;

    @Autowired
    private IMemberProfileAnalysisService memberProfileAnalysisService;

    /**
     * 获取智能运营概览数据
     */
    @GetMapping("/overview")
    @ApiOperation("获取智能运营概览数据")
    public Result getSmartOperationOverview(@RequestParam(defaultValue = "") String month) {
        try {
            // 使用Service层获取概览数据，支持月份参数
            Map<String, Object> overview = smartOperationOverviewService.getSmartOperationOverview("all", month);
            
            log.info("获取智能运营概览数据成功，月份: {}", month.isEmpty() ? "当前月份" : month);
            return Result.success("获取智能运营概览数据成功", overview);
        } catch (Exception e) {
            log.error("获取智能运营概览数据失败: {}", e.getMessage(), e);
            return Result.error("获取智能运营概览数据失败");
        }
    }

    /**
     * 获取实时监控预警数据
     */
    @GetMapping("/alerts")
    @ApiOperation("获取实时监控预警数据")
    public Result getAlerts(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "待处理") String status,
                           @RequestParam(defaultValue = "all") String region,
                           @RequestParam(required = false) String month) {
        try {
            // 暂时使用不带月份参数的方法，避免方法重载问题
            Map<String, Object> result = smartOperationAlertService.getAlertsDataWithPagination(region, status, page, size);
            
            log.info("获取实时监控预警数据成功，页码: {}, 大小: {}, 状态: {}, 区域: {}, 月份: {}", page, size, status, region, month);
            return Result.success("获取实时监控预警数据成功", result);
        } catch (Exception e) {
            log.error("获取实时监控预警数据失败: {}", e.getMessage(), e);
            return Result.error("获取实时监控预警数据失败");
        }
    }

    /**
     * 测试查询所有预警数据
     */
    /*
    @GetMapping("/alerts/test")
    @ApiOperation("测试查询所有预警数据")
    public Result testGetAllAlerts() {
        try {
            List<SmartOperationAlert> alerts = smartOperationAlertService.selectAllSmartOperationAlerts();
            log.info("测试查询所有预警数据成功，数量: {}", alerts.size());
            return Result.success("测试查询所有预警数据成功", alerts);
        } catch (Exception e) {
            log.error("测试查询所有预警数据失败: {}", e.getMessage(), e);
            return Result.error("测试查询所有预警数据失败");
        }
    }
    */

    /**
     * 获取AI推荐任务数据
     */
    @GetMapping("/ai-tasks")
    @ApiOperation("获取AI推荐任务数据")
    public Result getAiTasks(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String month) {
        try {
            // 使用Service层获取AI任务数据
            Map<String, Object> result;
            if (month != null && !month.trim().isEmpty()) {
                result = smartMarketingTaskService.getAiTasksData("待执行", "AI推荐", month);
            } else {
                result = smartMarketingTaskService.getAiTasksData("待执行", "AI推荐");
            }
            
            log.info("获取AI推荐任务数据成功，页码: {}, 大小: {}, 月份: {}", page, size, month);
            return Result.success("获取AI推荐任务数据成功", result);
        } catch (Exception e) {
            log.error("获取AI推荐任务数据失败: {}", e.getMessage(), e);
            return Result.error("获取AI推荐任务数据失败");
        }
    }

    /**
     * 获取AI推荐营销任务数据
     */
    @GetMapping("/marketing-tasks")
    @ApiOperation("获取AI推荐营销任务数据")
    public Result getMarketingTasks(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String month) {
        try {
            // 使用Service层获取营销任务数据，不过滤任务类型，返回所有待执行的任务
            Map<String, Object> result;
            if (month != null && !month.trim().isEmpty()) {
                result = smartMarketingTaskService.getMarketingTasksData("待执行", null, month);
            } else {
                result = smartMarketingTaskService.getMarketingTasksData("待执行", null);
            }
            
            log.info("获取AI推荐营销任务数据成功，页码: {}, 大小: {}, 月份: {}", page, size, month);
            return Result.success("获取AI推荐营销任务数据成功", result);
        } catch (Exception e) {
            log.error("获取AI推荐营销任务数据失败: {}", e.getMessage(), e);
            return Result.error("获取AI推荐营销任务数据失败");
        }
    }

    /**
     * 获取会员画像分析数据
     */
    @GetMapping("/member-profile")
    @ApiOperation("获取会员画像分析数据")
    public Result getMemberProfile(@RequestParam(required = false) String month,
                                   @RequestParam(required = false) String profileType,
                                   @RequestParam(required = false) String regionCode) {
        try {
            Map<String, Object> profileData;
            if (month != null && !month.trim().isEmpty()) {
                profileData = memberProfileAnalysisService.getMemberProfileData(profileType, regionCode, month);
            } else {
                profileData = memberProfileAnalysisService.getMemberProfileData(profileType, regionCode);
            }
            
            return Result.success("获取会员画像分析数据成功", profileData);
        } catch (Exception e) {
            log.error("获取会员画像分析数据失败: {}", e.getMessage(), e);
            return Result.error("获取会员画像分析数据失败");
        }
    }

    /**
     * 测试会员画像分析数据查询
     */
    @GetMapping("/member-profile-test")
    @ApiOperation("测试会员画像分析数据查询")
    public Result getMemberProfileTest() {
        try {
            // 直接查询最新的会员画像分析数据，不传任何参数
            List<MemberProfileAnalysis> profileList = memberProfileAnalysisService.selectLatestMemberProfileAnalysis(null, null);
            
            Map<String, Object> result = new HashMap<>();
            result.put("profileList", profileList);
            result.put("totalCount", profileList.size());
            
            log.info("测试获取会员画像分析数据成功，共{}条记录", profileList.size());
            return Result.success("测试获取会员画像分析数据成功", result);
        } catch (Exception e) {
            log.error("测试获取会员画像分析数据失败: {}", e.getMessage(), e);
            return Result.error("测试获取会员画像分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 下载会员画像Excel导入模板
     */
    @GetMapping("/member-profile/import/template")
    @ApiOperation("下载会员画像Excel导入模板")
    public void downloadMemberProfileImportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<MemberProfileAnalysis> util = new ExcelUtil<>(MemberProfileAnalysis.class);
            util.importTemplateExcel(response, "会员画像数据", "会员画像导入模板");
        } catch (Exception e) {
            log.error("下载会员画像Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 导入会员画像Excel数据
     */
    @PostMapping("/member-profile/import")
    @ApiOperation("导入会员画像Excel数据")
    public Result<Map<String, Object>> importMemberProfile(@RequestParam("file") MultipartFile file,
                                                           @RequestParam(value = "updateSupport", required = false, defaultValue = "false") boolean updateSupport,
                                                           @RequestParam(value = "defaultRegion", required = false, defaultValue = "") String defaultRegion) {
        try {
            log.info("开始导入会员画像数据，文件名: {}, 大小: {} bytes, updateSupport={}, defaultRegion={}",
                    file.getOriginalFilename(), file.getSize(), updateSupport, defaultRegion);

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls"))) {
                return Result.error("请上传Excel文件（.xlsx或.xls）");
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                return Result.error("文件大小不能超过10MB");
            }

            ExcelUtil<MemberProfileAnalysis> util = new ExcelUtil<>(MemberProfileAnalysis.class);
            List<MemberProfileAnalysis> dataList = util.importExcel(file.getInputStream());
            if (dataList == null) {
                dataList = Collections.emptyList();
            }
            log.info("Excel共读取到 {} 条记录", dataList.size());

            Map<String, Object> result = memberProfileAnalysisService.importMemberProfileExcel(dataList, updateSupport, defaultRegion);

            int successCount = (int) result.getOrDefault("successCount", 0);
            int failCount = (int) result.getOrDefault("failCount", 0);
            String message;
            if (failCount == 0) {
                message = String.format("导入成功！共处理 %d 条数据，全部成功", successCount);
            } else if (successCount == 0) {
                message = String.format("导入失败！共 %d 条数据，全部失败", failCount);
            } else {
                message = String.format("导入完成！成功: %d 条，失败: %d 条", successCount, failCount);
            }

            return Result.success(message, result);
        } catch (Exception e) {
            log.error("导入会员画像数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 获取最佳触达时间数据
     */
    @GetMapping("/best-touch-time")
    @ApiOperation("获取最佳触达时间数据")
    public Result getBestTouchTime() {
        try {
            Map<String, Object> touchTimeData = new HashMap<>();
            
            // 最佳触达时间柱状图数据
            List<String> timeSlots = Arrays.asList("6-8点", "8-10点", "10-12点", "12-14点", "14-16点", "16-18点", "18-20点", "20-22点", "22-24点");
            List<Integer> responseRates = Arrays.asList(15, 22, 35, 28, 25, 30, 42, 65, 38);
            
            touchTimeData.put("timeSlots", timeSlots);
            touchTimeData.put("responseRates", responseRates);
            
            log.info("获取最佳触达时间数据成功");
            return Result.success("获取最佳触达时间数据成功", touchTimeData);
        } catch (Exception e) {
            log.error("获取最佳触达时间数据失败: {}", e.getMessage(), e);
            return Result.error("获取最佳触达时间数据失败");
        }
    }

    /**
     * 获取数据闭环优化效果数据
     */
    @GetMapping("/optimization-effect")
    @ApiOperation("获取数据闭环优化效果数据")
    public Result getOptimizationEffect() {
        try {
            Map<String, Object> effectData = new HashMap<>();
            
            // 数据闭环优化效果折线图数据
            List<String> months = Arrays.asList("第1周", "第2周", "第3周", "第4周", "第5周", "第6周", "第7周", "第8周", "第9周", "第10周", "第11周", "第12周");
            List<Double> motExecutionRates = Arrays.asList(62.5, 63.2, 64.1, 65.8, 67.2, 68.5, 70.1, 71.5, 73.2, 74.8, 76.5, 78.3);
            List<Double> memberActivityRates = Arrays.asList(55.2, 56.5, 57.1, 58.3, 59.2, 60.5, 61.8, 62.5, 63.2, 64.1, 64.8, 65.2);
            List<Double> repurchaseRates = Arrays.asList(32.5, 33.1, 33.8, 34.2, 34.9, 35.5, 36.2, 36.8, 37.5, 38.2, 38.9, 39.5);
            
            effectData.put("months", months);
            effectData.put("motExecutionRates", motExecutionRates);
            effectData.put("memberActivityRates", memberActivityRates);
            effectData.put("repurchaseRates", repurchaseRates);
            
            log.info("获取数据闭环优化效果数据成功");
            return Result.success("获取数据闭环优化效果数据成功", effectData);
        } catch (Exception e) {
            log.error("获取数据闭环优化效果数据失败: {}", e.getMessage(), e);
            return Result.error("获取数据闭环优化效果数据失败");
        }
    }

    /**
     * 获取智能运营技术架构数据
     */
    @GetMapping("/tech-architecture")
    @ApiOperation("获取智能运营技术架构数据")
    public Result getTechArchitecture() {
        try {
            Map<String, Object> architectureData = new HashMap<>();
            
            // 技术架构桑基图数据 - 与前端完全匹配
            List<Map<String, Object>> data = new ArrayList<>();
            List<Map<String, Object>> links = new ArrayList<>();
            
            // 节点数据
            String[] nodeNames = {
                "数据源", "数据采集", "数据仓库", "数据处理", "分析引擎", "机器学习", "规则引擎",
                "会员管理", "营销自动化", "预警监控", "决策支持", "企业微信", "短信系统", "APP推送", "邮件系统"
            };
            
            for (String nodeName : nodeNames) {
                Map<String, Object> node = new HashMap<>();
                node.put("name", nodeName);
                data.add(node);
            }
            
            // 连接数据 - 与前端桑基图完全匹配
            Object[][] linkData = {
                {"数据源", "数据采集", 1},
                {"数据采集", "数据仓库", 1},
                {"数据仓库", "数据处理", 1},
                {"数据处理", "分析引擎", 0.7},
                {"数据处理", "机器学习", 0.5},
                {"数据处理", "规则引擎", 0.3},
                {"分析引擎", "会员管理", 0.3},
                {"分析引擎", "营销自动化", 0.2},
                {"分析引擎", "预警监控", 0.2},
                {"分析引擎", "决策支持", 0.2},
                {"机器学习", "会员管理", 0.1},
                {"机器学习", "营销自动化", 0.3},
                {"机器学习", "决策支持", 0.1},
                {"规则引擎", "预警监控", 0.2},
                {"规则引擎", "营销自动化", 0.1},
                {"会员管理", "企业微信", 0.1},
                {"会员管理", "短信系统", 0.1},
                {"会员管理", "APP推送", 0.1},
                {"营销自动化", "企业微信", 0.2},
                {"营销自动化", "短信系统", 0.1},
                {"营销自动化", "APP推送", 0.1},
                {"营销自动化", "邮件系统", 0.1},
                {"预警监控", "企业微信", 0.1},
                {"预警监控", "短信系统", 0.1},
                {"决策支持", "企业微信", 0.1},
                {"决策支持", "邮件系统", 0.1}
            };
            
            for (Object[] link : linkData) {
                Map<String, Object> linkMap = new HashMap<>();
                linkMap.put("source", link[0]);
                linkMap.put("target", link[1]);
                linkMap.put("value", link[2]);
                links.add(linkMap);
            }
            
            architectureData.put("data", data);
            architectureData.put("links", links);
            
            return Result.success("获取智能运营技术架构数据成功", architectureData);
        } catch (Exception e) {
            log.error("获取智能运营技术架构数据失败: {}", e.getMessage(), e);
            return Result.error("获取智能运营技术架构数据失败");
        }
    }

    /**
     * 获取精英导购最佳实践数据
     */
    @GetMapping("/best-practices")
    @ApiOperation("获取精英导购最佳实践数据")
    public Result getBestPractices() {
        try {
            List<Map<String, Object>> practices = new ArrayList<>();
            
            String[] guideNames = {"张小丽", "李明华", "王雅琪", "赵晓燕", "钱志强"};
            String[] regions = {"华东区", "华南区", "华中区", "华北区", "西南区"};
            String[] practices_desc = {
                "建立客户档案，定期跟进，提供个性化服务",
                "利用社群营销，增强客户粘性和转介绍",
                "专业知识分享，建立专家形象和信任度",
                "及时响应客户需求，提供优质售后服务",
                "数据驱动决策，精准营销和客户管理"
            };
            
            for (int i = 0; i < guideNames.length; i++) {
                Map<String, Object> practice = new HashMap<>();
                practice.put("guideName", guideNames[i]);
                practice.put("region", regions[i]);
                practice.put("practice", practices_desc[i]);
                practice.put("performance", 90 + i * 2);
                practice.put("improvement", "+" + (15 + i * 3) + "%");
                practices.add(practice);
            }
            
            return Result.success("获取精英导购最佳实践数据成功", practices);
        } catch (Exception e) {
            log.error("获取精英导购最佳实践数据失败: {}", e.getMessage(), e);
            return Result.error("获取精英导购最佳实践数据失败");
        }
    }

    /**
     * 获取区域差异化策略数据
     */
    @GetMapping("/regional-strategies")
    @ApiOperation("获取区域差异化策略数据")
    public Result getRegionalStrategies() {
        try {
            List<Map<String, Object>> strategies = new ArrayList<>();
            
            String[] regions = {"华东区", "华南区", "华中区", "华北区", "西南区"};
            String[] strategies_desc = {
                "重点发展线上渠道，提升数字化体验",
                "加强社群运营，利用口碑传播优势",
                "注重性价比宣传，满足理性消费需求",
                "强化品牌形象，提升高端产品占比",
                "深耕下沉市场，扩大品牌覆盖面"
            };
            
            String[] focuses = {"数字化", "社群化", "性价比", "品牌化", "下沉化"};
            
            for (int i = 0; i < regions.length; i++) {
                Map<String, Object> strategy = new HashMap<>();
                strategy.put("region", regions[i]);
                strategy.put("strategy", strategies_desc[i]);
                strategy.put("focus", focuses[i]);
                strategy.put("progress", 70 + i * 5);
                strategy.put("expectedROI", (120 + i * 10) + "%");
                strategies.add(strategy);
            }
            
            return Result.success("获取区域差异化策略数据成功", strategies);
        } catch (Exception e) {
            log.error("获取区域差异化策略数据失败: {}", e.getMessage(), e);
            return Result.error("获取区域差异化策略数据失败");
        }
    }

    /**
     * 生成推荐内容
     */
    @PostMapping("/generate-content")
    @ApiOperation("生成推荐内容")
    public Result generateContent(@RequestBody Map<String, Object> request) {
        try {
            String memberProfile = (String) request.get("memberProfile");
            String marketingGoal = (String) request.get("marketingGoal");
            List<String> channels = (List<String>) request.get("channels");
            
            log.info("收到生成内容请求 - 会员画像: {}, 营销目标: {}, 渠道: {}", memberProfile, marketingGoal, channels);
            
            // 模拟内容生成逻辑
            Map<String, Object> generatedContent = new HashMap<>();
            
            // 根据会员画像和营销目标生成内容
            String contentTitle = generateContentTitle(memberProfile, marketingGoal);
            String contentBody = generateContentBody(memberProfile, marketingGoal);
            List<String> channelStrategies = generateChannelStrategies(channels);
            
            generatedContent.put("title", contentTitle);
            generatedContent.put("content", contentBody);
            generatedContent.put("channels", channelStrategies);
            generatedContent.put("generatedAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            log.info("生成推荐内容成功");
            return Result.success("生成推荐内容成功", generatedContent);
        } catch (Exception e) {
            log.error("生成推荐内容失败: {}", e.getMessage(), e);
            return Result.error("生成推荐内容失败: " + e.getMessage());
        }
    }

    /**
     * 生成内容标题
     */
    private String generateContentTitle(String memberProfile, String marketingGoal) {
        Map<String, String> titleTemplates = new HashMap<>();
        titleTemplates.put("品质追求型_提升复购率", "精选优质产品，为您的品质生活加分");
        titleTemplates.put("品质追求型_增加客单价", "臻选高端系列，品味生活的每一个细节");
        titleTemplates.put("品质追求型_促进新品试用", "全新高端产品首发，邀您抢先体验");
        titleTemplates.put("成长探索型_提升复购率", "发现更多可能，让成长之路更精彩");
        titleTemplates.put("成长探索型_增加客单价", "升级您的选择，探索更多优质体验");
        titleTemplates.put("成长探索型_促进新品试用", "新品尝鲜计划，与您一起探索未知");
        titleTemplates.put("价格敏感型_提升复购率", "超值回购福利，让实惠成为习惯");
        titleTemplates.put("价格敏感型_增加客单价", "组合优惠套装，更多选择更多实惠");
        titleTemplates.put("价格敏感型_促进新品试用", "新品特价体验，好产品不贵才是真的好");
        
        String key = memberProfile + "_" + marketingGoal;
        return titleTemplates.getOrDefault(key, "专属推荐内容，为您精心定制");
    }

    /**
     * 生成内容正文
     */
    private String generateContentBody(String memberProfile, String marketingGoal) {
        StringBuilder content = new StringBuilder();
        
        // 根据会员画像定制开头
        switch (memberProfile) {
            case "品质追求型":
                content.append("尊敬的客户，我们为您精心准备了专属推荐。");
                break;
            case "成长探索型":
                content.append("充满活力的探索者，我们为您准备了全新的发现之旅。");
                break;
            case "价格敏感型":
                content.append("精明的消费者，我们为您带来超值的优质选择。");
                break;
            default:
                content.append("尊敬的客户，我们为您精心准备了专属推荐。");
        }
        
        // 根据营销目标定制内容
        switch (marketingGoal) {
            case "提升复购率":
                content.append("基于您的购买历史和偏好，我们为您推荐以下产品，让您的每次选择都物超所值。");
                break;
            case "增加客单价":
                content.append("为了让您获得更完整的体验，我们特别推荐以下组合套装和升级产品。");
                break;
            case "促进新品试用":
                content.append("我们的最新产品已经上市，特邀您成为首批体验者，享受专属优惠。");
                break;
            default:
                content.append("我们为您准备了精选推荐，希望能为您带来更好的体验。");
        }
        
        return content.toString();
    }

    /**
     * 生成渠道策略
     */
    private List<String> generateChannelStrategies(List<String> channels) {
        List<String> strategies = new ArrayList<>();
        
        if (channels != null) {
            for (String channel : channels) {
                switch (channel) {
                    case "微信":
                        strategies.add("微信：通过个人号一对一推送，配合朋友圈展示，提升信任度");
                        break;
                    case "短信":
                        strategies.add("短信：简洁明了的产品信息和优惠提醒，包含直接购买链接");
                        break;
                    case "APP":
                        strategies.add("APP：利用推送通知和个性化首页推荐，提升用户活跃度");
                        break;
                    case "邮件":
                        strategies.add("邮件：详细的产品介绍和使用指南，适合深度内容传播");
                        break;
                }
            }
        }
        
        return strategies;
    }
}