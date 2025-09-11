package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * 导购绩效管理控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/guide")
public class GuideController {

    private static final Logger log = LoggerFactory.getLogger(GuideController.class);

    /**
     * 获取导购列表
     */
    @PostMapping("/list")
    public Result getGuideList(@RequestBody(required = false) Map<String, Object> params) {
        int page = params != null && params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params != null && params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        String region = params != null ? (String) params.get("region") : null;
        String status = params != null ? (String) params.get("status") : null;
        try {
            List<Map<String, Object>> guides = new ArrayList<>();
            
            String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十", "陈一", "林二"};
            String[] regions = {"华东区", "华南区", "华中区", "华北区", "西南区", "西北区"};
            String[] statuses = {"在职", "离职", "休假"};
            
            for (int i = 0; i < names.length; i++) {
                Map<String, Object> guide = new HashMap<>();
                guide.put("id", i + 1);
                guide.put("name", names[i]);
                guide.put("employeeId", "EMP" + String.format("%04d", i + 1));
                guide.put("region", regions[i % regions.length]);
                guide.put("status", statuses[i % statuses.length]);
                guide.put("phone", "138" + String.format("%08d", i + 12345678));
                guide.put("email", names[i].toLowerCase() + "@company.com");
                guide.put("joinDate", LocalDate.now().minusMonths(i + 1).toString());
                guide.put("level", i % 3 == 0 ? "高级" : i % 3 == 1 ? "中级" : "初级");
                guides.add(guide);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("guides", guides.subList((page - 1) * size, Math.min(page * size, guides.size())));
            result.put("total", guides.size());
            result.put("page", page);
            result.put("size", size);
            
            log.info("获取导购列表成功，页码: {}, 大小: {}", page, size);
            return Result.success("获取导购列表成功", result);
        } catch (Exception e) {
            log.error("获取导购列表失败: {}", e.getMessage(), e);
            return Result.error("获取导购列表失败");
        }
    }

    /**
     * 获取导购绩效数据
     */
    @PostMapping("/performance")
    public Result getGuidePerformance(@RequestBody(required = false) Map<String, Object> params) {
        String guideId = params != null ? (String) params.get("guideId") : null;
        String startDate = params != null ? (String) params.get("startDate") : null;
        String endDate = params != null ? (String) params.get("endDate") : null;
        try {
            List<Map<String, Object>> performances = new ArrayList<>();
            
            String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
            String[] regions = {"华东区", "华南区", "华中区", "华北区"};
            
            for (int i = 0; i < names.length; i++) {
                Map<String, Object> performance = new HashMap<>();
                performance.put("guideId", i + 1);
                performance.put("guideName", names[i]);
                performance.put("region", regions[i % regions.length]);
                performance.put("newMembers", 50 - i * 3);
                performance.put("motCompletionRate", 95.5 - i * 2.5);
                performance.put("customerSatisfaction", 4.8 - i * 0.1);
                performance.put("sales", 150000 - i * 10000);
                performance.put("motTasksCompleted", 45 - i * 2);
                performance.put("responseTime", 2.5 + i * 0.3); // 小时
                performance.put("rank", i + 1);
                performances.add(performance);
            }
            
            log.info("获取导购绩效数据成功");
            return Result.success("获取导购绩效数据成功", performances);
        } catch (Exception e) {
            log.error("获取导购绩效数据失败: {}", e.getMessage(), e);
            return Result.error("获取导购绩效数据失败");
        }
    }

    /**
     * 获取导购详情
     */
    @GetMapping("/detail/{guideId}")
    public Result getGuideDetail(@PathVariable String guideId) {
        try {
            Map<String, Object> guide = new HashMap<>();
            guide.put("id", guideId);
            guide.put("name", "张三");
            guide.put("employeeId", "EMP0001");
            guide.put("region", "华东区");
            guide.put("status", "在职");
            guide.put("phone", "13812345678");
            guide.put("email", "zhangsan@company.com");
            guide.put("joinDate", "2023-01-15");
            guide.put("level", "高级");
            guide.put("department", "销售部");
            guide.put("manager", "李经理");
            
            // 绩效指标
            Map<String, Object> performance = new HashMap<>();
            performance.put("newMembers", 50);
            performance.put("motCompletionRate", 95.5);
            performance.put("customerSatisfaction", 4.8);
            performance.put("sales", 150000);
            performance.put("motTasksCompleted", 45);
            performance.put("responseTime", 2.5);
            guide.put("performance", performance);
            
            // 最近活动
            List<Map<String, Object>> recentActivities = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("date", LocalDate.now().minusDays(i).toString());
                activity.put("type", i % 2 == 0 ? "MOT任务" : "会员服务");
                activity.put("description", "完成会员" + (i + 1) + "的服务任务");
                activity.put("result", "成功");
                recentActivities.add(activity);
            }
            guide.put("recentActivities", recentActivities);
            
            log.info("获取导购详情成功，导购ID: {}", guideId);
            return Result.success("获取导购详情成功", guide);
        } catch (Exception e) {
            log.error("获取导购详情失败: {}", e.getMessage(), e);
            return Result.error("获取导购详情失败");
        }
    }

