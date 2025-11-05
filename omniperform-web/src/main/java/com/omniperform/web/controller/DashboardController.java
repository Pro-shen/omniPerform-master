package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import com.omniperform.system.service.*;
import com.omniperform.system.domain.*;
import com.omniperform.system.domain.DashboardMemberSource;
import com.omniperform.web.service.IGuidePerformanceService;
import com.omniperform.web.service.IMotTaskService;
import com.omniperform.web.domain.GuidePerformance;
import com.omniperform.web.domain.MotTask;
import com.omniperform.common.utils.poi.ExcelUtil;
import com.omniperform.common.core.domain.AjaxResult;
import com.omniperform.web.domain.OverviewKpiImportRow;
import com.omniperform.system.service.IDashboardOverviewKpiService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.*;
import java.util.Date;

/**
 * 仪表盘控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private IDashboardMemberOverviewService memberOverviewService;
    
    @Autowired
    private IDashboardMemberGrowthService memberGrowthService;
    
    @Autowired
    private IDashboardMemberStageService memberStageService;
    
    @Autowired
    private IDashboardRegionPerformanceService regionPerformanceService;
    
    @Autowired
    private IDashboardProductSalesService productSalesService;
    
    @Autowired
    private IDashboardMemberSourceService memberSourceService;
    
    @Autowired
    private IGuidePerformanceService guidePerformanceService;

    @Autowired
    private IMotTaskService motTaskService;

    @Autowired
    private IDashboardOverviewKpiService dashboardOverviewKpiService;

    /**
     * 获取仪表盘概览数据
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview(@RequestParam(required = false) String month) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            log.info("[Dashboard] /overview 请求, month={}, thread={}", month, Thread.currentThread().getName());
            // 获取会员概览数据
            DashboardMemberOverview queryParam = new DashboardMemberOverview();
            if (month != null && !month.isEmpty()) {
                queryParam.setDataMonth(month);
            }
            List<DashboardMemberOverview> overviewList = memberOverviewService.selectDashboardMemberOverviewList(queryParam);
            log.info("[Dashboard] /overview 查询结果条数: {}", overviewList != null ? overviewList.size() : 0);
            
            // 核心指标
            Map<String, Object> metrics = new HashMap<>();
            if (!overviewList.isEmpty()) {
                DashboardMemberOverview latest = overviewList.get(0);
                log.info("[Dashboard] /overview 使用最新记录 data_month={}, totalMembers={}, newMembers={}, activeMembers={}, growthRate={}",
                        latest.getDataMonth(), latest.getTotalMembers(), latest.getNewMembers(), latest.getActiveMembers(), latest.getMemberGrowthRate());
                metrics.put("totalMembers", latest.getTotalMembers());
                metrics.put("newMembersToday", latest.getNewMembers());
                metrics.put("activeMembers", latest.getActiveMembers());
                metrics.put("memberGrowthRate", latest.getMemberGrowthRate());
            } else {
                log.warn("[Dashboard] /overview 未找到数据，month={}，将返回空指标", month);
            }
            
            // 趋势数据（最近7天）
            List<Integer> dailyNewMembers = new ArrayList<>();
            List<Double> dailyMemberGrowthRate = new ArrayList<>();
            
            for (int i = Math.min(7, overviewList.size()) - 1; i >= 0; i--) {
                DashboardMemberOverview overview = overviewList.get(i);
                dailyNewMembers.add(overview.getNewMembers());
                dailyMemberGrowthRate.add(overview.getMemberGrowthRate() != null ? overview.getMemberGrowthRate().doubleValue() : 0.0);
            }
            log.info("[Dashboard] /overview 返回趋势数据: dailyNewMembers.size={}, dailyMemberGrowthRate.size={}", dailyNewMembers.size(), dailyMemberGrowthRate.size());
            
            data.put("metrics", metrics);
            data.put("dailyNewMembers", dailyNewMembers);
            data.put("dailyMemberGrowthRate", dailyMemberGrowthRate);
            
            return Result.success("获取概览数据成功", data);
        } catch (Exception e) {
            log.error("获取概览数据失败: {}", e.getMessage(), e);
            return Result.error("获取概览数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取核心指标数据
     */
    @GetMapping("/metrics")
    public Result<Map<String, Object>> getMetrics(@RequestParam(required = false) String month) {
        Map<String, Object> data = new HashMap<>();

        try {
            log.info("[getMetrics] 请求参数 month={}", month);
            // 从独立首页KPI表读取（按月份）
            if (month != null && !month.isEmpty()) {
                List<DashboardOverviewKpi> list = dashboardOverviewKpiService.listByMonth(month, null);
                log.info("[getMetrics] dashboard_overview_kpi 查询结果条数: {} (month={}, region=null)",
                         (list == null ? 0 : list.size()), month);
                if (list != null && !list.isEmpty()) {
                    DashboardOverviewKpi k = list.get(0);
                    log.info("[getMetrics] 命中KPI记录 id={}, dataMonth={}, regionCode={}",
                             k.getId(), k.getDataMonth(), k.getRegionCode());
                    data.put("newMembers", k.getNewMembers());
                    data.put("newMembersGrowth", toRate(k.getNewMembersGrowth()));
                    data.put("repeatPurchaseRate", toRate(k.getRepeatPurchaseRate()));
                    data.put("repeatPurchaseGrowth", toRate(k.getRepeatPurchaseGrowth()));
                    data.put("motSuccessRate", toRate(k.getMotSuccessRate()));
                    data.put("motSuccessGrowth", toRate(k.getMotSuccessGrowth()));
                    data.put("memberActivityRate", toRate(k.getMemberActivityRate()));
                    data.put("memberActivityGrowth", toRate(k.getMemberActivityGrowth()));
                } else {
                    log.warn("[getMetrics] 未在 dashboard_overview_kpi 查到该月份记录: month={}, 可能原因: 1) 数据未导入; 2) 月份格式不为YYYY-MM; 3) 全国数据region_code非NULL;", month);
                }
            }

            // 兼容原先的实时指标字段
            DashboardMemberOverview queryParam = new DashboardMemberOverview();
            if (month != null && !month.isEmpty()) {
                queryParam.setDataMonth(month);
            }
            List<DashboardMemberOverview> overviewList = memberOverviewService.selectDashboardMemberOverviewList(queryParam);
            log.info("[getMetrics] 兼容查询 member_overview 结果条数: {} (month={})",
                     (overviewList == null ? 0 : overviewList.size()), month);
            if (!overviewList.isEmpty()) {
                DashboardMemberOverview latest = overviewList.get(0);
                data.put("todayNewMembers", latest.getNewMembers());
                data.put("todayActiveMot", latest.getActiveMembers());
            } else {
                log.info("[getMetrics] 兼容查询 member_overview 无记录 (month={})", month);
            }

            log.info("[getMetrics] 返回数据键: {}", data.keySet());
            return Result.success("获取核心指标成功", data);
        } catch (Exception e) {
            log.error("获取核心指标失败: {}", e.getMessage(), e);
            return Result.error("获取核心指标失败: " + e.getMessage());
        }
    }

    private Integer toInt(BigDecimal v) {
        if (v == null) return null;
        try { return v.setScale(0, java.math.RoundingMode.HALF_UP).intValue(); } catch (Exception ignore) { return null; }
    }

    private String toRate(BigDecimal v) {
        if (v == null) return null;
        try { return v.setScale(1, java.math.RoundingMode.HALF_UP).toPlainString(); } catch (Exception ignore) { return null; }
    }

    /**
     * 获取会员增长趋势
     */
    @GetMapping("/member-growth-trend")
    public Result<Map<String, Object>> getMemberGrowthTrend(@RequestParam(defaultValue = "6") int months) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            List<String> monthLabels = new ArrayList<>();
            List<Integer> newMembers = new ArrayList<>();
            List<Integer> totalMembers = new ArrayList<>();
            List<Double> growthRate = new ArrayList<>();
            List<Integer> repeatMembers = new ArrayList<>();
            List<Double> repeatRate = new ArrayList<>();
            
            // 获取数据库中所有有数据的月份，按时间顺序排列
            List<DashboardMemberGrowth> allGrowthData = memberGrowthService.selectDashboardMemberGrowthList(new DashboardMemberGrowth());
            
            // 按月份排序
            allGrowthData.sort((a, b) -> a.getDataMonth().compareTo(b.getDataMonth()));
            
            for (DashboardMemberGrowth growth : allGrowthData) {
                // 将yyyy-MM格式转换为M月格式
                String[] parts = growth.getDataMonth().split("-");
                int monthNum = Integer.parseInt(parts[1]);
                monthLabels.add(monthNum + "月");
                
                newMembers.add(growth.getNewMembers());
                totalMembers.add(growth.getTotalMembers());
                growthRate.add(growth.getGrowthRate() != null ? growth.getGrowthRate().doubleValue() : 0.0);
                
                // 计算复购会员数量（基于新会员数量的固定比例，根据月份确定）
                int newMemberCount = growth.getNewMembers();
                // 使用月份作为种子来确保数据的一致性
                int monthSeed = Integer.parseInt(parts[1]); // 使用月份数字作为种子
                double ratio = 0.30 + (monthSeed % 3) * 0.05; // 30%, 35%, 40% 循环
                int repeatMemberCount = (int)(newMemberCount * ratio);
                repeatMembers.add(repeatMemberCount);
                
                // 计算复购率
                double rate = newMemberCount > 0 ? (double) repeatMemberCount / newMemberCount * 100 : 0.0;
                repeatRate.add(Math.round(rate * 10.0) / 10.0);
            }
            
            data.put("months", monthLabels);
            data.put("newMembers", newMembers);
            data.put("totalMembers", totalMembers);
            data.put("growthRate", growthRate);
            data.put("repeatMembers", repeatMembers);
            data.put("repeatRate", repeatRate);
            
            return Result.success("获取会员增长趋势成功", data);
        } catch (Exception e) {
            return Result.error("获取会员增长趋势失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员阶段分布
     */
    @GetMapping("/member-stage-distribution")
    public Result<Map<String, Object>> getMemberStageDistribution(@RequestParam(required = false) String month) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // 获取会员阶段分布数据
            String queryMonth = (month != null && !month.isEmpty()) ? month : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<DashboardMemberStage> stageList = memberStageService.selectByMonthRange(queryMonth, queryMonth);
            
            Map<String, Integer> stageData = new HashMap<>();
            Map<String, String> labels = new HashMap<>();
            
            // 初始化标签
            labels.put("stage0", "0阶段(孕妇)");
            labels.put("stage1", "1阶段(0-6个月)");
            labels.put("stage2", "2阶段(6-12个月)");
            labels.put("stage3", "3阶段(1-2岁)");
            labels.put("stage4", "4阶段(2-3岁)");
            
            // 初始化数据
            stageData.put("stage0", 0);
            stageData.put("stage1", 0);
            stageData.put("stage2", 0);
            stageData.put("stage3", 0);
            stageData.put("stage4", 0);
            
            // 填充真实数据
            for (DashboardMemberStage stage : stageList) {
                // 根据阶段名称映射到对应的key
                String stageName = stage.getStageName();
                String stageKey = null;
                if ("0阶段(孕妇)".equals(stageName)) {
                    stageKey = "stage0";
                } else if ("1阶段(0-6个月)".equals(stageName)) {
                    stageKey = "stage1";
                } else if ("2阶段(6-12个月)".equals(stageName)) {
                    stageKey = "stage2";
                } else if ("3阶段(1-2岁)".equals(stageName)) {
                    stageKey = "stage3";
                } else if ("4阶段(2-3岁)".equals(stageName)) {
                    stageKey = "stage4";
                }
                
                if (stageKey != null && stageData.containsKey(stageKey)) {
                    stageData.put(stageKey, stage.getMemberCount());
                }
            }
            
            data.put("data", stageData);
            data.put("labels", labels);
            data.put("total", stageData.values().stream().mapToInt(Integer::intValue).sum());
            
            return Result.success("获取会员阶段分布成功", data);
        } catch (Exception e) {
            return Result.error("获取会员阶段分布失败: " + e.getMessage());
        }
    }

    /**
     * 获取区域绩效对比
     */
    @GetMapping("/region-performance")
    public Result<Map<String, Object>> getRegionPerformance(@RequestParam(required = false) String month) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // 获取区域绩效数据
            String queryMonth = (month != null && !month.isEmpty()) ? month : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<DashboardRegionPerformance> performanceList = regionPerformanceService.selectByDataMonth(queryMonth);
            
            List<String> regions = new ArrayList<>();
            List<Integer> memberCount = new ArrayList<>();
            List<Double> salesAmount = new ArrayList<>();
            List<Double> performanceScore = new ArrayList<>();
            
            for (DashboardRegionPerformance performance : performanceList) {
                regions.add(performance.getRegionName());
                memberCount.add(performance.getMemberCount());
                salesAmount.add(performance.getSalesAmount() != null ? performance.getSalesAmount().doubleValue() : 0.0);
                performanceScore.add(performance.getPerformanceScore() != null ? performance.getPerformanceScore().doubleValue() : 0.0);
            }
            
            data.put("regions", regions);
            data.put("memberCount", memberCount);
            data.put("salesAmount", salesAmount);
            data.put("performanceScore", performanceScore);
            
            return Result.success("获取区域绩效对比成功", data);
        } catch (Exception e) {
            return Result.error("获取区域绩效对比失败: " + e.getMessage());
        }
    }

    /**
     * 获取导购绩效排行
     */
    @GetMapping("/guide-performance-ranking")
    public Result<List<Map<String, Object>>> getGuidePerformanceRanking(@RequestParam(required = false) String month) {
        List<Map<String, Object>> rankings = new ArrayList<>();
        
        try {
            // 获取导购绩效排行数据
            String queryMonth = (month != null && !month.isEmpty()) ? month : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<GuidePerformance> performanceList = guidePerformanceService.selectGuidePerformanceRanking(queryMonth, 10);
            
            for (int i = 0; i < performanceList.size(); i++) {
                GuidePerformance performance = performanceList.get(i);
                Map<String, Object> guide = new HashMap<>();
                guide.put("rank", i + 1);
                guide.put("name", performance.getGuideName() != null ? performance.getGuideName() : "导购" + (i + 1));
                guide.put("region", performance.getRegionName() != null ? performance.getRegionName() : "未知区域");
                guide.put("newMembers", performance.getNewMembers() != null ? performance.getNewMembers() : 0);
                guide.put("motCompletionRate", performance.getMotCompletionRate() != null ? performance.getMotCompletionRate().doubleValue() : 0.0);
                guide.put("customerSatisfaction", performance.getCustomerSatisfaction() != null ? performance.getCustomerSatisfaction().doubleValue() : 0.0);
                guide.put("sales", performance.getSalesAmount() != null ? performance.getSalesAmount().doubleValue() : 0.0);
                
                rankings.add(guide);
            }
            
            return Result.success("获取导购绩效排行成功", rankings);
        } catch (Exception e) {
            return Result.error("获取导购绩效排行失败: " + e.getMessage());
        }
    }

    /**
     * 获取待执行MOT任务
     */
    @GetMapping("/pending-mot-tasks")
    public Result<List<Map<String, Object>>> getPendingMotTasks(@RequestParam(required = false) String month) {
        List<Map<String, Object>> tasks = new ArrayList<>();
        
        try {
            // 获取待执行MOT任务数据
            String queryMonth = (month != null && !month.isEmpty()) ? month : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<MotTask> motTasks = motTaskService.selectPendingMotTasks(queryMonth, 10);
            
            for (MotTask motTask : motTasks) {
                Map<String, Object> task = new HashMap<>();
                task.put("id", motTask.getTaskId());
                task.put("memberName", motTask.getMemberName() != null ? motTask.getMemberName() : "未知会员");
                task.put("motType", motTask.getMotType() != null ? motTask.getMotType() : "未知类型");
                task.put("priority", motTask.getPriority() != null ? motTask.getPriority() : "中");
                task.put("dueDate", motTask.getDueDate() != null ? motTask.getDueDate().toString() : LocalDate.now().toString());
                task.put("guideName", motTask.getGuideName() != null ? motTask.getGuideName() : "未分配");
                task.put("status", motTask.getStatus() != null ? motTask.getStatus() : "待执行");
                task.put("description", motTask.getDescription() != null ? motTask.getDescription() : "");
                tasks.add(task);
            }
            
            return Result.success("获取待执行MOT任务成功", tasks);
        } catch (Exception e) {
            return Result.error("获取待执行MOT任务失败: " + e.getMessage());
        }
    }

    /**
     * 获取产品销售分析
     */
    @GetMapping("/product-sales-analysis")
    public Result<Map<String, Object>> getProductSalesAnalysis(@RequestParam(required = false) String month) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // 获取产品销售数据
            String queryMonth = (month != null && !month.isEmpty()) ? month : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<DashboardProductSales> salesList = productSalesService.selectByDataMonth(queryMonth);
            
            List<String> products = new ArrayList<>();
            List<Integer> salesQuantity = new ArrayList<>();
            List<Double> salesAmount = new ArrayList<>();
            List<Double> marketShare = new ArrayList<>();
            
            for (DashboardProductSales sales : salesList) {
                products.add(sales.getProductName());
                salesQuantity.add(sales.getSalesQuantity());
                salesAmount.add(sales.getSalesAmount() != null ? sales.getSalesAmount().doubleValue() : 0.0);
                marketShare.add(sales.getMarketShare() != null ? sales.getMarketShare().doubleValue() : 0.0);
            }
            
            data.put("products", products);
            data.put("salesQuantity", salesQuantity);
            data.put("salesAmount", salesAmount);
            data.put("marketShare", marketShare);
            
            return Result.success("获取产品销售分析成功", data);
        } catch (Exception e) {
            return Result.error("获取产品销售分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员来源分析
     */
    @GetMapping("/member-source-analysis")
    public Result<Map<String, Object>> getMemberSourceAnalysis(@RequestParam(required = false) String month) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // 获取会员来源数据
            String queryMonth = (month != null && !month.isEmpty()) ? month : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            // 查询会员来源数据
            List<DashboardMemberSource> sourceList = memberSourceService.selectByDataMonth(queryMonth);
            
            Map<String, Integer> sources = new HashMap<>();
            int total = 0;
            
            for (DashboardMemberSource source : sourceList) {
                sources.put(source.getSourceChannel(), source.getMemberCount());
                total += source.getMemberCount();
            }
            
            data.put("sources", sources);
            data.put("total", total);
            
            return Result.success("获取会员来源分析成功", data);
        } catch (Exception e) {
            return Result.error("获取会员来源分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取有数据的月份列表
     */
    @GetMapping("/available-months")
    public Result<List<String>> getAvailableMonths() {
        try {
            Set<String> monthSet = new HashSet<>();
            
            // 从各个数据表中获取有数据的月份
            List<DashboardMemberOverview> overviewList = memberOverviewService.selectDashboardMemberOverviewList(new DashboardMemberOverview());
            log.info("Member Overview 表中的月份数据: {}", overviewList.size());
            for (DashboardMemberOverview overview : overviewList) {
                if (overview.getDataMonth() != null && !overview.getDataMonth().isEmpty()) {
                    monthSet.add(overview.getDataMonth());
                    log.info("添加月份: {} (来自 Member Overview)", overview.getDataMonth());
                }
            }
            
            List<DashboardMemberGrowth> growthList = memberGrowthService.selectDashboardMemberGrowthList(new DashboardMemberGrowth());
            log.info("Member Growth 表中的月份数据: {}", growthList.size());
            for (DashboardMemberGrowth growth : growthList) {
                if (growth.getDataMonth() != null && !growth.getDataMonth().isEmpty()) {
                    monthSet.add(growth.getDataMonth());
                    log.info("添加月份: {} (来自 Member Growth)", growth.getDataMonth());
                }
            }
            
            List<DashboardMemberStage> stageList = memberStageService.selectDashboardMemberStageList(new DashboardMemberStage());
            log.info("Member Stage 表中的月份数据: {}", stageList.size());
            for (DashboardMemberStage stage : stageList) {
                if (stage.getDataMonth() != null && !stage.getDataMonth().isEmpty()) {
                    monthSet.add(stage.getDataMonth());
                    log.info("添加月份: {} (来自 Member Stage)", stage.getDataMonth());
                }
            }
            
            List<DashboardRegionPerformance> regionList = regionPerformanceService.selectDashboardRegionPerformanceList(new DashboardRegionPerformance());
            log.info("Region Performance 表中的月份数据: {}", regionList.size());
            for (DashboardRegionPerformance region : regionList) {
                if (region.getDataMonth() != null && !region.getDataMonth().isEmpty()) {
                    monthSet.add(region.getDataMonth());
                    log.info("添加月份: {} (来自 Region Performance)", region.getDataMonth());
                }
            }
            
            List<DashboardProductSales> salesList = productSalesService.selectDashboardProductSalesList(new DashboardProductSales());
            log.info("Product Sales 表中的月份数据: {}", salesList.size());
            for (DashboardProductSales sales : salesList) {
                if (sales.getDataMonth() != null && !sales.getDataMonth().isEmpty()) {
                    monthSet.add(sales.getDataMonth());
                    log.info("添加月份: {} (来自 Product Sales)", sales.getDataMonth());
                }
            }
            
            List<DashboardMemberSource> sourceList = memberSourceService.selectDashboardMemberSourceList(new DashboardMemberSource());
            log.info("Member Source 表中的月份数据: {}", sourceList.size());
            for (DashboardMemberSource source : sourceList) {
                if (source.getDataMonth() != null && !source.getDataMonth().isEmpty()) {
                    monthSet.add(source.getDataMonth());
                    log.info("添加月份: {} (来自 Member Source)", source.getDataMonth());
                }
            }
            
            // 转换为列表并排序（最新的月份在前）
            List<String> monthList = new ArrayList<>(monthSet);
            monthList.sort((a, b) -> b.compareTo(a)); // 降序排列
            
            log.info("最终可用月份列表: {}", monthList);
            return Result.success("获取有数据月份列表成功", monthList);
        } catch (Exception e) {
            log.error("获取有数据月份列表失败", e);
            return Result.error("获取有数据月份列表失败: " + e.getMessage());
        }
    }

    /**
     * 刷新数据
     */
    @GetMapping("/refresh")
    public Result<String> refreshData() {
        return Result.success("数据刷新成功");
    }

    /**
     * 下载Excel导入模板
     */
    @GetMapping("/import/template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            // 创建会员概览数据的Excel模板，包含示例数据
            List<DashboardMemberOverview> sampleData = createMemberOverviewSampleData();
            ExcelUtil<DashboardMemberOverview> util = new ExcelUtil<>(DashboardMemberOverview.class);
            util.exportExcel(response, sampleData, "会员概览数据", "会员概览数据模板");
            log.info("下载Excel导入模板成功");
        } catch (Exception e) {
            log.error("下载Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 下载产品销售数据Excel导入模板
     */
    @GetMapping("/import/template/product-sales")
    public void downloadProductSalesTemplate(HttpServletResponse response) {
        try {
            List<DashboardProductSales> sampleData = createProductSalesSampleData();
            ExcelUtil<DashboardProductSales> util = new ExcelUtil<>(DashboardProductSales.class);
            util.exportExcel(response, sampleData, "产品销售数据", "产品销售数据模板");
            log.info("下载产品销售数据Excel导入模板成功");
        } catch (Exception e) {
            log.error("下载产品销售数据Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 下载区域绩效数据Excel导入模板
     */
    @GetMapping("/import/template/region-performance")
    public void downloadRegionPerformanceTemplate(HttpServletResponse response) {
        try {
            List<DashboardRegionPerformance> sampleData = createRegionPerformanceSampleData();
            ExcelUtil<DashboardRegionPerformance> util = new ExcelUtil<>(DashboardRegionPerformance.class);
            util.exportExcel(response, sampleData, "区域绩效数据", "区域绩效数据模板");
            log.info("下载区域绩效数据Excel导入模板成功");
        } catch (Exception e) {
            log.error("下载区域绩效数据Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 下载会员增长趋势数据Excel导入模板
     */
    @GetMapping("/import/template/member-growth")
    public void downloadMemberGrowthTemplate(HttpServletResponse response) {
        try {
            List<DashboardMemberGrowth> sampleData = createMemberGrowthSampleData();
            ExcelUtil<DashboardMemberGrowth> util = new ExcelUtil<>(DashboardMemberGrowth.class);
            util.exportExcel(response, sampleData, "会员增长趋势数据", "会员增长趋势数据模板");
            log.info("下载会员增长趋势数据Excel导入模板成功");
        } catch (Exception e) {
            log.error("下载会员增长趋势数据Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 下载会员阶段分布数据Excel导入模板
     */
    @GetMapping("/import/template/member-stage")
    public void downloadMemberStageTemplate(HttpServletResponse response) {
        try {
            List<DashboardMemberStage> sampleData = createMemberStageSampleData();
            ExcelUtil<DashboardMemberStage> util = new ExcelUtil<>(DashboardMemberStage.class);
            util.exportExcel(response, sampleData, "会员阶段分布数据", "会员阶段分布数据模板");
            log.info("下载会员阶段分布数据Excel导入模板成功");
        } catch (Exception e) {
            log.error("下载会员阶段分布数据Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 下载会员来源分析数据Excel导入模板
     */
    @GetMapping("/import/template/member-source")
    public void downloadMemberSourceTemplate(HttpServletResponse response) {
        try {
            List<DashboardMemberSource> sampleData = createMemberSourceSampleData();
            ExcelUtil<DashboardMemberSource> util = new ExcelUtil<>(DashboardMemberSource.class);
            util.exportExcel(response, sampleData, "会员来源分析数据", "会员来源分析数据模板");
            log.info("下载会员来源分析数据Excel导入模板成功");
        } catch (Exception e) {
            log.error("下载会员来源分析数据Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 下载首页概览KPI（新会员数量、复购率、MOT成功率、会员活跃度）Excel导入模板
     */
    @GetMapping("/import/template/overview-kpi")
    public void downloadOverviewKpiTemplate(HttpServletResponse response) {
        try {
            // 使用Apache POI生成包含示例数据和校验规则的模板
            org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();

            // =========================
            // 主数据表: 首页KPI
            // =========================
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("首页KPI");

            // 样式：表头加粗、整数与小数格式
            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font bold = workbook.createFont();
            bold.setBold(true);
            headerStyle.setFont(bold);

            org.apache.poi.ss.usermodel.CellStyle integerStyle = workbook.createCellStyle();
            integerStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));

            org.apache.poi.ss.usermodel.CellStyle decimalStyle = workbook.createCellStyle();
            decimalStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

            // 表头
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = new String[] {
                    "月份", "新会员数量", "复购率", "MOT成功率", "会员活跃度",
                    "新会员数量环比", "复购率环比", "MOT成功率环比", "会员活跃度环比"
            };
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 示例数据：确保包含 2025-01 与 2025-02
            Object[][] sampleRows = new Object[][]{
                    { "2025-01", 985, 32.50, 20.10, 60.00, 0.00, 0.00, 0.00, 0.00 },
                    { "2025-02", 1052, 33.50, 20.90, 61.20, 6.80, 1.00, 0.80, 1.20 },
                    { "2025-05", 1138, 34.30, 22.60, 63.10, 8.20, 0.80, 1.70, 1.90 },
                    { "2025-06", 1285, 38.20, 24.50, 65.20, 12.80, 3.50, 2.10, 1.80 },
                    { "2025-07", 1302, 38.80, 25.20, 66.00, 1.30, 0.60, 0.70, 0.80 }
            };
            for (int r = 0; r < sampleRows.length; r++) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(r + 1);
                Object[] values = sampleRows[r];
                for (int c = 0; c < values.length; c++) {
                    org.apache.poi.ss.usermodel.Cell cell = row.createCell(c);
                    Object v = values[c];
                    if (v instanceof Number) {
                        cell.setCellValue(((Number) v).doubleValue());
                        // 第2列为整数，新会员数量；其它为两位小数
                        if (c == 1) {
                            cell.setCellStyle(integerStyle);
                        } else if (c >= 2) {
                            cell.setCellStyle(decimalStyle);
                        }
                    } else {
                        cell.setCellValue(String.valueOf(v));
                    }
                }
            }

            // 冻结首行，避免滚动丢失表头
            sheet.createFreezePane(0, 1);

            // 自动列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 月份列数据校验：必须为YYYY-MM格式
            try {
                org.apache.poi.xssf.usermodel.XSSFSheet xssfSheet = (org.apache.poi.xssf.usermodel.XSSFSheet) sheet;
                org.apache.poi.ss.usermodel.DataValidationHelper helper = new org.apache.poi.xssf.usermodel.XSSFDataValidationHelper(xssfSheet);
                org.apache.poi.ss.usermodel.DataValidationConstraint constraint = helper.createCustomConstraint("AND(LEN(A2)=7, MID(A2,5,1)='-', ISNUMBER(VALUE(LEFT(A2,4))), ISNUMBER(VALUE(RIGHT(A2,2))))");
                org.apache.poi.ss.util.CellRangeAddressList addressList = new org.apache.poi.ss.util.CellRangeAddressList(1, 200, 0, 0); // A2:A201
                org.apache.poi.ss.usermodel.DataValidation validation = helper.createValidation(constraint, addressList);
                validation.setShowErrorBox(true);
                validation.createErrorBox("格式错误", "月份必须为YYYY-MM，例如 2025-01");
                validation.setShowPromptBox(true);
                validation.createPromptBox("填写提示", "月份请填写YYYY-MM，例如 2025-01；区域留空表示全国");
                xssfSheet.addValidationData(validation);
            } catch (Exception e) {
                log.warn("创建月份数据校验时发生异常: {}", e.getMessage());
            }

            // 说明页：导入规则与注意事项
            org.apache.poi.ss.usermodel.Sheet guide = workbook.createSheet("说明");
            String[] lines = new String[] {
                    "首页KPI数据导入模板使用说明：",
                    "1. 月份格式必须为YYYY-MM，例如 2025-01、2025-02。",
                    "2. 区域代码无需填写，模板不含该列；后端将写入全国数据（region_code=NULL）。",
                    "3. 复购率、MOT成功率、会员活跃度及其环比均为数值，请直接填写数字，不要加百分号。",
                    "4. 新会员数量为整数，其余指标建议保留两位小数。",
                    "5. 示例数据已包含 2025-01 与 2025-02，可直接按需修改后导入。",
                    "6. 若某行格式不正确，系统将跳过该行并在返回结果中给出错误提示。"
            };
            for (int i = 0; i < lines.length; i++) {
                org.apache.poi.ss.usermodel.Row row = guide.createRow(i);
                row.createCell(0).setCellValue(lines[i]);
            }
            guide.autoSizeColumn(0);

            // 设置下载文件名并输出
            com.omniperform.common.utils.file.FileUtils.setAttachmentResponseHeader(response, "首页KPI模板.xlsx");
            java.io.OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            workbook.close();
            log.info("下载首页概览KPI模板成功");
        } catch (Exception e) {
            log.error("下载首页概览KPI模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 首页KPI导入（模板：overview-kpi），写入 dashboard_overview_kpi 表（全国数据 region_code 为空）
     */
    @PostMapping("/import/overview-kpi")
    public Result<Map<String, Object>> importOverviewKpi(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        try {
            ExcelUtil<OverviewKpiImportRow> util = new ExcelUtil<>(OverviewKpiImportRow.class);
            // 使用默认解析，模板首行是标题
            List<OverviewKpiImportRow> rows = util.importExcel(file.getInputStream());
            if (rows == null || rows.isEmpty()) {
                return Result.error("导入数据为空");
            }
            List<DashboardOverviewKpi> batch = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                OverviewKpiImportRow r = rows.get(i);
                try {
                    String monthYear = r.getMonth();
                    if (monthYear == null || monthYear.trim().isEmpty()) {
                        throw new IllegalArgumentException("月份为空");
                    }
                    monthYear = monthYear.trim();
                    // 严格校验月份格式为 YYYY-MM
                    if (!monthYear.matches("\\d{4}-\\d{2}")) {
                        throw new IllegalArgumentException("月份格式须为YYYY-MM");
                    }

                    DashboardOverviewKpi k = new DashboardOverviewKpi();
                    k.setDataMonth(monthYear);
                    k.setRegionCode(null);
                    k.setNewMembers(r.getNewMembers());
                    k.setNewMembersGrowth(r.getNewMembersGrowth());
                    k.setRepeatPurchaseRate(r.getRepeatPurchaseRate());
                    k.setRepeatPurchaseGrowth(r.getRepeatPurchaseGrowth());
                    k.setMotSuccessRate(r.getMotSuccessRate());
                    k.setMotSuccessGrowth(r.getMotSuccessGrowth());
                    k.setMemberActivityRate(r.getMemberActivityRate());
                    k.setMemberActivityGrowth(r.getMemberActivityGrowth());
                    batch.add(k);
                } catch (Exception e) {
                    failCount++;
                    errors.add("第" + (i + 1) + "行处理失败: " + e.getMessage());
                    log.warn("首页KPI导入失败: {}", e.getMessage());
                }
            }

            if (!batch.isEmpty()) {
                // 按真实入库成功的条数统计成功数
                successCount = dashboardOverviewKpiService.upsertBatch(batch);
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入首页KPI失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    // 旧的优化效果数据构建方法已废弃，首页KPI改为独立表管理

    /**
     * 创建会员概览示例数据
     */
    private List<DashboardMemberOverview> createMemberOverviewSampleData() {
        List<DashboardMemberOverview> sampleData = new ArrayList<>();
        
        // 示例数据1
        DashboardMemberOverview sample1 = new DashboardMemberOverview();
        sample1.setDataMonth("2025-01");
        sample1.setTotalMembers(10000);
        sample1.setNewMembers(500);
        sample1.setActiveMembers(8500);
        sample1.setMemberGrowthRate(new BigDecimal("5.26"));
        sample1.setCreateTime(new Date());
        sample1.setCreateBy("system");
        sample1.setRemark("示例数据1");
        sampleData.add(sample1);
        
        // 示例数据2
        DashboardMemberOverview sample2 = new DashboardMemberOverview();
        sample2.setDataMonth("2025-02");
        sample2.setTotalMembers(10500);
        sample2.setNewMembers(600);
        sample2.setActiveMembers(9000);
        sample2.setMemberGrowthRate(new BigDecimal("5.71"));
        sample2.setCreateTime(new Date());
        sample2.setCreateBy("system");
        sample2.setRemark("示例数据2");
        sampleData.add(sample2);
        
        return sampleData;
    }

    /**
     * 创建产品销售示例数据
     */
    private List<DashboardProductSales> createProductSalesSampleData() {
        List<DashboardProductSales> sampleData = new ArrayList<>();
        
        // 示例数据1
        DashboardProductSales sample1 = new DashboardProductSales();
        sample1.setDataMonth("2025-01");
        sample1.setProductName("雀巢咖啡");
        sample1.setSalesAmount(new BigDecimal("150000.00"));
        sample1.setSalesQuantity(5000);
        sample1.setMarketShare(new BigDecimal("25.5"));
        sample1.setCreateTime(new Date());
        sample1.setCreateBy("system");
        sampleData.add(sample1);
        
        // 示例数据2
        DashboardProductSales sample2 = new DashboardProductSales();
        sample2.setDataMonth("2025-01");
        sample2.setProductName("雀巢奶粉");
        sample2.setSalesAmount(new BigDecimal("200000.00"));
        sample2.setSalesQuantity(3000);
        sample2.setMarketShare(new BigDecimal("30.2"));
        sample2.setCreateTime(new Date());
        sample2.setCreateBy("system");
        sampleData.add(sample2);
        
        return sampleData;
    }

    /**
     * 创建区域绩效示例数据
     */
    private List<DashboardRegionPerformance> createRegionPerformanceSampleData() {
        List<DashboardRegionPerformance> sampleData = new ArrayList<>();
        
        // 示例数据1
        DashboardRegionPerformance sample1 = new DashboardRegionPerformance();
        sample1.setDataMonth("2025-01");
        sample1.setRegionName("华东区");
        sample1.setSalesAmount(new BigDecimal("500000.00"));
        sample1.setMemberCount(2500);
        sample1.setPerformanceScore(new BigDecimal("85.5"));
        sample1.setCreateTime(new Date());
        sample1.setCreateBy("system");
        sampleData.add(sample1);
        
        // 示例数据2
        DashboardRegionPerformance sample2 = new DashboardRegionPerformance();
        sample2.setDataMonth("2025-01");
        sample2.setRegionName("华南区");
        sample2.setSalesAmount(new BigDecimal("450000.00"));
        sample2.setMemberCount(2200);
        sample2.setPerformanceScore(new BigDecimal("82.3"));
        sample2.setCreateTime(new Date());
        sample2.setCreateBy("system");
        sampleData.add(sample2);
        
        return sampleData;
    }

    /**
     * 创建会员增长趋势示例数据
     */
    private List<DashboardMemberGrowth> createMemberGrowthSampleData() {
        List<DashboardMemberGrowth> sampleData = new ArrayList<>();
        
        // 示例数据1
        DashboardMemberGrowth sample1 = new DashboardMemberGrowth();
        sample1.setDataMonth("2025-01");
        sample1.setNewMembers(500);
        sample1.setTotalMembers(10000);
        sample1.setGrowthRate(new BigDecimal("5.26"));
        sample1.setCreateTime(new Date());
        sample1.setCreateBy("system");
        sample1.setRemark("示例数据1");
        sampleData.add(sample1);
        
        // 示例数据2
        DashboardMemberGrowth sample2 = new DashboardMemberGrowth();
        sample2.setDataMonth("2025-02");
        sample2.setNewMembers(600);
        sample2.setTotalMembers(10600);
        sample2.setGrowthRate(new BigDecimal("6.00"));
        sample2.setCreateTime(new Date());
        sample2.setCreateBy("system");
        sample2.setRemark("示例数据2");
        sampleData.add(sample2);
        
        return sampleData;
    }

    /**
     * 创建会员阶段分布示例数据
     */
    private List<DashboardMemberStage> createMemberStageSampleData() {
        List<DashboardMemberStage> sampleData = new ArrayList<>();
        
        // 示例数据1
        DashboardMemberStage sample1 = new DashboardMemberStage();
        sample1.setDataMonth("2025-01");
        sample1.setStageName("新会员");
        sample1.setMemberCount(2500);
        sample1.setPercentage(new BigDecimal("25.0"));
        sample1.setCreateTime(new Date());
        sample1.setCreateBy("system");
        sample1.setRemark("示例数据1");
        sampleData.add(sample1);
        
        // 示例数据2
        DashboardMemberStage sample2 = new DashboardMemberStage();
        sample2.setDataMonth("2025-01");
        sample2.setStageName("活跃会员");
        sample2.setMemberCount(4500);
        sample2.setPercentage(new BigDecimal("45.0"));
        sample2.setCreateTime(new Date());
        sample2.setCreateBy("system");
        sample2.setRemark("示例数据2");
        sampleData.add(sample2);
        
        return sampleData;
    }

    /**
     * 创建会员来源分析示例数据
     */
    private List<DashboardMemberSource> createMemberSourceSampleData() {
        List<DashboardMemberSource> sampleData = new ArrayList<>();
        
        // 示例数据1
        DashboardMemberSource sample1 = new DashboardMemberSource();
        sample1.setDataMonth("2025-01");
        sample1.setSourceChannel("线下门店");
        sample1.setMemberCount(3420);
        sample1.setPercentage(new BigDecimal("38.5"));
        sample1.setConversionRate(new BigDecimal("15.2"));
        sample1.setCreateTime(new Date());
        sample1.setCreateBy("system");
        sample1.setRemark("示例数据1");
        sampleData.add(sample1);
        
        // 示例数据2
        DashboardMemberSource sample2 = new DashboardMemberSource();
        sample2.setDataMonth("2025-01");
        sample2.setSourceChannel("线上商城");
        sample2.setMemberCount(2180);
        sample2.setPercentage(new BigDecimal("24.5"));
        sample2.setConversionRate(new BigDecimal("8.9"));
        sample2.setCreateTime(new Date());
        sample2.setCreateBy("system");
        sample2.setRemark("示例数据2");
        sampleData.add(sample2);
        
        return sampleData;
    }

    /**
     * 导入会员概览数据
     */
    @PostMapping("/import/member-overview")
    public Result<String> importMemberOverview(@RequestParam("file") MultipartFile file) {
        try {
            ExcelUtil<DashboardMemberOverview> util = new ExcelUtil<>(DashboardMemberOverview.class);
            // 使用titleNum=1跳过标题行
            List<DashboardMemberOverview> dataList = util.importExcel(file.getInputStream(), 1);
            
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }
            
            // 批量插入数据
            int successCount = 0;
            for (DashboardMemberOverview data : dataList) {
                try {
                    memberOverviewService.insertDashboardMemberOverview(data);
                    successCount++;
                } catch (Exception e) {
                    log.warn("插入会员概览数据失败: {}", e.getMessage());
                }
            }
            
            log.info("导入会员概览数据成功，共导入{}条数据", successCount);
            return Result.success(String.format("导入成功，共导入%d条数据", successCount));
        } catch (Exception e) {
            log.error("导入会员概览数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入产品销售数据
     */
    @PostMapping("/import/product-sales")
    public Result<String> importProductSales(@RequestParam("file") MultipartFile file) {
        try {
            ExcelUtil<DashboardProductSales> util = new ExcelUtil<>(DashboardProductSales.class);
            // 使用titleNum=1跳过标题行
            List<DashboardProductSales> dataList = util.importExcel(file.getInputStream(), 1);
            
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }
            
            // 批量导入：先查重复(月份+产品)，存在则更新，否则插入，避免唯一键冲突
            int successCount = 0;
            for (DashboardProductSales data : dataList) {
                try {
                    boolean hasMonth = data.getDataMonth() != null && !data.getDataMonth().trim().isEmpty();
                    boolean hasProduct = data.getProductName() != null && !data.getProductName().trim().isEmpty();
                    if (hasMonth && hasProduct) {
                        List<DashboardProductSales> existingList = productSalesService.selectByDataMonth(data.getDataMonth());
                        DashboardProductSales existing = null;
                        for (DashboardProductSales s : existingList) {
                            if (data.getProductName().equals(s.getProductName())) {
                                existing = s;
                                break;
                            }
                        }
                        if (existing != null) {
                            // 默认更新重复记录
                            data.setId(existing.getId());
                            data.setUpdateBy(data.getCreateBy());
                            data.setUpdateTime(new Date());
                            productSalesService.updateDashboardProductSales(data);
                            successCount++;
                            continue;
                        }
                    }
                    productSalesService.insertDashboardProductSales(data);
                    successCount++;
                } catch (Exception e) {
                    log.warn("插入产品销售数据失败: {}", e.getMessage());
                }
            }
            
            log.info("导入产品销售数据成功，共导入{}条数据", successCount);
            return Result.success(String.format("导入成功，共导入%d条数据", successCount));
        } catch (Exception e) {
            log.error("导入产品销售数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入区域绩效数据
     */
    @PostMapping("/import/region-performance")
    public Result<String> importRegionPerformance(@RequestParam("file") MultipartFile file) {
        try {
            ExcelUtil<DashboardRegionPerformance> util = new ExcelUtil<>(DashboardRegionPerformance.class);
            // 使用titleNum=1跳过标题行
            List<DashboardRegionPerformance> dataList = util.importExcel(file.getInputStream(), 1);
            
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }
            
            // 批量导入：先查重复(月份+区域)，存在则更新，否则插入，避免唯一键冲突
            int successCount = 0;
            for (DashboardRegionPerformance data : dataList) {
                try {
                    boolean hasMonth = data.getDataMonth() != null && !data.getDataMonth().trim().isEmpty();
                    boolean hasRegion = data.getRegionName() != null && !data.getRegionName().trim().isEmpty();
                    if (hasMonth && hasRegion) {
                        List<DashboardRegionPerformance> existingList = regionPerformanceService.selectByDataMonth(data.getDataMonth());
                        DashboardRegionPerformance existing = null;
                        for (DashboardRegionPerformance s : existingList) {
                            if (data.getRegionName().equals(s.getRegionName())) {
                                existing = s;
                                break;
                            }
                        }
                        if (existing != null) {
                            // 默认更新重复记录
                            data.setId(existing.getId());
                            data.setUpdateBy(data.getCreateBy());
                            data.setUpdateTime(new Date());
                            regionPerformanceService.updateDashboardRegionPerformance(data);
                            successCount++;
                            continue;
                        }
                    }
                    regionPerformanceService.insertDashboardRegionPerformance(data);
                    successCount++;
                } catch (Exception e) {
                    log.warn("插入区域绩效数据失败: {}", e.getMessage());
                }
            }
            
            log.info("导入区域绩效数据成功，共导入{}条数据", successCount);
            return Result.success(String.format("导入成功，共导入%d条数据", successCount));
        } catch (Exception e) {
            log.error("导入区域绩效数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入会员增长趋势数据
     */
    @PostMapping("/import/member-growth")
    public Result<String> importMemberGrowth(@RequestParam("file") MultipartFile file) {
        try {
            ExcelUtil<DashboardMemberGrowth> util = new ExcelUtil<>(DashboardMemberGrowth.class);
            // 使用titleNum=1跳过标题行
            List<DashboardMemberGrowth> dataList = util.importExcel(file.getInputStream(), 1);
            
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }
            
            // 批量插入数据
            int successCount = 0;
            for (DashboardMemberGrowth data : dataList) {
                try {
                    // 先查重复月份：存在则默认更新，避免唯一键报错
                    boolean hasMonth = data.getDataMonth() != null && !data.getDataMonth().trim().isEmpty();
                    if (hasMonth) {
                        DashboardMemberGrowth existing = memberGrowthService.selectByDataMonth(data.getDataMonth());
                        if (existing != null) {
                            data.setId(existing.getId());
                            data.setUpdateBy(data.getCreateBy());
                            data.setUpdateTime(new Date());
                            memberGrowthService.updateDashboardMemberGrowth(data);
                            successCount++;
                            continue;
                        }
                    }
                    // 不存在重复月份时执行插入
                    memberGrowthService.insertDashboardMemberGrowth(data);
                    successCount++;
                } catch (Exception e) {
                    log.warn("插入/更新会员增长趋势数据失败: {}", e.getMessage());
                }
            }
            
            log.info("导入会员增长趋势数据成功，共导入{}条数据", successCount);
            return Result.success(String.format("导入成功，共导入%d条数据", successCount));
        } catch (Exception e) {
            log.error("导入会员增长趋势数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入会员阶段分布数据
     */
    @PostMapping("/import/member-stage")
    public Result<String> importMemberStage(@RequestParam("file") MultipartFile file) {
        try {
            ExcelUtil<DashboardMemberStage> util = new ExcelUtil<>(DashboardMemberStage.class);
            // 使用titleNum=1跳过标题行
            List<DashboardMemberStage> dataList = util.importExcel(file.getInputStream(), 1);
            
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }
            
            // 批量插入数据
            int successCount = 0;
            for (DashboardMemberStage data : dataList) {
                try {
                    // 先查重复：按月份+阶段名称，存在则默认更新，避免唯一键报错
                    boolean hasMonth = data.getDataMonth() != null && !data.getDataMonth().trim().isEmpty();
                    boolean hasStage = data.getStageName() != null && !data.getStageName().trim().isEmpty();
                    if (hasMonth && hasStage) {
                        DashboardMemberStage filter = new DashboardMemberStage();
                        filter.setDataMonth(data.getDataMonth());
                        filter.setStageName(data.getStageName());
                        List<DashboardMemberStage> existingList = memberStageService.selectDashboardMemberStageList(filter);
                        if (existingList != null && !existingList.isEmpty()) {
                            DashboardMemberStage existing = existingList.get(0);
                            data.setId(existing.getId());
                            data.setUpdateBy(data.getCreateBy());
                            data.setUpdateTime(new Date());
                            memberStageService.updateDashboardMemberStage(data);
                            successCount++;
                            continue;
                        }
                    }
                    // 不存在重复时插入
                    memberStageService.insertDashboardMemberStage(data);
                    successCount++;
                } catch (Exception e) {
                    log.warn("插入/更新会员阶段分布数据失败: {}", e.getMessage());
                }
            }
            
            log.info("导入会员阶段分布数据成功，共导入{}条数据", successCount);
            return Result.success(String.format("导入成功，共导入%d条数据", successCount));
        } catch (Exception e) {
            log.error("导入会员阶段分布数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入会员来源分析数据
     */
    @PostMapping("/import/member-source")
    public Result<String> importMemberSource(@RequestParam("file") MultipartFile file) {
        try {
            ExcelUtil<DashboardMemberSource> util = new ExcelUtil<>(DashboardMemberSource.class);
            // 使用titleNum=1跳过标题行
            List<DashboardMemberSource> dataList = util.importExcel(file.getInputStream(), 1);
            
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }
            
            // 批量插入数据
            int successCount = 0;
            for (DashboardMemberSource data : dataList) {
                try {
                    memberSourceService.insertDashboardMemberSource(data);
                    successCount++;
                } catch (Exception e) {
                    log.warn("插入会员来源分析数据失败: {}", e.getMessage());
                }
            }
            
            log.info("导入会员来源分析数据成功，共导入{}条数据", successCount);
            return Result.success(String.format("导入成功，共导入%d条数据", successCount));
        } catch (Exception e) {
            log.error("导入会员来源分析数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 批量导入所有类型数据（通用接口）
     */
    @PostMapping("/import/batch")
    public Result<Map<String, Object>> batchImport(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("dataType") String dataType,
                                                   @RequestParam(value = "allowUpdate", required = false, defaultValue = "true") boolean allowUpdate) {
        try {
            log.info("开始批量导入数据，文件名: {}, 数据类型: {}, 文件大小: {} bytes", 
                    file.getOriginalFilename(), dataType, file.getSize());
            
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();
            
            switch (dataType.toLowerCase()) {
                case "member-overview":
                    log.info("开始处理会员概览数据导入");
                    ExcelUtil<DashboardMemberOverview> memberUtil = new ExcelUtil<>(DashboardMemberOverview.class);
                    
                    // 添加调试信息
                    log.info("Excel文件信息 - 文件名: {}, 大小: {} bytes", file.getOriginalFilename(), file.getSize());
                    
                    // 修改importExcel调用，使用titleNum=1跳过标题行，使用第二行作为表头
                    List<DashboardMemberOverview> memberDataList = memberUtil.importExcel(file.getInputStream(), 1);
                    
                    // 检查memberDataList是否为null
                    if (memberDataList == null) {
                        log.error("Excel导入返回null，无法处理数据");
                        return Result.error("Excel文件解析失败，请检查文件格式");
                    }
                    
                    log.info("从Excel文件中读取到 {} 条会员概览数据", memberDataList.size());
                    
                    // 添加Excel文件完整结构调试信息
                    try {
                        Workbook workbook = WorkbookFactory.create(file.getInputStream());
                        Sheet sheet = workbook.getSheetAt(0);
                        
                        log.info("Excel文件完整结构分析:");
                        log.info("总行数: {}", sheet.getLastRowNum() + 1);
                        
                        // 分析前5行的内容
                        for (int rowIndex = 0; rowIndex <= Math.min(4, sheet.getLastRowNum()); rowIndex++) {
                            Row row = sheet.getRow(rowIndex);
                            if (row != null) {
                                log.info("第 {} 行内容:", rowIndex + 1);
                                for (int colIndex = 0; colIndex < row.getPhysicalNumberOfCells(); colIndex++) {
                                    Cell cell = row.getCell(colIndex);
                                    if (cell != null) {
                                        String cellValue = "";
                                        try {
                                            cellValue = cell.getStringCellValue();
                                        } catch (Exception e) {
                                            try {
                                                cellValue = String.valueOf(cell.getNumericCellValue());
                                            } catch (Exception e2) {
                                                cellValue = cell.toString();
                                            }
                                        }
                                        log.info("  第 {} 列: '{}'", colIndex + 1, cellValue);
                                    }
                                }
                            } else {
                                log.info("第 {} 行: 空行", rowIndex + 1);
                            }
                        }
                        
                        workbook.close();
                    } catch (Exception e) {
                        log.error("读取Excel文件结构失败: {}", e.getMessage(), e);
                    }
                    
                    // 输出期望的表头名称
                    log.info("期望的表头名称: 数据月份, 总会员数, 新增会员数, 活跃会员数, 会员增长率");
                    
                    // 添加详细的调试信息
                    for (int i = 0; i < memberDataList.size(); i++) {
                        DashboardMemberOverview data = memberDataList.get(i);
                        log.info("第 {} 条原始数据对象: {}", i + 1, data);
                        if (data != null) {
                            log.info("第 {} 条数据详情 - dataMonth: {}, totalMembers: {}, newMembers: {}, activeMembers: {}, memberGrowthRate: {}", 
                                    i + 1, data.getDataMonth(), data.getTotalMembers(), data.getNewMembers(), 
                                    data.getActiveMembers(), data.getMemberGrowthRate());
                        }
                    }
                    
                    for (int i = 0; i < memberDataList.size(); i++) {
                        DashboardMemberOverview data = memberDataList.get(i);
                        
                        // 检查数据对象是否为null
                        if (data == null) {
                            log.warn("第 {} 条数据为null，跳过处理", i + 1);
                            continue;
                        }
                        
                        // 设置默认值
                        if (data.getCreateTime() == null) {
                            data.setCreateTime(new Date());
                        }
                        String createBy = data.getCreateBy();
                        if (createBy == null || createBy.trim().isEmpty()) {
                            data.setCreateBy("system");
                        }
                        if (data.getRemark() == null) {
                            data.setRemark("批量导入");
                        }
                        
                        log.info("处理第 {} 条数据 - 数据月份: {}, 总会员数: {}, 活跃会员数: {}, 新增会员数: {}, 会员增长率: {}", 
                                i + 1, 
                                data.getDataMonth() != null ? data.getDataMonth() : "null", 
                                data.getTotalMembers() != null ? data.getTotalMembers() : "null", 
                                data.getActiveMembers() != null ? data.getActiveMembers() : "null",
                                data.getNewMembers() != null ? data.getNewMembers() : "null", 
                                data.getMemberGrowthRate() != null ? data.getMemberGrowthRate() : "null");
                        
                        try {
                            // 先查重复月份：存在则更新或跳过，避免唯一键报错
                            if (data.getDataMonth() != null && !data.getDataMonth().trim().isEmpty()) {
                                DashboardMemberOverview existing = memberOverviewService.selectByDataMonth(data.getDataMonth());
                                if (existing != null) {
                                    if (allowUpdate) {
                                        data.setId(existing.getId());
                                        data.setUpdateBy(data.getCreateBy());
                                        data.setUpdateTime(new Date());
                                        memberOverviewService.updateDashboardMemberOverview(data);
                                    } else {
                                        // 不允许更新时，重复月份直接跳过并记为成功，避免报错
                                        // 如需记录，可在前端提示“已存在该月份，已跳过”
                                    }
                                    successCount++;
                                    continue;
                                }
                            }
                            // 不存在重复月份时执行插入
                            memberOverviewService.insertDashboardMemberOverview(data);
                            successCount++;
                        } catch (Exception e) {
                            failCount++;
                            errorMessages.add("会员概览数据导入失败: " + e.getMessage());
                            log.error("第 {} 条数据插入失败: {}", i + 1, e.getMessage());
                        }
                    }
                    break;
                    
                case "product-sales":
                    ExcelUtil<DashboardProductSales> productUtil = new ExcelUtil<>(DashboardProductSales.class);
                    // 使用titleNum=1跳过标题行
                    List<DashboardProductSales> productDataList = productUtil.importExcel(file.getInputStream(), 1);
                    for (DashboardProductSales data : productDataList) {
                        try {
                            // 先查重复记录：按月份+产品名称判断是否存在，存在则更新或跳过，避免唯一键报错
                            boolean hasMonth = data.getDataMonth() != null && !data.getDataMonth().trim().isEmpty();
                            boolean hasProduct = data.getProductName() != null && !data.getProductName().trim().isEmpty();
                            if (hasMonth && hasProduct) {
                                List<DashboardProductSales> existingList = productSalesService.selectByDataMonth(data.getDataMonth());
                                DashboardProductSales existing = null;
                                for (DashboardProductSales s : existingList) {
                                    if (data.getProductName().equals(s.getProductName())) {
                                        existing = s;
                                        break;
                                    }
                                }
                                if (existing != null) {
                                    if (allowUpdate) {
                                        data.setId(existing.getId());
                                        data.setUpdateBy(data.getCreateBy());
                                        data.setUpdateTime(new Date());
                                        productSalesService.updateDashboardProductSales(data);
                                    } else {
                                        // 不允许更新时，重复记录直接跳过并记为成功，避免报错
                                        // 如需记录，可在前端提示“该月份该产品已存在，已跳过”
                                    }
                                    successCount++;
                                    continue;
                                }
                            }
                            // 不存在重复记录时执行插入
                            productSalesService.insertDashboardProductSales(data);
                            successCount++;
                        } catch (Exception e) {
                            failCount++;
                            errorMessages.add("产品销售数据导入失败: " + e.getMessage());
                        }
                    }
                    break;
                    
                case "region-performance":
                    ExcelUtil<DashboardRegionPerformance> regionUtil = new ExcelUtil<>(DashboardRegionPerformance.class);
                    // 使用titleNum=1跳过标题行
                    List<DashboardRegionPerformance> regionDataList = regionUtil.importExcel(file.getInputStream(), 1);
                    for (DashboardRegionPerformance data : regionDataList) {
                        try {
                            // 先查重复记录：按月份+区域名称判断是否存在，存在则更新或跳过，避免唯一键报错
                            boolean hasMonth = data.getDataMonth() != null && !data.getDataMonth().trim().isEmpty();
                            boolean hasRegion = data.getRegionName() != null && !data.getRegionName().trim().isEmpty();
                            if (hasMonth && hasRegion) {
                                List<DashboardRegionPerformance> existingList = regionPerformanceService.selectByDataMonth(data.getDataMonth());
                                DashboardRegionPerformance existing = null;
                                for (DashboardRegionPerformance s : existingList) {
                                    if (data.getRegionName().equals(s.getRegionName())) {
                                        existing = s;
                                        break;
                                    }
                                }
                                if (existing != null) {
                                    if (allowUpdate) {
                                        data.setId(existing.getId());
                                        data.setUpdateBy(data.getCreateBy());
                                        data.setUpdateTime(new Date());
                                        regionPerformanceService.updateDashboardRegionPerformance(data);
                                    } else {
                                        // 不允许更新时，重复记录直接跳过并记为成功，避免报错
                                        // 如需记录，可在前端提示“该月份该区域已存在，已跳过”
                                    }
                                    successCount++;
                                    continue;
                                }
                            }
                            // 不存在重复记录时执行插入
                            regionPerformanceService.insertDashboardRegionPerformance(data);
                            successCount++;
                        } catch (Exception e) {
                            failCount++;
                            errorMessages.add("区域绩效数据导入失败: " + e.getMessage());
                        }
                    }
                    break;
                    
                case "member-growth":
                    ExcelUtil<DashboardMemberGrowth> memberGrowthUtil = new ExcelUtil<>(DashboardMemberGrowth.class);
                    // 使用titleNum=1跳过标题行
                    List<DashboardMemberGrowth> memberGrowthDataList = memberGrowthUtil.importExcel(file.getInputStream(), 1);
                    for (DashboardMemberGrowth data : memberGrowthDataList) {
                        try {
                            // 先查重复月份：存在则更新或跳过，避免唯一键报错
                            boolean hasMonth = data.getDataMonth() != null && !data.getDataMonth().trim().isEmpty();
                            if (hasMonth) {
                                DashboardMemberGrowth existing = memberGrowthService.selectByDataMonth(data.getDataMonth());
                                if (existing != null) {
                                    if (allowUpdate) {
                                        data.setId(existing.getId());
                                        data.setUpdateBy(data.getCreateBy());
                                        data.setUpdateTime(new Date());
                                        memberGrowthService.updateDashboardMemberGrowth(data);
                                    } else {
                                        // 不允许更新时，重复月份直接跳过并记为成功，避免报错
                                        // 如需记录，可在前端提示“已存在该月份，已跳过”
                                    }
                                    successCount++;
                                    continue;
                                }
                            }
                            // 不存在重复月份时执行插入
                            memberGrowthService.insertDashboardMemberGrowth(data);
                            successCount++;
                        } catch (Exception e) {
                            failCount++;
                            errorMessages.add("会员增长趋势数据导入失败: " + e.getMessage());
                        }
                    }
                    break;
                    
                case "member-stage":
                    ExcelUtil<DashboardMemberStage> memberStageUtil = new ExcelUtil<>(DashboardMemberStage.class);
                    // 使用titleNum=1跳过标题行
                    List<DashboardMemberStage> memberStageDataList = memberStageUtil.importExcel(file.getInputStream(), 1);
                    for (DashboardMemberStage data : memberStageDataList) {
                        try {
                            // 先查重复记录：按月份+阶段名称判断是否存在，存在则更新或跳过，避免唯一键报错
                            boolean hasMonth = data.getDataMonth() != null && !data.getDataMonth().trim().isEmpty();
                            boolean hasStage = data.getStageName() != null && !data.getStageName().trim().isEmpty();
                            if (hasMonth && hasStage) {
                                DashboardMemberStage filter = new DashboardMemberStage();
                                filter.setDataMonth(data.getDataMonth());
                                filter.setStageName(data.getStageName());
                                List<DashboardMemberStage> existingList = memberStageService.selectDashboardMemberStageList(filter);
                                if (existingList != null && !existingList.isEmpty()) {
                                    DashboardMemberStage existing = existingList.get(0);
                                    if (allowUpdate) {
                                        data.setId(existing.getId());
                                        data.setUpdateBy(data.getCreateBy());
                                        data.setUpdateTime(new Date());
                                        memberStageService.updateDashboardMemberStage(data);
                                    } else {
                                        // 不允许更新时，重复记录直接跳过并记为成功，避免报错
                                        // 如需记录，可在前端提示“该月份该阶段已存在，已跳过”
                                    }
                                    successCount++;
                                    continue;
                                }
                            }
                            // 不存在重复记录时执行插入
                            memberStageService.insertDashboardMemberStage(data);
                            successCount++;
                        } catch (Exception e) {
                            failCount++;
                            errorMessages.add("会员阶段分布数据导入失败: " + e.getMessage());
                        }
                    }
                    break;
                    
                case "member-source":
                    ExcelUtil<DashboardMemberSource> memberSourceUtil = new ExcelUtil<>(DashboardMemberSource.class);
                    // 使用titleNum=1跳过标题行
                    List<DashboardMemberSource> memberSourceDataList = memberSourceUtil.importExcel(file.getInputStream(), 1);
                    for (DashboardMemberSource data : memberSourceDataList) {
                        try {
                            memberSourceService.insertDashboardMemberSource(data);
                            successCount++;
                        } catch (Exception e) {
                            failCount++;
                            errorMessages.add("会员来源分析数据导入失败: " + e.getMessage());
                        }
                    }
                    break;
                    
                default:
                    return Result.error("不支持的数据类型: " + dataType);
            }
            
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);
            
            log.info("批量导入{}数据完成，成功{}条，失败{}条", dataType, successCount, failCount);
            return Result.success("批量导入完成", result);
        } catch (Exception e) {
            log.error("批量导入数据失败: {}", e.getMessage(), e);
            return Result.error("批量导入失败: " + e.getMessage());
        }
    }
}
