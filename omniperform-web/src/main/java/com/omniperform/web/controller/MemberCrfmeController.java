package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import com.omniperform.system.service.IMemberCrfmeDistributionService;
import com.omniperform.system.domain.MemberCrfmeDistribution;
import com.omniperform.common.utils.poi.ExcelUtil;
import com.omniperform.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 会员价值分层(CRFM-E)控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/member/crfme")
public class MemberCrfmeController {

    private static final Logger log = LoggerFactory.getLogger(MemberCrfmeController.class);

    @Autowired
    private IMemberCrfmeDistributionService memberCrfmeDistributionService;

    /**
     * 获取会员价值分层分布数据
     */
    @GetMapping("/distribution")
    public Result<Map<String, Object>> getCrfmeDistribution(@RequestParam(required = false) String month) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            log.info("🔍 [CRFM-E API] 接收到请求参数 month: {}", month);
            
            // 获取会员价值分层分布数据
            MemberCrfmeDistribution queryParam = new MemberCrfmeDistribution();
            if (month != null && !month.isEmpty()) {
                queryParam.setDataMonth(month);
                log.info("🔍 [CRFM-E API] 设置查询参数 dataMonth: {}", month);
            } else {
                log.info("🔍 [CRFM-E API] 月份参数为空，将查询所有数据");
            }
            
            List<MemberCrfmeDistribution> distributionList = memberCrfmeDistributionService.selectMemberCrfmeDistributionList(queryParam);
            log.info("🔍 [CRFM-E API] 查询到 {} 条分布数据", distributionList.size());
            
            // 打印查询结果详情
            for (MemberCrfmeDistribution dist : distributionList) {
                log.info("🔍 [CRFM-E API] 数据详情 - 月份: {}, 分层: {}, 数量: {}", 
                    dist.getDataMonth(), dist.getTier(), dist.getCount());
            }
            
            // 构建图表数据
            List<String> scoreRanges = new ArrayList<>();
            List<Integer> counts = new ArrayList<>();
            List<Double> percentages = new ArrayList<>();
            List<Double> avgScores = new ArrayList<>();
            List<String> tiers = new ArrayList<>();
            
            for (MemberCrfmeDistribution distribution : distributionList) {
                scoreRanges.add(distribution.getScoreRange());
                counts.add(distribution.getCount());
                percentages.add(distribution.getPercentage() != null ? distribution.getPercentage().doubleValue() : 0.0);
                avgScores.add(distribution.getAvgScore() != null ? distribution.getAvgScore().doubleValue() : 0.0);
                tiers.add(distribution.getTier());
            }
            
            data.put("scoreRanges", scoreRanges);
            data.put("counts", counts);
            data.put("percentages", percentages);
            data.put("avgScores", avgScores);
            data.put("tiers", tiers);
            data.put("distributionList", distributionList);
            
