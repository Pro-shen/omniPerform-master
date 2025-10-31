package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import com.omniperform.web.domain.GuideInfo;
import com.omniperform.web.domain.GuidePerformance;
import com.omniperform.web.service.IGuideInfoService;
import com.omniperform.web.service.IGuidePerformanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

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

    @Autowired
    private IGuideInfoService guideInfoService;

    @Autowired
    private IGuidePerformanceService guidePerformanceService;

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
            // 构建查询条件
            GuideInfo queryGuide = new GuideInfo();
            if (region != null && !region.isEmpty()) {
                queryGuide.setRegionCode(region);
            }
            if (status != null && !status.isEmpty()) {
                try {
                    queryGuide.setStatus(Integer.parseInt(status));
                } catch (NumberFormatException e) {
                    // 如果状态是字符串，转换为对应的数字
                    if ("在职".equals(status)) {
                        queryGuide.setStatus(1);
                    } else if ("离职".equals(status)) {
                        queryGuide.setStatus(2);
                    } else if ("休假".equals(status)) {
                        queryGuide.setStatus(3);
                    }
                }
            }
            
            // 查询导购列表
            List<GuideInfo> guideInfoList = guideInfoService.selectGuideInfoList(queryGuide);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> guides = new ArrayList<>();
            for (GuideInfo guideInfo : guideInfoList) {
                Map<String, Object> guide = new HashMap<>();
                guide.put("id", guideInfo.getGuideId());
                guide.put("name", guideInfo.getGuideName());
                guide.put("employeeId", guideInfo.getGuideCode());
                guide.put("region", guideInfo.getRegionName());
                guide.put("status", guideInfo.getStatus());
                guide.put("phone", guideInfo.getPhone());
                guide.put("email", guideInfo.getEmail());
                guide.put("joinDate", guideInfo.getHireDate() != null ? guideInfo.getHireDate().toString() : "");
                guide.put("level", guideInfo.getLevel());
                guides.add(guide);
            }
            
            // 分页处理
            int total = guides.size();
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(page * size, total);
            List<Map<String, Object>> pagedGuides = startIndex < total ? guides.subList(startIndex, endIndex) : new ArrayList<>();
            
            Map<String, Object> result = new HashMap<>();
            result.put("guides", pagedGuides);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            
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
            // 构建查询条件
            GuidePerformance queryPerformance = new GuidePerformance();
            if (guideId != null && !guideId.isEmpty()) {
                queryPerformance.setGuideId(Long.parseLong(guideId));
            }
            // 如果有日期范围，可以设置数据月份
            if (startDate != null && !startDate.isEmpty()) {
                // 从startDate提取年月，格式化为YYYY-MM
                String dataMonth = startDate.substring(0, 7);
                queryPerformance.setDataMonth(dataMonth);
            }
            
            // 查询绩效数据
            List<GuidePerformance> performanceList = guidePerformanceService.selectGuidePerformanceList(queryPerformance);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> performances = new ArrayList<>();
            for (GuidePerformance performance : performanceList) {
                Map<String, Object> perfMap = new HashMap<>();
                perfMap.put("guideId", performance.getGuideId());
                perfMap.put("guideName", performance.getGuideName());
                perfMap.put("region", performance.getRegionName());
                perfMap.put("newMembers", performance.getNewMembers() != null ? performance.getNewMembers() : 0);
                perfMap.put("motCompletionRate", performance.getMotCompletionRate() != null ? performance.getMotCompletionRate().doubleValue() : 0.0);
                perfMap.put("customerSatisfaction", performance.getCustomerSatisfaction() != null ? performance.getCustomerSatisfaction().doubleValue() : 0.0);
                perfMap.put("sales", performance.getSalesAmount() != null ? performance.getSalesAmount().doubleValue() : 0.0);
                perfMap.put("motTasksCompleted", performance.getMotTasksCompleted() != null ? performance.getMotTasksCompleted() : 0);
                perfMap.put("responseTime", performance.getResponseTime() != null ? performance.getResponseTime().doubleValue() : 0.0);
                perfMap.put("rank", performance.getRankOverall() != null ? performance.getRankOverall() : 0);
                perfMap.put("caiScore", performance.getCaiScore() != null ? performance.getCaiScore().doubleValue() : 0.0);
                perfMap.put("rmvScore", performance.getRmvScore() != null ? performance.getRmvScore().doubleValue() : 0.0);
                perfMap.put("matrixPosition", performance.getMatrixPosition());
                perfMap.put("matrixType", performance.getMatrixType());
                performances.add(perfMap);
            }
            
            log.info("获取导购绩效数据成功，查询到{}条记录", performances.size());
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
            Long id = Long.parseLong(guideId);
            
            // 查询导购基础信息
            GuideInfo guideInfo = guideInfoService.selectGuideInfoByGuideId(id);
            if (guideInfo == null) {
                return Result.error("导购信息不存在");
            }
            
            Map<String, Object> guide = new HashMap<>();
            guide.put("id", guideInfo.getGuideId());
            guide.put("name", guideInfo.getGuideName());
            guide.put("employeeId", guideInfo.getGuideCode());
            guide.put("region", guideInfo.getRegionName());
            guide.put("status", guideInfo.getStatus() == 1 ? "在职" : "离职");
            guide.put("phone", guideInfo.getPhone());
            guide.put("email", guideInfo.getEmail());
            guide.put("joinDate", guideInfo.getHireDate() != null ? guideInfo.getHireDate().toString() : "");
            guide.put("level", guideInfo.getLevel());
            guide.put("department", guideInfo.getDepartment());
            guide.put("manager", guideInfo.getSupervisorName());
            
            // 查询最新绩效数据
            GuidePerformance queryPerformance = new GuidePerformance();
            queryPerformance.setGuideId(id);
            // 获取当前月份的绩效数据
            String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            queryPerformance.setDataMonth(currentMonth);
            
            List<GuidePerformance> performanceList = guidePerformanceService.selectGuidePerformanceList(queryPerformance);
            
            // 绩效指标
            Map<String, Object> performance = new HashMap<>();
            if (!performanceList.isEmpty()) {
                GuidePerformance latestPerformance = performanceList.get(0);
                performance.put("newMembers", latestPerformance.getNewMembers() != null ? latestPerformance.getNewMembers() : 0);
                performance.put("motCompletionRate", latestPerformance.getMotCompletionRate() != null ? latestPerformance.getMotCompletionRate().doubleValue() : 0.0);
                performance.put("customerSatisfaction", latestPerformance.getCustomerSatisfaction() != null ? latestPerformance.getCustomerSatisfaction().doubleValue() : 0.0);
                performance.put("sales", latestPerformance.getSalesAmount() != null ? latestPerformance.getSalesAmount().doubleValue() : 0.0);
                performance.put("motTasksCompleted", latestPerformance.getMotTasksCompleted() != null ? latestPerformance.getMotTasksCompleted() : 0);
                performance.put("responseTime", latestPerformance.getResponseTime() != null ? latestPerformance.getResponseTime().doubleValue() : 0.0);
                performance.put("caiScore", latestPerformance.getCaiScore() != null ? latestPerformance.getCaiScore().doubleValue() : 0.0);
                performance.put("rmvScore", latestPerformance.getRmvScore() != null ? latestPerformance.getRmvScore().doubleValue() : 0.0);
                performance.put("matrixPosition", latestPerformance.getMatrixPosition());
                performance.put("matrixType", latestPerformance.getMatrixType());
            } else {
                // 如果没有绩效数据，返回默认值
                performance.put("newMembers", 0);
                performance.put("motCompletionRate", 0.0);
                performance.put("customerSatisfaction", 0.0);
                performance.put("sales", 0.0);
                performance.put("motTasksCompleted", 0);
                performance.put("responseTime", 0.0);
                performance.put("caiScore", 0.0);
                performance.put("rmvScore", 0.0);
                performance.put("matrixPosition", "");
                performance.put("matrixType", "");
            }
            guide.put("performance", performance);
            
            // 最近活动 - 这里可以根据实际需求查询MOT任务或其他活动记录
            // 暂时保留模拟数据，后续可以根据实际业务需求调整
            List<Map<String, Object>> recentActivities = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("date", LocalDate.now().minusDays(i).toString());
                activity.put("type", i % 2 == 0 ? "MOT任务" : "会员服务");
                activity.put("description", "完成会员服务任务");
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
            // 构建GuideInfo对象
            GuideInfo guideInfo = new GuideInfo();
            guideInfo.setGuideName((String) guideData.get("name"));
            guideInfo.setGuideCode((String) guideData.get("employeeId"));
            guideInfo.setPhone((String) guideData.get("phone"));
            guideInfo.setEmail((String) guideData.get("email"));
            guideInfo.setEmployeeId((String) guideData.get("employeeId"));
            guideInfo.setDepartment((String) guideData.get("department"));
            guideInfo.setLevel((String) guideData.get("level"));
            guideInfo.setStatus(1); // 1表示在职
            guideInfo.setHireDate(new Date()); // 设置入职日期为当前日期
            
            // 如果有区域信息
            if (guideData.get("region") != null) {
                guideInfo.setRegionName((String) guideData.get("region"));
            }
            
            // 如果有督导信息
            if (guideData.get("manager") != null) {
                guideInfo.setSupervisorName((String) guideData.get("manager"));
            }
            
            // 调用Service创建导购
            int result = guideInfoService.insertGuideInfo(guideInfo);
            
            if (result > 0) {
                return Result.success("创建导购成功", guideInfo);
            } else {
                return Result.error("创建导购失败");
            }
        } catch (Exception e) {
            log.error("创建导购失败: {}", e.getMessage(), e);
            return Result.error("创建导购失败: " + e.getMessage());
        }
    }

    /**
     * 更新导购信息
     */
    @PutMapping("/update/{guideId}")
    public Result updateGuide(@PathVariable String guideId, @RequestBody Map<String, Object> guideData) {
        try {
            Long id = Long.parseLong(guideId);
            
            // 先查询现有导购信息
            GuideInfo existingGuide = guideInfoService.selectGuideInfoByGuideId(id);
            if (existingGuide == null) {
                return Result.error("导购信息不存在");
            }
            
            // 更新字段
            if (guideData.get("name") != null) {
                existingGuide.setGuideName((String) guideData.get("name"));
            }
            if (guideData.get("employeeId") != null) {
                existingGuide.setGuideCode((String) guideData.get("employeeId"));
                existingGuide.setEmployeeId((String) guideData.get("employeeId"));
            }
            if (guideData.get("phone") != null) {
                existingGuide.setPhone((String) guideData.get("phone"));
            }
            if (guideData.get("email") != null) {
                existingGuide.setEmail((String) guideData.get("email"));
            }
            if (guideData.get("department") != null) {
                existingGuide.setDepartment((String) guideData.get("department"));
            }
            if (guideData.get("level") != null) {
                existingGuide.setLevel((String) guideData.get("level"));
            }
            if (guideData.get("region") != null) {
                existingGuide.setRegionName((String) guideData.get("region"));
            }
            if (guideData.get("manager") != null) {
                existingGuide.setSupervisorName((String) guideData.get("manager"));
            }
            if (guideData.get("status") != null) {
                String status = (String) guideData.get("status");
                existingGuide.setStatus("在职".equals(status) ? 1 : 2);
            }
            
            // 调用Service更新导购
            int result = guideInfoService.updateGuideInfo(existingGuide);
            
            if (result > 0) {
                // 返回更新后的导购信息
                Map<String, Object> guide = new HashMap<>();
                guide.put("id", existingGuide.getGuideId());
                guide.put("name", existingGuide.getGuideName());
                guide.put("employeeId", existingGuide.getGuideCode());
                guide.put("region", existingGuide.getRegionName());
                guide.put("status", existingGuide.getStatus() == 1 ? "在职" : "离职");
                guide.put("phone", existingGuide.getPhone());
                guide.put("email", existingGuide.getEmail());
                guide.put("level", existingGuide.getLevel());
                guide.put("department", existingGuide.getDepartment());
                guide.put("manager", existingGuide.getSupervisorName());
                guide.put("updateDate", LocalDate.now().toString());
                
                log.info("更新导购信息成功，导购ID: {}", guideId);
                return Result.success("更新导购信息成功", guide);
            } else {
                return Result.error("更新导购信息失败");
            }
        } catch (Exception e) {
            log.error("更新导购信息失败: {}", e.getMessage(), e);
            return Result.error("更新导购信息失败: " + e.getMessage());
        }
    }

    /**
     * 删除导购
     */
    @DeleteMapping("/delete/{guideId}")
    public Result deleteGuide(@PathVariable String guideId) {
        try {
            Long id = Long.parseLong(guideId);
            
            // 调用Service删除导购
            int result = guideInfoService.deleteGuideInfoByGuideId(id);
            
            if (result > 0) {
                log.info("删除导购成功，导购ID: {}", guideId);
                return Result.success("删除导购成功");
            } else {
                return Result.error("删除导购失败，导购不存在");
            }
        } catch (Exception e) {
            log.error("删除导购失败: {}", e.getMessage(), e);
            return Result.error("删除导购失败: " + e.getMessage());
        }
    }

    /**
     * 获取导购绩效概览
     */
    @PostMapping("/performance/overview")
    public Result getPerformanceOverview(@RequestBody(required = false) Map<String, Object> params) {
        String region = params != null ? (String) params.get("region") : null;
        String period = params != null ? (String) params.get("period") : null;
        String dataMonth = params != null ? (String) params.get("dataMonth") : null;
        
        // 优先使用dataMonth参数，然后是period参数
        if (dataMonth != null && !dataMonth.isEmpty()) {
            period = dataMonth;
        } else if (period == null || period.isEmpty()) {
            period = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }
        
        try {
            Map<String, Object> overview = new HashMap<>();
             
            // 从数据库查询真实数据
            Map<String, Object> statistics = guidePerformanceService.selectPerformanceStatistics(period);
            
            if (statistics != null && !statistics.isEmpty()) {
                // 使用数据库查询结果
                overview.put("totalGuides", statistics.get("total_guides") != null ? statistics.get("total_guides") : 0);
                overview.put("activeGuides", guidePerformanceService.countActiveGuides(period));
                overview.put("avgPerformanceScore", statistics.get("avg_performance_score") != null ? 
                    Math.round(((Number) statistics.get("avg_performance_score")).doubleValue() * 10.0) / 10.0 : 0.0);
                overview.put("topPerformerCount", statistics.get("high_performance_guides") != null ? statistics.get("high_performance_guides") : 0);
                overview.put("avgCAI", statistics.get("avg_cai_score") != null ? 
                    Math.round(((Number) statistics.get("avg_cai_score")).doubleValue() * 100.0) / 100.0 : 0.0);
                overview.put("avgRMV", statistics.get("avg_rmv_score") != null ? 
                    Math.round(((Number) statistics.get("avg_rmv_score")).doubleValue() * 100.0) / 100.0 : 0.0);
            } else {
                // 如果没有数据，返回默认值
                overview.put("totalGuides", 0);
                overview.put("activeGuides", 0);
                overview.put("avgPerformanceScore", 0.0);
                overview.put("topPerformerCount", 0);
                overview.put("avgCAI", 0.0);
                overview.put("avgRMV", 0.0);
            }
            
            log.info("获取导购绩效概览成功，期间: {}", period);
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
        log.info("接收到的参数: {}", params);
        String region = params != null ? (String) params.get("region") : null;
        String period = params != null ? (String) params.get("period") : null;
        String dataMonth = params != null ? (String) params.get("dataMonth") : null;
        
        log.info("解析参数 - region: {}, period: {}, dataMonth: {}", region, period, dataMonth);
        
        // 优先使用dataMonth，如果没有则使用period，最后使用默认值
        String queryMonth = dataMonth != null ? dataMonth : (period != null ? period : "2025-07");
        log.info("最终查询月份: {}", queryMonth);
        
        try {
            log.info("开始查询九宫格绩效分布数据，查询月份: {}", queryMonth);
            // 使用Service层查询数据库中的真实数据
            List<Map<String, Object>> matrixData = guidePerformanceService.selectMatrixDistribution(queryMonth);
            log.info("数据库查询结果: {}", matrixData);
            
            // 如果数据库中没有数据，返回默认的九宫格结构
            if (matrixData == null || matrixData.isEmpty()) {
                matrixData = new ArrayList<>();
                String[] positions = {"1-1", "1-2", "1-3", "2-1", "2-2", "2-3", "3-1", "3-2", "3-3"};
                String[] types = {"培训生", "服务达人", "忠诚专家", "基础型", "骨干力量", "关系专家", "获客能手", "成长之星", "超级明星"};
                String[] xLabels = {"低", "低", "低", "中", "中", "中", "高", "高", "高"};
                String[] yLabels = {"低", "中", "高", "低", "中", "高", "低", "中", "高"};
                
                for (int i = 0; i < positions.length; i++) {
                    Map<String, Object> point = new HashMap<>();
                    point.put("x", xLabels[i]);
                    point.put("y", yLabels[i]);
                    point.put("z", 0); // 没有数据时显示0
                    point.put("position", positions[i]);
                    point.put("type", types[i]);
                    matrixData.add(point);
                }
                log.info("数据库中没有找到绩效分布数据，返回默认九宫格结构，查询月份: {}", queryMonth);
            } else {
                log.info("数据库返回了{}条数据，开始转换格式", matrixData.size());
                // 转换数据库查询结果为前端需要的格式
                List<Map<String, Object>> formattedData = new ArrayList<>();
                String[] positions = {"1-1", "1-2", "1-3", "2-1", "2-2", "2-3", "3-1", "3-2", "3-3"};
                String[] types = {"培训生", "服务达人", "忠诚专家", "基础型", "骨干力量", "关系专家", "获客能手", "成长之星", "超级明星"};
                String[] xLabels = {"低", "低", "低", "中", "中", "中", "高", "高", "高"};
                String[] yLabels = {"低", "中", "高", "低", "中", "高", "低", "中", "高"};
                
                // 创建位置到数据的映射
                Map<String, Map<String, Object>> positionMap = new HashMap<>();
                for (Map<String, Object> item : matrixData) {
                    log.info("处理数据库记录: {}", item);
                    String position = (String) item.get("matrix_position"); // 使用下划线格式的字段名
                    if (position != null) {
                        positionMap.put(position, item);
                        log.info("添加位置映射: {} -> {}", position, item.get("guide_count"));
                    }
                }
                
                log.info("位置映射完成，共{}个位置", positionMap.size());
                
                // 确保所有九宫格位置都有数据
                for (int i = 0; i < positions.length; i++) {
                    Map<String, Object> point = new HashMap<>();
                    point.put("x", xLabels[i]);
                    point.put("y", yLabels[i]);
                    point.put("position", positions[i]);
                    point.put("type", types[i]);
                    
                    Map<String, Object> dbData = positionMap.get(positions[i]);
                    if (dbData != null) {
                        Object guideCount = dbData.get("guide_count");
                        point.put("z", guideCount != null ? guideCount : 0); // 使用下划线格式的字段名
                        log.info("位置{}找到数据，人数: {}", positions[i], guideCount);
                    } else {
                        point.put("z", 0);
                        log.info("位置{}没有数据，设置为0", positions[i]);
                    }
                    formattedData.add(point);
                }
                matrixData = formattedData;
                log.info("获取九宫格绩效分布数据成功，查询月份: {}，数据条数: {}", queryMonth, matrixData.size());
            }
            
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
            
            // 计算查询的时间范围（最近6个月）
            LocalDate currentDate = LocalDate.now();
            String endMonth = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            String startMonth = currentDate.minusMonths(5).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            // 从数据库查询趋势数据
            List<Map<String, Object>> trendResults = guidePerformanceService.selectPerformanceTrend(startMonth, endMonth);
            
            if (trendResults != null && !trendResults.isEmpty()) {
                // 构建时间轴
                List<String> categories = new ArrayList<>();
                Map<String, Map<String, Object>> monthlyData = new HashMap<>();
                
                for (Map<String, Object> result : trendResults) {
                    String month = (String) result.get("month");
                    if (month != null) {
                        String monthLabel = month.substring(5) + "月"; // 转换为"MM月"格式
                        categories.add(monthLabel);
                        monthlyData.put(monthLabel, result);
                    }
                }
                
                trendData.put("categories", categories);
                
                // 构建各等级趋势数据
                List<Map<String, Object>> series = new ArrayList<>();
                
                // 优秀等级 (>=90分)
                Map<String, Object> excellent = new HashMap<>();
                excellent.put("name", "优秀 (S级)");
                List<Integer> excellentData = new ArrayList<>();
                for (String category : categories) {
                    Map<String, Object> data = monthlyData.get(category);
                    excellentData.add(data != null && data.get("excellent_count") != null ? 
                        ((Number) data.get("excellent_count")).intValue() : 0);
                }
                excellent.put("data", excellentData);
                series.add(excellent);
                
                // 良好等级 (80-89分)
                Map<String, Object> good = new HashMap<>();
                good.put("name", "良好 (A级)");
                List<Integer> goodData = new ArrayList<>();
                for (String category : categories) {
                    Map<String, Object> data = monthlyData.get(category);
                    goodData.add(data != null && data.get("good_count") != null ? 
                        ((Number) data.get("good_count")).intValue() : 0);
                }
                good.put("data", goodData);
                series.add(good);
                
                // 一般等级 (70-79分)
                Map<String, Object> average = new HashMap<>();
                average.put("name", "一般 (B级)");
                List<Integer> averageData = new ArrayList<>();
                for (String category : categories) {
                    Map<String, Object> data = monthlyData.get(category);
                    averageData.add(data != null && data.get("average_count") != null ? 
                        ((Number) data.get("average_count")).intValue() : 0);
                }
                average.put("data", averageData);
                series.add(average);
                
                // 待改进等级 (<70分)
                Map<String, Object> poor = new HashMap<>();
                poor.put("name", "待改进 (C/D级)");
                List<Integer> poorData = new ArrayList<>();
                for (String category : categories) {
                    Map<String, Object> data = monthlyData.get(category);
                    poorData.add(data != null && data.get("poor_count") != null ? 
                        ((Number) data.get("poor_count")).intValue() : 0);
                }
                poor.put("data", poorData);
                series.add(poor);
                
                trendData.put("series", series);
            } else {
                // 如果没有数据，返回默认的空趋势
                trendData.put("categories", Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月"));
                List<Map<String, Object>> series = new ArrayList<>();
                
                String[] gradeNames = {"优秀 (S级)", "良好 (A级)", "一般 (B级)", "待改进 (C/D级)"};
                for (String gradeName : gradeNames) {
                    Map<String, Object> gradeData = new HashMap<>();
                    gradeData.put("name", gradeName);
                    gradeData.put("data", Arrays.asList(0, 0, 0, 0, 0, 0));
                    series.add(gradeData);
                }
                
                trendData.put("series", series);
            }
            
            log.info("获取导购绩效趋势数据成功，查询范围: {} 到 {}", startMonth, endMonth);
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
        String period = params != null ? (String) params.get("period") : null;
        String dataMonth = params != null ? (String) params.get("dataMonth") : null;
        
        try {
            // 构建查询条件
            GuidePerformance queryParam = new GuidePerformance();
            
            // 设置查询月份，优先使用dataMonth参数，其次是period参数，最后默认为当前月份
            if (dataMonth != null && !dataMonth.isEmpty()) {
                queryParam.setDataMonth(dataMonth);
            } else if (period != null && !period.isEmpty()) {
                queryParam.setDataMonth(period);
            } else {
                String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                queryParam.setDataMonth(currentMonth);
            }
            
            // 设置区域查询条件
            if (region != null && !region.isEmpty() && !"all".equals(region)) {
                queryParam.setRegionName(region);
            }
            
            // 设置搜索条件（导购姓名或编码）
            if (search != null && !search.isEmpty()) {
                queryParam.setGuideName(search);
                // 如果搜索内容像编码格式，也按编码搜索
                if (search.matches("^[A-Z]\\d+$")) {
                    queryParam.setGuideCode(search);
                }
            }
            
            // 查询数据库
            log.info("查询导购绩效详情，参数: dataMonth={}, region={}, search={}", queryParam.getDataMonth(), region, search);
            List<GuidePerformance> performanceList = guidePerformanceService.selectGuidePerformanceList(queryParam);
            log.info("查询到{}条导购绩效记录", performanceList.size());
            
            // 转换为前端需要的格式
            List<Map<String, Object>> details = new ArrayList<>();
            for (GuidePerformance performance : performanceList) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("guideName", performance.getGuideName());
                detail.put("guideCode", performance.getGuideCode());
                detail.put("storeName", performance.getStoreName());
                detail.put("region", performance.getRegionName());
                
                // CAI和RMV分数
                Double caiScore = performance.getCaiScore() != null ? performance.getCaiScore().doubleValue() : null;
                Double rmvScore = performance.getRmvScore() != null ? performance.getRmvScore().doubleValue() : null;
                detail.put("cai", caiScore != null ? caiScore : 0.0);
                detail.put("rmv", rmvScore != null ? rmvScore : 0.0);
                
                // 九宫格位置和类型
                detail.put("position", performance.getMatrixPosition());
                detail.put("type", performance.getMatrixType());
                detail.put("trend", performance.getTrend());
                
                // CAI等级
                String caiLevel = "低";
                if (caiScore != null) {
                    if (caiScore >= 0.8) {
                        caiLevel = "高";
                    } else if (caiScore >= 0.6) {
                        caiLevel = "中";
                    }
                }
                detail.put("caiLevel", caiLevel);
                
                // RMV等级
                String rmvLevel = "低";
                if (rmvScore != null) {
                    if (rmvScore >= 0.8) {
                        rmvLevel = "高";
                    } else if (rmvScore >= 0.6) {
                        rmvLevel = "中";
                    }
                }
                detail.put("rmvLevel", rmvLevel);
                
                // 绩效分数
                detail.put("performanceScore", performance.getPerformanceScore());
                detail.put("rankInRegion", performance.getRankInRegion());
                detail.put("rankOverall", performance.getRankOverall());
                
                details.add(detail);
            }
            
            // 分页处理
            int total = details.size();
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, total);
            
            List<Map<String, Object>> pagedDetails = new ArrayList<>();
            if (startIndex < total) {
                pagedDetails = details.subList(startIndex, endIndex);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("list", pagedDetails);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            
            log.info("获取导购绩效详情列表成功，查询条件: 区域={}, 搜索={}, 期间={}, 总数={}", region, search, period, total);
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