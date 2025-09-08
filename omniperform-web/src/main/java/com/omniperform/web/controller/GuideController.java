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
@Api(tags = "导购绩效管理")
public class GuideController {

    private static final Logger log = LoggerFactory.getLogger(GuideController.class);

    /**
     * 获取导购列表
     */
    @GetMapping("/list")
    @ApiOperation("获取导购列表")
    public Result getGuideList(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String region,
                               @RequestParam(required = false) String status) {
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
    @GetMapping("/performance")
    @ApiOperation("获取导购绩效数据")
    public Result getGuidePerformance(@RequestParam(required = false) String guideId,
                                      @RequestParam(required = false) String startDate,
                                      @RequestParam(required = false) String endDate) {
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
    @ApiOperation("获取导购详情")
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
    @ApiOperation("创建导购")
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
    @ApiOperation("更新导购信息")
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
    @ApiOperation("删除导购")
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
     * 获取导购绩效统计
     */
    @GetMapping("/performance/statistics")
    @ApiOperation("获取导购绩效统计")
    public Result getPerformanceStatistics(@RequestParam(required = false) String region,
                                           @RequestParam(required = false) String period) {
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