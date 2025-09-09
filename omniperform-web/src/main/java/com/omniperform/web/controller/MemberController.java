package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 会员管理控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/member")
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    /**
     * 获取会员概览数据
     * 
     * @param month 月份参数 (格式: YYYY-MM)
     * @return 会员概览数据
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview(@RequestParam(required = false) String month) {
        try {
            log.info("获取会员概览数据，月份: {}", month);
            
            // 模拟会员概览数据
            Map<String, Object> overviewData = new HashMap<>();
            overviewData.put("totalMembers", 15680);
            overviewData.put("activeMembers", 12450);
            overviewData.put("newMembersThisMonth", 850);
            overviewData.put("churnMembers", 320);
            overviewData.put("repeatPurchaseRate", 68.5);
            overviewData.put("averageOrderValue", 285.6);
            overviewData.put("memberGrowthRate", 12.3);
            
            return Result.success(overviewData);
        } catch (Exception e) {
            log.error("获取会员概览数据失败", e);
            return Result.error("获取会员概览数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param month 月份参数
     * @param keyword 搜索关键词
     * @param stage 会员阶段
     * @param tier 会员层级
     * @return 会员列表数据
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String stage,
            @RequestParam(required = false) String tier) {
        try {
            log.info("获取会员列表，页码: {}, 大小: {}, 月份: {}, 关键词: {}, 阶段: {}, 层级: {}", 
                    page, size, month, keyword, stage, tier);
            
            // 模拟会员列表数据
            List<Map<String, Object>> memberList = new ArrayList<>();
            
            // 生成模拟数据
            String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
            String[] stages = {"导入期", "成长期", "成熟期", "衰退期", "流失期"};
            String[] tiers = {"高价值会员", "潜力会员", "新会员", "沉默会员"};
            String[] phones = {"138****1234", "139****5678", "150****9012", "151****3456"};
            
            for (int i = 0; i < size; i++) {
                Map<String, Object> member = new HashMap<>();
                member.put("id", 1000 + i + (page - 1) * size);
                member.put("name", names[i % names.length]);
                member.put("phone", phones[i % phones.length]);
                member.put("stage", stages[i % stages.length]);
                member.put("tier", tiers[i % tiers.length]);
                member.put("totalOrders", 5 + (i % 20));
                member.put("totalAmount", 1500.0 + (i * 100));
                member.put("lastOrderDate", "2025-01-" + String.format("%02d", 1 + (i % 28)));
                member.put("crfmeScore", 60 + (i % 40));
                member.put("joinDate", "2024-" + String.format("%02d", 1 + (i % 12)) + "-15");
                memberList.add(member);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", memberList);
            result.put("total", 15680);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (15680 + size - 1) / size);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取会员列表失败", e);
            return Result.error("获取会员列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员详情
     * 
     * @param id 会员ID
     * @return 会员详情数据
     */
    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getDetail(@PathVariable Long id) {
        try {
            log.info("获取会员详情，ID: {}", id);
            
            // 模拟会员详情数据
            Map<String, Object> memberDetail = new HashMap<>();
            memberDetail.put("id", id);
            memberDetail.put("name", "张三");
            memberDetail.put("phone", "138****1234");
            memberDetail.put("email", "zhangsan@example.com");
            memberDetail.put("gender", "男");
            memberDetail.put("age", 28);
            memberDetail.put("city", "上海");
            memberDetail.put("joinDate", "2024-03-15");
            memberDetail.put("stage", "成熟期");
            memberDetail.put("tier", "高价值会员");
            memberDetail.put("totalOrders", 25);
            memberDetail.put("totalAmount", 8500.0);
            memberDetail.put("averageOrderValue", 340.0);
            memberDetail.put("lastOrderDate", "2025-01-10");
            memberDetail.put("crfmeScore", 85);
            
            // CRFM-E模型详细评分
            Map<String, Object> crfmeDetails = new HashMap<>();
            crfmeDetails.put("behaviorScore", 18); // B - 行为评分 (0-20)
            crfmeDetails.put("recencyScore", 16);   // R - 最近购买 (0-20)
            crfmeDetails.put("frequencyScore", 17); // F - 购买频次 (0-20)
            crfmeDetails.put("monetaryScore", 18);  // M - 消费金额 (0-20)
            crfmeDetails.put("engagementScore", 16); // E - 参与度 (0-20)
            memberDetail.put("crfmeDetails", crfmeDetails);
            
            // 最近订单记录
            List<Map<String, Object>> recentOrders = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Map<String, Object> order = new HashMap<>();
                order.put("orderId", "ORD" + (202501100 + i));
                order.put("orderDate", "2025-01-" + String.format("%02d", 10 - i));
                order.put("amount", 299.0 + (i * 50));
                order.put("status", i == 0 ? "已完成" : "已发货");
                recentOrders.add(order);
            }
            memberDetail.put("recentOrders", recentOrders);
            
            return Result.success(memberDetail);
        } catch (Exception e) {
            log.error("获取会员详情失败", e);
            return Result.error("获取会员详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建会员
     * 
     * @param memberData 会员数据
     * @return 创建结果
     */
    @PostMapping
    public Result<Map<String, Object>> create(@RequestBody Map<String, Object> memberData) {
        try {
            log.info("创建会员，数据: {}", memberData);
            
            // 模拟创建会员
            Long newMemberId = System.currentTimeMillis() % 100000;
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", newMemberId);
            result.put("message", "会员创建成功");
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("创建会员失败", e);
            return Result.error("创建会员失败: " + e.getMessage());
        }
    }

    /**
     * 更新会员
     * 
     * @param id 会员ID
     * @param memberData 会员数据
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @RequestBody Map<String, Object> memberData) {
        try {
            log.info("更新会员，ID: {}, 数据: {}", id, memberData);
            
            // 模拟更新会员
            return Result.success("会员信息更新成功");
        } catch (Exception e) {
            log.error("更新会员失败", e);
            return Result.error("更新会员失败: " + e.getMessage());
        }
    }

    /**
     * 删除会员
     * 
     * @param id 会员ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            log.info("删除会员，ID: {}", id);
            
            // 模拟删除会员
            return Result.success("会员删除成功");
        } catch (Exception e) {
            log.error("删除会员失败", e);
            return Result.error("删除会员失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员分群
     * 
     * @return 会员分群数据
     */
    @GetMapping("/segments")
    public Result<List<Map<String, Object>>> getSegments() {
        try {
            log.info("获取会员分群数据");
            
            List<Map<String, Object>> segments = new ArrayList<>();
            
            // 高价值会员
            Map<String, Object> highValue = new HashMap<>();
            highValue.put("id", 1);
            highValue.put("name", "高价值会员");
            highValue.put("description", "CRFM-E评分≥80分，消费金额高，购买频次高");
            highValue.put("count", 1250);
            highValue.put("percentage", 8.0);
            segments.add(highValue);
            
            // 潜力会员
            Map<String, Object> potential = new HashMap<>();
            potential.put("id", 2);
            potential.put("name", "潜力会员");
            potential.put("description", "CRFM-E评分60-79分，有增长潜力");
            potential.put("count", 3920);
            potential.put("percentage", 25.0);
            segments.add(potential);
            
            // 新会员
            Map<String, Object> newMember = new HashMap<>();
            newMember.put("id", 3);
            newMember.put("name", "新会员");
            newMember.put("description", "注册时间≤3个月，需要培育");
            newMember.put("count", 2350);
            newMember.put("percentage", 15.0);
            segments.add(newMember);
            
            // 沉默会员
            Map<String, Object> silent = new HashMap<>();
            silent.put("id", 4);
            silent.put("name", "沉默会员");
            silent.put("description", "近6个月无购买行为，需要激活");
            silent.put("count", 8160);
            silent.put("percentage", 52.0);
            segments.add(silent);
            
            return Result.success(segments);
        } catch (Exception e) {
            log.error("获取会员分群失败", e);
            return Result.error("获取会员分群失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员阶段统计
     * 
     * @param month 月份参数
     * @return 会员阶段统计数据
     */
    @GetMapping("/stage-statistics")
    public Result<Map<String, Object>> getStatistics(@RequestParam(required = false) String month) {
        try {
            log.info("获取会员阶段统计，月份: {}", month);
            
            // 模拟会员阶段统计数据
            List<Map<String, Object>> stageStatistics = new ArrayList<>();
            
            String[] stages = {"导入期", "成长期", "成熟期", "衰退期", "流失期"};
            int[] counts = {850, 1520, 2030, 1350, 1000};
            String[] colors = {"#0d6efd", "#198754", "#0dcaf0", "#ffc107", "#dc3545"};
            
            for (int i = 0; i < stages.length; i++) {
                Map<String, Object> stage = new HashMap<>();
                stage.put("stageName", stages[i]);
                stage.put("count", counts[i]);
                stage.put("percentage", Math.round(counts[i] * 100.0 / 6750 * 100) / 100.0);
                stage.put("color", colors[i]);
                stageStatistics.add(stage);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("stageStatistics", stageStatistics);
            result.put("totalMembers", 6750);
            result.put("month", month != null ? month : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取会员阶段统计失败", e);
            return Result.error("获取会员阶段统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取CRFM-E评分分布
     * 
     * @param month 月份参数
     * @return CRFM-E评分分布数据
     */
    @GetMapping("/crfme-distribution")
    public Result<Map<String, Object>> getCrfmeDistribution(@RequestParam(required = false) String month) {
        try {
            log.info("获取CRFM-E评分分布，月份: {}", month);
            
            // 模拟CRFM-E评分分布数据
            List<Map<String, Object>> distribution = new ArrayList<>();
            
            String[] scoreRanges = {"0-20", "21-40", "41-60", "61-80", "81-100"};
            int[] counts = {500, 800, 2500, 2100, 850};
            String[] levels = {"低价值", "一般价值", "中等价值", "高价值", "超高价值"};
            
            for (int i = 0; i < scoreRanges.length; i++) {
                Map<String, Object> range = new HashMap<>();
                range.put("scoreRange", scoreRanges[i]);
                range.put("count", counts[i]);
                range.put("level", levels[i]);
                range.put("percentage", Math.round(counts[i] * 100.0 / 6750 * 100) / 100.0);
                distribution.add(range);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("distribution", distribution);
            result.put("totalMembers", 6750);
            result.put("averageScore", 62.5);
            result.put("month", month != null ? month : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取CRFM-E评分分布失败", e);
            return Result.error("获取CRFM-E评分分布失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员增长趋势
     * 
     * @param period 时间周期 (month/quarter/year)
     * @param months 月份数量
     * @return 会员增长趋势数据
     */
    @GetMapping("/growth-trend")
    public Result<Map<String, Object>> getGrowthTrend(
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(defaultValue = "12") int months) {
        try {
            log.info("获取会员增长趋势，周期: {}, 月份数: {}", period, months);
            
            // 模拟会员增长趋势数据
            List<Map<String, Object>> trendData = new ArrayList<>();
            
            LocalDate currentDate = LocalDate.now();
            for (int i = months - 1; i >= 0; i--) {
                LocalDate date = currentDate.minusMonths(i);
                Map<String, Object> monthData = new HashMap<>();
                monthData.put("month", date.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                monthData.put("newMembers", 800 + (int)(Math.random() * 400)); // 800-1200随机
                monthData.put("totalMembers", 15000 + i * 200); // 递增趋势
                monthData.put("activeMembers", (int)((15000 + i * 200) * (0.6 + Math.random() * 0.2))); // 60-80%活跃
                monthData.put("churnMembers", 50 + (int)(Math.random() * 100)); // 50-150流失
                trendData.add(monthData);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("trendData", trendData);
            result.put("period", period);
            result.put("months", months);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取会员增长趋势失败", e);
            return Result.error("获取会员增长趋势失败: " + e.getMessage());
        }
    }

    /**
     * 计算会员CRFM-E评分
     * 
     * @param id 会员ID
     * @return CRFM-E评分结果
     */
    @PostMapping("/calculate-crfme/{id}")
    public Result<Map<String, Object>> calculateCrfme(@PathVariable Long id) {
        try {
            log.info("计算会员CRFM-E评分，ID: {}", id);
            
            // 模拟CRFM-E评分计算
            Map<String, Object> crfmeScore = new HashMap<>();
            crfmeScore.put("memberId", id);
            crfmeScore.put("behaviorScore", 15 + (int)(Math.random() * 5)); // B - 行为评分 (0-20)
            crfmeScore.put("recencyScore", 14 + (int)(Math.random() * 6));   // R - 最近购买 (0-20)
            crfmeScore.put("frequencyScore", 16 + (int)(Math.random() * 4)); // F - 购买频次 (0-20)
            crfmeScore.put("monetaryScore", 17 + (int)(Math.random() * 3));  // M - 消费金额 (0-20)
            crfmeScore.put("engagementScore", 15 + (int)(Math.random() * 5)); // E - 参与度 (0-20)
            
            int totalScore = (Integer)crfmeScore.get("behaviorScore") + 
                           (Integer)crfmeScore.get("recencyScore") + 
                           (Integer)crfmeScore.get("frequencyScore") + 
                           (Integer)crfmeScore.get("monetaryScore") + 
                           (Integer)crfmeScore.get("engagementScore");
            
            crfmeScore.put("totalScore", totalScore);
            crfmeScore.put("level", totalScore >= 80 ? "超高价值" : 
                                   totalScore >= 60 ? "高价值" : 
                                   totalScore >= 40 ? "中等价值" : 
                                   totalScore >= 20 ? "一般价值" : "低价值");
            crfmeScore.put("calculatedAt", LocalDate.now().toString());
            
            return Result.success(crfmeScore);
        } catch (Exception e) {
            log.error("计算会员CRFM-E评分失败", e);
            return Result.error("计算会员CRFM-E评分失败: " + e.getMessage());
        }
    }

    /**
     * 根据手机号查询会员
     * 
     * @param phone 手机号
     * @return 会员信息
     */
    @GetMapping("/phone/{phone}")
    public Result<Map<String, Object>> getByPhone(@PathVariable String phone) {
        try {
            log.info("根据手机号查询会员: {}", phone);
            
            // 模拟根据手机号查询会员
            Map<String, Object> member = new HashMap<>();
            member.put("id", 1001L);
            member.put("name", "张三");
            member.put("phone", phone);
            member.put("email", "zhangsan@example.com");
            member.put("stage", "成熟期");
            member.put("tier", "高价值会员");
            member.put("joinDate", "2024-03-15");
            member.put("crfmeScore", 85);
            
            return Result.success(member);
        } catch (Exception e) {
            log.error("根据手机号查询会员失败", e);
            return Result.error("根据手机号查询会员失败: " + e.getMessage());
        }
    }
}