package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
public class WechatOperationController {

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
            
            // 计算企业微信绑定率
            Map<String, Object> bindingRate = new HashMap<>();
            if (!recentMetrics.isEmpty()) {
                // 取最新数据计算绑定率
                WechatOperationMetrics latest = recentMetrics.get(0);
                double bindingValue = latest.getFriendAccepts() != null ? 
                    Math.min(latest.getFriendAccepts() * 2.5, 100.0) : 85.2; // 基于好友通过数计算
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
            
            // 会员入群率 - 根据period参数返回不同的数据
            Map<String, Object> groupJoinRate = new HashMap<>();
            double joinRateValue;
            
            // 根据period参数调整会员入群率数据
            if ("2025-06".equals(period)) {
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
            
            // 社群活跃度 - 根据period参数返回不同的数据
            Map<String, Object> groupActivity = new HashMap<>();
            double activityValue;
            
            // 根据period参数调整社群活跃度数据
            if ("2025-06".equals(period)) {
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
            groupActivity.put("progressPercent", activityValue * 20); // 转换为百分比
            metrics.put("groupActivity", groupActivity);
            
            // 企微转化率 - 基于转化数据计算
            Map<String, Object> conversionRate = new HashMap<>();
            if (!recentMetrics.isEmpty()) {
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
                for (int i = 0; i < dataCount; i++) {
                    WechatOperationMetrics metrics = recentMetrics.get(i);
                    
                    // 格式化日期
                    String dateStr = metrics.getStatDate() != null ? 
                        new java.text.SimpleDateFormat("MM-dd").format(metrics.getStatDate()) : 
                        "01-" + (15 + i);
                    
                    categories.add(dateStr);
                    
                    // 群组活跃度 - 基于群聊互动数估算
                    Integer groupInteractions = metrics.getGroupInteractions();
                    double activity;
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
                    } else {
                        // 根据period参数生成不同的模拟数据
                        if ("2025-06".equals(period)) {
                            activity = 3.8 + Math.random() * 0.4; // 3.8-4.2
                        } else if ("2025-05".equals(period)) {
                            activity = 4.2 + Math.random() * 0.6; // 4.2-4.8
                        } else if ("2025-04".equals(period)) {
                            activity = 3.5 + Math.random() * 0.5; // 3.5-4.0
                        } else {
                            activity = 4.0 + Math.random() * 0.8; // 4.0-4.8
                        }
                    }
                    activityData.add(Math.round(activity * 10.0) / 10.0);
                }
            } else {
                // 如果没有数据，返回模拟数据
                String[] dates = {"01-15", "01-16", "01-17", "01-18", "01-19", "01-20", "01-21"};
                double[] activities;
                
                // 根据period参数生成不同月份的模拟数据
                if ("2025-06".equals(period)) {
                    activities = new double[]{3.8, 4.1, 3.9, 4.2, 3.7, 4.0, 3.9};
                } else if ("2025-05".equals(period)) {
                    activities = new double[]{4.3, 4.6, 4.2, 4.8, 4.4, 4.7, 4.5};
                } else if ("2025-04".equals(period)) {
                    activities = new double[]{3.6, 3.8, 3.5, 3.9, 3.7, 3.8, 3.6};
                } else {
                    // 默认数据（当前月份或未指定）
                    activities = new double[]{4.2, 4.5, 4.1, 4.8, 4.3, 4.6, 4.4};
                }
                
                for (int i = 0; i < dates.length; i++) {
                    categories.add(dates[i]);
                    activityData.add(activities[i]);
                }
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
            if ("week".equals(period)) {
                statMonth = "2025-07"; // 最近一周使用7月数据
            } else if ("quarter".equals(period)) {
                statMonth = "2025-06"; // 季度使用6月数据
            } else if (period != null && !period.isEmpty()) {
                statMonth = period; // 使用指定的月份
            } else {
                statMonth = "2025-07"; // 默认月度使用7月数据
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
}