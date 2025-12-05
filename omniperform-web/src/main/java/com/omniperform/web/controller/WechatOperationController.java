package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import com.omniperform.common.utils.poi.ExcelUtil;
import com.omniperform.common.core.controller.BaseController;
import com.omniperform.system.domain.WechatOperationMetrics;
import com.omniperform.system.domain.WechatGroup;
import com.omniperform.system.domain.WechatGroupMember;
import com.omniperform.system.domain.WechatSopPlan;
import com.omniperform.system.service.IWechatOperationMetricsService;
import com.omniperform.system.service.IWechatGroupService;
import com.omniperform.system.service.IWechatGroupMemberService;
import com.omniperform.system.service.IWechatSopPlanService;
import com.omniperform.system.service.IWechatGroupStatisticsService;
import com.omniperform.system.service.IWechatOperationStatisticsService;
import com.omniperform.system.service.IWechatSopDetailsService;
import com.omniperform.system.domain.WechatGroupStatistics;
import com.omniperform.system.domain.WechatOperationStatistics;
import com.omniperform.system.domain.WechatSopDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 企业微信运营管理控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/wechat-operation")
@CrossOrigin(origins = "*")
@Api(tags = "企业微信运营管理")
public class WechatOperationController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(WechatOperationController.class);

    @Autowired
    private IWechatOperationMetricsService wechatOperationMetricsService;

    @Autowired
    private IWechatGroupService wechatGroupService;

    @Autowired
    private IWechatGroupMemberService wechatGroupMemberService;

    @Autowired
    private IWechatSopPlanService wechatSopPlanService;

    @Autowired
    private IWechatGroupStatisticsService wechatGroupStatisticsService;

    @Autowired
    private IWechatOperationStatisticsService wechatOperationStatisticsService;

    @Autowired
    private IWechatSopDetailsService wechatSopDetailsService;

    /**
     * 获取数据库中可用的月份列表（用于前端周期下拉）
     */
    @GetMapping("/available-months")
    @ApiOperation("获取可用月份列表")
    public Result<List<String>> getAvailableMonths() {
        try {
            // 合并运营指标与群组统计两个来源的月份，避免某模块导入后另一个模块下拉无该月
            Set<String> monthSet = new HashSet<>();
            List<String> metricMonths = wechatOperationMetricsService.selectDistinctStatMonths();
            if (metricMonths != null) { monthSet.addAll(metricMonths); }
            List<String> groupMonths = wechatGroupStatisticsService.selectDistinctStatMonths();
            if (groupMonths != null) { monthSet.addAll(groupMonths); }
            List<String> coreMonths = wechatOperationStatisticsService.selectDistinctStatMonths();
            if (coreMonths != null) { monthSet.addAll(coreMonths); }

            List<String> months = new ArrayList<>(monthSet);
            months.sort(Comparator.reverseOrder());

            // 如果数据库暂时为空，返回最近3个月占位，避免前端无数据
            if (months.isEmpty()) {
                LocalDate now = LocalDate.now();
                months.add(now.minusMonths(0).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")));
                months.add(now.minusMonths(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")));
                months.add(now.minusMonths(2).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")));
            }
            return Result.success(months);
        } catch (Exception e) {
            log.error("获取可用月份列表失败", e);
            return Result.error("获取月份列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取企业微信核心指标
     */
    @GetMapping("/metrics")
    @ApiOperation("获取企业微信核心指标")
    public Result getWechatMetrics(@RequestParam(required = false) String period) {
        try {
            Map<String, Object> metrics = new HashMap<>();
            
            // 根据period参数获取指定月份的运营指标数据
            WechatOperationMetrics queryMetrics = new WechatOperationMetrics();
            if (period != null && !period.isEmpty()) {
                queryMetrics.setStatMonth(period);
            }
            List<WechatOperationMetrics> recentMetrics = wechatOperationMetricsService.selectWechatOperationMetricsList(queryMetrics);
            
            // 计算企业微信绑定率（优先使用统计表中的绑定率）
            Map<String, Object> bindingRate = new HashMap<>();
            WechatOperationStatistics statData = null;
            if (period != null && !period.isEmpty()) {
                statData = wechatOperationStatisticsService.selectWechatOperationStatisticsByMonth(period);
            }
            if (statData != null && statData.getBindingRate() != null) {
                double bindingValue = statData.getBindingRate().doubleValue();
                bindingRate.put("value", bindingValue);
                bindingRate.put("target", 90.0);
                bindingRate.put("trend", bindingValue > 80 ? 6.2 : -2.1);
                bindingRate.put("trendDirection", bindingValue > 80 ? "up" : "down");
                bindingRate.put("progressPercent", bindingValue);
            } else if (!recentMetrics.isEmpty()) {
                // 回退：根据运营指标表的好友通过数估算绑定率
                WechatOperationMetrics latest = recentMetrics.get(0);
                double bindingValue = latest.getFriendAccepts() != null ?
                        Math.min(latest.getFriendAccepts() * 2.5, 100.0) : 85.2;
                bindingRate.put("value", bindingValue);
                bindingRate.put("target", 90.0);
                bindingRate.put("trend", bindingValue > 80 ? 6.2 : -2.1);
                bindingRate.put("trendDirection", bindingValue > 80 ? "up" : "down");
                bindingRate.put("progressPercent", bindingValue);
            } else {
                // 默认值
                bindingRate.put("value", 85.2);
                bindingRate.put("target", 90.0);
                bindingRate.put("trend", 6.2);
                bindingRate.put("trendDirection", "up");
                bindingRate.put("progressPercent", 85.2);
            }
            metrics.put("bindingRate", bindingRate);
            
            // 会员入群率 - 优先使用统计表中的导入值，其次根据period参数回退
            Map<String, Object> groupJoinRate = new HashMap<>();
            double joinRateValue;
            
            if (statData != null && statData.getJoinRate() != null) {
                joinRateValue = statData.getJoinRate().doubleValue();
            } else if ("2025-06".equals(period)) {
                joinRateValue = 69.5;
            } else if ("2025-05".equals(period)) {
                joinRateValue = 66.8;
            } else if ("2025-04".equals(period)) {
                joinRateValue = 64.2;
            } else {
                // 默认数据 (2025-07)
                joinRateValue = 72.8;
            }
            
            groupJoinRate.put("value", joinRateValue);
            groupJoinRate.put("target", 80.0);
            groupJoinRate.put("trend", joinRateValue > 70 ? 5.3 : -1.2);
            groupJoinRate.put("trendDirection", joinRateValue > 70 ? "up" : "down");
            groupJoinRate.put("progressPercent", joinRateValue);
            metrics.put("groupJoinRate", groupJoinRate);
            
            // 社群活跃度 - 优先使用统计表中的导入值，其次根据period参数回退
            Map<String, Object> groupActivity = new HashMap<>();
            double activityValue;
            
            if (statData != null && statData.getActivityScore() != null) {
                activityValue = statData.getActivityScore().doubleValue();
            } else if ("2025-06".equals(period)) {
                activityValue = 3.9;
            } else if ("2025-05".equals(period)) {
                activityValue = 3.6;
            } else if ("2025-04".equals(period)) {
                activityValue = 3.3;
            } else if (!recentMetrics.isEmpty()) {
                // 如果有数据库数据，优先使用数据库数据
                WechatOperationMetrics latest = recentMetrics.get(0);
                Integer groupInteractions = latest.getGroupInteractions();
                activityValue = groupInteractions != null ? Math.min(groupInteractions / 10.0, 5.0) : 4.2;
            } else {
                // 默认数据 (2025-07)
                activityValue = 4.2;
            }
            
            groupActivity.put("value", activityValue);
            groupActivity.put("maxValue", 5.0);
            groupActivity.put("trend", activityValue > 4.0 ? 0.3 : -0.2);
            groupActivity.put("trendDirection", activityValue > 4.0 ? "up" : "down");
            groupActivity.put("progressPercent", (activityValue / 5.0) * 100); // 转换为百分比
            metrics.put("groupActivity", groupActivity);
            
            // 企微转化率 - 优先使用统计表中的导入值，其次回退到指标估算
            Map<String, Object> conversionRate = new HashMap<>();
            if (statData != null && statData.getConversionRate() != null) {
                double conversionValue = statData.getConversionRate().doubleValue();
                conversionRate.put("value", conversionValue);
                conversionRate.put("target", 18.0);
                conversionRate.put("trend", conversionValue > 15 ? 2.1 : -0.8);
                conversionRate.put("trendDirection", conversionValue > 15 ? "up" : "down");
                conversionRate.put("progressPercent", Math.min(conversionValue / 18.0 * 100, 100.0));
            } else if (!recentMetrics.isEmpty()) {
                WechatOperationMetrics latest = recentMetrics.get(0);
                Integer activityConversions = latest.getActivityConversions();
                double conversionValue = activityConversions != null ? Math.min(activityConversions * 0.5, 20.0) : 15.6;
                conversionRate.put("value", conversionValue);
                conversionRate.put("target", 18.0);
                conversionRate.put("trend", conversionValue > 15 ? 2.1 : -0.8);
                conversionRate.put("trendDirection", conversionValue > 15 ? "up" : "down");
                conversionRate.put("progressPercent", conversionValue / 18.0 * 100);
            } else {
                conversionRate.put("value", 15.6);
                conversionRate.put("target", 18.0);
                conversionRate.put("trend", 2.1);
                conversionRate.put("trendDirection", "up");
                conversionRate.put("progressPercent", 86.7);
            }
            metrics.put("conversionRate", conversionRate);
            
            return Result.success(metrics);
        } catch (Exception e) {
            log.error("获取企业微信核心指标失败", e);
            return Result.error("获取企业微信核心指标失败: " + e.getMessage());
        }
    }

    /**
     * 获取群组活跃度趋势
     */
    @GetMapping("/group-activity-trend")
    @ApiOperation("获取群组活跃度趋势")
    public Result getGroupActivityTrend(@RequestParam(required = false) String period) {
        try {
            List<String> categories = new ArrayList<>();
            List<Double> activityData = new ArrayList<>();
            
            // 根据period参数获取指定月份的运营指标数据
            WechatOperationMetrics queryMetrics = new WechatOperationMetrics();
            if (period != null && !period.isEmpty()) {
                queryMetrics.setStatMonth(period);
            }
            List<WechatOperationMetrics> recentMetrics = wechatOperationMetricsService.selectWechatOperationMetricsList(queryMetrics);
            
            if (!recentMetrics.isEmpty()) {
                // 取最近7条数据或所有数据（如果少于7条）
                int dataCount = Math.min(7, recentMetrics.size());
                
                // 截取需要的最新数据并按时间正序排列（从旧到新）
                List<WechatOperationMetrics> displayList = new ArrayList<>(recentMetrics.subList(0, dataCount));
                // 使用sort确保按日期正序排列
                if (!displayList.isEmpty() && displayList.get(0).getStatDate() != null) {
                    displayList.sort(Comparator.comparing(WechatOperationMetrics::getStatDate));
                } else {
                    Collections.reverse(displayList);
                }

                for (int i = 0; i < displayList.size(); i++) {
                    WechatOperationMetrics metrics = displayList.get(i);
                    
                    // 格式化日期
                    String dateStr = metrics.getStatDate() != null ? 
                        new java.text.SimpleDateFormat("MM-dd").format(metrics.getStatDate()) : 
                        "01-" + (15 + i);
                    
                    categories.add(dateStr);
                    
                    // 群组活跃度 - 基于群聊互动数估算
                    Integer groupInteractions = metrics.getGroupInteractions();
                    Double activity = null;
                    if (groupInteractions != null) {
                        // 将群聊互动数映射到1-5的评分范围
                        // 调整映射范围，让150-400为合理范围，映射到2-5分
                        if (groupInteractions >= 350) {
                            activity = 5.0;
                        } else if (groupInteractions >= 300) {
                            activity = 4.5 + (groupInteractions - 300) * 0.5 / 50.0; // 4.5-5.0
                        } else if (groupInteractions >= 250) {
                            activity = 4.0 + (groupInteractions - 250) * 0.5 / 50.0; // 4.0-4.5
                        } else if (groupInteractions >= 200) {
                            activity = 3.5 + (groupInteractions - 200) * 0.5 / 50.0; // 3.5-4.0
                        } else if (groupInteractions >= 150) {
                            activity = 3.0 + (groupInteractions - 150) * 0.5 / 50.0; // 3.0-3.5
                        } else if (groupInteractions >= 100) {
                            activity = 2.5 + (groupInteractions - 100) * 0.5 / 50.0; // 2.5-3.0
                        } else {
                            activity = Math.max(1.0, 1.5 + groupInteractions * 1.0 / 100.0); // 1.5-2.5
                        }
                    } 
                    
                    if (activity != null) {
                        activityData.add(Math.round(activity * 10.0) / 10.0);
                    } else {
                         activityData.add(0.0); // 无数据时补0
                    }
                }
            } else {
                // 如果没有数据，不返回任何数据
                // 保持categories和activityData为空
            }
            
            // 构建ApexCharts期望的数据格式
            Map<String, Object> chartData = new HashMap<>();
            chartData.put("title", "近七天社群活跃度趋势");
            chartData.put("yAxisTitle", "活跃度评分 (1-5)");
            chartData.put("minValue", 1.0);
            chartData.put("maxValue", 5.0);
            chartData.put("categories", categories);
            
            // 构建series数据
            List<Map<String, Object>> series = new ArrayList<>();
            Map<String, Object> seriesData = new HashMap<>();
            seriesData.put("name", "社群活跃度");
            seriesData.put("data", activityData);
            series.add(seriesData);
            chartData.put("series", series);
            
            return Result.success(chartData);
        } catch (Exception e) {
            log.error("获取群组活跃度趋势失败", e);
            return Result.error("获取群组活跃度趋势失败: " + e.getMessage());
        }
    }

    /**
     * 获取热门社群排行
     */
    @GetMapping("/hot-groups")
    @ApiOperation("获取热门社群排行")
    public Result getHotGroups(@RequestParam(required = false) String period) {
        try {
            // 根据period参数确定查询月份
            String statMonth;
            if (period != null && !period.isEmpty()) {
                statMonth = period.trim();
            } else {
                // 默认选最新可用月份（合并两个来源）
                Set<String> monthSet = new HashSet<>();
                List<String> metricMonths = wechatOperationMetricsService.selectDistinctStatMonths();
                if (metricMonths != null) { monthSet.addAll(metricMonths); }
                List<String> groupMonths = wechatGroupStatisticsService.selectDistinctStatMonths();
                if (groupMonths != null) { monthSet.addAll(groupMonths); }
                List<String> months = new ArrayList<>(monthSet);
                months.sort(Comparator.reverseOrder());
                statMonth = !months.isEmpty() ? months.get(0) : LocalDate.now().toString().substring(0, 7);
            }
            
            // 从数据库查询热门群组数据
             List<WechatGroupStatistics> groupStatsList = wechatGroupStatisticsService.selectHotGroupsByMonth(statMonth, 10);
             List<Map<String, Object>> groups = new ArrayList<>();
             
             // 将数据库数据转换为Map格式
             String[] badgeClasses = {"bg-danger", "bg-warning", "bg-info", "", ""};
             String[] scoreClasses = {"text-success", "text-success", "text-success", 
                                    "text-warning", "text-warning"};
             
             for (int i = 0; i < groupStatsList.size() && i < 5; i++) {
                 WechatGroupStatistics stats = groupStatsList.get(i);
                 Map<String, Object> group = new HashMap<>();
                 group.put("rank", i + 1);
                 group.put("groupName", stats.getGroupName());
                 group.put("activityScore", stats.getActivityScore());
                 group.put("memberCount", stats.getMemberCount());
                 group.put("joinRate", stats.getJoinRate());
                 group.put("badgeClass", i < badgeClasses.length ? badgeClasses[i] : "");
                 group.put("scoreClass", i < scoreClasses.length ? scoreClasses[i] : "");
                 groups.add(group);
             }
            
            return Result.success(groups);
        } catch (Exception e) {
            log.error("获取热门社群排行失败", e);
            return Result.error("获取热门社群排行失败: " + e.getMessage());
        }
    }

    /**
     * 获取企业微信运营统计数据
     */
    @GetMapping("/statistics")
    @ApiOperation("获取企业微信运营统计数据")
    public Result getWechatStatistics(@RequestParam(required = false) String period) {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 确定查询月份
            String statMonth;
            if ("week".equals(period)) {
                statMonth = LocalDate.now().toString().substring(0, 7); // 当前月份
            } else if ("month".equals(period)) {
                statMonth = LocalDate.now().toString().substring(0, 7); // 当前月份
            } else if ("quarter".equals(period)) {
                statMonth = LocalDate.now().minusMonths(2).toString().substring(0, 7); // 季度开始月份
            } else {
                statMonth = LocalDate.now().toString().substring(0, 7); // 默认当前月份
            }
            
            // 从数据库获取当前月份的统计数据
            WechatOperationStatistics currentStats = wechatOperationStatisticsService.selectWechatOperationStatisticsByMonth(statMonth);
            
            if (currentStats != null) {
                // 使用数据库数据
                statistics.put("totalMembers", currentStats.getTotalMembers());
                statistics.put("boundMembers", currentStats.getBoundMembers());
                statistics.put("groupMembers", currentStats.getGroupMembers());
                statistics.put("activeGroups", currentStats.getActiveGroups());
                statistics.put("totalGroups", currentStats.getTotalGroups());
                statistics.put("monthlyConversions", currentStats.getMonthlyConversions());
                statistics.put("avgResponseTime", currentStats.getAvgResponseTime());
                statistics.put("satisfactionRate", currentStats.getSatisfactionRate());
            } else {
                // 如果没有数据库数据，返回空值或默认值
                statistics.put("totalMembers", 0);
                statistics.put("boundMembers", 0);
                statistics.put("groupMembers", 0);
                statistics.put("activeGroups", 0);
                statistics.put("totalGroups", 0);
                statistics.put("monthlyConversions", 0);
                statistics.put("avgResponseTime", "0分钟");
                statistics.put("satisfactionRate", 0.0);
            }
            
            // 获取最近几个月的趋势数据
            List<WechatOperationStatistics> recentStats = wechatOperationStatisticsService.selectRecentMonthsStatistics(6);
            List<Map<String, Object>> monthlyTrend = new ArrayList<>();
            
            if (recentStats != null && !recentStats.isEmpty()) {
                for (WechatOperationStatistics stat : recentStats) {
                    Map<String, Object> monthData = new HashMap<>();
                    // 将YYYY-MM格式转换为中文月份显示
                    String month = stat.getStatMonth();
                    if (month != null && month.length() >= 7) {
                        String monthNum = month.substring(5, 7);
                        monthData.put("month", monthNum + "月");
                    } else {
                        monthData.put("month", "未知");
                    }
                    
                    // 计算绑定率和转换率
                    double bindingRate = 0.0;
                    double conversionRate = 0.0;
                    
                    if (stat.getTotalMembers() != null && stat.getTotalMembers() > 0) {
                        bindingRate = (double) stat.getBoundMembers() / stat.getTotalMembers() * 100;
                    }
                    
                    if (stat.getBoundMembers() != null && stat.getBoundMembers() > 0) {
                        conversionRate = (double) stat.getMonthlyConversions() / stat.getBoundMembers() * 100;
                    }
                    
                    monthData.put("bindingRate", Math.round(bindingRate * 10.0) / 10.0);
                    monthData.put("conversionRate", Math.round(conversionRate * 10.0) / 10.0);
                    monthlyTrend.add(monthData);
                }
            }
            
            statistics.put("monthlyTrend", monthlyTrend);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取企业微信运营统计数据失败", e);
            return Result.error("获取企业微信运营统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取SOP详情
     */
    @GetMapping("/sop-details")
    @ApiOperation("获取SOP详情")
    public Result getSopDetails() {
        try {
            Map<String, Object> sopData = new HashMap<>();
            
            // 获取所有SOP计划数据（用于统计）
            List<WechatSopPlan> allPlans = wechatSopPlanService.selectWechatSopPlanList(new WechatSopPlan());
            
            // SOP执行统计
            Map<String, Object> executionStats = new HashMap<>();
            if (!allPlans.isEmpty()) {
                int totalPlans = allPlans.size();
                long completedPlans = allPlans.stream().filter(plan -> 
                    plan.getExecutionStatus() != null && plan.getExecutionStatus() == 3).count(); // 3表示已完成
                long pendingPlans = allPlans.stream().filter(plan -> 
                    plan.getExecutionStatus() != null && plan.getExecutionStatus() == 1).count(); // 1表示待执行
                long failedPlans = allPlans.stream().filter(plan -> 
                    plan.getExecutionStatus() != null && plan.getExecutionStatus() == 4).count(); // 4表示已取消
                
                double successRate = totalPlans > 0 ? (double) completedPlans / totalPlans * 100 : 0;
                
                executionStats.put("totalPlans", totalPlans);
                executionStats.put("completedPlans", (int) completedPlans);
                executionStats.put("pendingPlans", (int) pendingPlans);
                executionStats.put("failedPlans", (int) failedPlans);
                executionStats.put("successRate", Math.round(successRate * 10.0) / 10.0);
            } else {
                // 如果没有数据库数据，返回空值
                executionStats.put("totalPlans", 0);
                executionStats.put("completedPlans", 0);
                executionStats.put("pendingPlans", 0);
                executionStats.put("failedPlans", 0);
                executionStats.put("successRate", 0.0);
            }
            sopData.put("executionStats", executionStats);
            
            // 近期SOP执行记录
            List<Map<String, Object>> recentExecutions = new ArrayList<>();
            if (!allPlans.isEmpty()) {
                // 取最近5条记录，按创建时间倒序排列
                List<WechatSopPlan> recentPlans = allPlans.stream()
                    .sorted((a, b) -> {
                        if (a.getCreateTime() == null && b.getCreateTime() == null) return 0;
                        if (a.getCreateTime() == null) return 1;
                        if (b.getCreateTime() == null) return -1;
                        return b.getCreateTime().compareTo(a.getCreateTime());
                    })
                    .limit(5)
                    .collect(Collectors.toList());
                
                for (WechatSopPlan plan : recentPlans) {
                    Map<String, Object> execution = new HashMap<>();
                    
                    // SOP类型映射
                    String sopTypeName = getSopTypeName(plan.getSopType());
                    execution.put("sopType", sopTypeName);
                    
                    // 状态映射
                    String statusName = getExecutionStatusName(plan.getExecutionStatus());
                    execution.put("status", statusName);
                    
                    // 执行时间
                    String executionTime = plan.getExecutionTime() != null ? 
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(plan.getExecutionTime()) : 
                        (plan.getCreateTime() != null ? 
                            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(plan.getCreateTime()) : 
                            "未知时间");
                    execution.put("executionTime", executionTime);
                    
                    // 目标数量（使用计划名称长度作为模拟数据，或设置默认值）
                    execution.put("targetCount", plan.getSopName() != null ? plan.getSopName().length() * 5 : 20);
                    recentExecutions.add(execution);
                }
            }
            sopData.put("recentExecutions", recentExecutions);
            
            return Result.success(sopData);
        } catch (Exception e) {
            log.error("获取SOP详情失败", e);
            return Result.error("获取SOP详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取SOP类型名称
     */
    private String getSopTypeName(Integer sopType) {
        if (sopType == null) return "未知类型";
        switch (sopType) {
            case 1: return "新客户欢迎";
            case 2: return "产品推荐";
            case 3: return "活动邀请";
            case 4: return "满意度调研";
            case 5: return "续费提醒";
            default: return "其他类型";
        }
    }
    
    /**
     * 获取执行状态名称
     */
    private String getExecutionStatusName(Integer status) {
        if (status == null) return "未知状态";
        switch (status) {
            case 0: return "待执行";
            case 1: return "执行中";
            case 2: return "已完成";
            case 3: return "失败";
            default: return "未知状态";
        }
    }

    /**
     * 获取企业微信SOP详细方案
     */
    @GetMapping("/sop-details/{type}")
    @ApiOperation("获取企业微信SOP详细方案")
    public Result getSopDetailsByType(@PathVariable String type) {
        try {
            Map<String, Object> sopDetails = new HashMap<>();
            
            switch (type) {
                case "add-friend":
                    sopDetails.put("title", "添加好友与打标签详细方案");
                    sopDetails.put("overview", "建立规范化的好友添加流程和精准的标签体系，为后续个性化运营奠定基础。");
                    sopDetails.put("keyPoints", Arrays.asList(
                        "线下门店添加流程", "线上渠道引导", "标签体系建立", "首次沟通话术"
                    ));
                    break;
                case "one-on-one":
                    sopDetails.put("title", "一对一沟通与服务详细方案");
                    sopDetails.put("overview", "建立个性化的一对一服务体系，提升会员满意度和忠诚度。");
                    sopDetails.put("keyPoints", Arrays.asList(
                        "沟通时机把握", "个性化服务", "问题解决流程", "满意度跟踪"
                    ));
                    break;
                case "group-operation":
                    sopDetails.put("title", "社群运营与互动详细方案");
                    sopDetails.put("overview", "构建活跃的社群生态，促进会员间互动和品牌认知。");
                    sopDetails.put("keyPoints", Arrays.asList(
                        "社群规则制定", "内容策划", "互动活动", "氛围维护"
                    ));
                    break;
                default:
                    sopDetails.put("title", "企业微信运营详细方案");
                    sopDetails.put("overview", "该方案的详细内容正在完善中，敬请期待...");
                    sopDetails.put("keyPoints", Arrays.asList());
            }
            
            return Result.success(sopDetails);
        } catch (Exception e) {
            log.error("获取企业微信SOP详细方案失败", e);
            return Result.error("获取企业微信SOP详细方案失败: " + e.getMessage());
        }
    }

    /**
     * 下载企业微信运营数据导入模板
     */
    @GetMapping("/template/{templateType}")
    @ApiOperation("下载企业微信运营数据导入模板")
    public void downloadWechatTemplate(@PathVariable String templateType, HttpServletResponse response, HttpServletRequest request) {
        try {
            log.info("🔽 [模板下载] 开始下载企业微信运营模板，类型: {}", templateType);
            // 记录关键请求头，便于诊断file协议或跨域问题
            try {
                log.info("🧾 [模板下载] 请求信息 - 方法: {}, 来源IP: {}, Origin: {}, Referer: {}, UA: {}",
                        request.getMethod(),
                        request.getRemoteAddr(),
                        request.getHeader("Origin"),
                        request.getHeader("Referer"),
                        request.getHeader("User-Agent"));
            } catch (Exception e) {
                log.warn("🧾 [模板下载] 记录请求头失败: {}", e.getMessage());
            }
            
            switch (templateType) {
                case "wechat-core-metrics":
                    log.info("📊 [模板下载] 下载核心指标模板(合集)");
                    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("核心指标");
                        
                        // 设置单元格格式为文本，防止月份被Excel自动格式化
                        CellStyle textStyle = workbook.createCellStyle();
                        DataFormat format = workbook.createDataFormat();
                        textStyle.setDataFormat(format.getFormat("@"));
                        sheet.setDefaultColumnStyle(0, textStyle);

                        Row header = sheet.createRow(0);
                        header.createCell(0).setCellValue("统计月份");
                        header.createCell(1).setCellValue("企业微信绑定率");
                        header.createCell(2).setCellValue("会员入群率");
                        header.createCell(3).setCellValue("社群活跃度");
                        header.createCell(4).setCellValue("企微转化率");
                        
                        Row r1 = sheet.createRow(1);
                        Cell c0 = r1.createCell(0);
                        c0.setCellStyle(textStyle);
                        c0.setCellValue("2025-01");
                        r1.createCell(1).setCellValue(45.6);
                        r1.createCell(2).setCellValue(68.5);
                        r1.createCell(3).setCellValue(4.2);
                        r1.createCell(4).setCellValue(12.3);
                        
                        sheet.autoSizeColumn(0);
                        sheet.autoSizeColumn(1);
                        sheet.autoSizeColumn(2);
                        sheet.autoSizeColumn(3);
                        sheet.autoSizeColumn(4);
                        
                        String fileName = "核心指标模板.xlsx";
                        String encoded = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setHeader("Content-Disposition", "attachment;filename=" + encoded);
                        workbook.write(response.getOutputStream());
                    }
                    break;

                case "wechat-binding-rate":
                    log.info("📊 [模板下载] 下载企业微信绑定率模板");
                    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("企业微信绑定率");
                        Row header = sheet.createRow(0);
                        header.createCell(0).setCellValue("统计月份");
                        header.createCell(1).setCellValue("绑定率(%)");
                        
                        Row r1 = sheet.createRow(1);
                        r1.createCell(0).setCellValue("2025-01");
                        r1.createCell(1).setCellValue(45.6);
                        
                        sheet.autoSizeColumn(0);
                        sheet.autoSizeColumn(1);
                        
                        String fileName = "企业微信绑定率模板.xlsx";
                        String encoded = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setHeader("Content-Disposition", "attachment;filename=" + encoded);
                        workbook.write(response.getOutputStream());
                    }
                    break;

                case "wechat-group-join-rate":
                    log.info("📊 [模板下载] 下载会员入群率模板");
                    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("会员入群率");
                        Row header = sheet.createRow(0);
                        header.createCell(0).setCellValue("统计月份");
                        header.createCell(1).setCellValue("入群率(%)");
                        
                        Row r1 = sheet.createRow(1);
                        r1.createCell(0).setCellValue("2025-01");
                        r1.createCell(1).setCellValue(68.5);
                        
                        sheet.autoSizeColumn(0);
                        sheet.autoSizeColumn(1);
                        
                        String fileName = "会员入群率模板.xlsx";
                        String encoded = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setHeader("Content-Disposition", "attachment;filename=" + encoded);
                        workbook.write(response.getOutputStream());
                    }
                    break;

                case "wechat-activity-score":
                    log.info("📊 [模板下载] 下载社群活跃度模板");
                    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("社群活跃度");
                        Row header = sheet.createRow(0);
                        header.createCell(0).setCellValue("统计月份");
                        header.createCell(1).setCellValue("活跃度评分");
                        
                        Row r1 = sheet.createRow(1);
                        r1.createCell(0).setCellValue("2025-01");
                        r1.createCell(1).setCellValue(4.2);
                        
                        sheet.autoSizeColumn(0);
                        sheet.autoSizeColumn(1);
                        
                        String fileName = "社群活跃度模板.xlsx";
                        String encoded = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setHeader("Content-Disposition", "attachment;filename=" + encoded);
                        workbook.write(response.getOutputStream());
                    }
                    break;


                case "wechat-conversion-rate":
                    log.info("📊 [模板下载] 下载企微转化率模板（精简版）");
                    // 使用Apache POI生成仅包含必要列的精简模板：统计月份、转化率(%)
                    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("企微转化率");
                        // 表头
                        Row header = sheet.createRow(0);
                        Cell h0 = header.createCell(0);
                        h0.setCellValue("统计月份");
                        Cell h1 = header.createCell(1);
                        h1.setCellValue("转化率(%)");

                        // 示例数据行
                        Row r1 = sheet.createRow(1);
                        r1.createCell(0).setCellValue("2025-01");
                        r1.createCell(1).setCellValue(12.3);
                        Row r2 = sheet.createRow(2);
                        r2.createCell(0).setCellValue("2025-02");
                        r2.createCell(1).setCellValue(13.8);

                        sheet.autoSizeColumn(0);
                        sheet.autoSizeColumn(1);

                        String fileName = "企微转化率模板.xlsx";
                        String encoded = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setHeader("Content-Disposition", "attachment;filename=" + encoded);
                        workbook.write(response.getOutputStream());
                    }
                    break;

                case "wechat-group-statistics":
                    log.info("📊 [模板下载] 下载热门社群排行（群组统计）模板");
                    ExcelUtil<WechatGroupStatistics> groupStatisticsUtil = new ExcelUtil<>(WechatGroupStatistics.class);
                    List<WechatGroupStatistics> groupStatisticsSampleData = createWechatGroupStatisticsSampleData();
                    // 显式设置下载文件名为中文，避免浏览器使用URL路径作为默认文件名
                    try {
                        String fileName = "热门社群排行模板.xlsx";
                        String encoded = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        // 同时设置 filename 和 filename* 以兼容不同浏览器的中文文件名解析
                        response.setHeader("Content-Disposition", "attachment;filename=" + encoded + ";filename*=UTF-8''" + encoded);
                    } catch (Exception ignore) {
                        // 编码异常时，退回到默认行为
                    }
                    groupStatisticsUtil.exportExcel(response, groupStatisticsSampleData, "热门社群排行数据", "热门社群排行模板.xlsx");
                    break;

                case "wechat-group-activity-trend":
                    log.info("📊 [模板下载] 下载社群活跃度趋势模板（精简版）");
                    // 使用Apache POI生成仅包含必要列的精简模板：统计月份、群聊互动数
                    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("社群活跃度趋势");
                        
                        // 设置单元格格式为文本，防止月份被Excel自动格式化
                        CellStyle textStyle = workbook.createCellStyle();
                        DataFormat format = workbook.createDataFormat();
                        textStyle.setDataFormat(format.getFormat("@"));
                        sheet.setDefaultColumnStyle(0, textStyle);

                        // 表头
                        Row header = sheet.createRow(0);
                        Cell h0 = header.createCell(0);
                        h0.setCellValue("统计月份");
                        Cell h1 = header.createCell(1);
                        h1.setCellValue("群聊互动数");

                        // 示例数据行
                        Row r1 = sheet.createRow(1);
                        Cell c1 = r1.createCell(0);
                        c1.setCellStyle(textStyle);
                        c1.setCellValue("2025-09-01");
                        r1.createCell(1).setCellValue(120);
                        
                        Row r2 = sheet.createRow(2);
                        Cell c2 = r2.createCell(0);
                        c2.setCellStyle(textStyle);
                        c2.setCellValue("2025-09-02");
                        r2.createCell(1).setCellValue(135);

                        // 自适应列宽
                        sheet.autoSizeColumn(0);
                        sheet.autoSizeColumn(1);

                        String fileName = "社群活跃度趋势模板.xlsx";
                        String encoded = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        response.setHeader("Content-Disposition", "attachment;filename=" + encoded);
                        workbook.write(response.getOutputStream());
                    }
                    break;
                
                default:
                    log.warn("❌ [模板下载] 不支持的模板类型: {}", templateType);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
            }
            
            log.info("✅ [模板下载] 企业微信运营模板下载完成，类型: {}", templateType);
            
        } catch (Exception e) {
            log.error("❌ [模板下载] 下载企业微信运营模板失败，类型: {}", templateType, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 批量导入企业微信运营数据
     */
    @PostMapping({"/import/batch", "/batchImport"})
    @ApiOperation("批量导入企业微信运营数据")
    public Result<Map<String, Object>> batchImport(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("dataType") String dataType,
                                                   @RequestParam(value = "updateSupport", defaultValue = "true") Boolean updateSupport,
                                                   HttpServletRequest request) {
        try {
            log.info("🚀 [批量导入] 开始批量导入企业微信运营数据，数据类型: {}, 文件名: {}", dataType, file.getOriginalFilename());
            // 记录关键请求头，便于诊断file协议或跨域问题
            try {
                log.info("🧾 [批量导入] 请求信息 - 方法: {}, 来源IP: {}, Origin: {}, Referer: {}, UA: {}",
                        request.getMethod(),
                        request.getRemoteAddr(),
                        request.getHeader("Origin"),
                        request.getHeader("Referer"),
                        request.getHeader("User-Agent"));
                log.info("🧾 [批量导入] 内容类型: {}", request.getHeader("Content-Type"));
            } catch (Exception e) {
                log.warn("🧾 [批量导入] 记录请求头失败: {}", e.getMessage());
            }
            log.info("🚀 [批量导入] 文件详情 - 大小: {} bytes, 内容类型: {}, 更新支持: {}", 
                    file.getSize(), file.getContentType(), updateSupport);
            
            // 文件格式验证
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls"))) {
                log.warn("❌ [批量导入] 不支持的文件格式: {}", fileName);
                return Result.error("请上传Excel文件（.xlsx或.xls格式）");
            }
            log.info("✅ [批量导入] 文件格式验证通过: {}", fileName);
            
            // 文件大小验证（10MB限制）
            if (file.getSize() > 10 * 1024 * 1024) {
                log.warn("❌ [批量导入] 文件过大: {} bytes", file.getSize());
                return Result.error("文件大小不能超过10MB");
            }
            log.info("✅ [批量导入] 文件大小验证通过: {} bytes", file.getSize());
            
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failureCount = 0;
            List<String> errorMessages = new ArrayList<>();
            
            // 获取当前操作用户（空安全，未登录时使用默认用户名）
            String operName = "system";
            String loginName = getLoginName();
            if (loginName != null && !loginName.trim().isEmpty()) {
                operName = loginName;
                log.info("✅ [批量导入] 获取到当前登录用户: {}", operName);
            } else {
                log.info("ℹ️ [批量导入] 未登录或用户名为空，使用默认用户名: system");
            }
            
            log.info("🔄 [批量导入] 开始处理数据类型: {}", dataType);
            
            switch (dataType) {
                case "wechat-core-metrics":
                    log.info("📊 [核心指标] 开始解析Excel文件...");
                    ExcelUtil<WechatOperationStatistics> coreMetricsUtil = new ExcelUtil<>(WechatOperationStatistics.class);
                    
                    try {
                        byte[] fileBytes = file.getBytes();
                        List<WechatOperationStatistics> coreMetricsList = null;
                        int chosenHeaderRow = -1;

                        try {
                            coreMetricsList = coreMetricsUtil.importExcel(new ByteArrayInputStream(fileBytes), 0);
                            if (coreMetricsList == null || coreMetricsList.isEmpty()) {
                                coreMetricsList = coreMetricsUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            }
                        } catch (Exception e) {
                            try {
                                coreMetricsList = coreMetricsUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            } catch (Exception ex) {
                                throw ex;
                            }
                        }

                        if (coreMetricsList == null || coreMetricsList.isEmpty()) {
                            log.error("📊 [核心指标] Excel文件中没有解析到任何数据");
                            return Result.error("Excel文件中没有找到有效的数据行，请检查文件内容");
                        }

                        boolean headerMismatch = coreMetricsList.stream().allMatch(s -> s == null || (
                                s.getStatMonth() == null &&
                                s.getBindingRate() == null &&
                                s.getJoinRate() == null &&
                                s.getActivityScore() == null &&
                                s.getConversionRate() == null
                        ));
                        if (headerMismatch) {
                            log.error("📊 [核心指标] 解析后所有行关键字段均为空");
                            return Result.error("Excel列名不匹配：请使用模板并确保首行为字段名");
                        }

                        log.info("💾 [核心指标] 开始保存数据到数据库...");
                        for (WechatOperationStatistics data : coreMetricsList) {
                            try {
                                String month = data.getStatMonth();
                                if (month == null || month.trim().isEmpty()) {
                                    failureCount++;
                                    errorMessages.add("统计月份为空");
                                    continue;
                                }

                                WechatOperationStatistics existing = wechatOperationStatisticsService.selectWechatOperationStatisticsByMonth(month);
                                if (existing != null) {
                                    if (Boolean.TRUE.equals(updateSupport)) {
                                        data.setStatId(existing.getStatId());
                                        data.setUpdateBy(operName);
                                        data.setUpdateTime(new Date());
                                        wechatOperationStatisticsService.updateWechatOperationStatistics(data);
                                        successCount++;
                                    } else {
                                        failureCount++;
                                        errorMessages.add("月份 " + month + " 已存在，未更新");
                                    }
                                } else {
                                    data.setCreateBy(operName);
                                    data.setCreateTime(new Date());
                                    wechatOperationStatisticsService.insertWechatOperationStatistics(data);
                                    successCount++;
                                }
                            } catch (Exception e) {
                                failureCount++;
                                errorMessages.add("保存失败: " + e.getMessage());
                                log.error("❌ [核心指标] 保存失败", e);
                            }
                        }
                        log.info("💾 [核心指标] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);

                    } catch (Exception e) {
                        log.error("❌ [核心指标] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                    }
                    break;

                case "wechat-metrics":
                    log.info("📊 [企业微信运营指标] 开始解析Excel文件...");
                    ExcelUtil<WechatOperationMetrics> metricsUtil = new ExcelUtil<>(WechatOperationMetrics.class);
                    
                    try {
                        // 读取文件为字节数组，便于多次尝试不同表头行
                        byte[] fileBytes = file.getBytes();
                        List<WechatOperationMetrics> metricsList = null;
                        int chosenHeaderRow = 0;

                        try {
                            // 尝试默认导入（首行表头）
                            metricsList = metricsUtil.importExcel(new ByteArrayInputStream(fileBytes), 0);
                            if (metricsList == null || metricsList.isEmpty()) {
                                // 如果第一行没数据，尝试第二行
                                metricsList = metricsUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            }
                        } catch (Exception e) {
                            log.warn("首行导入失败，尝试第2行作为表头", e);
                            try {
                                metricsList = metricsUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            } catch (Exception ex) {
                                log.error("导入失败", ex);
                                throw ex;
                            }
                        }

                        log.info("📊 [企业微信运营指标] Excel解析完成，使用表头行: {}，解析到 {} 条数据",
                                chosenHeaderRow, metricsList != null ? metricsList.size() : 0);
                        
                        if (metricsList == null || metricsList.isEmpty()) {
                            log.error("📊 [企业微信运营指标] Excel文件中没有解析到任何数据");
                            return Result.error("Excel文件中没有找到有效的数据行，请检查文件内容");
                        }
                        
                        // 解析结果防御性校验：如果所有行的关键字段均为空，视为列头不匹配或首行不是字段名
                        boolean headerMismatchSuspected = metricsList.stream().allMatch(m -> m == null || (
                                m.getUserName() == null &&
                                m.getDepartment() == null &&
                                m.getStatDate() == null &&
                                m.getFriendRequests() == null &&
                                m.getFriendAccepts() == null &&
                                m.getChatMessages() == null &&
                                m.getReportGenerates() == null
                        ));
                        if (headerMismatchSuspected) {
                            log.error("📊 [企业微信运营指标] 解析后所有行关键字段均为空，疑似列头不匹配或首行不是字段名。请使用模板下载接口获取规范模板，并确保第一行是字段名。");
                            return Result.error("Excel列名不匹配：请使用模板并确保首行为字段名");
                        }
                        // 过滤掉空行，避免后续空指针
                        metricsList = metricsList.stream().filter(Objects::nonNull).collect(Collectors.toList());
                        if (metricsList.isEmpty()) {
                            log.error("📊 [企业微信运营指标] 解析后有效数据行为空");
                            return Result.error("Excel解析未得到有效数据，请检查模板与列名");
                        }

                        // 打印前几条数据的详细信息用于调试
                        for (int i = 0; i < Math.min(3, metricsList.size()); i++) {
                            WechatOperationMetrics metrics = metricsList.get(i);
                            if (metrics != null) {
                                log.info("📊 [企业微信运营指标] 第{}条数据详情 - 用户: {}, 部门: {}, 好友申请: {}, 好友通过: {}", 
                                        (i + 1), metrics.getUserName(), metrics.getDepartment(), 
                                        metrics.getFriendRequests(), metrics.getFriendAccepts());
                            }
                        }
                        
                        log.info("💾 [企业微信运营指标] 开始保存数据到数据库...");
                        // 批量保存运营指标数据
                        for (int rowIndex = 0; rowIndex < metricsList.size(); rowIndex++) {
                            WechatOperationMetrics metricsData = metricsList.get(rowIndex);
                            if (metricsData == null) {
                                failureCount++;
                                errorMessages.add("第" + (rowIndex + 1) + "行为空，已跳过");
                                log.warn("⚠️ [企业微信运营指标] 第{}行数据为空，跳过", (rowIndex + 1));
                                continue;
                            }
                            try {
                                // 设置创建信息
                                metricsData.setCreateBy(operName);
                                metricsData.setCreateTime(new Date());
                                
                                // 调用Service保存数据
                                int insertResult = wechatOperationMetricsService.insertWechatOperationMetrics(metricsData);
                                if (insertResult > 0) {
                                    successCount++;
                                    log.debug("💾 [企业微信运营指标] 保存成功 - 用户: {}", metricsData.getUserName() != null ? metricsData.getUserName() : "未知用户");
                                } else {
                                    failureCount++;
                                    String errorMsg = "保存企业微信运营指标数据失败";
                                    errorMessages.add(errorMsg);
                                    log.error("❌ [企业微信运营指标] 保存失败 - 用户: {}", metricsData.getUserName() != null ? metricsData.getUserName() : "未知用户");
                                }
                            } catch (Exception e) {
                                failureCount++;
                                String errorMsg = "保存企业微信运营指标数据失败: " + (e.getMessage() != null ? e.getMessage() : e.toString());
                                errorMessages.add(errorMsg);
                                String safeUser = metricsData.getUserName() != null ? metricsData.getUserName() : "未知用户";
                                log.error("❌ [企业微信运营指标] 保存失败 - 用户: {}, 错误: {}", safeUser, e.getMessage());
                                log.error("❌ [企业微信运营指标] 保存失败堆栈 - 用户: {}", safeUser, e);
                            }
                        }
                        log.info("💾 [企业微信运营指标] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                        
                    } catch (Exception e) {
                        log.error("❌ [企业微信运营指标] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                    }
                    break;
                    
                case "wechat-statistics":
                    log.info("📊 [企业微信运营统计] 开始解析Excel文件...");
                    ExcelUtil<WechatOperationStatistics> statisticsUtil = new ExcelUtil<>(WechatOperationStatistics.class);
                    
                    try {
                        // 读取文件为字节数组，便于多次尝试不同表头行
                        byte[] fileBytes = file.getBytes();
                        List<WechatOperationStatistics> statisticsList = null;
                        int chosenHeaderRow = -1;

                        try {
                            statisticsList = statisticsUtil.importExcel(new ByteArrayInputStream(fileBytes), 0);
                            if (statisticsList == null || statisticsList.isEmpty()) {
                                statisticsList = statisticsUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            }
                        } catch (Exception e) {
                            try {
                                statisticsList = statisticsUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            } catch (Exception ex) {
                                throw ex;
                            }
                        }

                        log.info("📊 [企业微信运营统计] Excel解析完成，使用表头行: {}，解析到 {} 条数据",
                                chosenHeaderRow, statisticsList != null ? statisticsList.size() : 0);
                        
                        if (statisticsList == null || statisticsList.isEmpty()) {
                            log.error("📊 [企业微信运营统计] Excel文件中没有解析到任何数据");
                            return Result.error("Excel文件中没有找到有效的数据行，请检查文件内容");
                        }

                        // 解析结果防御性校验：如果所有行的关键字段均为空，视为列头不匹配或首行不是字段名
                        boolean headerMismatchSuspected = statisticsList.stream().allMatch(s -> s == null || (
                                s.getStatMonth() == null &&
                                s.getTotalMembers() == null &&
                                s.getBoundMembers() == null &&
                                s.getGroupMembers() == null &&
                                s.getActiveGroups() == null &&
                                s.getTotalGroups() == null &&
                                s.getMonthlyConversions() == null &&
                                s.getAvgResponseTime() == null &&
                                s.getSatisfactionRate() == null &&
                                s.getBindingRate() == null &&
                                s.getConversionRate() == null
                        ));
                        if (headerMismatchSuspected) {
                            log.error("📊 [企业微信运营统计] 解析后所有行关键字段均为空，疑似列头不匹配或首行不是字段名。请使用模板下载接口获取规范模板，并确保第一行是字段名。");
                            return Result.error("Excel列名不匹配：请使用模板并确保首行为字段名");
                        }

                        // 防御性校验：仅解析到月份，其它字段全为null，基本可以确定列头不匹配
                        boolean onlyMonthAvailable = statisticsList.stream().allMatch(s -> s != null &&
                                s.getStatMonth() != null &&
                                s.getTotalMembers() == null &&
                                s.getBoundMembers() == null &&
                                s.getGroupMembers() == null &&
                                s.getActiveGroups() == null &&
                                s.getTotalGroups() == null &&
                                s.getMonthlyConversions() == null &&
                                s.getAvgResponseTime() == null &&
                                s.getSatisfactionRate() == null &&
                                s.getBindingRate() == null &&
                                s.getConversionRate() == null);
                        if (onlyMonthAvailable) {
                            log.error("📊 [企业微信运营统计] 仅解析到月份，其它字段均为空，疑似列头不匹配。请使用模板下载接口获取规范模板，并确保第一行是字段名。");
                            return Result.error("仅解析到月份，其它字段为空：请使用模板并确保首行为字段名");
                        }

                        // 打印前几条数据的详细信息用于调试
                        for (int i = 0; i < Math.min(3, statisticsList.size()); i++) {
                            WechatOperationStatistics statistics = statisticsList.get(i);
                            if (statistics != null) {
                                log.info("📊 [企业微信运营统计] 第{}条数据详情 - 月份: {}, 总成员: {}, 绑定成员: {}, 群成员: {}", 
                                        (i + 1), statistics.getStatMonth(), statistics.getTotalMembers(), 
                                        statistics.getBoundMembers(), statistics.getGroupMembers());
                            }
                        }
                        
                        log.info("💾 [企业微信运营统计] 开始保存数据到数据库...");
                        // 批量保存运营统计数据（支持重复月份更新或跳过）
                        for (WechatOperationStatistics statisticsData : statisticsList) {
                            try {
                                String month = statisticsData.getStatMonth();
                                if (month == null || month.trim().isEmpty()) {
                                    failureCount++;
                                    String errorMsg = "统计月份为空，无法保存该条记录";
                                    errorMessages.add(errorMsg);
                                    log.error("❌ [企业微信运营统计] 保存失败 - 原因: 统计月份为空");
                                    continue;
                                }

                                // 查重：根据月份查看是否已存在记录
                                WechatOperationStatistics existing = wechatOperationStatisticsService.selectWechatOperationStatisticsByMonth(month);
                                if (existing != null) {
                                    // 已存在：依据updateSupport处理
                                    if (Boolean.TRUE.equals(updateSupport)) {
                                        statisticsData.setStatId(existing.getStatId());
                                        // 安全合并：新值为空则保留旧值，避免将字段更新为null
                                        statisticsData.setTotalMembers(statisticsData.getTotalMembers() != null ? statisticsData.getTotalMembers() : existing.getTotalMembers());
                                        statisticsData.setBoundMembers(statisticsData.getBoundMembers() != null ? statisticsData.getBoundMembers() : existing.getBoundMembers());
                                        statisticsData.setGroupMembers(statisticsData.getGroupMembers() != null ? statisticsData.getGroupMembers() : existing.getGroupMembers());
                                        statisticsData.setActiveGroups(statisticsData.getActiveGroups() != null ? statisticsData.getActiveGroups() : existing.getActiveGroups());
                                        statisticsData.setTotalGroups(statisticsData.getTotalGroups() != null ? statisticsData.getTotalGroups() : existing.getTotalGroups());
                                        statisticsData.setMonthlyConversions(statisticsData.getMonthlyConversions() != null ? statisticsData.getMonthlyConversions() : existing.getMonthlyConversions());
                                        statisticsData.setAvgResponseTime(statisticsData.getAvgResponseTime() != null ? statisticsData.getAvgResponseTime() : existing.getAvgResponseTime());
                                        statisticsData.setSatisfactionRate(statisticsData.getSatisfactionRate() != null ? statisticsData.getSatisfactionRate() : existing.getSatisfactionRate());
                                        statisticsData.setBindingRate(statisticsData.getBindingRate() != null ? statisticsData.getBindingRate() : existing.getBindingRate());
                                        statisticsData.setConversionRate(statisticsData.getConversionRate() != null ? statisticsData.getConversionRate() : existing.getConversionRate());
                                        statisticsData.setUpdateBy(operName);
                                        statisticsData.setUpdateTime(new Date());
                                        // 执行更新（底层XML按非空字段SET更新）
                                        int updateResult = wechatOperationStatisticsService.updateWechatOperationStatistics(statisticsData);
                                        if (updateResult > 0) {
                                            successCount++;
                                            log.debug("💾 [企业微信运营统计] 更新成功 - 月份: {}", month);
                                        } else {
                                            failureCount++;
                                            String errorMsg = String.format("更新企业微信运营统计数据失败 - 月份: %s", month);
                                            errorMessages.add(errorMsg);
                                            log.error("❌ [企业微信运营统计] 更新失败 - 月份: {}", month);
                                        }
                                    } else {
                                        // 不允许更新：跳过并记录提示
                                        failureCount++;
                                        String errorMsg = String.format("月份 %s 已存在，未更新（请勾选允许更新后重试或删除旧数据）", month);
                                        errorMessages.add(errorMsg);
                                        log.warn("⚠️ [企业微信运营统计] 月份重复，未更新 - 月份: {}", month);
                                    }
                                } else {
                                    // 不存在：插入新纪录
                                    statisticsData.setCreateBy(operName);
                                    statisticsData.setCreateTime(new Date());
                                    int insertResult = wechatOperationStatisticsService.insertWechatOperationStatistics(statisticsData);
                                    if (insertResult > 0) {
                                        successCount++;
                                        log.debug("💾 [企业微信运营统计] 新增成功 - 月份: {}", month);
                                    } else {
                                        failureCount++;
                                        String errorMsg = String.format("新增企业微信运营统计数据失败 - 月份: %s", month);
                                        errorMessages.add(errorMsg);
                                        log.error("❌ [企业微信运营统计] 新增失败 - 月份: {}", month);
                                    }
                                }
                            } catch (Exception e) {
                                failureCount++;
                                String safeMonth = statisticsData != null ? statisticsData.getStatMonth() : "未知月份";
                                String errorMsg = "保存企业微信运营统计数据失败: " + (e.getMessage() != null ? e.getMessage() : e.toString());
                                errorMessages.add(errorMsg);
                                log.error("❌ [企业微信运营统计] 保存失败 - 月份: {}, 错误: {}", safeMonth, e.getMessage(), e);
                            }
                        }
                        log.info("💾 [企业微信运营统计] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                        
                    } catch (Exception e) {
                        log.error("❌ [企业微信运营统计] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                    }
                    break;

                case "wechat-group-statistics":
                    log.info("📊 [热门社群排行/群组统计] 开始解析Excel文件...");
                    ExcelUtil<WechatGroupStatistics> groupStatisticsUtil = new ExcelUtil<>(WechatGroupStatistics.class);
                    try {
                        byte[] fileBytes = file.getBytes();
                        List<WechatGroupStatistics> groupStatisticsList = null;
                        int chosenHeaderRow = -1;

                        try {
                            groupStatisticsList = groupStatisticsUtil.importExcel(new ByteArrayInputStream(fileBytes), 0);
                            if (groupStatisticsList == null || groupStatisticsList.isEmpty()) {
                                groupStatisticsList = groupStatisticsUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            }
                        } catch (Exception e) {
                            try {
                                groupStatisticsList = groupStatisticsUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            } catch (Exception ex) {
                                throw ex;
                            }
                        }

                        log.info("📊 [群组统计] Excel解析完成，使用表头行: {}，解析到 {} 条数据",
                                chosenHeaderRow, groupStatisticsList != null ? groupStatisticsList.size() : 0);

                        if (groupStatisticsList == null || groupStatisticsList.isEmpty()) {
                            log.error("📊 [群组统计] Excel文件中没有解析到任何数据");
                            return Result.error("Excel文件中没有找到有效的数据行，请检查文件内容");
                        }

                        boolean headerMismatchSuspected = groupStatisticsList.stream().allMatch(g -> g == null || (
                                g.getStatMonth() == null &&
                                g.getGroupId() == null &&
                                g.getActivityScore() == null &&
                                g.getJoinRate() == null &&
                                g.getInteractionCount() == null &&
                                g.getMessageCount() == null &&
                                g.getActiveMemberCount() == null
                        ));
                        if (headerMismatchSuspected) {
                            log.error("📊 [群组统计] 解析后所有行关键字段均为空，疑似列头不匹配或首行不是字段名。请使用模板下载接口获取规范模板，并确保第一行是字段名。");
                            return Result.error("Excel列名不匹配：请使用模板并确保首行为字段名");
                        }

                        // 仅解析到群组ID+月份，其它关键字段均为空，判定模板/列头不匹配
                        boolean onlyKeysAvailable = groupStatisticsList.stream().allMatch(g -> g != null &&
                                g.getStatMonth() != null && g.getGroupId() != null &&
                                g.getActivityScore() == null && g.getJoinRate() == null &&
                                g.getInteractionCount() == null && g.getMessageCount() == null &&
                                g.getActiveMemberCount() == null);
                        if (onlyKeysAvailable) {
                            log.error("📊 [群组统计] 仅解析到群组ID与月份，其它字段均为空，疑似列头不匹配。请使用模板下载接口获取规范模板，并确保第一行是字段名。");
                            return Result.error("仅解析到群组ID与月份，其它字段为空：请使用模板并确保首行为字段名");
                        }

                        // 打印前几条数据用于调试
                        for (int i = 0; i < Math.min(3, groupStatisticsList.size()); i++) {
                            WechatGroupStatistics gs = groupStatisticsList.get(i);
                            if (gs != null) {
                                log.info("📊 [群组统计] 第{}条数据详情 - 月份: {}, 群组ID: {}, 活跃度评分: {}, 入群率: {}, 互动次数: {}",
                                        (i + 1), gs.getStatMonth(), gs.getGroupId(), gs.getActivityScore(), gs.getJoinRate(), gs.getInteractionCount());
                            }
                        }

                        log.info("💾 [群组统计] 开始保存数据到数据库...");
                        for (WechatGroupStatistics data : groupStatisticsList) {
                            try {
                                // 基础校验
                                if (data == null) {
                                    failureCount++;
                                    errorMessages.add("存在空行，已跳过");
                                    continue;
                                }
                                String month = data.getStatMonth() != null ? data.getStatMonth().trim() : null;
                                data.setStatMonth(month);
                                Long groupId = data.getGroupId();
                                if (month == null || month.trim().isEmpty() || groupId == null) {
                                    failureCount++;
                                    errorMessages.add("群组ID或统计月份为空，无法保存该条记录");
                                    log.error("❌ [群组统计] 保存失败 - 原因: 群组ID或统计月份为空");
                                    continue;
                                }

                                // 校验并自动创建缺失的群组，确保外键不报错
                                try {
                                    WechatGroup groupEntity = wechatGroupService.selectWechatGroupByGroupId(groupId);
                                    if (groupEntity == null) {
                                        // 自动创建群组（最小字段集），以保证导入不中断
                                        WechatGroup autoGroup = new WechatGroup();
                                        autoGroup.setGroupId(groupId);
                                        autoGroup.setGroupName("群组-" + groupId);
                                        autoGroup.setStatus(1);
                                        autoGroup.setCreateBy(operName);
                                        autoGroup.setCreateTime(new Date());
                                        int created = wechatGroupService.insertWechatGroup(autoGroup);
                                        if (created > 0) {
                                            log.info("✅ [群组统计] 自动创建缺失群组成功 - 群组ID: {}", groupId);
                                        } else {
                                            failureCount++;
                                            errorMessages.add("自动创建缺失群组失败: 群组ID=" + groupId + ", 月份=" + month);
                                            log.error("❌ [群组统计] 自动创建群组失败 - 群组ID: {}，月份: {}", groupId, month);
                                            continue;
                                        }
                                    }
                                } catch (Exception ex) {
                                    // 若查询或创建群组发生异常，标记失败并继续后续行，避免中断整个导入
                                    failureCount++;
                                    errorMessages.add("群组校验/创建失败，已跳过: 群组ID=" + groupId + ", 月份=" + month + ", 错误=" + (ex.getMessage() != null ? ex.getMessage() : ex.toString()));
                                    log.error("❌ [群组统计] 群组校验/创建异常", ex);
                                    continue;
                                }

                                // 查重：根据群组ID+月份
                                WechatGroupStatistics query = new WechatGroupStatistics();
                                query.setGroupId(groupId);
                                query.setStatMonth(month);
                                List<WechatGroupStatistics> existedList = wechatGroupStatisticsService.selectWechatGroupStatisticsList(query);
                                WechatGroupStatistics existing = (existedList != null && !existedList.isEmpty()) ? existedList.get(0) : null;

                                if (existing != null) {
                                    if (Boolean.TRUE.equals(updateSupport)) {
                                        // 安全合并更新
                                        data.setStatId(existing.getStatId());
                                        data.setActivityScore(data.getActivityScore() != null ? data.getActivityScore() : existing.getActivityScore());
                                        data.setJoinRate(data.getJoinRate() != null ? data.getJoinRate() : existing.getJoinRate());
                                        data.setInteractionCount(data.getInteractionCount() != null ? data.getInteractionCount() : existing.getInteractionCount());
                                        data.setMessageCount(data.getMessageCount() != null ? data.getMessageCount() : existing.getMessageCount());
                                        data.setActiveMemberCount(data.getActiveMemberCount() != null ? data.getActiveMemberCount() : existing.getActiveMemberCount());
                                        data.setUpdateBy(operName);
                                        data.setUpdateTime(new Date());

                                        int update = wechatGroupStatisticsService.updateWechatGroupStatistics(data);
                                        if (update > 0) {
                                            successCount++;
                                        } else {
                                            failureCount++;
                                            errorMessages.add("更新群组统计失败: 群组ID=" + groupId + ", 月份=" + month);
                                        }
                                    } else {
                                        // 跳过
                                        log.warn("⚠️ [群组统计] 记录已存在，按配置跳过 - 群组ID: {}, 月份: {}", groupId, month);
                                        errorMessages.add("记录已存在，已跳过: 群组ID=" + groupId + ", 月份=" + month);
                                    }
                                } else {
                                    // 新增
                                    data.setCreateBy(operName);
                                    data.setCreateTime(new Date());
                                    int insert = wechatGroupStatisticsService.insertWechatGroupStatistics(data);
                                    if (insert > 0) {
                                        successCount++;
                                    } else {
                                        failureCount++;
                                        errorMessages.add("保存群组统计失败: 群组ID=" + groupId + ", 月份=" + month);
                                    }
                                }
                            } catch (Exception e) {
                                failureCount++;
                                errorMessages.add("保存群组统计数据失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                                log.error("❌ [群组统计] 保存失败 - 错误: {}", e.getMessage());
                                log.error("❌ [群组统计] 保存失败堆栈", e);
                            }
                        }
                        log.info("💾 [群组统计] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                    } catch (Exception e) {
                        log.error("❌ [群组统计] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                    }
                    break;

                case "wechat-group-activity-trend":
                    log.info("📊 [社群活跃度趋势] 开始解析Excel文件（精简模板）...");
                    ExcelUtil<WechatOperationMetrics> trendUtil = new ExcelUtil<>(WechatOperationMetrics.class);
                    try {
                        byte[] fileBytes = file.getBytes();
                        List<WechatOperationMetrics> trendList = null;
                        int chosenHeaderRow = -1;

                        try {
                            trendList = trendUtil.importExcel(new ByteArrayInputStream(fileBytes), 0);
                            if (trendList == null || trendList.isEmpty()) {
                                trendList = trendUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            }
                        } catch (Exception e) {
                            try {
                                trendList = trendUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            } catch (Exception ex) {
                                throw ex;
                            }
                        }

                        log.info("📊 [社群活跃度趋势] Excel解析完成，使用表头行: {}，解析到 {} 条数据", chosenHeaderRow, trendList != null ? trendList.size() : 0);
                        if (trendList == null || trendList.isEmpty()) {
                            return Result.error("Excel文件中没有找到有效的数据行，请检查文件内容");
                        }

                        // 允许精简列：只要包含统计月份或群聊互动数中的任一关键列即可
                        boolean headerMismatchSuspected = trendList.stream().allMatch(m -> m == null || (
                                m.getStatMonth() == null && m.getGroupInteractions() == null
                        ));
                        if (headerMismatchSuspected) {
                            log.error("📊 [社群活跃度趋势] 未检测到关键列（统计月份或群聊互动数），疑似表头不匹配");
                            return Result.error("Excel列名不匹配：请使用社群活跃度趋势模板，并确保首行为字段名");
                        }

                        // 过滤空行
                        trendList = trendList.stream().filter(Objects::nonNull).collect(Collectors.toList());

                        // 保存数据：仅依赖statMonth与groupInteractions
                        log.info("💾 [社群活跃度趋势] 开始保存数据到数据库...");
                        for (int rowIndex = 0; rowIndex < trendList.size(); rowIndex++) {
                            WechatOperationMetrics metricsData = trendList.get(rowIndex);
                            if (metricsData == null) {
                                failureCount++;
                                errorMessages.add("第" + (rowIndex + 1) + "行为空，已跳过");
                                continue;
                            }
                            if (metricsData.getStatMonth() == null || metricsData.getStatMonth().trim().isEmpty()) {
                                failureCount++;
                                errorMessages.add("第" + (rowIndex + 1) + "行缺少统计月份(statMonth)");
                                continue;
                            }
                            if (metricsData.getGroupInteractions() == null) {
                                failureCount++;
                                errorMessages.add("第" + (rowIndex + 1) + "行缺少群聊互动数(groupInteractions)");
                                continue;
                            }
                            try {
                                // 处理统计日期
                                Date statDate = null;
                                if (metricsData.getStatDate() != null) {
                                    statDate = metricsData.getStatDate();
                                } else if (metricsData.getStatMonth() != null) {
                                    try {
                                        String dateStr = metricsData.getStatMonth().trim();
                                        // 尝试解析 yyyy-MM-dd 格式
                                        try {
                                            LocalDate day = LocalDate.parse(dateStr);
                                            statDate = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                        } catch (Exception e1) {
                                            // 如果不是日期格式，尝试作为月份处理（回退逻辑，虽然模板已要求是日期）
                                            LocalDate firstDay = LocalDate.parse(dateStr + "-01");
                                            statDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                        }
                                    } catch (Exception e) {
                                        log.warn("⚠️ [社群活跃度趋势] 日期解析失败: {}", metricsData.getStatMonth());
                                    }
                                }

                                if (statDate == null) {
                                    failureCount++;
                                    errorMessages.add("第" + (rowIndex + 1) + "行缺少有效日期");
                                    continue;
                                }
                                
                                metricsData.setStatDate(statDate);

                                // 检查是否存在该日期的记录（针对社群活跃度趋势，假设每天一条或需覆盖）
                                // 注意：这里使用 metric_type 或类似的区分可能更严谨，但目前表结构未明确区分类型字段，暂按 stat_date + group_interactions 非空来判断
                                WechatOperationMetrics existing = wechatOperationMetricsService.selectWechatOperationMetricsByDateAndType(statDate, null); // 假设type为null或特定值
                                
                                if (existing != null) {
                                    // 覆盖更新
                                    existing.setGroupInteractions(metricsData.getGroupInteractions());
                                    existing.setStatMonth(metricsData.getStatMonth()); // 更新月份字段
                                    existing.setUpdateBy(operName);
                                    existing.setUpdateTime(new Date());
                                    wechatOperationMetricsService.updateWechatOperationMetrics(existing);
                                } else {
                                    // 新增
                                    metricsData.setCreateBy(operName);
                                    metricsData.setCreateTime(new Date());
                                    wechatOperationMetricsService.insertWechatOperationMetrics(metricsData);
                                }
                                successCount++;
                            } catch (Exception e) {
                                failureCount++;
                                errorMessages.add("保存社群活跃度趋势数据失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                                log.error("❌ [社群活跃度趋势] 保存失败 - 行: {}, 错误: {}", (rowIndex + 1), e.getMessage());
                            }
                        }
                        log.info("💾 [社群活跃度趋势] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                    } catch (Exception e) {
                        log.error("❌ [社群活跃度趋势] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                    }
                    break;

                case "wechat-binding-rate":
                    log.info("📈 [企业微信绑定率] 开始解析Excel文件（精简模板）...");
                    ExcelUtil<WechatOperationStatistics> bindingRateUtil = new ExcelUtil<>(WechatOperationStatistics.class);
                    try {
                        byte[] fileBytes = file.getBytes();
                        List<WechatOperationStatistics> bindingList = null;
                        int chosenHeaderRow = -1;

                        try {
                            bindingList = bindingRateUtil.importExcel(new ByteArrayInputStream(fileBytes), 0);
                            if (bindingList == null || bindingList.isEmpty()) {
                                bindingList = bindingRateUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            }
                        } catch (Exception e) {
                            try {
                                bindingList = bindingRateUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            } catch (Exception ex) {
                                throw ex;
                            }
                        }

                        log.info("📈 [企业微信绑定率] Excel解析完成，使用表头行: {}，解析到 {} 条数据", chosenHeaderRow, bindingList != null ? bindingList.size() : 0);
                        if (bindingList == null || bindingList.isEmpty()) {
                            return Result.error("Excel文件中没有找到有效的数据行，请检查文件内容");
                        }

                        // 防御：若所有行均缺少关键列，判定列头不匹配
                        boolean headerMismatch = bindingList.stream().allMatch(s -> s == null || (
                                s.getStatMonth() == null && s.getBindingRate() == null
                        ));
                        if (headerMismatch) {
                            return Result.error("Excel列名不匹配：请使用绑定率模板并确保首行为字段名");
                        }

                        // 过滤空行
                        bindingList = bindingList.stream().filter(Objects::nonNull).collect(Collectors.toList());

                        log.info("💾 [企业微信绑定率] 开始保存数据到数据库...");
                        for (WechatOperationStatistics row : bindingList) {
                            try {
                                if (row.getStatMonth() == null || row.getStatMonth().trim().isEmpty()) {
                                    failureCount++;
                                    errorMessages.add("统计月份为空，无法保存该条记录");
                                    continue;
                                }
                                if (row.getBindingRate() == null) {
                                    failureCount++;
                                    errorMessages.add("绑定率为空，无法保存月份 " + row.getStatMonth());
                                    continue;
                                }

                                String month = row.getStatMonth().trim();
                                WechatOperationStatistics existing = wechatOperationStatisticsService.selectWechatOperationStatisticsByMonth(month);
                                if (existing != null) {
                                    if (Boolean.TRUE.equals(updateSupport)) {
                                        existing.setBindingRate(row.getBindingRate());
                                        existing.setUpdateBy(operName);
                                        existing.setUpdateTime(new Date());
                                        int update = wechatOperationStatisticsService.updateWechatOperationStatistics(existing);
                                        if (update > 0) {
                                            successCount++;
                                        } else {
                                            failureCount++;
                                            errorMessages.add("更新失败 - 月份: " + month);
                                        }
                                    } else {
                                        failureCount++;
                                        errorMessages.add("月份 " + month + " 已存在，未更新（请勾选允许更新后重试）");
                                    }
                                } else {
                                    WechatOperationStatistics insertObj = new WechatOperationStatistics();
                                    insertObj.setStatMonth(month);
                                    insertObj.setBindingRate(row.getBindingRate());
                                    insertObj.setCreateBy(operName);
                                    insertObj.setCreateTime(new Date());
                                    int insert = wechatOperationStatisticsService.insertWechatOperationStatistics(insertObj);
                                    if (insert > 0) {
                                        successCount++;
                                    } else {
                                        failureCount++;
                                        errorMessages.add("新增失败 - 月份: " + month);
                                    }
                                }
                            } catch (Exception e) {
                                failureCount++;
                                errorMessages.add("保存绑定率数据失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                                log.error("❌ [企业微信绑定率] 保存失败", e);
                            }
                        }
                        log.info("💾 [企业微信绑定率] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                    } catch (Exception e) {
                        log.error("❌ [企业微信绑定率] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                    }
                    break;

                case "wechat-group-join-rate":
                    log.info("📈 [会员入群率] 开始解析Excel文件...");
                    ExcelUtil<WechatOperationStatistics> joinRateUtil = new ExcelUtil<>(WechatOperationStatistics.class);
                    try {
                        byte[] fileBytes = file.getBytes();
                        List<WechatOperationStatistics> joinList = null;
                        int chosenHeaderRow = -1;

                        try {
                            joinList = joinRateUtil.importExcel(new ByteArrayInputStream(fileBytes), 0);
                            if (joinList == null || joinList.isEmpty()) {
                                joinList = joinRateUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            }
                        } catch (Exception e) {
                            try {
                                joinList = joinRateUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            } catch (Exception ex) {
                                throw ex;
                            }
                        }

                        log.info("📈 [会员入群率] Excel解析完成，使用表头行: {}，解析到 {} 条数据", chosenHeaderRow, joinList != null ? joinList.size() : 0);
                        if (joinList == null || joinList.isEmpty()) {
                            return Result.error("Excel文件中没有找到有效的数据行");
                        }

                        joinList = joinList.stream().filter(Objects::nonNull).collect(Collectors.toList());

                        log.info("💾 [会员入群率] 开始保存数据到数据库...");
                        for (WechatOperationStatistics row : joinList) {
                            try {
                                if (row.getStatMonth() == null || row.getStatMonth().trim().isEmpty()) {
                                    failureCount++;
                                    errorMessages.add("统计月份为空，无法保存该条记录");
                                    continue;
                                }
                                if (row.getJoinRate() == null) {
                                    failureCount++;
                                    errorMessages.add("入群率为空，无法保存月份 " + row.getStatMonth());
                                    continue;
                                }

                                String month = row.getStatMonth().trim();
                                WechatOperationStatistics existing = wechatOperationStatisticsService.selectWechatOperationStatisticsByMonth(month);
                                if (existing != null) {
                                    if (Boolean.TRUE.equals(updateSupport)) {
                                        existing.setJoinRate(row.getJoinRate());
                                        existing.setUpdateBy(operName);
                                        existing.setUpdateTime(new Date());
                                        int update = wechatOperationStatisticsService.updateWechatOperationStatistics(existing);
                                        if (update > 0) successCount++;
                                        else {
                                            failureCount++;
                                            errorMessages.add("更新失败 - 月份: " + month);
                                        }
                                    } else {
                                        failureCount++;
                                        errorMessages.add("月份 " + month + " 已存在，未更新");
                                    }
                                } else {
                                    WechatOperationStatistics insertObj = new WechatOperationStatistics();
                                    insertObj.setStatMonth(month);
                                    insertObj.setJoinRate(row.getJoinRate());
                                    insertObj.setCreateBy(operName);
                                    insertObj.setCreateTime(new Date());
                                    int insert = wechatOperationStatisticsService.insertWechatOperationStatistics(insertObj);
                                    if (insert > 0) successCount++;
                                    else {
                                        failureCount++;
                                        errorMessages.add("新增失败 - 月份: " + month);
                                    }
                                }
                            } catch (Exception e) {
                                failureCount++;
                                errorMessages.add("保存入群率数据失败: " + e.getMessage());
                                log.error("❌ [会员入群率] 保存失败", e);
                            }
                        }
                    } catch (Exception e) {
                        log.error("❌ [会员入群率] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + e.getMessage());
                    }
                    break;

                case "wechat-activity-score":
                    log.info("📈 [社群活跃度] 开始解析Excel文件...");
                    ExcelUtil<WechatOperationStatistics> activityUtil = new ExcelUtil<>(WechatOperationStatistics.class);
                    try {
                        byte[] fileBytes = file.getBytes();
                        List<WechatOperationStatistics> activityList = null;
                        int chosenHeaderRow = -1;

                        try {
                            activityList = activityUtil.importExcel(new ByteArrayInputStream(fileBytes), 0);
                            if (activityList == null || activityList.isEmpty()) {
                                activityList = activityUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            }
                        } catch (Exception e) {
                            try {
                                activityList = activityUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            } catch (Exception ex) {
                                throw ex;
                            }
                        }

                        log.info("📈 [社群活跃度] Excel解析完成，使用表头行: {}，解析到 {} 条数据", chosenHeaderRow, activityList != null ? activityList.size() : 0);
                        if (activityList == null || activityList.isEmpty()) {
                            return Result.error("Excel文件中没有找到有效的数据行");
                        }

                        activityList = activityList.stream().filter(Objects::nonNull).collect(Collectors.toList());

                        log.info("💾 [社群活跃度] 开始保存数据到数据库...");
                        for (WechatOperationStatistics row : activityList) {
                            try {
                                if (row.getStatMonth() == null || row.getStatMonth().trim().isEmpty()) {
                                    failureCount++;
                                    errorMessages.add("统计月份为空，无法保存该条记录");
                                    continue;
                                }
                                if (row.getActivityScore() == null) {
                                    failureCount++;
                                    errorMessages.add("活跃度评分为空，无法保存月份 " + row.getStatMonth());
                                    continue;
                                }

                                String month = row.getStatMonth().trim();
                                WechatOperationStatistics existing = wechatOperationStatisticsService.selectWechatOperationStatisticsByMonth(month);
                                if (existing != null) {
                                    if (Boolean.TRUE.equals(updateSupport)) {
                                        existing.setActivityScore(row.getActivityScore());
                                        existing.setUpdateBy(operName);
                                        existing.setUpdateTime(new Date());
                                        int update = wechatOperationStatisticsService.updateWechatOperationStatistics(existing);
                                        if (update > 0) successCount++;
                                        else {
                                            failureCount++;
                                            errorMessages.add("更新失败 - 月份: " + month);
                                        }
                                    } else {
                                        failureCount++;
                                        errorMessages.add("月份 " + month + " 已存在，未更新");
                                    }
                                } else {
                                    WechatOperationStatistics insertObj = new WechatOperationStatistics();
                                    insertObj.setStatMonth(month);
                                    insertObj.setActivityScore(row.getActivityScore());
                                    insertObj.setCreateBy(operName);
                                    insertObj.setCreateTime(new Date());
                                    int insert = wechatOperationStatisticsService.insertWechatOperationStatistics(insertObj);
                                    if (insert > 0) successCount++;
                                    else {
                                        failureCount++;
                                        errorMessages.add("新增失败 - 月份: " + month);
                                    }
                                }
                            } catch (Exception e) {
                                failureCount++;
                                errorMessages.add("保存活跃度数据失败: " + e.getMessage());
                                log.error("❌ [社群活跃度] 保存失败", e);
                            }
                        }
                    } catch (Exception e) {
                        log.error("❌ [社群活跃度] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + e.getMessage());
                    }
                    break;

                case "wechat-conversion-rate":
                    log.info("📈 [企微转化率] 开始解析Excel文件（精简模板）...");
                    ExcelUtil<WechatOperationStatistics> conversionRateUtil = new ExcelUtil<>(WechatOperationStatistics.class);
                    try {
                        byte[] fileBytes = file.getBytes();
                        List<WechatOperationStatistics> conversionList = null;
                        int chosenHeaderRow = -1;

                        try {
                            conversionList = conversionRateUtil.importExcel(new ByteArrayInputStream(fileBytes), 0);
                            if (conversionList == null || conversionList.isEmpty()) {
                                conversionList = conversionRateUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            }
                        } catch (Exception e) {
                            try {
                                conversionList = conversionRateUtil.importExcel(new ByteArrayInputStream(fileBytes), 1);
                                chosenHeaderRow = 1;
                            } catch (Exception ex) {
                                throw ex;
                            }
                        }

                        log.info("📈 [企微转化率] Excel解析完成，使用表头行: {}，解析到 {} 条数据", chosenHeaderRow, conversionList != null ? conversionList.size() : 0);
                        if (conversionList == null || conversionList.isEmpty()) {
                            return Result.error("Excel文件中没有找到有效的数据行，请检查文件内容");
                        }

                        boolean headerMismatch = conversionList.stream().allMatch(s -> s == null || (
                                s.getStatMonth() == null && s.getConversionRate() == null
                        ));
                        if (headerMismatch) {
                            return Result.error("Excel列名不匹配：请使用转化率模板并确保首行为字段名");
                        }

                        conversionList = conversionList.stream().filter(Objects::nonNull).collect(Collectors.toList());

                        log.info("💾 [企微转化率] 开始保存数据到数据库...");
                        for (WechatOperationStatistics row : conversionList) {
                            try {
                                if (row.getStatMonth() == null || row.getStatMonth().trim().isEmpty()) {
                                    failureCount++;
                                    errorMessages.add("统计月份为空，无法保存该条记录");
                                    continue;
                                }
                                if (row.getConversionRate() == null) {
                                    failureCount++;
                                    errorMessages.add("转化率为空，无法保存月份 " + row.getStatMonth());
                                    continue;
                                }

                                String month = row.getStatMonth().trim();
                                WechatOperationStatistics existing = wechatOperationStatisticsService.selectWechatOperationStatisticsByMonth(month);
                                if (existing != null) {
                                    if (Boolean.TRUE.equals(updateSupport)) {
                                        existing.setConversionRate(row.getConversionRate());
                                        existing.setUpdateBy(operName);
                                        existing.setUpdateTime(new Date());
                                        int update = wechatOperationStatisticsService.updateWechatOperationStatistics(existing);
                                        if (update > 0) {
                                            successCount++;
                                        } else {
                                            failureCount++;
                                            errorMessages.add("更新失败 - 月份: " + month);
                                        }
                                    } else {
                                        failureCount++;
                                        errorMessages.add("月份 " + month + " 已存在，未更新（请勾选允许更新后重试）");
                                    }
                                } else {
                                    WechatOperationStatistics insertObj = new WechatOperationStatistics();
                                    insertObj.setStatMonth(month);
                                    insertObj.setConversionRate(row.getConversionRate());
                                    insertObj.setCreateBy(operName);
                                    insertObj.setCreateTime(new Date());
                                    int insert = wechatOperationStatisticsService.insertWechatOperationStatistics(insertObj);
                                    if (insert > 0) {
                                        successCount++;
                                    } else {
                                        failureCount++;
                                        errorMessages.add("新增失败 - 月份: " + month);
                                    }
                                }
                            } catch (Exception e) {
                                failureCount++;
                                errorMessages.add("保存转化率数据失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                                log.error("❌ [企微转化率] 保存失败", e);
                            }
                        }
                        log.info("💾 [企微转化率] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                    } catch (Exception e) {
                        log.error("❌ [企微转化率] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
                    }
                    break;
                
                default:
                    log.error("❌ [批量导入] 不支持的数据类型: {}", dataType);
                    return Result.error("不支持的数据类型: " + dataType);
            }
            
            // 构建返回结果
            result.put("successCount", successCount);
            result.put("failureCount", failureCount);
            result.put("totalCount", successCount + failureCount);
            result.put("errorMessages", errorMessages);
            
            log.info("🎉 [批量导入] 企业微信运营数据导入完成 - 总计: {}, 成功: {}, 失败: {}", 
                    (successCount + failureCount), successCount, failureCount);
            
            // 返回语义调整：
            // - 若全部失败（成功数为0），返回错误码并附带数据详情，避免前端误判为成功
            // - 若部分成功，维持成功码但在 message 中体现失败数量
            // - 若全部成功，返回成功码与成功信息
            if (successCount == 0) {
                result.put("message", String.format("导入失败，全部失败 %d 条", failureCount));
                Result<Map<String, Object>> errorResult = Result.error("导入失败");
                errorResult.put("data", result);
                return errorResult;
            } else if (failureCount > 0) {
                result.put("message", String.format("部分成功，成功 %d 条，失败 %d 条", successCount, failureCount));
                return Result.success(result);
            } else {
                result.put("message", String.format("导入成功，共导入 %d 条数据", successCount));
                return Result.success(result);
            }
            
        } catch (Exception e) {
            log.error("❌ [批量导入] 企业微信运营数据导入失败", e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 创建企业微信运营指标示例数据
     */
    private List<WechatOperationMetrics> createWechatMetricsSampleData() {
        List<WechatOperationMetrics> sampleData = new ArrayList<>();
        
        // 示例数据1
        WechatOperationMetrics metrics1 = new WechatOperationMetrics();
        metrics1.setStatDate(new Date());
        metrics1.setStatMonth("2025-01");
        metrics1.setUserId(1001L);
        metrics1.setUserName("张三");
        metrics1.setDepartment("销售部");
        metrics1.setFriendRequests(50);
        metrics1.setFriendAccepts(45);
        metrics1.setFriendTotal(320);
        metrics1.setFriendActive(280);
        metrics1.setChatSessions(150);
        sampleData.add(metrics1);
        
        // 示例数据2
        WechatOperationMetrics metrics2 = new WechatOperationMetrics();
        metrics2.setStatDate(new Date());
        metrics2.setStatMonth("2025-01");
        metrics2.setUserId(1002L);
        metrics2.setUserName("李四");
        metrics2.setDepartment("客服部");
        metrics2.setFriendRequests(35);
        metrics2.setFriendAccepts(32);
        metrics2.setFriendTotal(250);
        metrics2.setFriendActive(220);
        metrics2.setChatSessions(120);
        sampleData.add(metrics2);
        
        return sampleData;
    }

    /**
     * 创建企业微信运营统计示例数据
     */
    private List<WechatOperationStatistics> createWechatStatisticsSampleData() {
        List<WechatOperationStatistics> sampleData = new ArrayList<>();
        
        // 示例数据1
        WechatOperationStatistics statistics1 = new WechatOperationStatistics();
        statistics1.setStatMonth("2025-01");
        statistics1.setTotalMembers(1500L);
        statistics1.setBoundMembers(1200L);
        statistics1.setGroupMembers(800L);
        statistics1.setActiveGroups(25L);
        statistics1.setTotalGroups(30L);
        statistics1.setMonthlyConversions(150L);
        statistics1.setAvgResponseTime("2分钟");
        statistics1.setSatisfactionRate(new BigDecimal("4.5"));
        statistics1.setBindingRate(new BigDecimal("80.0"));
        statistics1.setConversionRate(new BigDecimal("12.5"));
        sampleData.add(statistics1);
        
        // 示例数据2
        WechatOperationStatistics statistics2 = new WechatOperationStatistics();
        statistics2.setStatMonth("2024-12");
        statistics2.setTotalMembers(1450L);
        statistics2.setBoundMembers(1150L);
        statistics2.setGroupMembers(750L);
        statistics2.setActiveGroups(23L);
        statistics2.setTotalGroups(28L);
        statistics2.setMonthlyConversions(140L);
        statistics2.setAvgResponseTime("2.5分钟");
        statistics2.setSatisfactionRate(new BigDecimal("4.3"));
        statistics2.setBindingRate(new BigDecimal("79.3"));
        statistics2.setConversionRate(new BigDecimal("12.2"));
        sampleData.add(statistics2);
        
        return sampleData;
    }

    /**
     * 创建热门社群排行（群组统计）示例数据
     */
    private List<WechatGroupStatistics> createWechatGroupStatisticsSampleData() {
        List<WechatGroupStatistics> sampleData = new ArrayList<>();

        WechatGroupStatistics gs1 = new WechatGroupStatistics();
        gs1.setGroupId(2001L);
        gs1.setStatMonth("2025-01");
        gs1.setActivityScore(new BigDecimal("88.5"));
        gs1.setJoinRate(new BigDecimal("72.3"));
        gs1.setInteractionCount(560L);
        gs1.setMessageCount(1200L);
        gs1.setActiveMemberCount(320L);
        sampleData.add(gs1);

        WechatGroupStatistics gs2 = new WechatGroupStatistics();
        gs2.setGroupId(2002L);
        gs2.setStatMonth("2025-01");
        gs2.setActivityScore(new BigDecimal("84.2"));
        gs2.setJoinRate(new BigDecimal("68.9"));
        gs2.setInteractionCount(480L);
        gs2.setMessageCount(980L);
        gs2.setActiveMemberCount(290L);
        sampleData.add(gs2);

        // 更多示例数据，便于用户理解模板格式并批量填写
        WechatGroupStatistics gs3 = new WechatGroupStatistics();
        gs3.setGroupId(2003L);
        gs3.setStatMonth("2025-01");
        gs3.setActivityScore(new BigDecimal("81.7"));
        gs3.setJoinRate(new BigDecimal("65.2"));
        gs3.setInteractionCount(430L);
        gs3.setMessageCount(905L);
        gs3.setActiveMemberCount(270L);
        sampleData.add(gs3);

        WechatGroupStatistics gs4 = new WechatGroupStatistics();
        gs4.setGroupId(2004L);
        gs4.setStatMonth("2025-01");
        gs4.setActivityScore(new BigDecimal("79.3"));
        gs4.setJoinRate(new BigDecimal("61.8"));
        gs4.setInteractionCount(390L);
        gs4.setMessageCount(840L);
        gs4.setActiveMemberCount(250L);
        sampleData.add(gs4);

        WechatGroupStatistics gs5 = new WechatGroupStatistics();
        gs5.setGroupId(2005L);
        gs5.setStatMonth("2025-01");
        gs5.setActivityScore(new BigDecimal("76.5"));
        gs5.setJoinRate(new BigDecimal("58.4"));
        gs5.setInteractionCount(355L);
        gs5.setMessageCount(780L);
        gs5.setActiveMemberCount(235L);
        sampleData.add(gs5);

        WechatGroupStatistics gs6 = new WechatGroupStatistics();
        gs6.setGroupId(2006L);
        gs6.setStatMonth("2025-01");
        gs6.setActivityScore(new BigDecimal("74.1"));
        gs6.setJoinRate(new BigDecimal("55.9"));
        gs6.setInteractionCount(330L);
        gs6.setMessageCount(720L);
        gs6.setActiveMemberCount(220L);
        sampleData.add(gs6);

        WechatGroupStatistics gs7 = new WechatGroupStatistics();
        gs7.setGroupId(2007L);
        gs7.setStatMonth("2025-01");
        gs7.setActivityScore(new BigDecimal("71.8"));
        gs7.setJoinRate(new BigDecimal("53.2"));
        gs7.setInteractionCount(305L);
        gs7.setMessageCount(690L);
        gs7.setActiveMemberCount(210L);
        sampleData.add(gs7);

        WechatGroupStatistics gs8 = new WechatGroupStatistics();
        gs8.setGroupId(2008L);
        gs8.setStatMonth("2025-01");
        gs8.setActivityScore(new BigDecimal("69.4"));
        gs8.setJoinRate(new BigDecimal("50.1"));
        gs8.setInteractionCount(280L);
        gs8.setMessageCount(640L);
        gs8.setActiveMemberCount(195L);
        sampleData.add(gs8);

        WechatGroupStatistics gs9 = new WechatGroupStatistics();
        gs9.setGroupId(2009L);
        gs9.setStatMonth("2025-01");
        gs9.setActivityScore(new BigDecimal("67.0"));
        gs9.setJoinRate(new BigDecimal("47.8"));
        gs9.setInteractionCount(255L);
        gs9.setMessageCount(600L);
        gs9.setActiveMemberCount(185L);
        sampleData.add(gs9);

        WechatGroupStatistics gs10 = new WechatGroupStatistics();
        gs10.setGroupId(2010L);
        gs10.setStatMonth("2025-01");
        gs10.setActivityScore(new BigDecimal("64.6"));
        gs10.setJoinRate(new BigDecimal("45.2"));
        gs10.setInteractionCount(230L);
        gs10.setMessageCount(560L);
        gs10.setActiveMemberCount(170L);
        sampleData.add(gs10);

        return sampleData;
    }

    /**
     * 将输入的月份字符串归一化为标准格式 YYYY-MM。
     * 支持 "yyyy-MM", "yyyy/MM", "yyyy年MM月", "yyyy-MM-dd" 等常见形式；
     * 也支持纯数字形式如 "yyyyMM" 或 "yyyyMMdd"（取前6位作为年月）。
     * 若无法解析，则回退为当前系统月份。
     */

}