            return Result.success("获取会员价值分层分布数据成功", data);
        } catch (Exception e) {
            log.error("获取会员价值分层分布数据失败: {}", e.getMessage(), e);
            return Result.error("获取会员价值分层分布数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员价值分层统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getCrfmeStatistics(@RequestParam(required = false) String month) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // 获取会员价值分层分布数据
            MemberCrfmeDistribution queryParam = new MemberCrfmeDistribution();
            if (month != null && !month.isEmpty()) {
                queryParam.setDataMonth(month);
            }
            List<MemberCrfmeDistribution> distributionList = memberCrfmeDistributionService.selectMemberCrfmeDistributionList(queryParam);
            
            // 计算统计数据
            int totalMembers = 0;
            int highValueMembers = 0; // 81-100分
            int potentialMembers = 0; // 61-80分
            int normalMembers = 0; // 41-60分
            int lowValueMembers = 0; // 21-40分
            int silentMembers = 0; // 0-20分
            double totalScore = 0.0;
            
            for (MemberCrfmeDistribution distribution : distributionList) {
                int count = distribution.getCount();
                totalMembers += count;
                
                String scoreRange = distribution.getScoreRange();
                if ("81-100".equals(scoreRange)) {
                    highValueMembers = count;
                } else if ("61-80".equals(scoreRange)) {
                    potentialMembers = count;
                } else if ("41-60".equals(scoreRange)) {
                    normalMembers = count;
                } else if ("21-40".equals(scoreRange)) {
                    lowValueMembers = count;
                } else if ("0-20".equals(scoreRange)) {
                    silentMembers = count;
                }
                
                if (distribution.getAvgScore() != null) {
                    totalScore += distribution.getAvgScore().doubleValue() * count;
                }
            }
            
            // 计算平均分
            double avgScore = totalMembers > 0 ? totalScore / totalMembers : 0.0;
            
            // 计算各层级占比
            double highValueRate = totalMembers > 0 ? (double) highValueMembers / totalMembers * 100 : 0.0;
            double potentialRate = totalMembers > 0 ? (double) potentialMembers / totalMembers * 100 : 0.0;
            double normalRate = totalMembers > 0 ? (double) normalMembers / totalMembers * 100 : 0.0;
            double lowValueRate = totalMembers > 0 ? (double) lowValueMembers / totalMembers * 100 : 0.0;
            double silentRate = totalMembers > 0 ? (double) silentMembers / totalMembers * 100 : 0.0;
            
            data.put("totalMembers", totalMembers);
            data.put("avgScore", Math.round(avgScore * 100.0) / 100.0);
            data.put("highValueMembers", highValueMembers);
            data.put("highValueRate", Math.round(highValueRate * 100.0) / 100.0);
            data.put("potentialMembers", potentialMembers);
            data.put("potentialRate", Math.round(potentialRate * 100.0) / 100.0);
            data.put("normalMembers", normalMembers);
            data.put("normalRate", Math.round(normalRate * 100.0) / 100.0);
            data.put("lowValueMembers", lowValueMembers);
            data.put("lowValueRate", Math.round(lowValueRate * 100.0) / 100.0);
            data.put("silentMembers", silentMembers);
            data.put("silentRate", Math.round(silentRate * 100.0) / 100.0);
            
            return Result.success("获取会员价值分层统计数据成功", data);
        } catch (Exception e) {
            log.error("获取会员价值分层统计数据失败: {}", e.getMessage(), e);
            return Result.error("获取会员价值分层统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取会员价值分层趋势数据
     */
    @GetMapping("/trend")
    public Result<Map<String, Object>> getCrfmeTrend(@RequestParam(defaultValue = "6") int months) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            List<String> monthLabels = new ArrayList<>();
            List<Integer> highValueTrend = new ArrayList<>();
            List<Integer> potentialTrend = new ArrayList<>();
            List<Integer> normalTrend = new ArrayList<>();
            List<Integer> lowValueTrend = new ArrayList<>();
            List<Integer> silentTrend = new ArrayList<>();
            List<Double> avgScoreTrend = new ArrayList<>();
            
            // 获取数据库中所有有数据的月份，按时间顺序排列
            List<MemberCrfmeDistribution> allDistributionData = memberCrfmeDistributionService.selectMemberCrfmeDistributionList(new MemberCrfmeDistribution());
            
            // 按月份分组
            Map<String, List<MemberCrfmeDistribution>> monthlyData = new LinkedHashMap<>();
            for (MemberCrfmeDistribution distribution : allDistributionData) {
                String month = distribution.getDataMonth();
                monthlyData.computeIfAbsent(month, k -> new ArrayList<>()).add(distribution);
            }
            
            // 获取最近几个月的数据
            List<String> sortedMonths = new ArrayList<>(monthlyData.keySet());
            Collections.sort(sortedMonths);
            
            int startIndex = Math.max(0, sortedMonths.size() - months);
            for (int i = startIndex; i < sortedMonths.size(); i++) {
                String month = sortedMonths.get(i);
                monthLabels.add(month);
                
                List<MemberCrfmeDistribution> monthData = monthlyData.get(month);
                
                int highValue = 0, potential = 0, normal = 0, lowValue = 0, silent = 0;
                double totalScore = 0.0;
                int totalCount = 0;
                
                for (MemberCrfmeDistribution distribution : monthData) {
                    String scoreRange = distribution.getScoreRange();
                    int count = distribution.getCount();
                    
                    if ("81-100".equals(scoreRange)) {
                        highValue = count;
                    } else if ("61-80".equals(scoreRange)) {
                        potential = count;
                    } else if ("41-60".equals(scoreRange)) {
                        normal = count;
                    } else if ("21-40".equals(scoreRange)) {
                        lowValue = count;
                    } else if ("0-20".equals(scoreRange)) {
                        silent = count;
                    }
                    
                    if (distribution.getAvgScore() != null) {
                        totalScore += distribution.getAvgScore().doubleValue() * count;
                        totalCount += count;
                    }
                }
                
                highValueTrend.add(highValue);
                potentialTrend.add(potential);
                normalTrend.add(normal);
                lowValueTrend.add(lowValue);
                silentTrend.add(silent);
                
                double avgScore = totalCount > 0 ? totalScore / totalCount : 0.0;
                avgScoreTrend.add(Math.round(avgScore * 100.0) / 100.0);
            }
            
            data.put("monthLabels", monthLabels);
            data.put("highValueTrend", highValueTrend);
            data.put("potentialTrend", potentialTrend);
            data.put("normalTrend", normalTrend);
            data.put("lowValueTrend", lowValueTrend);
            data.put("silentTrend", silentTrend);
            data.put("avgScoreTrend", avgScoreTrend);
            
            return Result.success("获取会员价值分层趋势数据成功", data);
        } catch (Exception e) {
            log.error("获取会员价值分层趋势数据失败: {}", e.getMessage(), e);
            return Result.error("获取会员价值分层趋势数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取可用月份列表
     */
    @GetMapping("/available-months")
    public Result<List<String>> getAvailableMonths() {
        try {
            List<MemberCrfmeDistribution> allData = memberCrfmeDistributionService.selectMemberCrfmeDistributionList(new MemberCrfmeDistribution());
            Set<String> monthSet = new HashSet<>();
            
            for (MemberCrfmeDistribution distribution : allData) {
                if (distribution.getDataMonth() != null) {
                    monthSet.add(distribution.getDataMonth());
                }
            }
            
            List<String> months = new ArrayList<>(monthSet);
            Collections.sort(months, Collections.reverseOrder()); // 按时间倒序排列
            
            return Result.success("获取可用月份成功", months);
        } catch (Exception e) {
            log.error("获取可用月份失败: {}", e.getMessage(), e);
            return Result.error("获取可用月份列表失败: " + e.getMessage());
        }
    }

    /**
     * 导出会员价值分层数据模板
     */
    @GetMapping("/import/template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<MemberCrfmeDistribution> util = new ExcelUtil<>(MemberCrfmeDistribution.class);
            util.exportExcel(response, new ArrayList<>(), "会员价值分层数据模板");
        } catch (Exception e) {
            log.error("导出会员价值分层数据模板失败", e);
        }
    }

    /**
     * 导入会员价值分层数据
     */
    @PostMapping("/import")
    public Result<String> importCrfmeData(@RequestParam("file") MultipartFile file) {
        try {
            ExcelUtil<MemberCrfmeDistribution> util = new ExcelUtil<>(MemberCrfmeDistribution.class);
            List<MemberCrfmeDistribution> dataList = util.importExcel(file.getInputStream());
            
            int successCount = 0;
            int failureCount = 0;
            StringBuilder failureMsg = new StringBuilder();
            
            for (MemberCrfmeDistribution data : dataList) {
                try {
                    memberCrfmeDistributionService.insertMemberCrfmeDistribution(data);
                    successCount++;
                } catch (Exception e) {
                    failureCount++;
                    String msg = "<br/>" + failureCount + "、数据月份 " + data.getDataMonth() + 
                               " 评分区间 " + data.getScoreRange() + " 导入失败：";
                    failureMsg.append(msg).append(e.getMessage());
                }
            }
            
            if (failureCount > 0) {
                failureMsg.insert(0, "很抱歉，导入失败！共 " + failureCount + " 条数据格式不正确，错误如下：");
                return Result.error(failureMsg.toString());
            } else {
                return Result.success("恭喜您，数据已全部导入成功！共 " + successCount + " 条，数据如下：");
            }
        } catch (Exception e) {
            log.error("导入会员价值分层数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }
}