    /**
     * 创建导购
     */
    @PostMapping("/create")
    public Result createGuide(@RequestBody Map<String, Object> guideData) {
        try {
            Map<String, Object> guide = new HashMap<>();
            guide.put("id", System.currentTimeMillis());
            guide.put("name", guideData.get("name"));
            guide.put("employeeId", guideData.get("employeeId"));
            guide.put("region", guideData.get("region"));
            guide.put("status", "在职");
            guide.put("phone", guideData.get("phone"));
            guide.put("email", guideData.get("email"));
            guide.put("joinDate", LocalDate.now().toString());
            guide.put("level", guideData.get("level"));
            guide.put("department", guideData.get("department"));
            guide.put("manager", guideData.get("manager"));
            
            log.info("创建导购成功，导购ID: {}", guide.get("id"));
            return Result.success("创建导购成功", guide);
        } catch (Exception e) {
            log.error("创建导购失败: {}", e.getMessage(), e);
            return Result.error("创建导购失败");
        }
    }

    /**
     * 更新导购信息
     */
    @PutMapping("/update/{guideId}")
    public Result updateGuide(@PathVariable String guideId, @RequestBody Map<String, Object> guideData) {
        try {
            Map<String, Object> guide = new HashMap<>();
            guide.put("id", guideId);
            guide.put("name", guideData.get("name"));
            guide.put("employeeId", guideData.get("employeeId"));
            guide.put("region", guideData.get("region"));
            guide.put("status", guideData.get("status"));
            guide.put("phone", guideData.get("phone"));
            guide.put("email", guideData.get("email"));
            guide.put("level", guideData.get("level"));
            guide.put("department", guideData.get("department"));
            guide.put("manager", guideData.get("manager"));
            guide.put("updateDate", LocalDate.now().toString());
            
            log.info("更新导购信息成功，导购ID: {}", guideId);
            return Result.success("更新导购信息成功", guide);
        } catch (Exception e) {
            log.error("更新导购信息失败: {}", e.getMessage(), e);
            return Result.error("更新导购信息失败");
        }
    }

    /**
     * 删除导购
     */
    @DeleteMapping("/delete/{guideId}")
    public Result deleteGuide(@PathVariable String guideId) {
        try {
            log.info("删除导购成功，导购ID: {}", guideId);
            return Result.success("删除导购成功");
        } catch (Exception e) {
            log.error("删除导购失败: {}", e.getMessage(), e);
            return Result.error("删除导购失败");
        }
    }

    /**
     * 获取导购绩效概览
     */
    @PostMapping("/performance/overview")
    public Result getPerformanceOverview(@RequestBody(required = false) Map<String, Object> params) {
        String region = params != null ? (String) params.get("region") : null;
        String period = params != null ? (String) params.get("period") : null;
        try {
            Map<String, Object> overview = new HashMap<>();
            
            // 总体概览数据
            overview.put("totalGuides", 156);
            overview.put("activeGuides", 142);
            overview.put("avgPerformanceScore", 85.6);
            overview.put("topPerformerCount", 23);
            overview.put("avgCAI", 0.72);
            overview.put("avgRMV", 0.68);
            
            log.info("获取导购绩效概览成功");
            return Result.success("获取导购绩效概览成功", overview);
        } catch (Exception e) {
            log.error("获取导购绩效概览失败: {}", e.getMessage(), e);
            return Result.error("获取导购绩效概览失败");
        }
    }

