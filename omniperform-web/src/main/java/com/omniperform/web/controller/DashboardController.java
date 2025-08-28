package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

/**
 * 仪表盘控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    /**
     * 获取仪表盘概览数据
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        Map<String, Object> data = new HashMap<>();
        
        // 核心KPI指标
        data.put("newMembers", 1285);
        data.put("newMembersGrowth", 12.8);
        data.put("repeatPurchaseRate", 38.2);
        data.put("repeatPurchaseGrowth", 3.5);
        data.put("motSuccessRate", 24.5);
        data.put("motSuccessGrowth", 2.1);
        data.put("memberActivityRate", 65.2);
        data.put("memberActivityGrowth", 1.8);
        
        // 今日统计
        data.put("todayNewMembers", 45);
        data.put("todayActiveMot", 128);
        data.put("todayCompletedMot", 89);
        data.put("todayActiveMembers", 2340);
        
        // 本月概况
        data.put("monthNewMembers", 1285);
        data.put("monthMotTasks", 3456);
        data.put("monthMotSuccess", 847);
        data.put("monthSales", 2890000.50);
        
        return Result.success("获取概览数据成功", data);
    }

    /**
     * 获取核心指标数据
     */
    @GetMapping("/metrics")
    public Result<Map<String, Object>> getMetrics() {
        Map<String, Object> data = new HashMap<>();
        
        // 实时指标
        data.put("todayNewMembers", 45);
        data.put("todayActiveMot", 128);
        data.put("todayCompletedMot", 89);
        data.put("onlineGuides", 23);
        data.put("systemLoad", 68.5);
        data.put("responseTime", 245); // 毫秒
        
        // 系统状态
        data.put("systemStatus", "正常");
        data.put("databaseStatus", "正常");
        data.put("apiStatus", "正常");
        data.put("lastUpdateTime", new Date());
        
        // 警告信息
        List<Map<String, Object>> alerts = new ArrayList<>();
        Map<String, Object> alert1 = new HashMap<>();
        alert1.put("level", "warning");
        alert1.put("message", "华南区MOT完成率较低，需要关注");
        alert1.put("time", "10:30");
        alerts.add(alert1);
        
        Map<String, Object> alert2 = new HashMap<>();
        alert2.put("level", "info");
        alert2.put("message", "新会员注册量超出预期15%");
        alert2.put("time", "09:45");
        alerts.add(alert2);
        
        data.put("alerts", alerts);
        
        return Result.success("获取核心指标成功", data);
    }

    /**
     * 获取会员增长趋势
     */
    @GetMapping("/member-growth-trend")
    public Result<Map<String, Object>> getMemberGrowthTrend(@RequestParam(defaultValue = "6") int months) {
        Map<String, Object> data = new HashMap<>();
        
        List<String> monthLabels = new ArrayList<>();
        List<Integer> newMembers = new ArrayList<>();
        List<Integer> repeatMembers = new ArrayList<>();
        List<Double> repeatRate = new ArrayList<>();
        
        // 生成最近几个月的趋势数据
        LocalDate now = LocalDate.now();
        for (int i = months - 1; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            monthLabels.add(month.getMonthValue() + "月");
            
            int newCount = 800 + (int)(Math.random() * 400); // 800-1200
            int repeatCount = (int)(newCount * (0.3 + Math.random() * 0.2)); // 30%-50%
            double rate = (double) repeatCount / newCount * 100;
            
            newMembers.add(newCount);
            repeatMembers.add(repeatCount);
            repeatRate.add(Math.round(rate * 100.0) / 100.0);
        }
        
        data.put("months", monthLabels);
        data.put("newMembers", newMembers);
        data.put("repeatMembers", repeatMembers);
        data.put("repeatRate", repeatRate);
        
        return Result.success("获取会员增长趋势成功", data);
    }

    /**
     * 获取会员阶段分布
     */
    @GetMapping("/member-stage-distribution")
    public Result<Map<String, Object>> getMemberStageDistribution() {
        Map<String, Object> data = new HashMap<>();
        
        Map<String, Integer> stageData = new HashMap<>();
        stageData.put("stage0", 1050); // 孕妇
        stageData.put("stage1", 1820); // 0-6个月
        stageData.put("stage2", 1650); // 6-12个月
        stageData.put("stage3", 1250); // 1-2岁
        stageData.put("stage4", 980);  // 2-3岁
        
        Map<String, String> labels = new HashMap<>();
        labels.put("stage0", "0阶段(孕妇)");
        labels.put("stage1", "1阶段(0-6个月)");
        labels.put("stage2", "2阶段(6-12个月)");
        labels.put("stage3", "3阶段(1-2岁)");
        labels.put("stage4", "4阶段(2-3岁)");
        
        data.put("data", stageData);
        data.put("labels", labels);
        data.put("total", stageData.values().stream().mapToInt(Integer::intValue).sum());
        
        return Result.success("获取会员阶段分布成功", data);
    }

    /**
     * 获取区域绩效对比
     */
    @GetMapping("/region-performance")
    public Result<Map<String, Object>> getRegionPerformance() {
        Map<String, Object> data = new HashMap<>();
        
        List<String> regions = Arrays.asList("华东区", "华南区", "华中区", "华北区", "西南区", "西北区");
        List<Integer> newMembers = Arrays.asList(320, 302, 301, 334, 290, 230);
        List<Double> repeatRate = Arrays.asList(42.1, 38.5, 35.8, 40.2, 36.5, 33.6);
        List<Double> motSuccessRate = Arrays.asList(45.6, 38.2, 32.4, 35.8, 30.5, 48.2);
        List<Double> activityRate = Arrays.asList(78.3, 70.5, 65.7, 72.8, 68.2, 62.5);
        
        data.put("regions", regions);
        data.put("newMembers", newMembers);
        data.put("repeatRate", repeatRate);
        data.put("motSuccessRate", motSuccessRate);
        data.put("activityRate", activityRate);
        
        return Result.success("获取区域绩效对比成功", data);
    }

    /**
     * 获取导购绩效排行
     */
    @GetMapping("/guide-performance-ranking")
    public Result<List<Map<String, Object>>> getGuidePerformanceRanking() {
        List<Map<String, Object>> rankings = new ArrayList<>();
        
        String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
        String[] regions = {"华东区", "华南区", "华中区", "华北区"};
        
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> guide = new HashMap<>();
            guide.put("rank", i + 1);
            guide.put("name", names[i]);
            guide.put("region", regions[i % regions.length]);
            guide.put("newMembers", 50 - i * 3);
            guide.put("motCompletionRate", 95.5 - i * 2.5);
            guide.put("customerSatisfaction", 4.8 - i * 0.1);
            guide.put("sales", 150000 - i * 10000);
            rankings.add(guide);
        }
        
        return Result.success("获取导购绩效排行成功", rankings);
    }

    /**
     * 获取待执行MOT任务
     */
    @GetMapping("/pending-mot-tasks")
    public Result<List<Map<String, Object>>> getPendingMotTasks() {
        List<Map<String, Object>> tasks = new ArrayList<>();
        
        String[] types = {"首购后回访", "购买后指导", "复购提醒", "生日关怀", "节日问候"};
        String[] priorities = {"高", "中", "低"};
        
        for (int i = 0; i < 10; i++) {
            Map<String, Object> task = new HashMap<>();
            task.put("id", i + 1);
            task.put("memberName", "会员" + (i + 1));
            task.put("motType", types[i % types.length]);
            task.put("priority", priorities[i % priorities.length]);
            task.put("dueDate", LocalDate.now().plusDays(i % 3 + 1).toString());
            task.put("guideName", "导购" + ((i % 5) + 1));
            tasks.add(task);
        }
        
        return Result.success("获取待执行MOT任务成功", tasks);
    }

    /**
     * 获取产品销售分析
     */
    @GetMapping("/product-sales-analysis")
    public Result<Map<String, Object>> getProductSalesAnalysis() {
        Map<String, Object> data = new HashMap<>();
        
        List<String> products = Arrays.asList("1阶段奶粉", "2阶段奶粉", "3阶段奶粉", "4阶段奶粉", "孕妇奶粉");
        List<Integer> sales = Arrays.asList(2580, 3420, 2890, 1960, 1150);
        List<Double> growth = Arrays.asList(12.5, -3.2, 8.7, 15.3, 5.8);
        
        data.put("products", products);
        data.put("sales", sales);
        data.put("growth", growth);
        
        return Result.success("获取产品销售分析成功", data);
    }

    /**
     * 获取会员来源分析
     */
    @GetMapping("/member-source-analysis")
    public Result<Map<String, Object>> getMemberSourceAnalysis() {
        Map<String, Object> data = new HashMap<>();
        
        Map<String, Integer> sources = new HashMap<>();
        sources.put("线下门店", 3420);
        sources.put("线上商城", 2180);
        sources.put("微信推广", 1560);
        sources.put("朋友推荐", 890);
        sources.put("广告投放", 650);
        sources.put("其他渠道", 340);
        
        data.put("sources", sources);
        data.put("total", sources.values().stream().mapToInt(Integer::intValue).sum());
        
        return Result.success("获取会员来源分析成功", data);
    }

    /**
     * 刷新数据
     */
    @GetMapping("/refresh")
    public Result<String> refreshData() {
        // 模拟刷新操作
        try {
            Thread.sleep(1000); // 模拟处理时间
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return Result.success("数据刷新成功", "数据已更新到最新状态");
    }
}
