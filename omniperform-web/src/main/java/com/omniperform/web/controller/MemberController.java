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
 * 会员管理控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/member")
@Api(tags = "会员管理")
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    /**
     * 获取会员概览数据
     */
    @GetMapping("/overview")
    @ApiOperation("获取会员概览数据")
    public Result getMemberOverview(@RequestParam(required = false) String month) {
        try {
            Map<String, Object> data = new HashMap<>();
            
            // 会员总体统计
            data.put("totalMembers", 8650);
            data.put("totalMembersGrowth", 15.2);
            data.put("activeMembers", 5420);
            data.put("activeMembersGrowth", 8.7);
            data.put("newMembersThisMonth", 1285);
            data.put("newMembersGrowth", 12.8);
            data.put("repeatPurchaseRate", 38.2);
            data.put("repeatPurchaseGrowth", 3.5);
            
            // 会员阶段分布
            Map<String, Object> stageDistribution = new HashMap<>();
            stageDistribution.put("stage0", 1050); // 孕妇
            stageDistribution.put("stage1", 1820); // 0-6个月
            stageDistribution.put("stage2", 1650); // 6-12个月
            stageDistribution.put("stage3", 1250); // 1-2岁
            stageDistribution.put("stage4", 980);  // 2-3岁
            data.put("stageDistribution", stageDistribution);
            
            // 会员价值分布
            Map<String, Object> valueDistribution = new HashMap<>();
            valueDistribution.put("highValue", 1250);    // 高价值会员
            valueDistribution.put("mediumValue", 3200);  // 中等价值会员
            valueDistribution.put("lowValue", 2850);     // 低价值会员
            valueDistribution.put("potential", 1350);    // 潜力会员
            data.put("valueDistribution", valueDistribution);
            
            // 会员活跃度趋势（最近12个月）
            List<Map<String, Object>> activityTrend = new ArrayList<>();
            String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
            int[] activeUsers = {4850, 4920, 5100, 5280, 5150, 5320, 5420, 5380, 5250, 5180, 5350, 5420};
            
            for (int i = 0; i < months.length; i++) {
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", months[i]);
                monthData.put("activeUsers", activeUsers[i]);
                monthData.put("activityRate", Math.round(activeUsers[i] * 100.0 / 8650 * 100) / 100.0);
                activityTrend.add(monthData);
            }
            data.put("activityTrend", activityTrend);
            
            log.info("获取会员概览数据成功");
            return Result.success("获取会员概览数据成功", data);
        } catch (Exception e) {
            log.error("获取会员概览数据失败: {}", e.getMessage(), e);
            return Result.error("获取会员概览数据失败");
        }
    }

    /**
     * 获取会员列表
     */
    @GetMapping("/list")
    @ApiOperation("获取会员列表")
    public Result getMemberList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String stage,
            @RequestParam(required = false) String status) {
        try {
            // 生成模拟会员数据
            List<Map<String, Object>> members = new ArrayList<>();
            
            for (int i = 1; i <= pageSize; i++) {
                Map<String, Object> member = new HashMap<>();
                member.put("memberId", "M" + String.format("%06d", (pageNum - 1) * pageSize + i));
                member.put("memberName", "会员" + ((pageNum - 1) * pageSize + i));
                member.put("phone", "138" + String.format("%08d", (pageNum - 1) * pageSize + i));
                member.put("email", "member" + i + "@example.com");
                member.put("babyName", "宝宝" + i);
                member.put("babyBirthday", LocalDate.now().minusDays(new Random().nextInt(1095))); // 0-3岁随机
                
                // 根据宝宝年龄计算阶段
                int daysOld = (int) (Math.random() * 1095);
                String memberStage;
                if (daysOld < 0) {
                    memberStage = "0"; // 孕妇
                } else if (daysOld < 180) {
                    memberStage = "1"; // 0-6个月
                } else if (daysOld < 365) {
                    memberStage = "2"; // 6-12个月
                } else if (daysOld < 730) {
                    memberStage = "3"; // 1-2岁
                } else {
                    memberStage = "4"; // 2-3岁
                }
                member.put("stage", memberStage);
                
                member.put("status", new Random().nextBoolean() ? "active" : "inactive");
                member.put("totalPurchases", new Random().nextInt(50) + 1);
                member.put("totalAmount", Math.round((Math.random() * 10000 + 500) * 100) / 100.0);
                member.put("lastPurchaseDate", LocalDate.now().minusDays(new Random().nextInt(365)));
                member.put("registerDate", LocalDate.now().minusDays(new Random().nextInt(730) + 30));
                member.put("motScore", Math.round((Math.random() * 40 + 60) * 100) / 100.0); // 60-100分
                
                members.add(member);
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", members);
            data.put("total", 8650);
            data.put("pageNum", pageNum);
            data.put("pageSize", pageSize);
            data.put("pages", (8650 + pageSize - 1) / pageSize);
            
            log.info("获取会员列表成功: pageNum={}, pageSize={}, keyword={}", pageNum, pageSize, keyword);
            return Result.success("获取会员列表成功", data);
        } catch (Exception e) {
            log.error("获取会员列表失败: {}", e.getMessage(), e);
            return Result.error("获取会员列表失败");
        }
    }

    /**
     * 获取会员详情
     */
    @GetMapping("/detail/{memberId}")
    @ApiOperation("获取会员详情")
    public Result getMemberDetail(@PathVariable String memberId) {
        try {
            Map<String, Object> member = new HashMap<>();
            
            // 基本信息
            member.put("memberId", memberId);
            member.put("memberName", "张三");
            member.put("phone", "13812345678");
            member.put("email", "zhangsan@example.com");
            member.put("gender", "female");
            member.put("birthday", "1992-05-15");
            member.put("address", "上海市浦东新区张江高科技园区");
            member.put("registerDate", "2023-03-15");
            member.put("status", "active");
            
            // 宝宝信息
            Map<String, Object> babyInfo = new HashMap<>();
            babyInfo.put("babyName", "小宝");
            babyInfo.put("babyGender", "male");
            babyInfo.put("babyBirthday", "2024-01-15");
            babyInfo.put("currentStage", "2"); // 6-12个月
            babyInfo.put("expectedDate", "2024-01-15");
            member.put("babyInfo", babyInfo);
            
            // 购买历史
            List<Map<String, Object>> purchaseHistory = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                Map<String, Object> purchase = new HashMap<>();
                purchase.put("orderId", "ORD" + String.format("%08d", i));
                purchase.put("orderDate", LocalDate.now().minusDays(i * 30));
                purchase.put("products", Arrays.asList("能恩3段奶粉", "DHA营养品"));
                purchase.put("amount", Math.round((Math.random() * 500 + 200) * 100) / 100.0);
                purchase.put("status", "completed");
                purchaseHistory.add(purchase);
            }
            member.put("purchaseHistory", purchaseHistory);
            
            // MOT评分历史
            List<Map<String, Object>> motHistory = new ArrayList<>();
            for (int i = 1; i <= 6; i++) {
                Map<String, Object> mot = new HashMap<>();
                mot.put("date", LocalDate.now().minusMonths(i));
                mot.put("score", Math.round((Math.random() * 20 + 70) * 100) / 100.0);
                mot.put("category", "product_satisfaction");
                motHistory.add(mot);
            }
            member.put("motHistory", motHistory);
            
            // 统计信息
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalOrders", 25);
            statistics.put("totalAmount", 5680.50);
            statistics.put("avgOrderAmount", 227.22);
            statistics.put("motScore", 85.6);
            statistics.put("loyaltyLevel", "gold");
            member.put("statistics", statistics);
            
            log.info("获取会员详情成功: memberId={}", memberId);
            return Result.success("获取会员详情成功", member);
        } catch (Exception e) {
            log.error("获取会员详情失败: memberId={}, error={}", memberId, e.getMessage(), e);
            return Result.error("获取会员详情失败");
        }
    }

    /**
     * 获取会员阶段分布统计
     */
    @GetMapping("/stage-statistics")
    @ApiOperation("获取会员阶段分布统计")
    public Result getMemberStageStatistics(@RequestParam(required = false) String month) {
        try {
            List<Map<String, Object>> stageStats = new ArrayList<>();
            
            String[] stageNames = {"孕妇", "0-6个月", "6-12个月", "1-2岁", "2-3岁"};
            int[] stageCounts = {1050, 1820, 1650, 1250, 980};
            String[] stageColors = {"#ff6b6b", "#4ecdc4", "#45b7d1", "#96ceb4", "#feca57"};
            
            for (int i = 0; i < stageNames.length; i++) {
                Map<String, Object> stage = new HashMap<>();
                stage.put("stage", i);
                stage.put("stageName", stageNames[i]);
                stage.put("count", stageCounts[i]);
                stage.put("percentage", Math.round(stageCounts[i] * 100.0 / 6750 * 100) / 100.0);
                stage.put("color", stageColors[i]);
                
                // 最近趋势
                stage.put("trend", Math.random() > 0.5 ? "up" : "down");
                stage.put("trendValue", Math.round((Math.random() * 10 + 1) * 100) / 100.0);
                
                stageStats.add(stage);
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("stageStatistics", stageStats);
            data.put("totalMembers", 6750);
            data.put("lastUpdateTime", new Date());
            
            log.info("获取会员阶段分布统计成功");
            return Result.success("获取会员阶段分布统计成功", data);
        } catch (Exception e) {
            log.error("获取会员阶段分布统计失败: {}", e.getMessage(), e);
            return Result.error("获取会员阶段分布统计失败");
        }
    }

    /**
     * 更新会员阶段
     */
    @PutMapping("/{memberId}/stage")
    @ApiOperation("更新会员阶段")
    public Result updateMemberStage(@PathVariable String memberId, @RequestBody Map<String, Object> request) {
        try {
            String newStage = (String) request.get("stage");
            String reason = (String) request.get("reason");
            
            log.info("更新会员阶段: memberId={}, newStage={}, reason={}", memberId, newStage, reason);
            
            // 这里应该调用实际的会员服务更新阶段
            // memberService.updateStage(memberId, newStage, reason);
            
            return Result.success("会员阶段更新成功");
        } catch (Exception e) {
            log.error("更新会员阶段失败: memberId={}, error={}", memberId, e.getMessage(), e);
            return Result.error("更新会员阶段失败");
        }
    }
}