    /**
     * 获取九宫格绩效分布数据
     */
    @PostMapping("/performance/matrix")
    public Result getPerformanceMatrix(@RequestBody(required = false) Map<String, Object> params) {
        String region = params != null ? (String) params.get("region") : null;
        String period = params != null ? (String) params.get("period") : null;
        try {
            List<Map<String, Object>> matrixData = new ArrayList<>();
            
            // 九宫格位置数据 (x: CAI等级, y: RMV等级, z: 导购人数)
            String[] positions = {"1-1", "1-2", "1-3", "2-1", "2-2", "2-3", "3-1", "3-2", "3-3"};
            String[] types = {"培训生", "服务达人", "忠诚专家", "基础型", "骨干力量", "关系专家", "获客能手", "成长之星", "超级明星"};
            String[] xLabels = {"低", "低", "低", "中", "中", "中", "高", "高", "高"};
            String[] yLabels = {"低", "中", "高", "低", "中", "高", "低", "中", "高"};
            int[] counts = {15, 12, 8, 10, 25, 18, 7, 20, 15};
            
            for (int i = 0; i < positions.length; i++) {
                Map<String, Object> point = new HashMap<>();
                point.put("x", xLabels[i]);
                point.put("y", yLabels[i]);
                point.put("z", counts[i]);
                point.put("position", positions[i]);
                point.put("type", types[i]);
                matrixData.add(point);
            }
            
            log.info("获取九宫格绩效分布数据成功");
            return Result.success("获取九宫格绩效分布数据成功", matrixData);
        } catch (Exception e) {
            log.error("获取九宫格绩效分布数据失败: {}", e.getMessage(), e);
            return Result.error("获取九宫格绩效分布数据失败");
        }
    }

    /**
     * 获取导购绩效趋势数据
     */
    @PostMapping("/performance/trend")
    public Result getPerformanceTrend(@RequestBody(required = false) Map<String, Object> params) {
        String region = params != null ? (String) params.get("region") : null;
        String period = params != null ? (String) params.get("period") : null;
        try {
            Map<String, Object> trendData = new HashMap<>();
            
            // 时间轴
            trendData.put("categories", Arrays.asList("4月", "5月", "6月"));
            
            // 各等级趋势数据
            List<Map<String, Object>> series = new ArrayList<>();
            
            Map<String, Object> superStar = new HashMap<>();
            superStar.put("name", "超级明星 (3-3)");
            superStar.put("data", Arrays.asList(8, 12, 15));
            series.add(superStar);
            
            Map<String, Object> growthStar = new HashMap<>();
            growthStar.put("name", "成长之星 (3-2)");
            growthStar.put("data", Arrays.asList(15, 18, 20));
            series.add(growthStar);
            
            Map<String, Object> backbone = new HashMap<>();
            backbone.put("name", "骨干力量 (2-2)");
            backbone.put("data", Arrays.asList(22, 25, 25));
            series.add(backbone);
            
            Map<String, Object> others = new HashMap<>();
            others.put("name", "其他位置");
            others.put("data", Arrays.asList(85, 75, 70));
            series.add(others);
            
            trendData.put("series", series);
            
            log.info("获取导购绩效趋势数据成功");
            return Result.success("获取导购绩效趋势数据成功", trendData);
        } catch (Exception e) {
            log.error("获取导购绩效趋势数据失败: {}", e.getMessage(), e);
            return Result.error("获取导购绩效趋势数据失败");
        }
    }

