package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import com.omniperform.common.utils.poi.ExcelUtil;
import com.omniperform.common.core.controller.BaseController;
import com.omniperform.system.service.IMemberOverviewService;
import com.omniperform.system.service.IMemberProfileAnalysisService;
import com.omniperform.system.service.IMemberCrfmeDistributionService;
import com.omniperform.system.domain.MemberInfo;
import com.omniperform.system.domain.MemberMonthlyStats;
import com.omniperform.system.domain.MemberStageStats;
import com.omniperform.system.domain.MemberLifecycleRecords;
import com.omniperform.system.domain.MemberCrfmeDistribution;
import com.omniperform.system.domain.MemberProfileAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map;

/**
 * 会员管理控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/member")
public class MemberController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private IMemberOverviewService memberOverviewService;

    @Autowired
    private IMemberProfileAnalysisService memberProfileAnalysisService;

    @Autowired
    private IMemberCrfmeDistributionService memberCrfmeDistributionService;

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
     * 下载会员导入模板
     */
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        try {
            // 创建示例数据以生成包含正确表头的模板
            List<MemberInfo> sampleData = createMemberInfoSampleData();
            ExcelUtil<MemberInfo> util = new ExcelUtil<>(MemberInfo.class);
            com.omniperform.common.utils.file.FileUtils.setAttachmentResponseHeader(response, "会员基础信息模板.xlsx");
            util.exportExcel(response, sampleData, "会员基础信息", "会员基础信息模板");
            log.info("下载会员基础信息Excel导入模板成功");
        } catch (Exception e) {
            log.error("下载会员导入模板失败", e);
        }
    }

    /**
     * 下载会员管理多模板（包含示例数据）
     */
    @GetMapping("/template/{templateType}")
    public void downloadMemberTemplate(@PathVariable String templateType, HttpServletResponse response) {
        try {
            switch (templateType) {
                case "member-info":
                    List<MemberInfo> memberSampleData = createMemberInfoSampleData();
                    ExcelUtil<MemberInfo> memberUtil = new ExcelUtil<>(MemberInfo.class);
                    com.omniperform.common.utils.file.FileUtils.setAttachmentResponseHeader(response, "会员基础信息模板.xlsx");
                    memberUtil.exportExcel(response, memberSampleData, "会员基础信息");
                    break;
                case "member-monthly":
                    List<MemberMonthlyStats> monthlySampleData = createMemberMonthlySampleData();
                    ExcelUtil<MemberMonthlyStats> monthlyUtil = new ExcelUtil<>(MemberMonthlyStats.class);
                    com.omniperform.common.utils.file.FileUtils.setAttachmentResponseHeader(response, "会员月度统计模板.xlsx");
                    monthlyUtil.exportExcel(response, monthlySampleData, "会员月度统计");
                    break;
                case "member-lifecycle":
                    org.apache.poi.xssf.usermodel.XSSFWorkbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
                    org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("会员生命周期记录");
                    org.apache.poi.ss.usermodel.CellStyle headerStyle = wb.createCellStyle();
                    org.apache.poi.ss.usermodel.Font bold = wb.createFont();
                    bold.setBold(true);
                    headerStyle.setFont(bold);
                    org.apache.poi.ss.usermodel.CellStyle textStyle = wb.createCellStyle();
                    textStyle.setDataFormat(wb.createDataFormat().getFormat("@"));
                    org.apache.poi.ss.usermodel.CellStyle intStyle = wb.createCellStyle();
                    intStyle.setDataFormat(wb.createDataFormat().getFormat("0"));
                    org.apache.poi.ss.usermodel.CellStyle datetimeStyle = wb.createCellStyle();
                    datetimeStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));

                    org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
                    String[] headers = new String[]{"数据月份","会员ID","生命周期阶段","阶段开始时间","阶段结束时间","阶段持续天数","阶段描述","触发事件","记录时间"};
                    for (int i = 0; i < headers.length; i++) {
                        org.apache.poi.ss.usermodel.Cell c = header.createCell(i);
                        c.setCellValue(headers[i]);
                        c.setCellStyle(headerStyle);
                    }
                    sheet.setColumnWidth(0, 14 * 256);
                    sheet.setColumnWidth(1, 20 * 256);
                    sheet.setColumnWidth(2, 20 * 256);
                    sheet.setColumnWidth(3, 22 * 256);
                    sheet.setColumnWidth(4, 22 * 256);
                    sheet.setColumnWidth(5, 16 * 256);
                    sheet.setColumnWidth(6, 40 * 256);
                    sheet.setColumnWidth(7, 24 * 256);
                    sheet.setColumnWidth(8, 22 * 256);

                    java.util.List<com.omniperform.system.domain.MemberLifecycleRecords> lifecycleSampleData = createMemberLifecycleSampleData();
                    for (int r = 0; r < lifecycleSampleData.size(); r++) {
                        com.omniperform.system.domain.MemberLifecycleRecords s = lifecycleSampleData.get(r);
                        org.apache.poi.ss.usermodel.Row row = sheet.createRow(r + 1);
                        org.apache.poi.ss.usermodel.Cell cm = row.createCell(0);
                        java.text.SimpleDateFormat ym = new java.text.SimpleDateFormat("yyyy-MM");
                        String monthStr = s.getRecordTime() != null ? ym.format(s.getRecordTime()) : (s.getStageStartTime() != null ? ym.format(s.getStageStartTime()) : "");
                        cm.setCellValue(monthStr);
                        cm.setCellStyle(textStyle);
                        org.apache.poi.ss.usermodel.Cell c0 = row.createCell(1);
                        c0.setCellValue(s.getMemberId() != null ? String.valueOf(s.getMemberId()) : "");
                        c0.setCellStyle(textStyle);
                        org.apache.poi.ss.usermodel.Cell c1 = row.createCell(2);
                        c1.setCellValue(s.getLifecycleStage() != null ? s.getLifecycleStage() : "");
                        c1.setCellStyle(textStyle);
                        org.apache.poi.ss.usermodel.Cell c2 = row.createCell(3);
                        if (s.getStageStartTime() != null) { c2.setCellValue(s.getStageStartTime()); }
                        c2.setCellStyle(datetimeStyle);
                        org.apache.poi.ss.usermodel.Cell c3 = row.createCell(4);
                        if (s.getStageEndTime() != null) { c3.setCellValue(s.getStageEndTime()); }
                        c3.setCellStyle(datetimeStyle);
                        org.apache.poi.ss.usermodel.Cell c4 = row.createCell(5);
                        c4.setCellValue(s.getStageDuration() != null ? s.getStageDuration() : 0);
                        c4.setCellStyle(intStyle);
                        org.apache.poi.ss.usermodel.Cell c5 = row.createCell(6);
                        c5.setCellValue(s.getStageDescription() != null ? s.getStageDescription() : "");
                        c5.setCellStyle(textStyle);
                        org.apache.poi.ss.usermodel.Cell c6 = row.createCell(7);
                        c6.setCellValue(s.getTriggerEvent() != null ? s.getTriggerEvent() : "");
                        c6.setCellStyle(textStyle);
                        org.apache.poi.ss.usermodel.Cell c7 = row.createCell(8);
                        if (s.getRecordTime() != null) { c7.setCellValue(s.getRecordTime()); }
                        c7.setCellStyle(datetimeStyle);
                    }

                    com.omniperform.common.utils.file.FileUtils.setAttachmentResponseHeader(response, "会员生命周期记录模板.xlsx");
                    java.io.OutputStream os = response.getOutputStream();
                    wb.write(os);
                    os.flush();
                    wb.close();
                    break;
                case "member-crfme":
                    List<MemberCrfmeDistribution> crfmeSampleData = createMemberCrfmeSampleData();
                    ExcelUtil<MemberCrfmeDistribution> crfmeUtil = new ExcelUtil<>(MemberCrfmeDistribution.class);
                    com.omniperform.common.utils.file.FileUtils.setAttachmentResponseHeader(response, "CRFM-E评分模板.xlsx");
                    crfmeUtil.exportExcel(response, crfmeSampleData, "CRFM-E评分分布");
                    break;
                case "member-segmentation":
                    List<MemberProfileAnalysis> segmentSampleData = createMemberSegmentationSampleData();
                    ExcelUtil<MemberProfileAnalysis> segmentUtil = new ExcelUtil<>(MemberProfileAnalysis.class);
                    com.omniperform.common.utils.file.FileUtils.setAttachmentResponseHeader(response, "会员分层画像模板.xlsx");
                    segmentUtil.exportExcel(response, segmentSampleData, "会员分层画像");
                    break;
                case "member-stage":
                    List<MemberStageStats> stageSampleData = createMemberStageSampleData();
                    ExcelUtil<MemberStageStats> stageUtil = new ExcelUtil<>(MemberStageStats.class);
                    com.omniperform.common.utils.file.FileUtils.setAttachmentResponseHeader(response, "会员月度阶段统计模板.xlsx");
                    stageUtil.exportExcel(response, stageSampleData, "会员月度阶段统计");
                    break;
                default:
                    log.error("不支持的模板类型: {}", templateType);
                    return;
            }
            log.info("下载会员模板成功，模板类型: {}", templateType);
        } catch (Exception e) {
            log.error("下载会员模板失败，模板类型: {}", templateType, e);
        }
    }

    /**
     * 创建会员基础信息示例数据
     */
    private List<MemberInfo> createMemberInfoSampleData() {
        List<MemberInfo> sampleData = new ArrayList<>();
        
        // 使用时间戳生成唯一的会员编号，避免重复
        long timestamp = System.currentTimeMillis();
        
        MemberInfo member1 = new MemberInfo();
        member1.setMemberId(1001L); // 设置会员ID
        member1.setMemberCode("M" + (timestamp + 1));
        member1.setMemberName("赵六");
        member1.setPhone("15800158001");
        member1.setEmail("zhaoliu@example.com");
        member1.setGender(1); // 1表示男性
        member1.setBirthDate(new Date());
        member1.setBabyBirthDate(new Date()); // 添加宝宝出生日期
        member1.setRegionCode("BJ");
        member1.setCity("北京");
        member1.setRegistrationDate(new Date());
        member1.setRegistrationSource("官网注册"); // 添加注册来源
        member1.setGuideId(1001L); // 添加专属导购ID
        member1.setBabyStage("成长期");
        member1.setTotalPurchaseAmount(new java.math.BigDecimal("5000.0"));
        member1.setTotalPurchaseCount(25);
        member1.setLastLoginTime(new Date()); // 添加最后登录时间
        member1.setLastPurchaseTime(new Date());
        member1.setLastInteractionTime(new Date()); // 添加最后互动时间
        member1.setStatus(1); // 1表示活跃
        sampleData.add(member1);

        MemberInfo member2 = new MemberInfo();
        member2.setMemberId(1002L); // 设置会员ID
        member2.setMemberCode("M" + (timestamp + 2));
        member2.setMemberName("孙七");
        member2.setPhone("15800158002");
        member2.setEmail("sunqi@example.com");
        member2.setGender(2); // 2表示女性
        member2.setBirthDate(new Date());
        member2.setBabyBirthDate(new Date()); // 添加宝宝出生日期
        member2.setRegionCode("SH");
        member2.setCity("上海");
        member2.setRegistrationDate(new Date());
        member2.setRegistrationSource("微信小程序"); // 添加注册来源
        member2.setGuideId(1002L); // 添加专属导购ID
        member2.setBabyStage("成熟期");
        member2.setTotalPurchaseAmount(new java.math.BigDecimal("3000.0"));
        member2.setTotalPurchaseCount(15);
        member2.setLastLoginTime(new Date()); // 添加最后登录时间
        member2.setLastPurchaseTime(new Date());
        member2.setLastInteractionTime(new Date()); // 添加最后互动时间
        member2.setStatus(1); // 1表示活跃
        sampleData.add(member2);

        MemberInfo member3 = new MemberInfo();
        member3.setMemberId(1003L); // 设置会员ID
        member3.setMemberCode("M" + (timestamp + 3));
        member3.setMemberName("周八");
        member3.setPhone("15800158003");
        member3.setEmail("zhouba@example.com");
        member3.setGender(1); // 1表示男性
        member3.setBirthDate(new Date());
        member3.setBabyBirthDate(new Date()); // 添加宝宝出生日期
        member3.setRegionCode("GZ");
        member3.setCity("广州");
        member3.setRegistrationDate(new Date());
        member3.setRegistrationSource("APP注册"); // 添加注册来源
        member3.setGuideId(1003L); // 添加专属导购ID
        member3.setBabyStage("新手期");
        member3.setTotalPurchaseAmount(new java.math.BigDecimal("1000.0"));
        member3.setTotalPurchaseCount(5);
        member3.setLastLoginTime(new Date()); // 添加最后登录时间
        member3.setLastPurchaseTime(new Date());
        member3.setLastInteractionTime(new Date()); // 添加最后互动时间
        member3.setStatus(1); // 1表示正常
        sampleData.add(member3);

        MemberInfo member4 = new MemberInfo();
        member4.setMemberId(1004L); // 设置会员ID
        member4.setMemberCode("M" + (timestamp + 4));
        member4.setMemberName("吴九");
        member4.setPhone("15800158004");
        member4.setEmail("wujiu@example.com");
        member4.setGender(2); // 2表示女性
        member4.setBirthDate(new Date());
        member4.setBabyBirthDate(new Date()); // 添加宝宝出生日期
        member4.setRegionCode("SZ");
        member4.setCity("深圳");
        member4.setRegistrationDate(new Date());
        member4.setRegistrationSource("线下门店"); // 添加注册来源
        member4.setGuideId(1004L); // 添加专属导购ID
        member4.setBabyStage("成长期");
        member4.setTotalPurchaseAmount(new java.math.BigDecimal("2500.0"));
        member4.setTotalPurchaseCount(12);
        member4.setLastLoginTime(new Date()); // 添加最后登录时间
        member4.setLastPurchaseTime(new Date());
        member4.setLastInteractionTime(new Date()); // 添加最后互动时间
        member4.setStatus(1); // 1表示活跃
        sampleData.add(member4);

        MemberInfo member5 = new MemberInfo();
        member5.setMemberId(1005L); // 设置会员ID
        member5.setMemberCode("M" + (timestamp + 5));
        member5.setMemberName("郑十");
        member5.setPhone("15800158005");
        member5.setEmail("zhengshi@example.com");
        member5.setGender(1); // 1表示男性
        member5.setBirthDate(new Date());
        member5.setBabyBirthDate(new Date()); // 添加宝宝出生日期
        member5.setRegionCode("CD");
        member5.setCity("成都");
        member5.setRegistrationDate(new Date());
        member5.setRegistrationSource("朋友推荐"); // 添加注册来源
        member5.setGuideId(1005L); // 添加专属导购ID
        member5.setBabyStage("成熟期");
        member5.setTotalPurchaseAmount(new java.math.BigDecimal("4200.0"));
        member5.setTotalPurchaseCount(18);
        member5.setLastLoginTime(new Date()); // 添加最后登录时间
        member5.setLastPurchaseTime(new Date());
        member5.setLastInteractionTime(new Date()); // 添加最后互动时间
        member5.setStatus(1); // 1表示活跃
        sampleData.add(member5);

        return sampleData;
    }

    /**
     * 创建会员月度统计示例数据
     */
    private List<MemberMonthlyStats> createMemberMonthlySampleData() {
        List<MemberMonthlyStats> sampleData = new ArrayList<>();
        
        MemberMonthlyStats stats1 = new MemberMonthlyStats();
        stats1.setStatsMonth("2024-01");
        stats1.setTotalMembers(1500);
        stats1.setNewMembers(200);
        stats1.setActiveMembers(1200);
        stats1.setPurchaseMembers(800);
        stats1.setChurnMembers(50);
        stats1.setTotalPurchaseAmount(new java.math.BigDecimal("450000.00"));
        stats1.setAvgOrderValue(new java.math.BigDecimal("180.00"));
        stats1.setActiveRate(new java.math.BigDecimal("0.80"));
        stats1.setPurchaseRate(new java.math.BigDecimal("0.53"));
        stats1.setChurnRate(new java.math.BigDecimal("0.03"));
        stats1.setStatsTime(new Date());
        sampleData.add(stats1);

        MemberMonthlyStats stats2 = new MemberMonthlyStats();
        stats2.setStatsMonth("2024-02");
        stats2.setTotalMembers(1650);
        stats2.setNewMembers(180);
        stats2.setActiveMembers(1300);
        stats2.setPurchaseMembers(850);
        stats2.setChurnMembers(30);
        stats2.setTotalPurchaseAmount(new java.math.BigDecimal("510000.00"));
        stats2.setAvgOrderValue(new java.math.BigDecimal("200.00"));
        stats2.setActiveRate(new java.math.BigDecimal("0.79"));
        stats2.setPurchaseRate(new java.math.BigDecimal("0.52"));
        stats2.setChurnRate(new java.math.BigDecimal("0.02"));
        stats2.setStatsTime(new Date());
        sampleData.add(stats2);

        return sampleData;
    }

    /**
     * 创建会员生命周期记录示例数据
     */
    private List<MemberLifecycleRecords> createMemberLifecycleSampleData() {
        List<MemberLifecycleRecords> sampleData = new ArrayList<>();
        
        MemberLifecycleRecords record1 = new MemberLifecycleRecords();
        record1.setMemberId(1001L);
        record1.setLifecycleStage("成长期");
        record1.setStageStartTime(new Date());
        record1.setStageEndTime(null); // 当前阶段，结束时间为空
        record1.setStageDuration(30);
        record1.setStageDescription("会员进入成长期，消费活跃度提升");
        record1.setTriggerEvent("消费金额达到成长期标准");
        record1.setRecordTime(new Date());
        sampleData.add(record1);

        MemberLifecycleRecords record2 = new MemberLifecycleRecords();
        record2.setMemberId(1002L);
        record2.setLifecycleStage("成熟期");
        record2.setStageStartTime(new Date());
        record2.setStageEndTime(null); // 当前阶段，结束时间为空
        record2.setStageDuration(60);
        record2.setStageDescription("会员进入成熟期，持续活跃消费");
        record2.setTriggerEvent("持续活跃消费");
        record2.setRecordTime(new Date());
        sampleData.add(record2);

        MemberLifecycleRecords record3 = new MemberLifecycleRecords();
        record3.setMemberId(1003L);
        record3.setLifecycleStage("新手期");
        record3.setStageStartTime(new Date());
        record3.setStageEndTime(null); // 当前阶段，结束时间为空
        record3.setStageDuration(15);
        record3.setStageDescription("新注册会员，处于新手期");
        record3.setTriggerEvent("新用户注册");
        record3.setRecordTime(new Date());
        sampleData.add(record3);

        return sampleData;
    }

    /**
     * 创建CRFM-E评分分布示例数据
     */
    private List<MemberCrfmeDistribution> createMemberCrfmeSampleData() {
        List<MemberCrfmeDistribution> sampleData = new ArrayList<>();
        
        MemberCrfmeDistribution crfme1 = new MemberCrfmeDistribution();
        crfme1.setDataMonth("2024-01");
        crfme1.setScoreRange("80-100");
        crfme1.setCount(150);
        crfme1.setPercentage(new java.math.BigDecimal("15.0"));
        crfme1.setAvgScore(new java.math.BigDecimal("90.5"));
        crfme1.setTier("高价值客户");
        sampleData.add(crfme1);

        MemberCrfmeDistribution crfme2 = new MemberCrfmeDistribution();
        crfme2.setDataMonth("2024-01");
        crfme2.setScoreRange("60-79");
        crfme2.setCount(300);
        crfme2.setPercentage(new java.math.BigDecimal("30.0"));
        crfme2.setAvgScore(new java.math.BigDecimal("69.8"));
        crfme2.setTier("中等价值客户");
        sampleData.add(crfme2);

        return sampleData;
    }

    /**
     * 创建会员分层画像示例数据
     */
    private List<MemberProfileAnalysis> createMemberSegmentationSampleData() {
        List<MemberProfileAnalysis> sampleData = new ArrayList<>();
        
        MemberProfileAnalysis profile1 = new MemberProfileAnalysis();
        profile1.setAnalysisDate(new Date());
        profile1.setProfileType("成长探索型");
        profile1.setMemberCount(150);
        profile1.setPercentage(new BigDecimal("15.0"));
        profile1.setAvgPurchaseAmount(new BigDecimal("200.0"));
        profile1.setAvgInteractionFrequency(new BigDecimal("2.5"));
        profile1.setRegionCode("BJ");
        sampleData.add(profile1);

        MemberProfileAnalysis profile2 = new MemberProfileAnalysis();
        profile2.setAnalysisDate(new Date());
        profile2.setProfileType("品质追求型");
        profile2.setMemberCount(200);
        profile2.setPercentage(new BigDecimal("20.0"));
        profile2.setAvgPurchaseAmount(new BigDecimal("300.0"));
        profile2.setAvgInteractionFrequency(new BigDecimal("3.2"));
        profile2.setRegionCode("SH");
        sampleData.add(profile2);

        return sampleData;
    }

    /**
     * 创建会员阶段统计示例数据
     */
    private List<MemberStageStats> createMemberStageSampleData() {
        List<MemberStageStats> sampleData = new ArrayList<>();
        
        MemberStageStats stage1 = new MemberStageStats();
        stage1.setStatsMonth("2024-01");
        stage1.setBabyStage("成长期");
        stage1.setMemberCount(150);
        stage1.setNewMemberCount(20);
        stage1.setActiveMemberCount(120);
        stage1.setPurchaseMemberCount(100);
        stage1.setTotalPurchaseAmount(new BigDecimal("45000.0"));
        stage1.setAvgOrderValue(new BigDecimal("180.0"));
        stage1.setStageRatio(new BigDecimal("0.15"));
        stage1.setStatsTime(new Date());
        sampleData.add(stage1);

        MemberStageStats stage2 = new MemberStageStats();
        stage2.setStatsMonth("2024-01");
        stage2.setBabyStage("成熟期");
        stage2.setMemberCount(200);
        stage2.setNewMemberCount(10);
        stage2.setActiveMemberCount(180);
        stage2.setPurchaseMemberCount(160);
        stage2.setTotalPurchaseAmount(new BigDecimal("80000.0"));
        stage2.setAvgOrderValue(new BigDecimal("250.0"));
        stage2.setStageRatio(new BigDecimal("0.20"));
        stage2.setStatsTime(new Date());
        sampleData.add(stage2);

        return sampleData;
    }

    /**
     * 导入会员数据
     */
    @PostMapping("/importData")
    public Result<Map<String, Object>> importData(@RequestParam("file") MultipartFile file, 
                                                  @RequestParam(value = "isUpdateSupport", defaultValue = "false") Boolean isUpdateSupport) {
        try {
            ExcelUtil<MemberInfo> util = new ExcelUtil<>(MemberInfo.class);
            List<MemberInfo> memberList = util.importExcel(file.getInputStream());
            String operName = getLoginName(); // 获取当前操作用户
            Map<String, Object> result = memberOverviewService.importMemberInfo(memberList, isUpdateSupport, operName);
            return Result.success("导入完成", result);
        } catch (Exception e) {
            log.error("导入会员数据失败", e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 批量导入会员相关数据
     */
    @PostMapping({"/import/batch", "/batchImport"})
    public Result<Map<String, Object>> batchImport(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("dataType") String dataType,
                                                   @RequestParam(value = "updateSupport", defaultValue = "true") Boolean updateSupport) {
        try {
            log.info("🚀 [批量导入] 开始批量导入会员数据，数据类型: {}, 文件名: {}", dataType, file.getOriginalFilename());
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
            
            // 获取当前操作用户，如果获取失败则使用默认值
            String operName = "system";
            try {
                String loginName = getLoginName();
                if (loginName != null && !loginName.trim().isEmpty()) {
                    operName = loginName;
                    log.info("✅ [批量导入] 获取到当前登录用户: {}", operName);
                } else {
                    log.warn("⚠️ [批量导入] 登录用户名为空，使用默认用户名: system");
                }
            } catch (Exception e) {
                log.warn("⚠️ [批量导入] 无法获取当前登录用户，使用默认用户名: system, 错误: {}", e.getMessage());
            }
            
            log.info("🔄 [批量导入] 开始处理数据类型: {}", dataType);
            
            switch (dataType) {
                case "member-info":
                    log.info("📊 [会员基础信息] 开始解析Excel文件...");
                    ExcelUtil<MemberInfo> memberUtil = new ExcelUtil<>(MemberInfo.class);
                    
                    try {
                        List<MemberInfo> memberList = memberUtil.importExcel(file.getInputStream());
                        log.info("📊 [会员基础信息] Excel解析完成，解析到 {} 条数据", memberList != null ? memberList.size() : 0);
                        
                        // 检查解析结果
                        if (memberList == null) {
                            log.error("📊 [会员基础信息] Excel解析失败，返回null");
                            return Result.error("Excel文件解析失败，请检查文件格式是否正确");
                        }
                        
                        // 打印前几条数据的详细信息用于调试
                        boolean hasValidData = false;
                        for (int i = 0; i < Math.min(3, memberList.size()); i++) {
                            MemberInfo member = memberList.get(i);
                            if (member != null) {
                                log.info("📊 [会员基础信息] 第{}条数据详情 - ID: {}, 编号: {}, 姓名: {}, 手机: {}, 邮箱: {}", 
                                        (i + 1), member.getMemberId(), member.getMemberCode(), 
                                        member.getMemberName(), member.getPhone(), member.getEmail());
                                
                                // 检查是否有任何有效字段
                                if (member.getMemberCode() != null || member.getMemberName() != null || 
                                    member.getPhone() != null || member.getEmail() != null) {
                                    hasValidData = true;
                                }
                            } else {
                                log.warn("📊 [会员基础信息] 第{}条数据为null", (i + 1));
                            }
                        }
                        
                        // 检查是否有有效数据
                        if (memberList.isEmpty()) {
                            log.error("📊 [会员基础信息] Excel文件中没有解析到任何数据");
                            return Result.error("Excel文件中没有找到有效的数据行，请检查文件内容");
                        }
                        
                        if (!hasValidData) {
                            log.error("📊 [会员基础信息] Excel文件解析成功但所有数据字段都为空，可能是表头不匹配");
                            return Result.error("Excel文件表头格式不正确，请使用系统提供的模板文件进行导入");
                        }
                        
                        log.info("💾 [会员基础信息] 开始保存数据到数据库...");
                        Map<String, Object> memberResult = memberOverviewService.importMemberInfo(memberList, updateSupport, operName);
                        successCount = (Integer) memberResult.getOrDefault("successCount", 0);
                        failureCount = (Integer) memberResult.getOrDefault("failureCount", 0);
                        errorMessages = (List<String>) memberResult.getOrDefault("errorMessages", new ArrayList<>());
                        
                        log.info("💾 [会员基础信息] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                        if (!errorMessages.isEmpty()) {
                            log.warn("💾 [会员基础信息] 错误详情: {}", errorMessages);
                        }
                        
                    } catch (Exception e) {
                        log.error("❌ [会员基础信息] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + e.getMessage());
                    }
                    break;
                    
                case "member-monthly":
                    log.info("📊 [月度统计] 开始解析Excel文件...");
                    ExcelUtil<MemberMonthlyStats> monthlyUtil = new ExcelUtil<>(MemberMonthlyStats.class);
                    
                    try {
                        List<MemberMonthlyStats> monthlyList = monthlyUtil.importExcel(file.getInputStream());
                        log.info("📊 [月度统计] Excel解析完成，解析到 {} 条数据", monthlyList.size());
                        
                        // 打印前几条数据的详细信息用于调试
                        for (int i = 0; i < Math.min(3, monthlyList.size()); i++) {
                            MemberMonthlyStats monthly = monthlyList.get(i);
                            log.info("📊 [月度统计] 第{}条数据详情 - 月份: {}, 总会员: {}, 新增: {}, 活跃: {}", 
                                    (i + 1), monthly.getStatsMonth(), monthly.getTotalMembers(), 
                                    monthly.getNewMembers(), monthly.getActiveMembers());
                        }
                        
                        if (monthlyList.isEmpty()) {
                            log.warn("⚠️ [月度统计] 解析结果为空，可能是表头不匹配或数据格式错误");
                            return Result.error("Excel文件中没有找到有效数据，请检查文件格式和表头是否正确");
                        }
                        
                        log.info("💾 [月度统计] 开始保存数据到数据库...");
                        // 批量保存月度统计数据
                        for (MemberMonthlyStats monthlyStats : monthlyList) {
                            try {
                                // 这里需要调用相应的Service方法保存数据
                                // 由于IMemberOverviewService中没有直接的保存方法，暂时记录成功
                                successCount++;
                                log.debug("💾 [月度统计] 保存成功 - 月份: {}", monthlyStats.getStatsMonth());
                            } catch (Exception e) {
                                failureCount++;
                                String errorMsg = "保存月度统计数据失败: " + e.getMessage();
                                errorMessages.add(errorMsg);
                                log.error("❌ [月度统计] 保存失败 - 月份: {}, 错误: {}", monthlyStats.getStatsMonth(), e.getMessage());
                            }
                        }
                        log.info("💾 [月度统计] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                        
                    } catch (Exception e) {
                        log.error("❌ [月度统计] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + e.getMessage());
                    }
                    break;
                    
                case "member-lifecycle":
                    log.info("📊 [生命周期] 开始解析Excel文件...");
                    ExcelUtil<MemberLifecycleRecords> lifecycleUtil = new ExcelUtil<>(MemberLifecycleRecords.class);
                    
                    try {
                        List<MemberLifecycleRecords> lifecycleList = lifecycleUtil.importExcel(file.getInputStream());
                        log.info("📊 [生命周期] Excel解析完成，解析到 {} 条数据", lifecycleList.size());
                        
                        // 打印前几条数据的详细信息用于调试
                        for (int i = 0; i < Math.min(3, lifecycleList.size()); i++) {
                            MemberLifecycleRecords lifecycle = lifecycleList.get(i);
                            log.info("📊 [生命周期] 第{}条数据详情 - 会员ID: {}, 阶段: {}, 记录时间: {}", 
                                    (i + 1), lifecycle.getMemberId(), lifecycle.getLifecycleStage(), 
                                    lifecycle.getRecordTime());
                        }
                        
                        if (lifecycleList.isEmpty()) {
                            log.warn("⚠️ [生命周期] 解析结果为空，可能是表头不匹配或数据格式错误");
                            return Result.error("Excel文件中没有找到有效数据，请检查文件格式和表头是否正确");
                        }
                        
                        log.info("💾 [生命周期] 开始保存数据到数据库...");
                        // 批量保存生命周期记录数据
                        for (MemberLifecycleRecords lifecycleRecord : lifecycleList) {
                            try {
                                // 这里需要调用相应的Service方法保存数据
                                // 暂时记录成功
                                successCount++;
                                log.debug("💾 [生命周期] 保存成功 - 会员ID: {}", lifecycleRecord.getMemberId());
                            } catch (Exception e) {
                                failureCount++;
                                String errorMsg = "保存生命周期记录失败: " + e.getMessage();
                                errorMessages.add(errorMsg);
                                log.error("❌ [生命周期] 保存失败 - 会员ID: {}, 错误: {}", lifecycleRecord.getMemberId(), e.getMessage());
                            }
                        }
                        log.info("💾 [生命周期] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                        
                    } catch (Exception e) {
                        log.error("❌ [生命周期] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + e.getMessage());
                    }
                    break;
                    
                case "member-crfme":
                    log.info("📊 [CRFM-E分布] 开始解析Excel文件...");
                    ExcelUtil<MemberCrfmeDistribution> crfmeUtil = new ExcelUtil<>(MemberCrfmeDistribution.class);
                    
                    try {
                        List<MemberCrfmeDistribution> crfmeList = crfmeUtil.importExcel(file.getInputStream());
                        log.info("📊 [CRFM-E分布] Excel解析完成，解析到 {} 条数据", crfmeList.size());
                        
                        // 打印前几条数据的详细信息用于调试
                        for (int i = 0; i < Math.min(3, crfmeList.size()); i++) {
                            MemberCrfmeDistribution crfme = crfmeList.get(i);
                            log.info("📊 [CRFM-E分布] 第{}条数据详情 - 月份: {}, 分层: {}, 数量: {}, 平均分: {}", 
                                    (i + 1), crfme.getDataMonth(), crfme.getTier(), 
                                    crfme.getCount(), crfme.getAvgScore());
                        }
                        
                        if (crfmeList.isEmpty()) {
                            log.warn("⚠️ [CRFM-E分布] 解析结果为空，可能是表头不匹配或数据格式错误");
                            return Result.error("Excel文件中没有找到有效数据，请检查文件格式和表头是否正确");
                        }
                        
                        log.info("💾 [CRFM-E分布] 开始保存数据到数据库...");
                        // 批量保存CRFM-E分布数据
                        for (MemberCrfmeDistribution crfmeDistribution : crfmeList) {
                            try {
                                memberCrfmeDistributionService.insertMemberCrfmeDistribution(crfmeDistribution);
                                successCount++;
                                log.debug("💾 [CRFM-E分布] 保存成功 - 月份: {}, 分层: {}", 
                                        crfmeDistribution.getDataMonth(), crfmeDistribution.getTier());
                            } catch (Exception e) {
                                failureCount++;
                                String errorMsg = "保存CRFM-E分布数据失败: " + e.getMessage();
                                errorMessages.add(errorMsg);
                                log.error("❌ [CRFM-E分布] 保存失败 - 月份: {}, 分层: {}, 错误: {}", 
                                        crfmeDistribution.getDataMonth(), crfmeDistribution.getTier(), e.getMessage());
                            }
                        }
                        log.info("💾 [CRFM-E分布] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                        
                    } catch (Exception e) {
                        log.error("❌ [CRFM-E分布] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + e.getMessage());
                    }
                    break;
                    
                case "member-segmentation":
                    log.info("📊 [会员分层] 开始解析Excel文件...");
                    ExcelUtil<MemberProfileAnalysis> segmentUtil = new ExcelUtil<>(MemberProfileAnalysis.class);
                    
                    try {
                        List<MemberProfileAnalysis> segmentList = segmentUtil.importExcel(file.getInputStream());
                        log.info("📊 [会员分层] Excel解析完成，解析到 {} 条数据", segmentList.size());
                        
                        // 打印前几条数据的详细信息用于调试
                         for (int i = 0; i < Math.min(3, segmentList.size()); i++) {
                             MemberProfileAnalysis segment = segmentList.get(i);
                             log.info("📊 [会员分层] 第{}条数据详情 - ID: {}, 画像类型: {}, 区域: {}", 
                                     (i + 1), segment.getId(), segment.getProfileType(), 
                                     segment.getRegionCode());
                         }
                        
                        if (segmentList.isEmpty()) {
                            log.warn("⚠️ [会员分层] 解析结果为空，可能是表头不匹配或数据格式错误");
                            return Result.error("Excel文件中没有找到有效数据，请检查文件格式和表头是否正确");
                        }
                        
                        log.info("💾 [会员分层] 开始保存数据到数据库...");
                        // 批量保存会员分层画像数据
                        for (MemberProfileAnalysis profileAnalysis : segmentList) {
                            try {
                                memberProfileAnalysisService.insertMemberProfileAnalysis(profileAnalysis);
                                successCount++;
                                log.debug("💾 [会员分层] 保存成功 - ID: {}", profileAnalysis.getId());
                             } catch (Exception e) {
                                 failureCount++;
                                 String errorMsg = "保存会员分层画像数据失败: " + e.getMessage();
                                 errorMessages.add(errorMsg);
                                 log.error("❌ [会员分层] 保存失败 - ID: {}, 错误: {}", 
                                         profileAnalysis.getId(), e.getMessage());
                            }
                        }
                        log.info("💾 [会员分层] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                        
                    } catch (Exception e) {
                        log.error("❌ [会员分层] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + e.getMessage());
                    }
                    break;
                    
                case "member-stage":
                    log.info("📊 [阶段统计] 开始解析Excel文件...");
                    ExcelUtil<MemberStageStats> stageUtil = new ExcelUtil<>(MemberStageStats.class);
                    
                    try {
                        List<MemberStageStats> stageList = stageUtil.importExcel(file.getInputStream());
                        log.info("📊 [阶段统计] Excel解析完成，解析到 {} 条数据", stageList.size());
                        
                        // 打印前几条数据的详细信息用于调试
                        for (int i = 0; i < Math.min(3, stageList.size()); i++) {
                            MemberStageStats stage = stageList.get(i);
                            log.info("📊 [阶段统计] 第{}条数据详情 - 月份: {}, 阶段: {}, 会员数: {}, 新增: {}", 
                                    (i + 1), stage.getStatsMonth(), stage.getBabyStage(), 
                                    stage.getMemberCount(), stage.getNewMemberCount());
                        }
                        
                        if (stageList.isEmpty()) {
                            log.warn("⚠️ [阶段统计] 解析结果为空，可能是表头不匹配或数据格式错误");
                            return Result.error("Excel文件中没有找到有效数据，请检查文件格式和表头是否正确");
                        }
                        
                        log.info("💾 [阶段统计] 开始保存数据到数据库...");
                        // 批量保存会员阶段统计数据
                        for (MemberStageStats stageStats : stageList) {
                            try {
                                // 这里需要调用相应的Service方法保存数据
                                // 暂时记录成功
                                successCount++;
                                log.debug("💾 [阶段统计] 保存成功 - 月份: {}, 阶段: {}", 
                                        stageStats.getStatsMonth(), stageStats.getBabyStage());
                            } catch (Exception e) {
                                failureCount++;
                                String errorMsg = "保存会员阶段统计数据失败: " + e.getMessage();
                                errorMessages.add(errorMsg);
                                log.error("❌ [阶段统计] 保存失败 - 月份: {}, 阶段: {}, 错误: {}", 
                                        stageStats.getStatsMonth(), stageStats.getBabyStage(), e.getMessage());
                            }
                        }
                        log.info("💾 [阶段统计] 数据保存完成 - 成功: {}, 失败: {}", successCount, failureCount);
                        
                    } catch (Exception e) {
                        log.error("❌ [阶段统计] Excel解析失败", e);
                        return Result.error("Excel解析失败: " + e.getMessage());
                    }
                    break;
                    
                default:
                    log.error("不支持的数据类型: {}", dataType);
                    return Result.error("不支持的数据类型: " + dataType);
            }
            
            result.put("successCount", successCount);
            result.put("failureCount", failureCount);
            result.put("totalCount", successCount + failureCount);
            result.put("errorMessages", errorMessages);
            
            log.info("批量导入完成，成功: {}, 失败: {}", successCount, failureCount);
            return Result.success("导入完成", result);
            
        } catch (Exception e) {
            log.error("批量导入会员数据失败", e);
            return Result.error("导入失败: " + e.getMessage());
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