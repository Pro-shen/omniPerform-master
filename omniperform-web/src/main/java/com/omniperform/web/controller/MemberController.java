package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import com.omniperform.system.service.IMemberOverviewService;
import com.omniperform.system.service.IMemberProfileAnalysisService;
import com.omniperform.system.domain.MemberInfo;
import com.omniperform.system.domain.MemberMonthlyStats;
import com.omniperform.system.domain.MemberStageStats;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IMemberOverviewService memberOverviewService;

    @Autowired
    private IMemberProfileAnalysisService memberProfileAnalysisService;

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
            
            // 使用Service层获取真实数据
            Map<String, Object> overviewData = memberOverviewService.getMemberOverview(month);
            
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
            // 使用Service层获取会员列表数据
            MemberInfo memberInfo = new MemberInfo();
            if (keyword != null && !keyword.trim().isEmpty()) {
                memberInfo.setMemberName(keyword);
            }
            if (stage != null && !stage.trim().isEmpty()) {
                memberInfo.setBabyStage(stage);
            }
            
            List<MemberInfo> memberList = memberOverviewService.selectMemberInfoList(memberInfo);
            
            // 分页处理
            int total = memberList.size();
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, total);
            List<MemberInfo> pagedList = memberList.subList(startIndex, endIndex);
            
            // 转换为Map格式
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (MemberInfo member : pagedList) {
                Map<String, Object> memberMap = new HashMap<>();
                memberMap.put("id", member.getMemberId());
                memberMap.put("name", member.getMemberName());
                memberMap.put("phone", member.getPhone());
                memberMap.put("stage", member.getBabyStage());
                memberMap.put("tier", "普通会员"); // 默认值，可以后续扩展
                memberMap.put("totalOrders", member.getTotalPurchaseCount());
                memberMap.put("totalAmount", member.getTotalPurchaseAmount());
                memberMap.put("lastOrderDate", member.getLastPurchaseTime());
                memberMap.put("crfmeScore", 0); // 默认值，可以通过Service计算
                memberMap.put("joinDate", member.getRegistrationDate());
                resultList.add(memberMap);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", resultList);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            
            return Result.success("获取会员列表成功", result);
        } catch (Exception e) {
            log.error("获取会员列表失败: {}", e.getMessage(), e);
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
     * @param month 月份参数
     * @return 会员分群数据
     */
    @GetMapping("/segments")
    public Result<List<Map<String, Object>>> getSegments(@RequestParam(required = false) String month) {
        try {
            log.info("获取会员分群数据，月份: {}", month);
            
            List<Map<String, Object>> segments = new ArrayList<>();
            
            // 根据月份调整数据（模拟不同月份的数据变化）
            double monthFactor = 1.0;
            if (month != null) {
                if (month.equals("2025-05")) {
                    monthFactor = 0.85; // 5月数据相对较低
                } else if (month.equals("2025-06")) {
                    monthFactor = 0.92; // 6月数据中等
                } else if (month.equals("2025-07")) {
                    monthFactor = 1.0; // 7月数据最高
                }
            }
            
            // 高价值会员
            Map<String, Object> highValue = new HashMap<>();
            highValue.put("id", 1);
            highValue.put("name", "高价值会员");
            highValue.put("description", "CRFM-E评分≥80分，消费金额高，购买频次高");
            highValue.put("count", (int)(1250 * monthFactor));
            highValue.put("percentage", 8.0);
            segments.add(highValue);
            
            // 潜力会员
            Map<String, Object> potential = new HashMap<>();
            potential.put("id", 2);
            potential.put("name", "潜力会员");
            potential.put("description", "CRFM-E评分60-79分，有增长潜力");
            potential.put("count", (int)(3920 * monthFactor));
            potential.put("percentage", 25.0);
            segments.add(potential);
            
            // 新会员
            Map<String, Object> newMember = new HashMap<>();
            newMember.put("id", 3);
            newMember.put("name", "新会员");
            newMember.put("description", "注册时间≤3个月，需要培育");
            newMember.put("count", (int)(2350 * monthFactor));
            newMember.put("percentage", 15.0);
            segments.add(newMember);
            
            // 沉默会员
            Map<String, Object> silent = new HashMap<>();
            silent.put("id", 4);
            silent.put("name", "沉默会员");
            silent.put("description", "近6个月无购买行为，需要激活");
            silent.put("count", (int)(8160 * monthFactor));
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
            
            // 使用Service层获取会员阶段统计数据
            List<Map<String, Object>> stageStatistics = memberOverviewService.getMemberStageDistribution(month);
            
            Map<String, Object> result = new HashMap<>();
            result.put("stageStatistics", stageStatistics);
            result.put("month", month != null ? month : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取会员阶段统计失败", e);
            return Result.error("获取会员阶段统计失败: " + e.getMessage());
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
            
            // 计算开始和结束月份
            LocalDate currentDate = LocalDate.now();
            String endMonth = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            String startMonth = currentDate.minusMonths(months - 1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            // 使用Service层获取会员增长趋势数据
            List<Map<String, Object>> trendData = memberOverviewService.getMemberGrowthTrend(startMonth, endMonth);
            
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
            
            // 使用Service层计算CRFM-E评分
            Map<String, Object> crfmeScore = memberOverviewService.calculateMemberCrfme(id);
            
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
            
            // 使用Service层根据手机号查询会员
            MemberInfo memberInfo = memberOverviewService.getMemberByPhone(phone);
            
            if (memberInfo == null) {
                return Result.error("未找到该手机号对应的会员信息");
            }
            
            Map<String, Object> member = new HashMap<>();
            member.put("id", memberInfo.getMemberId());
            member.put("name", memberInfo.getMemberName());
            member.put("phone", memberInfo.getPhone());
            member.put("email", memberInfo.getEmail());
            member.put("stage", memberInfo.getBabyStage());
            member.put("tier", "普通会员"); // 默认值，可以后续扩展
            member.put("joinDate", memberInfo.getRegistrationDate());
            member.put("crfmeScore", 0); // 默认值，可以通过Service计算
            
            return Result.success(member);
        } catch (Exception e) {
            log.error("根据手机号查询会员失败", e);
            return Result.error("根据手机号查询会员失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员画像分析
     * 
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @param month 月份参数（格式：YYYY-MM）
     * @return 会员画像分析数据
     */
    @GetMapping("/profile-analysis")
    public Result<Map<String, Object>> getProfileAnalysis(
            @RequestParam(required = false) String profileType,
            @RequestParam(required = false) String regionCode,
            @RequestParam(required = false) String month) {
        try {
            log.info("获取会员画像分析，画像类型: {}, 区域代码: {}, 月份: {}", profileType, regionCode, month);
            
            // 使用Service层获取会员画像分析数据
            Map<String, Object> profileData;
            if (month != null && !month.trim().isEmpty()) {
                profileData = memberProfileAnalysisService.getMemberProfileData(profileType, regionCode, month);
            } else {
                profileData = memberProfileAnalysisService.getMemberProfileData(profileType, regionCode);
            }
            
            return Result.success(profileData);
        } catch (Exception e) {
            log.error("获取会员画像分析失败", e);
            return Result.error("获取会员画像分析失败: " + e.getMessage());
        }
    }
}