    /**
     * 获取导购绩效详情列表
     */
    @PostMapping("/performance/detail")
    public Result getPerformanceDetail(@RequestBody(required = false) Map<String, Object> params) {
        int page = params != null && params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params != null && params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        String region = params != null ? (String) params.get("region") : null;
        String search = params != null ? (String) params.get("search") : null;
        try {
            List<Map<String, Object>> details = new ArrayList<>();
            
            String[] names = {"张明", "李华", "王丽", "陈静", "赵薇", "刘强", "孙娜", "周杰", "吴琳", "郑浩"};
            String[] codes = {"G20250001", "G20250015", "G20250023", "G20250042", "G20250056", "G20250067", "G20250078", "G20250089", "G20250091", "G20250102"};
            String[] stores = {"杭州西湖店", "南京新街口店", "合肥滨湖店", "深圳南山店", "北京朝阳店", "上海浦东店", "广州天河店", "成都春熙店", "武汉江汉店", "西安雁塔店"};
            String[] regions = {"East", "East", "East", "South", "North", "East", "South", "West", "Central", "West"};
            double[] caiValues = {0.92, 0.85, 0.65, 0.72, 0.45, 0.88, 0.78, 0.55, 0.69, 0.82};
            double[] rmvValues = {0.88, 0.75, 0.82, 0.68, 0.58, 0.91, 0.73, 0.85, 0.62, 0.79};
            String[] positions = {"3-3", "3-2", "2-3", "2-2", "1-2", "3-3", "3-2", "2-3", "2-2", "3-2"};
            String[] types = {"超级明星", "成长之星", "关系专家", "骨干力量", "服务达人", "超级明星", "成长之星", "关系专家", "骨干力量", "成长之星"};
            String[] trends = {"提升", "提升", "持平", "提升", "下降", "提升", "持平", "提升", "下降", "提升"};
            
            for (int i = 0; i < names.length; i++) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("guideName", names[i]);
                detail.put("guideCode", codes[i]);
                detail.put("storeName", stores[i]);
                detail.put("region", regions[i]);
                detail.put("cai", caiValues[i]);
                detail.put("rmv", rmvValues[i]);
                detail.put("position", positions[i]);
                detail.put("type", types[i]);
                detail.put("trend", trends[i]);
                
                // CAI等级
                String caiLevel = caiValues[i] >= 0.8 ? "高" : (caiValues[i] >= 0.6 ? "中" : "低");
                detail.put("caiLevel", caiLevel);
                
                // RMV等级
                String rmvLevel = rmvValues[i] >= 0.8 ? "高" : (rmvValues[i] >= 0.6 ? "中" : "低");
                detail.put("rmvLevel", rmvLevel);
                
                details.add(detail);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", details);
            result.put("total", 130);
            result.put("page", page);
            result.put("size", size);
            
            log.info("获取导购绩效详情列表成功");
            return Result.success("获取导购绩效详情列表成功", result);
        } catch (Exception e) {
            log.error("获取导购绩效详情列表失败: {}", e.getMessage(), e);
            return Result.error("获取导购绩效详情列表失败");
        }
    }

    /**
     * 获取导购绩效统计
     */
    @PostMapping("/performance/statistics")
    public Result getPerformanceStatistics(@RequestBody(required = false) Map<String, Object> params) {
        String region = params != null ? (String) params.get("region") : null;
        String period = params != null ? (String) params.get("period") : null;
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总体统计
            statistics.put("totalGuides", 156);
            statistics.put("activeGuides", 142);
            statistics.put("avgPerformanceScore", 85.6);
            statistics.put("topPerformerCount", 23);
            
            // 区域分布
            Map<String, Integer> regionDistribution = new HashMap<>();
            regionDistribution.put("华东区", 45);
            regionDistribution.put("华南区", 38);
            regionDistribution.put("华中区", 32);
            regionDistribution.put("华北区", 28);
            regionDistribution.put("西南区", 13);
            statistics.put("regionDistribution", regionDistribution);
            
            // 绩效等级分布
            Map<String, Integer> levelDistribution = new HashMap<>();
            levelDistribution.put("优秀", 34);
            levelDistribution.put("良好", 67);
            levelDistribution.put("一般", 41);
            levelDistribution.put("待改进", 14);
            statistics.put("levelDistribution", levelDistribution);
            
            log.info("获取导购绩效统计成功");
            return Result.success("获取导购绩效统计成功", statistics);
        } catch (Exception e) {
            log.error("获取导购绩效统计失败: {}", e.getMessage(), e);
            return Result.error("获取导购绩效统计失败");
        }
    }
}