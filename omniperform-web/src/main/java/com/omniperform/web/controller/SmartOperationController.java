package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.annotation.Anonymous;
import com.omniperform.system.domain.SmartOperationAlert;
import com.omniperform.system.domain.MemberProfileAnalysis;
import com.omniperform.system.service.ISmartOperationOverviewService;
import com.omniperform.system.service.ISmartOperationAlertService;
import com.omniperform.system.service.ISmartMarketingTaskService;
import com.omniperform.system.service.IMemberProfileAnalysisService;
import com.omniperform.system.service.IOptimizationEffectDataService;
import com.omniperform.system.service.IBestTouchTimeAnalysisService;
import com.omniperform.system.domain.dto.OptimizationEffectImportDTO;
import com.omniperform.common.utils.poi.ExcelUtil;
import com.omniperform.common.utils.file.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

/**
 * 智能运营控制器
 * 
 * @author omniperform
 */
@Anonymous
@RestController
@RequestMapping("/smart-operation")
@CrossOrigin(origins = "*")
@Api(tags = "智能运营管理")
public class SmartOperationController {

    private static final Logger log = LoggerFactory.getLogger(SmartOperationController.class);

    @Autowired
    private ISmartOperationOverviewService smartOperationOverviewService;

    @Autowired
    private ISmartOperationAlertService smartOperationAlertService;

    @Autowired
    private ISmartMarketingTaskService smartMarketingTaskService;

    @Autowired
    private IMemberProfileAnalysisService memberProfileAnalysisService;

    @Autowired
    private IOptimizationEffectDataService optimizationEffectDataService;

    @Autowired
    private IBestTouchTimeAnalysisService bestTouchTimeAnalysisService;

    /**
     * 下载智能运营概览Excel导入模板
     */
    @GetMapping("/overview/import/template")
    @ApiOperation("下载智能运营概览Excel导入模板")
    public void downloadSmartOperationOverviewImportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<com.omniperform.system.domain.SmartOperationOverview> util = new ExcelUtil<>(com.omniperform.system.domain.SmartOperationOverview.class);
            // 导出包含示例数据与标题的模板，便于参考填写
            java.util.List<com.omniperform.system.domain.SmartOperationOverview> sample = createSmartOperationOverviewSampleData();
            // 显式设置下载文件名，避免浏览器使用URL最后一段“template”作为文件名
            FileUtils.setAttachmentResponseHeader(response, "智能运营概览.xlsx");
            util.exportExcel(response, sample, "智能运营概览", "概览导入模板");
        } catch (Exception e) {
            log.error("下载智能运营概览Excel导入模板失败: {}", e.getMessage(), e);
        }
    }


    /**
     * 导入智能运营概览Excel数据（按统计日期+区域去重，存在则更新）
     */
    @PostMapping("/overview/import")
    @ApiOperation("导入智能运营概览Excel数据")
    public Result<Map<String, Object>> importSmartOperationOverview(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        try {
            ExcelUtil<com.omniperform.system.domain.SmartOperationOverview> util = new ExcelUtil<>(com.omniperform.system.domain.SmartOperationOverview.class);
            // 读取字节
            byte[] bytes = file.getBytes();
            List<com.omniperform.system.domain.SmartOperationOverview> dataList = null;
            
            // 尝试不同的标题行位置（兼容用户删除了标题行或保留了标题行的情况）
            // 0: 无标题行，第1行是表头
            // 1: 有1行标题，第2行是表头（标准模板）
            // 2: 有2行标题，第3行是表头
            int bestSize = -1;
            for (int titleNum = 0; titleNum <= 2; titleNum++) {
                try {
                    ExcelUtil.ExcelImportResult<com.omniperform.system.domain.SmartOperationOverview> resultObj = 
                        util.importExcelEnhanced(new java.io.ByteArrayInputStream(bytes), titleNum);
                    
                    if (resultObj.isSuccess() && resultObj.getData() != null && !resultObj.getData().isEmpty()) {
                        int currentSize = resultObj.getData().size();
                        // 优先选择解析出更多数据的方案
                        if (currentSize > bestSize) {
                            dataList = resultObj.getData();
                            bestSize = currentSize;
                            log.info("智能运营概览导入 - 尝试titleNum={}成功，解析到{}条数据", titleNum, currentSize);
                        }
                    }
                } catch (Exception ex) {
                    log.warn("智能运营概览导入 - 尝试titleNum={}失败: {}", titleNum, ex.getMessage());
                }
            }

            if (dataList == null || dataList.isEmpty()) {
                return Result.error("Excel解析失败，未找到有效数据。请确保表头包含：统计日期/月份、今日待处理预警数等列");
            }

            for (int i = 0; i < dataList.size(); i++) {
                com.omniperform.system.domain.SmartOperationOverview data = dataList.get(i);
                try {
                    if (data == null) {
                        failCount++;
                        String msg = "第" + (i + 1) + "条记录为空或字段映射失败";
                        errors.add(msg);
                        log.warn(msg);
                        continue;
                    }
                    // 兜底补全monthYear或statDate
                    Date statDate = data.getStatDate();
                    String monthYear = data.getMonthYear();
                    
                    // 预处理monthYear，兼容可能包含的时间部分（例如"2025-09-01 00:00:00"）
                    if (monthYear != null) {
                        monthYear = monthYear.trim();
                        if (monthYear.length() > 7 && monthYear.matches("^\\d{4}-\\d{2}.*")) {
                             monthYear = monthYear.substring(0, 7);
                             data.setMonthYear(monthYear);
                        }
                    }
                    
                    String regionCode = data.getRegionCode();

                    if ((statDate == null) && monthYear != null && !monthYear.isEmpty()) {
                        // 若仅提供月份，则按该月最后一天作为统计日期
                        try {
                            java.time.LocalDate ld = java.time.LocalDate.parse(monthYear + "-01");
                            ld = ld.withDayOfMonth(ld.lengthOfMonth());
                            statDate = java.sql.Date.valueOf(ld);
                            data.setStatDate(statDate);
                        } catch (Exception pe) {
                            // 月份解析失败
                            throw new IllegalArgumentException("无效的月份格式: " + monthYear + ", 期望YYYY-MM");
                        }
                    }

                    if (monthYear == null || monthYear.trim().isEmpty()) {
                        if (statDate != null) {
                            java.time.LocalDate ld = ((java.sql.Date) statDate).toLocalDate();
                            monthYear = ld.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
                            data.setMonthYear(monthYear);
                        }
                    }

                    // 去重依据：stat_date + region_code（region为空视为'all'匹配由SQL处理）
                    String regionForQuery = regionCode;
                    if (regionForQuery != null) {
                        regionForQuery = regionForQuery.trim();
                    }

                    com.omniperform.system.domain.SmartOperationOverview existing = smartOperationOverviewService.selectSmartOperationOverviewByDate(statDate, regionForQuery);
                    if (existing != null && existing.getId() != null) {
                        // 更新已有记录
                        data.setId(existing.getId());
                        smartOperationOverviewService.updateSmartOperationOverview(data);
                    } else {
                        // 插入新记录
                        smartOperationOverviewService.insertSmartOperationOverview(data);
                    }
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    String msg = "第" + (i + 1) + "条数据处理失败: " + e.getMessage();
                    errors.add(msg);
                    log.error(msg, e);
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入智能运营概览Excel失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 获取智能运营概览数据
     */
    @GetMapping("/overview")
    @ApiOperation("获取智能运营概览数据")
    public Result getSmartOperationOverview(@RequestParam(defaultValue = "") String month) {
        try {
            // 使用Service层获取概览数据，支持月份参数
            Map<String, Object> overview = smartOperationOverviewService.getSmartOperationOverview("all", month);

            log.info("获取智能运营概览数据成功，月份: {}", month.isEmpty() ? "当前月份" : month);
            return Result.success("获取智能运营概览数据成功", overview);
        } catch (Exception e) {
            log.error("获取智能运营概览数据失败: {}", e.getMessage(), e);
            return Result.error("获取智能运营概览数据失败");
        }
    }

    /**
     * 获取实时监控预警数据
     */
    @GetMapping("/alerts")
    @ApiOperation("获取实时监控预警数据")
    public Result getAlerts(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(defaultValue = "待处理") String status,
                            @RequestParam(defaultValue = "all") String region,
                            @RequestParam(required = false) String month) {
        try {
            Map<String, Object> result;
            String monthYear = (month == null) ? null : month.trim();
            if (monthYear != null && !monthYear.isEmpty()) {
                // 当仅选择月份时，默认展示该月的全部状态数据（不按“待处理”过滤）
                result = smartOperationAlertService.getAlertsDataWithPagination(region, "", page, size, monthYear);
                Integer monthlyTotal = (result != null && result.get("totalCount") instanceof Integer)
                        ? (Integer) result.get("totalCount") : 0;
                if (monthlyTotal == 0) {
                    // 若该月无数据，回退到按状态查询，避免前端空白
                    result = smartOperationAlertService.getAlertsDataWithPagination(region, status.trim(), page, size);
                }
            } else {
                result = smartOperationAlertService.getAlertsDataWithPagination(region, status.trim(), page, size);
            }

            Integer total = (result != null && result.get("totalCount") instanceof Integer) ? (Integer) result.get("totalCount") : null;
            log.info("获取实时监控预警数据成功，页码: {}, 大小: {}, 状态: {}, 区域: {}, 月份: {}, 总数: {}", page, size, status, region, month, total);
            return Result.success("获取实时监控预警数据成功", result);
        } catch (Exception e) {
            log.error("获取实时监控预警数据失败: {}", e.getMessage(), e);
            return Result.error("获取实时监控预警数据失败");
        }
    }

    /**
     * 测试查询所有预警数据
     */
    /*
    @GetMapping("/alerts/test")
    @ApiOperation("测试查询所有预警数据")
    public Result testGetAllAlerts() {
        try {
            List<SmartOperationAlert> alerts = smartOperationAlertService.selectAllSmartOperationAlerts();
            log.info("测试查询所有预警数据成功，数量: {}", alerts.size());
            return Result.success("测试查询所有预警数据成功", alerts);
        } catch (Exception e) {
            log.error("测试查询所有预警数据失败: {}", e.getMessage(), e);
            return Result.error("测试查询所有预警数据失败");
        }
    }
    */

    /**
     * 获取AI推荐任务数据
     */
    @GetMapping("/ai-tasks")
    @ApiOperation("获取AI推荐任务数据")
    public Result getAiTasks(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String month) {
        try {
            // 使用Service层获取AI任务数据
            Map<String, Object> result;
            if (month != null && !month.trim().isEmpty()) {
                result = smartMarketingTaskService.getAiTasksData("待执行", "AI推荐", month);
            } else {
                result = smartMarketingTaskService.getAiTasksData("待执行", "AI推荐");
            }

            log.info("获取AI推荐任务数据成功，页码: {}, 大小: {}, 月份: {}", page, size, month);
            return Result.success("获取AI推荐任务数据成功", result);
        } catch (Exception e) {
            log.error("获取AI推荐任务数据失败: {}", e.getMessage(), e);
            return Result.error("获取AI推荐任务数据失败");
        }
    }

    /**
     * 获取AI推荐营销任务数据
     */
    @GetMapping("/marketing-tasks")
    @ApiOperation("获取AI推荐营销任务数据")
    public Result getMarketingTasks(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String month) {
        try {
            // 使用Service层获取营销任务数据，不过滤任务类型，返回所有待执行的任务
            Map<String, Object> result;
            if (month != null && !month.trim().isEmpty()) {
                result = smartMarketingTaskService.getMarketingTasksData("待执行", null, month);
            } else {
                result = smartMarketingTaskService.getMarketingTasksData("待执行", null);
            }

            log.info("获取AI推荐营销任务数据成功，页码: {}, 大小: {}, 月份: {}", page, size, month);
            return Result.success("获取AI推荐营销任务数据成功", result);
        } catch (Exception e) {
            log.error("获取AI推荐营销任务数据失败: {}", e.getMessage(), e);
            return Result.error("获取AI推荐营销任务数据失败");
        }
    }

    /**
     * 获取会员画像分析数据
     */
    @GetMapping("/member-profile")
    @ApiOperation("获取会员画像分析数据")
    public Result getMemberProfile(@RequestParam(required = false) String month,
                                   @RequestParam(required = false) String profileType,
                                   @RequestParam(required = false) String regionCode) {
        try {
            log.info("[member-profile] 请求开始, month={}, profileType={}, regionCode={}", month, profileType, regionCode);
            Map<String, Object> profileData;
            if (month != null && !month.trim().isEmpty()) {
                profileData = memberProfileAnalysisService.getMemberProfileData(profileType, regionCode, month);
            } else {
                profileData = memberProfileAnalysisService.getMemberProfileData(profileType, regionCode);
            }
            int listSize = 0;
            Object listObj = profileData != null ? profileData.get("profileList") : null;
            if (listObj instanceof java.util.List) {
                listSize = ((java.util.List<?>) listObj).size();
            }
            log.info("[member-profile] 请求结束, month={}, 返回条数={}", month, listSize);
            return Result.success("获取会员画像分析数据成功", profileData);
        } catch (Exception e) {
            log.error("获取会员画像分析数据失败: {}", e.getMessage(), e);
            return Result.error("获取会员画像分析数据失败");
        }
    }

    /**
     * 测试会员画像分析数据查询
     */
    @GetMapping("/member-profile-test")
    @ApiOperation("测试会员画像分析数据查询")
    public Result getMemberProfileTest() {
        try {
            // 直接查询最新的会员画像分析数据，不传任何参数
            List<MemberProfileAnalysis> profileList = memberProfileAnalysisService.selectLatestMemberProfileAnalysis(null, null);

            Map<String, Object> result = new HashMap<>();
            result.put("profileList", profileList);
            result.put("totalCount", profileList.size());

            log.info("测试获取会员画像分析数据成功，共{}条记录", profileList.size());
            return Result.success("测试获取会员画像分析数据成功", result);
        } catch (Exception e) {
            log.error("测试获取会员画像分析数据失败: {}", e.getMessage(), e);
            return Result.error("测试获取会员画像分析数据失败: " + e.getMessage());
        }
    }

    /**
     * 下载会员画像Excel导入模板
     */
    @GetMapping("/member-profile/import/template")
    @ApiOperation("下载会员画像Excel导入模板")
    public void downloadMemberProfileImportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<MemberProfileAnalysis> util = new ExcelUtil<>(MemberProfileAnalysis.class);
            // 显式设置下载文件名
            FileUtils.setAttachmentResponseHeader(response, "会员画像.xlsx");
            util.importTemplateExcel(response, "会员画像数据", "会员画像导入模板");
        } catch (Exception e) {
            log.error("下载会员画像Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 导入会员画像Excel数据
     */
    @PostMapping("/member-profile/import")
    @ApiOperation("导入会员画像Excel数据")
    public Result<Map<String, Object>> importMemberProfile(@RequestParam("file") MultipartFile file,
                                                           @RequestParam(value = "updateSupport", required = false, defaultValue = "false") boolean updateSupport,
                                                           @RequestParam(value = "defaultRegion", required = false, defaultValue = "") String defaultRegion) {
        try {
            log.info("开始导入会员画像数据，文件名: {}, 大小: {} bytes, updateSupport={}, defaultRegion={}",
                    file.getOriginalFilename(), file.getSize(), updateSupport, defaultRegion);

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls"))) {
                return Result.error("请上传Excel文件（.xlsx或.xls）");
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                return Result.error("文件大小不能超过10MB");
            }

            ExcelUtil<MemberProfileAnalysis> util = new ExcelUtil<>(MemberProfileAnalysis.class);
            List<MemberProfileAnalysis> dataList = util.importExcel(file.getInputStream());
            if (dataList == null) {
                dataList = Collections.emptyList();
            }
            log.info("Excel共读取到 {} 条记录", dataList.size());

            Map<String, Object> result = memberProfileAnalysisService.importMemberProfileExcel(dataList, updateSupport, defaultRegion);

            int successCount = (int) result.getOrDefault("successCount", 0);
            int failCount = (int) result.getOrDefault("failCount", 0);
            String message;
            if (failCount == 0) {
                message = String.format("导入成功！共处理 %d 条数据，全部成功", successCount);
            } else if (successCount == 0) {
                message = String.format("导入失败！共 %d 条数据，全部失败", failCount);
            } else {
                message = String.format("导入完成！成功: %d 条，失败: %d 条", successCount, failCount);
            }

            return Result.success(message, result);
        } catch (Exception e) {
            log.error("导入会员画像数据失败: {}", e.getMessage(), e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 下载智能运营预警Excel导入模板
     */
    @GetMapping("/alerts/import/template")
    @ApiOperation("下载智能运营预警Excel导入模板")
    public void downloadAlertsImportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<com.omniperform.system.domain.SmartOperationAlert> util = new ExcelUtil<>(com.omniperform.system.domain.SmartOperationAlert.class);
            java.util.List<com.omniperform.system.domain.SmartOperationAlert> sample = createSmartOperationAlertSampleData();
            FileUtils.setAttachmentResponseHeader(response, "预警.xlsx");
            util.exportExcel(response, sample, "预警导入模板");
        } catch (Exception e) {
            log.error("下载智能运营预警Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 导入智能运营预警Excel数据（按预警编号去重，存在则更新）
     */
    @PostMapping("/alerts/import")
    @ApiOperation("导入智能运营预警Excel数据")
    public Result<Map<String, Object>> importAlerts(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        try {
            ExcelUtil<com.omniperform.system.domain.SmartOperationAlert> util = new ExcelUtil<>(com.omniperform.system.domain.SmartOperationAlert.class);
            List<com.omniperform.system.domain.SmartOperationAlert> dataList = util.importExcel(file.getInputStream());
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }

            for (int i = 0; i < dataList.size(); i++) {
                com.omniperform.system.domain.SmartOperationAlert data = dataList.get(i);
                try {
                    // 规范化字符串，避免状态/区域因空格或大小写不一致导致查询不到
                    if (data.getStatus() != null) {
                        data.setStatus(data.getStatus().trim());
                    }
                    if (data.getRegion() != null) {
                        data.setRegion(data.getRegion().trim());
                    }

                    // 规范化/自动补全月份字段 monthYear（统一为：YYYY-MM），供前端按月筛选
                    String normalizedMonthYear = null;
                    String rawMonthYear = data.getMonthYear();
                    if (rawMonthYear == null || rawMonthYear.trim().isEmpty()) {
                        // 无显式月份时，基于处理时间推断
                        java.util.Date pt = data.getProcessTime();
                        java.time.LocalDate ld;
                        if (pt != null) {
                            ld = pt.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                        } else {
                            ld = java.time.LocalDate.now();
                        }
                        normalizedMonthYear = ld.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
                    } else {
                        rawMonthYear = rawMonthYear.trim();
                        try {
                            // 支持：YYYY-MM、YYYY/M、YYYY/MM
                            if (rawMonthYear.matches("^\\d{4}-\\d{1,2}$") || rawMonthYear.matches("^\\d{4}/\\d{1,2}$")) {
                                String sep = rawMonthYear.contains("/") ? "/" : "-";
                                String[] parts = rawMonthYear.split(sep);
                                String mm = parts[1].length() == 1 ? ("0" + parts[1]) : parts[1];
                                normalizedMonthYear = parts[0] + "-" + mm;
                            }
                            // 支持：YYYY年M月 或 YYYY年MM月
                            else if (rawMonthYear.matches("^\\d{4}年\\d{1,2}月$")) {
                                String year = rawMonthYear.substring(0, 4);
                                String monthStr = rawMonthYear.substring(5, rawMonthYear.length() - 1);
                                String mm = monthStr.length() == 1 ? ("0" + monthStr) : monthStr;
                                normalizedMonthYear = year + "-" + mm;
                            }
                            // 支持：YYYY-M-D 或 YYYY-MM-DD（取年-月）
                            else if (rawMonthYear.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                                java.time.LocalDate ld = java.time.LocalDate.parse(rawMonthYear, java.time.format.DateTimeFormatter.ofPattern("yyyy-M-d"));
                                normalizedMonthYear = ld.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
                            } else if (rawMonthYear.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                                java.time.LocalDate ld = java.time.LocalDate.parse(rawMonthYear, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                normalizedMonthYear = ld.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
                            }
                            // 兜底：替换中文与斜杠为连字符，并提取前两段
                            else {
                                String candidate = rawMonthYear.replace("年", "-").replace("月", "").replace("/", "-");
                                candidate = candidate.replaceAll("\\s+", "");
                                java.util.regex.Matcher m = java.util.regex.Pattern.compile("^(\\d{4})-(\\d{1,2})").matcher(candidate);
                                if (m.find()) {
                                    String year = m.group(1);
                                    String monthStr = m.group(2);
                                    String mm = monthStr.length() == 1 ? ("0" + monthStr) : monthStr;
                                    normalizedMonthYear = year + "-" + mm;
                                } else {
                                    java.time.LocalDate now = java.time.LocalDate.now();
                                    normalizedMonthYear = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
                                }
                            }
                        } catch (Exception pe) {
                            java.time.LocalDate now = java.time.LocalDate.now();
                            normalizedMonthYear = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
                        }
                    }
                    data.setMonthYear(normalizedMonthYear);
                    // 查重：按alertId
                    com.omniperform.system.domain.SmartOperationAlert existing = smartOperationAlertService.selectSmartOperationAlertByAlertId(data.getAlertId());
                    if (existing != null && existing.getId() != null) {
                        data.setId(existing.getId());
                        smartOperationAlertService.updateSmartOperationAlert(data);
                    } else {
                        smartOperationAlertService.insertSmartOperationAlert(data);
                    }
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    String msg = "第" + (i + 1) + "条数据处理失败: " + e.getMessage();
                    errors.add(msg);
                    log.error(msg, e);
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入智能运营预警Excel失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 下载AI推荐营销任务Excel导入模板
     */
    @GetMapping("/marketing-tasks/import/template")
    @ApiOperation("下载AI推荐营销任务Excel导入模板")
    public void downloadMarketingTasksImportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<com.omniperform.system.domain.SmartMarketingTask> util = new ExcelUtil<>(com.omniperform.system.domain.SmartMarketingTask.class);
            java.util.List<com.omniperform.system.domain.SmartMarketingTask> sample = createSmartMarketingTaskSampleData();
            FileUtils.setAttachmentResponseHeader(response, "AI推荐营销任务.xlsx");
            util.exportExcel(response, sample, "AI推荐营销任务导入模板");
        } catch (Exception e) {
            log.error("下载AI推荐营销任务Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 导入AI推荐营销任务Excel数据（按任务编号去重，存在则更新）
     */
    @PostMapping("/marketing-tasks/import")
    @ApiOperation("导入AI推荐营销任务Excel数据")
    public Result<Map<String, Object>> importMarketingTasks(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        try {
            ExcelUtil<com.omniperform.system.domain.SmartMarketingTask> util = new ExcelUtil<>(com.omniperform.system.domain.SmartMarketingTask.class);
            List<com.omniperform.system.domain.SmartMarketingTask> dataList = util.importExcel(file.getInputStream());
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }

            for (int i = 0; i < dataList.size(); i++) {
                com.omniperform.system.domain.SmartMarketingTask data = dataList.get(i);
                try {
                    com.omniperform.system.domain.SmartMarketingTask existing = smartMarketingTaskService.selectSmartMarketingTaskByTaskId(data.getTaskId());
                    if (existing != null && existing.getId() != null) {
                        data.setId(existing.getId());
                        smartMarketingTaskService.updateSmartMarketingTask(data);
                    } else {
                        smartMarketingTaskService.insertSmartMarketingTask(data);
                    }
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    String msg = "第" + (i + 1) + "条数据处理失败: " + e.getMessage();
                    errors.add(msg);
                    log.error(msg, e);
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入AI推荐营销任务Excel失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 下载数据闭环优化效果Excel导入模板
     */
    @GetMapping("/optimization-effect/import/template")
    @ApiOperation("下载数据闭环优化效果Excel导入模板")
    public void downloadOptimizationEffectImportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<OptimizationEffectImportDTO> util = new ExcelUtil<>(OptimizationEffectImportDTO.class);
            java.util.List<OptimizationEffectImportDTO> sample = createOptimizationEffectImportSampleData();
            FileUtils.setAttachmentResponseHeader(response, "数据闭环优化效果.xlsx");
            util.exportExcel(response, sample, "数据闭环优化效果", "优化效果数据导入模板");
        } catch (Exception e) {
            log.error("下载优化效果Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 导入数据闭环优化效果Excel数据（按日期+指标+区域去重）
     */
    @PostMapping("/optimization-effect/import")
    @ApiOperation("导入数据闭环优化效果Excel数据")
    public Result<Map<String, Object>> importOptimizationEffect(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        try {
            ExcelUtil<OptimizationEffectImportDTO> util = new ExcelUtil<>(OptimizationEffectImportDTO.class);
            List<OptimizationEffectImportDTO> dataList = util.importExcel(file.getInputStream());
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }

            for (int i = 0; i < dataList.size(); i++) {
                OptimizationEffectImportDTO dto = dataList.get(i);
                try {
                    if (dto.getStatDate() == null) {
                        throw new IllegalArgumentException("统计日期不能为空");
                    }
                    // 转换并保存MOT执行率
                    saveOptimizationMetric(dto, "MOT执行率", dto.getMotExecutionRate());
                    // 转换并保存会员活跃度
                    saveOptimizationMetric(dto, "会员活跃度", dto.getMemberActivityRate());
                    // 转换并保存复购率
                    saveOptimizationMetric(dto, "复购率", dto.getRepurchaseRate());
                    
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    String msg = "第" + (i + 1) + "条数据处理失败: " + e.getMessage();
                    errors.add(msg);
                    log.error(msg, e);
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入优化效果Excel失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    private void saveOptimizationMetric(OptimizationEffectImportDTO dto, String metricName, java.math.BigDecimal value) {
        if (value == null) return;
        com.omniperform.system.domain.OptimizationEffectData data = new com.omniperform.system.domain.OptimizationEffectData();
        data.setStatDate(dto.getStatDate());
        // 自动生成monthYear
        java.time.LocalDate ld = new java.sql.Date(dto.getStatDate().getTime()).toLocalDate();
        data.setMonthYear(ld.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")));
        data.setMetricName(metricName);
        data.setMetricValue(value);
        data.setRegionCode(dto.getRegionCode());
        // 默认同比环比为0，后续可增加计算逻辑
        data.setMomRate(java.math.BigDecimal.ZERO);
        data.setYoyRate(java.math.BigDecimal.ZERO);
        
        optimizationEffectDataService.upsert(data);
    }

    /**
     * 下载会员画像分析Excel导入模板
     */
    @GetMapping("/member-profile-analysis/import/template")
    @ApiOperation("下载会员画像分析Excel导入模板")
    public void downloadMemberProfileAnalysisImportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<com.omniperform.system.domain.MemberProfileAnalysis> util = new ExcelUtil<>(com.omniperform.system.domain.MemberProfileAnalysis.class);
            java.util.List<com.omniperform.system.domain.MemberProfileAnalysis> sample = createMemberProfileAnalysisSampleData();
            FileUtils.setAttachmentResponseHeader(response, "会员画像分析.xlsx");
            util.exportExcel(response, sample, "会员画像分析", "会员画像分析导入模板");
        } catch (Exception e) {
            log.error("下载会员画像分析Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 导入会员画像分析Excel数据
     */
    @PostMapping("/member-profile-analysis/import")
    @ApiOperation("导入会员画像分析Excel数据")
    public Result<Map<String, Object>> importMemberProfileAnalysis(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        try {
            ExcelUtil<com.omniperform.system.domain.MemberProfileAnalysis> util = new ExcelUtil<>(com.omniperform.system.domain.MemberProfileAnalysis.class);
            List<com.omniperform.system.domain.MemberProfileAnalysis> dataList = util.importExcel(file.getInputStream());
            
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }

            for (int i = 0; i < dataList.size(); i++) {
                com.omniperform.system.domain.MemberProfileAnalysis data = dataList.get(i);
                try {
                    // 基本校验
                    if (data.getAnalysisDate() == null) {
                        // 如果没有日期，默认为当天
                        data.setAnalysisDate(new Date());
                    }
                    if (data.getProfileType() == null || data.getProfileType().trim().isEmpty()) {
                        throw new IllegalArgumentException("画像类型不能为空");
                    }

                    // 插入或更新逻辑（这里简化为插入，实际业务可能需要根据日期和类型去重）
                    // 检查是否存在
                    com.omniperform.system.domain.MemberProfileAnalysis existing = new com.omniperform.system.domain.MemberProfileAnalysis();
                    existing.setAnalysisDate(data.getAnalysisDate());
                    existing.setProfileType(data.getProfileType());
                    existing.setRegionCode(data.getRegionCode());
                    List<com.omniperform.system.domain.MemberProfileAnalysis> list = memberProfileAnalysisService.selectMemberProfileAnalysisList(existing);
                    
                    if (list != null && !list.isEmpty()) {
                        // 更新
                        data.setId(list.get(0).getId());
                        memberProfileAnalysisService.updateMemberProfileAnalysis(data);
                    } else {
                        // 插入
                        memberProfileAnalysisService.insertMemberProfileAnalysis(data);
                    }
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    String msg = "第" + (i + 1) + "条数据处理失败: " + e.getMessage();
                    errors.add(msg);
                    log.error(msg, e);
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入会员画像分析Excel失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 下载最佳触达时间Excel导入模板
     */
    @GetMapping("/best-touch-time/import/template")
    @ApiOperation("下载最佳触达时间Excel导入模板")
    public void downloadBestTouchTimeImportTemplate(HttpServletResponse response) {
        try {
            ExcelUtil<com.omniperform.system.domain.BestTouchTimeAnalysis> util = new ExcelUtil<>(com.omniperform.system.domain.BestTouchTimeAnalysis.class);
            java.util.List<com.omniperform.system.domain.BestTouchTimeAnalysis> sample = createBestTouchTimeSampleData();
            FileUtils.setAttachmentResponseHeader(response, "最佳触达时间.xlsx");
            util.exportExcel(response, sample, "最佳触达时间导入模板");
        } catch (Exception e) {
            log.error("下载最佳触达时间Excel导入模板失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 导入最佳触达时间Excel数据（按日期+时间段+区域去重）
     */
    @PostMapping("/best-touch-time/import")
    @ApiOperation("导入最佳触达时间Excel数据")
    public Result<Map<String, Object>> importBestTouchTime(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        try {
            ExcelUtil<com.omniperform.system.domain.BestTouchTimeAnalysis> util = new ExcelUtil<>(com.omniperform.system.domain.BestTouchTimeAnalysis.class);
            List<com.omniperform.system.domain.BestTouchTimeAnalysis> dataList = util.importExcel(file.getInputStream());
            if (dataList == null || dataList.isEmpty()) {
                return Result.error("导入数据为空");
            }

            for (int i = 0; i < dataList.size(); i++) {
                com.omniperform.system.domain.BestTouchTimeAnalysis data = dataList.get(i);
                try {
                    bestTouchTimeAnalysisService.upsert(data);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    String msg = "第" + (i + 1) + "条数据处理失败: " + e.getMessage();
                    errors.add(msg);
                    log.error(msg, e);
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入最佳触达时间Excel失败: {}", e.getMessage(), e);
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 获取最佳触达时间数据
     */
    @GetMapping("/best-touch-time")
    @ApiOperation("获取最佳触达时间数据")
    public Result getBestTouchTime(@RequestParam(defaultValue = "") String month) {
        try {
            Map<String, Object> touchTimeData = new HashMap<>();

            List<String> timeSlots;
            List<Integer> responseRates;

            log.info("[best-touch-time] 请求开始, month={}", month);
            if (month != null && !month.isEmpty()) {
                List<com.omniperform.system.domain.BestTouchTimeAnalysis> list = bestTouchTimeAnalysisService.listByMonth(month, null);
                if (list != null && !list.isEmpty()) {
                    timeSlots = new ArrayList<>();
                    responseRates = new ArrayList<>();
                    for (com.omniperform.system.domain.BestTouchTimeAnalysis r : list) {
                        if (r.getTimeSlot() != null) {
                            timeSlots.add(r.getTimeSlot());
                        } else {
                            timeSlots.add("未知时段");
                        }
                        responseRates.add(r.getResponseRate() == null ? 0 : r.getResponseRate().setScale(0, java.math.RoundingMode.HALF_UP).intValue());
                    }
                    touchTimeData.put("timeSlots", timeSlots);
                    touchTimeData.put("responseRates", responseRates);
                    log.info("[best-touch-time] 请求结束, month={}, slots={}, rates={}", month, timeSlots.size(), responseRates.size());
                    return Result.success("获取最佳触达时间数据成功", touchTimeData);
                }
            }

            // 兜底：静态示例数据
            timeSlots = Arrays.asList("6-8点", "8-10点", "10-12点", "12-14点", "14-16点", "16-18点", "18-20点", "20-22点", "22-24点");
            responseRates = Arrays.asList(15, 22, 35, 28, 25, 30, 42, 65, 38);

            touchTimeData.put("timeSlots", timeSlots);
            touchTimeData.put("responseRates", responseRates);

            log.info("[best-touch-time] 使用示例数据, month={}, slots={}, rates={}", (month == null || month.isEmpty() ? "未指定" : month), timeSlots.size(), responseRates.size());
            return Result.success("获取最佳触达时间数据成功", touchTimeData);
        } catch (Exception e) {
            log.error("获取最佳触达时间数据失败: {}", e.getMessage(), e);
            return Result.error("获取最佳触达时间数据失败");
        }
    }

    /**
     * 获取数据闭环优化效果数据
     */
    @GetMapping("/optimization-effect")
    @ApiOperation("获取数据闭环优化效果数据")
    public Result getOptimizationEffect(@RequestParam(defaultValue = "") String month) {
        try {
            Map<String, Object> effectData = new HashMap<>();

            if (month != null && !month.isEmpty()) {
                List<com.omniperform.system.domain.OptimizationEffectData> list = optimizationEffectDataService.listByMonth(month, null);
                if (list != null && !list.isEmpty()) {
                    // 计算每个指标在当月的均值，并生成4周的稳定趋势
                    Map<String, java.math.BigDecimal> sum = new HashMap<>();
                    Map<String, Integer> cnt = new HashMap<>();
                    for (com.omniperform.system.domain.OptimizationEffectData d : list) {
                        if (d.getMetricName() == null || d.getMetricValue() == null) continue;
                        java.math.BigDecimal s = sum.getOrDefault(d.getMetricName(), java.math.BigDecimal.ZERO);
                        sum.put(d.getMetricName(), s.add(d.getMetricValue()));
                        cnt.put(d.getMetricName(), cnt.getOrDefault(d.getMetricName(), 0) + 1);
                    }

                    java.util.function.Function<String, Double> avg = name -> {
                        java.math.BigDecimal s = sum.get(name);
                        Integer c = cnt.get(name);
                        if (s == null || c == null || c == 0) return null;
                        return s.divide(new java.math.BigDecimal(c), 2, java.math.RoundingMode.HALF_UP).doubleValue();
                    };

                    List<String> weeks = Arrays.asList("第1周", "第2周", "第3周", "第4周");
                    Double motAvg = avg.apply("MOT执行率");
                    Double actAvg = avg.apply("会员活跃度");
                    Double repAvg = avg.apply("复购率");

                    List<Double> motExecutionRates = motAvg == null ? Collections.emptyList() : Arrays.asList(motAvg, motAvg, motAvg, motAvg);
                    List<Double> memberActivityRates = actAvg == null ? Collections.emptyList() : Arrays.asList(actAvg, actAvg, actAvg, actAvg);
                    List<Double> repurchaseRates = repAvg == null ? Collections.emptyList() : Arrays.asList(repAvg, repAvg, repAvg, repAvg);

                    if (!motExecutionRates.isEmpty() || !memberActivityRates.isEmpty() || !repurchaseRates.isEmpty()) {
                        effectData.put("months", weeks);
                        effectData.put("motExecutionRates", motExecutionRates);
                        effectData.put("memberActivityRates", memberActivityRates);
                        effectData.put("repurchaseRates", repurchaseRates);
                        log.info("获取数据闭环优化效果数据成功，月份: {}", month);
                        return Result.success("获取数据闭环优化效果数据成功", effectData);
                    }
                }
            }

            // 兜底：静态示例数据（12周趋势）
            List<String> months = Arrays.asList("第1周", "第2周", "第3周", "第4周", "第5周", "第6周", "第7周", "第8周", "第9周", "第10周", "第11周", "第12周");
            List<Double> motExecutionRates = Arrays.asList(62.5, 63.2, 64.1, 65.8, 67.2, 68.5, 70.1, 71.5, 73.2, 74.8, 76.5, 78.3);
            List<Double> memberActivityRates = Arrays.asList(55.2, 56.5, 57.1, 58.3, 59.2, 60.5, 61.8, 62.5, 63.2, 64.1, 64.8, 65.2);
            List<Double> repurchaseRates = Arrays.asList(32.5, 33.1, 33.8, 34.2, 34.9, 35.5, 36.2, 36.8, 37.5, 38.2, 38.9, 39.5);

            effectData.put("months", months);
            effectData.put("motExecutionRates", motExecutionRates);
            effectData.put("memberActivityRates", memberActivityRates);
            effectData.put("repurchaseRates", repurchaseRates);

            log.info("获取数据闭环优化效果数据成功（使用示例数据），月份: {}", month == null || month.isEmpty() ? "未指定" : month);
            return Result.success("获取数据闭环优化效果数据成功", effectData);
        } catch (Exception e) {
            log.error("获取数据闭环优化效果数据失败: {}", e.getMessage(), e);
            return Result.error("获取数据闭环优化效果数据失败");
        }
    }

    /**
     * 获取智能运营技术架构数据
     */
    @GetMapping("/tech-architecture")
    @ApiOperation("获取智能运营技术架构数据")
    public Result getTechArchitecture() {
        try {
            Map<String, Object> architectureData = new HashMap<>();

            // 技术架构桑基图数据 - 与前端完全匹配
            List<Map<String, Object>> data = new ArrayList<>();
            List<Map<String, Object>> links = new ArrayList<>();

            // 节点数据
            String[] nodeNames = {
                    "数据源", "数据采集", "数据仓库", "数据处理", "分析引擎", "机器学习", "规则引擎",
                    "会员管理", "营销自动化", "预警监控", "决策支持", "企业微信", "短信系统", "APP推送", "邮件系统"
            };

            for (String nodeName : nodeNames) {
                Map<String, Object> node = new HashMap<>();
                node.put("name", nodeName);
                data.add(node);
            }

            // 连接数据 - 与前端桑基图完全匹配
            Object[][] linkData = {
                    {"数据源", "数据采集", 1},
                    {"数据采集", "数据仓库", 1},
                    {"数据仓库", "数据处理", 1},
                    {"数据处理", "分析引擎", 0.7},
                    {"数据处理", "机器学习", 0.5},
                    {"数据处理", "规则引擎", 0.3},
                    {"分析引擎", "会员管理", 0.3},
                    {"分析引擎", "营销自动化", 0.2},
                    {"分析引擎", "预警监控", 0.2},
                    {"分析引擎", "决策支持", 0.2},
                    {"机器学习", "会员管理", 0.1},
                    {"机器学习", "营销自动化", 0.3},
                    {"机器学习", "决策支持", 0.1},
                    {"规则引擎", "预警监控", 0.2},
                    {"规则引擎", "营销自动化", 0.1},
                    {"会员管理", "企业微信", 0.1},
                    {"会员管理", "短信系统", 0.1},
                    {"会员管理", "APP推送", 0.1},
                    {"营销自动化", "企业微信", 0.2},
                    {"营销自动化", "短信系统", 0.1},
                    {"营销自动化", "APP推送", 0.1},
                    {"营销自动化", "邮件系统", 0.1},
                    {"预警监控", "企业微信", 0.1},
                    {"预警监控", "短信系统", 0.1},
                    {"决策支持", "企业微信", 0.1},
                    {"决策支持", "邮件系统", 0.1}
            };

            for (Object[] link : linkData) {
                Map<String, Object> linkMap = new HashMap<>();
                linkMap.put("source", link[0]);
                linkMap.put("target", link[1]);
                linkMap.put("value", link[2]);
                links.add(linkMap);
            }

            architectureData.put("data", data);
            architectureData.put("links", links);

            return Result.success("获取智能运营技术架构数据成功", architectureData);
        } catch (Exception e) {
            log.error("获取智能运营技术架构数据失败: {}", e.getMessage(), e);
            return Result.error("获取智能运营技术架构数据失败");
        }
    }

    /**
     * 获取精英导购最佳实践数据
     */
    @GetMapping("/best-practices")
    @ApiOperation("获取精英导购最佳实践数据")
    public Result getBestPractices() {
        try {
            List<Map<String, Object>> practices = new ArrayList<>();

            String[] guideNames = {"张小丽", "李明华", "王雅琪", "赵晓燕", "钱志强"};
            String[] regions = {"华东区", "华南区", "华中区", "华北区", "西南区"};
            String[] practices_desc = {
                    "建立客户档案，定期跟进，提供个性化服务",
                    "利用社群营销，增强客户粘性和转介绍",
                    "专业知识分享，建立专家形象和信任度",
                    "及时响应客户需求，提供优质售后服务",
                    "数据驱动决策，精准营销和客户管理"
            };

            for (int i = 0; i < guideNames.length; i++) {
                Map<String, Object> practice = new HashMap<>();
                practice.put("guideName", guideNames[i]);
                practice.put("region", regions[i]);
                practice.put("practice", practices_desc[i]);
                practice.put("performance", 90 + i * 2);
                practice.put("improvement", "+" + (15 + i * 3) + "%");
                practices.add(practice);
            }

            return Result.success("获取精英导购最佳实践数据成功", practices);
        } catch (Exception e) {
            log.error("获取精英导购最佳实践数据失败: {}", e.getMessage(), e);
            return Result.error("获取精英导购最佳实践数据失败");
        }
    }

    /**
     * 获取区域差异化策略数据
     */
    @GetMapping("/regional-strategies")
    @ApiOperation("获取区域差异化策略数据")
    public Result getRegionalStrategies() {
        try {
            List<Map<String, Object>> strategies = new ArrayList<>();

            String[] regions = {"华东区", "华南区", "华中区", "华北区", "西南区"};
            String[] strategies_desc = {
                    "重点发展线上渠道，提升数字化体验",
                    "加强社群运营，利用口碑传播优势",
                    "注重性价比宣传，满足理性消费需求",
                    "强化品牌形象，提升高端产品占比",
                    "深耕下沉市场，扩大品牌覆盖面"
            };

            String[] focuses = {"数字化", "社群化", "性价比", "品牌化", "下沉化"};

            for (int i = 0; i < regions.length; i++) {
                Map<String, Object> strategy = new HashMap<>();
                strategy.put("region", regions[i]);
                strategy.put("strategy", strategies_desc[i]);
                strategy.put("focus", focuses[i]);
                strategy.put("progress", 70 + i * 5);
                strategy.put("expectedROI", (120 + i * 10) + "%");
                strategies.add(strategy);
            }

            return Result.success("获取区域差异化策略数据成功", strategies);
        } catch (Exception e) {
            log.error("获取区域差异化策略数据失败: {}", e.getMessage(), e);
            return Result.error("获取区域差异化策略数据失败");
        }
    }

    /**
     * 生成推荐内容
     */
    @PostMapping("/generate-content")
    @ApiOperation("生成推荐内容")
    public Result generateContent(@RequestBody Map<String, Object> request) {
        try {
            String memberProfile = (String) request.get("memberProfile");
            String marketingGoal = (String) request.get("marketingGoal");
            List<String> channels = (List<String>) request.get("channels");

            log.info("收到生成内容请求 - 会员画像: {}, 营销目标: {}, 渠道: {}", memberProfile, marketingGoal, channels);

            // 模拟内容生成逻辑
            Map<String, Object> generatedContent = new HashMap<>();

            // 根据会员画像和营销目标生成内容
            String contentTitle = generateContentTitle(memberProfile, marketingGoal);
            String contentBody = generateContentBody(memberProfile, marketingGoal);
            List<String> channelStrategies = generateChannelStrategies(channels);

            generatedContent.put("title", contentTitle);
            generatedContent.put("content", contentBody);
            generatedContent.put("channels", channelStrategies);
            generatedContent.put("generatedAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            log.info("生成推荐内容成功");
            return Result.success("生成推荐内容成功", generatedContent);
        } catch (Exception e) {
            log.error("生成推荐内容失败: {}", e.getMessage(), e);
            return Result.error("生成推荐内容失败: " + e.getMessage());
        }
    }

    /**
     * 生成内容标题
     */
    private String generateContentTitle(String memberProfile, String marketingGoal) {
        Map<String, String> titleTemplates = new HashMap<>();
        titleTemplates.put("品质追求型_提升复购率", "精选优质产品，为您的品质生活加分");
        titleTemplates.put("品质追求型_增加客单价", "臻选高端系列，品味生活的每一个细节");
        titleTemplates.put("品质追求型_促进新品试用", "全新高端产品首发，邀您抢先体验");
        titleTemplates.put("成长探索型_提升复购率", "发现更多可能，让成长之路更精彩");
        titleTemplates.put("成长探索型_增加客单价", "升级您的选择，探索更多优质体验");
        titleTemplates.put("成长探索型_促进新品试用", "新品尝鲜计划，与您一起探索未知");
        titleTemplates.put("价格敏感型_提升复购率", "超值回购福利，让实惠成为习惯");
        titleTemplates.put("价格敏感型_增加客单价", "组合优惠套装，更多选择更多实惠");
        titleTemplates.put("价格敏感型_促进新品试用", "新品特价体验，好产品不贵才是真的好");

        String key = memberProfile + "_" + marketingGoal;
        return titleTemplates.getOrDefault(key, "专属推荐内容，为您精心定制");
    }

    /**
     * 生成内容正文
     */
    private String generateContentBody(String memberProfile, String marketingGoal) {
        StringBuilder content = new StringBuilder();

        // 根据会员画像定制开头
        switch (memberProfile) {
            case "品质追求型":
                content.append("尊敬的客户，我们为您精心准备了专属推荐。");
                break;
            case "成长探索型":
                content.append("充满活力的探索者，我们为您准备了全新的发现之旅。");
                break;
            case "价格敏感型":
                content.append("精明的消费者，我们为您带来超值的优质选择。");
                break;
            default:
                content.append("尊敬的客户，我们为您精心准备了专属推荐。");
        }

        // 根据营销目标定制内容
        switch (marketingGoal) {
            case "提升复购率":
                content.append("基于您的购买历史和偏好，我们为您推荐以下产品，让您的每次选择都物超所值。");
                break;
            case "增加客单价":
                content.append("为了让您获得更完整的体验，我们特别推荐以下组合套装和升级产品。");
                break;
            case "促进新品试用":
                content.append("我们的最新产品已经上市，特邀您成为首批体验者，享受专属优惠。");
                break;
            default:
                content.append("我们为您准备了精选推荐，希望能为您带来更好的体验。");
        }

        return content.toString();
    }

    /**
     * 生成渠道策略
     */
    private List<String> generateChannelStrategies(List<String> channels) {
        List<String> strategies = new ArrayList<>();

        if (channels != null) {
            for (String channel : channels) {
                switch (channel) {
                    case "微信":
                        strategies.add("微信：通过个人号一对一推送，配合朋友圈展示，提升信任度");
                        break;
                    case "短信":
                        strategies.add("短信：简洁明了的产品信息和优惠提醒，包含直接购买链接");
                        break;
                    case "APP":
                        strategies.add("APP：利用推送通知和个性化首页推荐，提升用户活跃度");
                        break;
                    case "邮件":
                        strategies.add("邮件：详细的产品介绍和使用指南，适合深度内容传播");
                        break;
                }
            }
        }

        return strategies;
    }

    /**
     * 构造智能运营概览示例数据（含2025-01与2025-02）
     */
    private java.util.List<com.omniperform.system.domain.SmartOperationOverview> createSmartOperationOverviewSampleData() {
        java.util.List<com.omniperform.system.domain.SmartOperationOverview> list = new java.util.ArrayList<>();
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            com.omniperform.system.domain.SmartOperationOverview jan = new com.omniperform.system.domain.SmartOperationOverview();
            jan.setStatDate(sdf.parse("2025-01-31"));
            jan.setMonthYear("2025-01");
            jan.setTodayAlerts(12);
            jan.setAiRecommendedTasks(8);
            jan.setMotExecutionRate(new java.math.BigDecimal("76.5"));
            jan.setMemberActivityRate(new java.math.BigDecimal("42.3"));
            jan.setRegionCode("CN");

            com.omniperform.system.domain.SmartOperationOverview feb = new com.omniperform.system.domain.SmartOperationOverview();
            feb.setStatDate(sdf.parse("2025-02-28"));
            feb.setMonthYear("2025-02");
            feb.setTodayAlerts(15);
            feb.setAiRecommendedTasks(10);
            feb.setMotExecutionRate(new java.math.BigDecimal("79.2"));
            feb.setMemberActivityRate(new java.math.BigDecimal("44.8"));
            feb.setRegionCode("CN");

            list.add(jan);
            list.add(feb);
        } catch (Exception ignore) {
        }
        return list;
    }

    /**
     * 构造智能运营预警示例数据（用处理时间体现2025-01与2025-02）
     */
    private java.util.List<com.omniperform.system.domain.SmartOperationAlert> createSmartOperationAlertSampleData() {
        java.util.List<com.omniperform.system.domain.SmartOperationAlert> list = new java.util.ArrayList<>();
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            com.omniperform.system.domain.SmartOperationAlert a1 = new com.omniperform.system.domain.SmartOperationAlert();
            a1.setAlertId("AL202501001");
            a1.setAlertType("会员流失风险");
            a1.setAlertContent("过去30天未复购，建议回访");
            a1.setSeverity("中");
            a1.setStatus("待处理");
            a1.setRegion("CN-North");
            a1.setMemberId(10001L);
            a1.setGuideId(20001L);
            a1.setProcessTime(sdf.parse("2025-01-20 10:30:00"));

            com.omniperform.system.domain.SmartOperationAlert a2 = new com.omniperform.system.domain.SmartOperationAlert();
            a2.setAlertId("AL202502001");
            a2.setAlertType("销售异常");
            a2.setAlertContent("当日销量异常下降，需核查");
            a2.setSeverity("高");
            a2.setStatus("处理中");
            a2.setRegion("CN-East");
            a2.setMemberId(10002L);
            a2.setGuideId(20002L);
            a2.setProcessTime(sdf.parse("2025-02-18 15:45:00"));

            // 新增示例：2025-05、2025-06、2025-07
            com.omniperform.system.domain.SmartOperationAlert a3 = new com.omniperform.system.domain.SmartOperationAlert();
            a3.setAlertId("AL202505001");
            a3.setAlertType("库存预警");
            a3.setAlertContent("热门SKU库存低于安全线");
            a3.setSeverity("中");
            a3.setStatus("待处理");
            a3.setRegion("CN-South");
            a3.setMemberId(10003L);
            a3.setGuideId(20003L);
            a3.setProcessTime(sdf.parse("2025-05-12 09:20:00"));

            com.omniperform.system.domain.SmartOperationAlert a4 = new com.omniperform.system.domain.SmartOperationAlert();
            a4.setAlertId("AL202506001");
            a4.setAlertType("服务质量");
            a4.setAlertContent("客服满意度下降，需培训跟进");
            a4.setSeverity("低");
            a4.setStatus("处理中");
            a4.setRegion("CN-North");
            a4.setMemberId(10004L);
            a4.setGuideId(20004L);
            a4.setProcessTime(sdf.parse("2025-06-22 14:10:00"));

            com.omniperform.system.domain.SmartOperationAlert a5 = new com.omniperform.system.domain.SmartOperationAlert();
            a5.setAlertId("AL202507001");
            a5.setAlertType("系统异常");
            a5.setAlertContent("支付回调延迟，订单确认异常");
            a5.setSeverity("高");
            a5.setStatus("已处理");
            a5.setRegion("CN-East");
            a5.setMemberId(10005L);
            a5.setGuideId(20005L);
            a5.setProcessTime(sdf.parse("2025-07-03 11:05:00"));

            list.add(a1);
            list.add(a2);
            list.add(a3);
            list.add(a4);
            list.add(a5);
        } catch (Exception ignore) {
        }
        return list;
    }

    /**
     * 构造AI推荐营销任务示例数据（用推荐/执行时间体现月份）
     */
    private java.util.List<com.omniperform.system.domain.SmartMarketingTask> createSmartMarketingTaskSampleData() {
        java.util.List<com.omniperform.system.domain.SmartMarketingTask> list = new java.util.ArrayList<>();
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 2025-01 多条示例
            com.omniperform.system.domain.SmartMarketingTask jan1 = new com.omniperform.system.domain.SmartMarketingTask();
            jan1.setTaskId("MT202501001");
            jan1.setTaskName("春节复购提升");
            jan1.setTaskType("个性化推荐");
            jan1.setTargetGroup("近90天活跃会员");
            jan1.setMemberCount(500);
            jan1.setExpectedEffect("提升复购率2%");
            jan1.setRecommendTime(sdf.parse("2025-01-10 09:00:00"));
            jan1.setStatus("待执行");
            jan1.setPriority("高");
            jan1.setAiConfidence(new java.math.BigDecimal("82.5"));

            com.omniperform.system.domain.SmartMarketingTask jan2 = new com.omniperform.system.domain.SmartMarketingTask();
            jan2.setTaskId("MT202501002");
            jan2.setTaskName("新春拉新礼包");
            jan2.setTaskType("活动邀请");
            jan2.setTargetGroup("新注册会员");
            jan2.setMemberCount(800);
            jan2.setExpectedEffect("提升注册转化率3%");
            jan2.setRecommendTime(sdf.parse("2025-01-15 14:30:00"));
            jan2.setStatus("待执行");
            jan2.setPriority("中");
            jan2.setAiConfidence(new java.math.BigDecimal("74.0"));

            com.omniperform.system.domain.SmartMarketingTask jan3 = new com.omniperform.system.domain.SmartMarketingTask();
            jan3.setTaskId("MT202501003");
            jan3.setTaskName("老客关怀短信");
            jan3.setTaskType("触达优化");
            jan3.setTargetGroup("近180天有购买");
            jan3.setMemberCount(420);
            jan3.setExpectedEffect("提升回流率1.5%");
            jan3.setRecommendTime(sdf.parse("2025-01-22 18:00:00"));
            jan3.setStatus("待执行");
            jan3.setPriority("低");
            jan3.setAiConfidence(new java.math.BigDecimal("68.2"));

            // 2025-02 多条示例
            com.omniperform.system.domain.SmartMarketingTask feb1 = new com.omniperform.system.domain.SmartMarketingTask();
            feb1.setTaskId("MT202502001");
            feb1.setTaskName("情人节新品试用");
            feb1.setTaskType("内容推送");
            feb1.setTargetGroup("价格敏感型会员");
            feb1.setMemberCount(300);
            feb1.setExpectedEffect("提升新品试用率5%");
            feb1.setRecommendTime(sdf.parse("2025-02-05 10:00:00"));
            feb1.setStatus("待执行");
            feb1.setPriority("中");
            feb1.setAiConfidence(new java.math.BigDecimal("76.0"));

            com.omniperform.system.domain.SmartMarketingTask feb2 = new com.omniperform.system.domain.SmartMarketingTask();
            feb2.setTaskId("MT202502002");
            feb2.setTaskName("节后复购关怀");
            feb2.setTaskType("关怀提醒");
            feb2.setTargetGroup("春节期间有购买");
            feb2.setMemberCount(650);
            feb2.setExpectedEffect("提升复购率2.5%");
            feb2.setRecommendTime(sdf.parse("2025-02-18 11:20:00"));
            feb2.setStatus("待执行");
            feb2.setPriority("高");
            feb2.setAiConfidence(new java.math.BigDecimal("80.3"));

            com.omniperform.system.domain.SmartMarketingTask feb3 = new com.omniperform.system.domain.SmartMarketingTask();
            feb3.setTaskId("MT202502003");
            feb3.setTaskName("售后满意度回访");
            feb3.setTaskType("触达优化");
            feb3.setTargetGroup("近30天完成售后");
            feb3.setMemberCount(200);
            feb3.setExpectedEffect("提升满意度1.2%");
            feb3.setRecommendTime(sdf.parse("2025-02-25 16:45:00"));
            feb3.setStatus("待执行");
            feb3.setPriority("低");
            feb3.setAiConfidence(new java.math.BigDecimal("65.0"));

            // 2025-05 多条示例
            com.omniperform.system.domain.SmartMarketingTask may1 = new com.omniperform.system.domain.SmartMarketingTask();
            may1.setTaskId("MT202505001");
            may1.setTaskName("五一出游礼包");
            may1.setTaskType("活动邀请");
            may1.setTargetGroup("亲子家庭会员");
            may1.setMemberCount(450);
            may1.setExpectedEffect("提升活动报名4%");
            may1.setRecommendTime(sdf.parse("2025-05-01 09:30:00"));
            may1.setStatus("待执行");
            may1.setPriority("中");
            may1.setAiConfidence(new java.math.BigDecimal("71.0"));

            com.omniperform.system.domain.SmartMarketingTask may2 = new com.omniperform.system.domain.SmartMarketingTask();
            may2.setTaskId("MT202505002");
            may2.setTaskName("新品口碑种草");
            may2.setTaskType("内容推送");
            may2.setTargetGroup("社交分享型会员");
            may2.setMemberCount(380);
            may2.setExpectedEffect("提升分享率3%");
            may2.setRecommendTime(sdf.parse("2025-05-12 13:00:00"));
            may2.setStatus("待执行");
            may2.setPriority("高");
            may2.setAiConfidence(new java.math.BigDecimal("83.1"));

            com.omniperform.system.domain.SmartMarketingTask may3 = new com.omniperform.system.domain.SmartMarketingTask();
            may3.setTaskId("MT202505003");
            may3.setTaskName("老客专属返利");
            may3.setTaskType("个性化推荐");
            may3.setTargetGroup("忠诚依赖型会员");
            may3.setMemberCount(520);
            may3.setExpectedEffect("提升复购率3.2%");
            may3.setRecommendTime(sdf.parse("2025-05-25 17:20:00"));
            may3.setStatus("待执行");
            may3.setPriority("中");
            may3.setAiConfidence(new java.math.BigDecimal("78.4"));

            // 2025-06 多条示例
            com.omniperform.system.domain.SmartMarketingTask jun1 = new com.omniperform.system.domain.SmartMarketingTask();
            jun1.setTaskId("MT202506001");
            jun1.setTaskName("618大促预热");
            jun1.setTaskType("内容推送");
            jun1.setTargetGroup("价格敏感型会员");
            jun1.setMemberCount(900);
            jun1.setExpectedEffect("提升转化率5%");
            jun1.setRecommendTime(sdf.parse("2025-06-10 10:00:00"));
            jun1.setStatus("待执行");
            jun1.setPriority("高");
            jun1.setAiConfidence(new java.math.BigDecimal("85.0"));

            com.omniperform.system.domain.SmartMarketingTask jun2 = new com.omniperform.system.domain.SmartMarketingTask();
            jun2.setTaskId("MT202506002");
            jun2.setTaskName("冷启动用户唤醒");
            jun2.setTaskType("关怀提醒");
            jun2.setTargetGroup("近180天未购买");
            jun2.setMemberCount(600);
            jun2.setExpectedEffect("提升唤醒率2%");
            jun2.setRecommendTime(sdf.parse("2025-06-18 15:10:00"));
            jun2.setStatus("待执行");
            jun2.setPriority("中");
            jun2.setAiConfidence(new java.math.BigDecimal("72.6"));

            com.omniperform.system.domain.SmartMarketingTask jun3 = new com.omniperform.system.domain.SmartMarketingTask();
            jun3.setTaskId("MT202506003");
            jun3.setTaskName("导购专属优惠券");
            jun3.setTaskType("触达优化");
            jun3.setTargetGroup("有导购互动会员");
            jun3.setMemberCount(350);
            jun3.setExpectedEffect("提升互动转化1.8%");
            jun3.setRecommendTime(sdf.parse("2025-06-25 19:30:00"));
            jun3.setStatus("待执行");
            jun3.setPriority("低");
            jun3.setAiConfidence(new java.math.BigDecimal("66.9"));

            // 2025-07 多条示例
            com.omniperform.system.domain.SmartMarketingTask jul1 = new com.omniperform.system.domain.SmartMarketingTask();
            jul1.setTaskId("MT202507001");
            jul1.setTaskName("暑期清凉套餐");
            jul1.setTaskType("个性化推荐");
            jul1.setTargetGroup("成长探索型会员");
            jul1.setMemberCount(480);
            jul1.setExpectedEffect("提升客单价2.3%");
            jul1.setRecommendTime(sdf.parse("2025-07-06 09:40:00"));
            jul1.setStatus("待执行");
            jul1.setPriority("中");
            jul1.setAiConfidence(new java.math.BigDecimal("77.7"));

            com.omniperform.system.domain.SmartMarketingTask jul2 = new com.omniperform.system.domain.SmartMarketingTask();
            jul2.setTaskId("MT202507002");
            jul2.setTaskName("暑期会员关怀");
            jul2.setTaskType("关怀提醒");
            jul2.setTargetGroup("近60天活跃会员");
            jul2.setMemberCount(520);
            jul2.setExpectedEffect("提升互动率1.7%");
            jul2.setRecommendTime(sdf.parse("2025-07-18 12:15:00"));
            jul2.setStatus("待执行");
            jul2.setPriority("低");
            jul2.setAiConfidence(new java.math.BigDecimal("69.5"));

            com.omniperform.system.domain.SmartMarketingTask jul3 = new com.omniperform.system.domain.SmartMarketingTask();
            jul3.setTaskId("MT202507003");
            jul3.setTaskName("新品直播引流");
            jul3.setTaskType("内容推送");
            jul3.setTargetGroup("社交分享型会员");
            jul3.setMemberCount(700);
            jul3.setExpectedEffect("提升直播观看率6%");
            jul3.setRecommendTime(sdf.parse("2025-07-28 20:00:00"));
            jul3.setStatus("待执行");
            jul3.setPriority("高");
            jul3.setAiConfidence(new java.math.BigDecimal("84.2"));

            // 汇总加入列表
            list.add(jan1); list.add(jan2); list.add(jan3);
            list.add(feb1); list.add(feb2); list.add(feb3);
            list.add(may1); list.add(may2); list.add(may3);
            list.add(jun1); list.add(jun2); list.add(jun3);
            list.add(jul1); list.add(jul2); list.add(jul3);
        } catch (Exception ignore) {
        }
        return list;
    }

    /**
     * 构造数据闭环优化效果导入示例数据（宽表结构）
     */
    private java.util.List<OptimizationEffectImportDTO> createOptimizationEffectImportSampleData() {
        java.util.List<OptimizationEffectImportDTO> list = new java.util.ArrayList<>();
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            
            // 示例：连续4周的数据
            OptimizationEffectImportDTO w1 = new OptimizationEffectImportDTO();
            w1.setStatDate(sdf.parse("2025-01-07"));
            w1.setMotExecutionRate(new java.math.BigDecimal("65.0"));
            w1.setMemberActivityRate(new java.math.BigDecimal("58.0"));
            w1.setRepurchaseRate(new java.math.BigDecimal("42.0"));
            w1.setRegionCode("CN");
            
            OptimizationEffectImportDTO w2 = new OptimizationEffectImportDTO();
            w2.setStatDate(sdf.parse("2025-01-14"));
            w2.setMotExecutionRate(new java.math.BigDecimal("68.0"));
            w2.setMemberActivityRate(new java.math.BigDecimal("62.0"));
            w2.setRepurchaseRate(new java.math.BigDecimal("45.0"));
            w2.setRegionCode("CN");
            
            OptimizationEffectImportDTO w3 = new OptimizationEffectImportDTO();
            w3.setStatDate(sdf.parse("2025-01-21"));
            w3.setMotExecutionRate(new java.math.BigDecimal("72.0"));
            w3.setMemberActivityRate(new java.math.BigDecimal("65.0"));
            w3.setRepurchaseRate(new java.math.BigDecimal("48.0"));
            w3.setRegionCode("CN");
            
            OptimizationEffectImportDTO w4 = new OptimizationEffectImportDTO();
            w4.setStatDate(sdf.parse("2025-01-28"));
            w4.setMotExecutionRate(new java.math.BigDecimal("75.0"));
            w4.setMemberActivityRate(new java.math.BigDecimal("68.0"));
            w4.setRepurchaseRate(new java.math.BigDecimal("52.0"));
            w4.setRegionCode("CN");
            
            list.add(w1);
            list.add(w2);
            list.add(w3);
            list.add(w4);
        } catch (Exception ignore) {
        }
        return list;
    }

    /**
     * 构造会员画像分析示例数据
     */
    private java.util.List<com.omniperform.system.domain.MemberProfileAnalysis> createMemberProfileAnalysisSampleData() {
        java.util.List<com.omniperform.system.domain.MemberProfileAnalysis> list = new java.util.ArrayList<>();
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            
            com.omniperform.system.domain.MemberProfileAnalysis p1 = new com.omniperform.system.domain.MemberProfileAnalysis();
            p1.setAnalysisDate(today);
            p1.setProfileType("成长探索型");
            p1.setMemberCount(1500);
            p1.setPercentage(new java.math.BigDecimal("30.0"));
            p1.setAvgPurchaseAmount(new java.math.BigDecimal("450.0"));
            p1.setAvgInteractionFrequency(new java.math.BigDecimal("5.2"));
            p1.setRegionCode("CN");

            com.omniperform.system.domain.MemberProfileAnalysis p2 = new com.omniperform.system.domain.MemberProfileAnalysis();
            p2.setAnalysisDate(today);
            p2.setProfileType("品质追求型");
            p2.setMemberCount(1000);
            p2.setPercentage(new java.math.BigDecimal("20.0"));
            p2.setAvgPurchaseAmount(new java.math.BigDecimal("800.0"));
            p2.setAvgInteractionFrequency(new java.math.BigDecimal("3.5"));
            p2.setRegionCode("CN");
            
            com.omniperform.system.domain.MemberProfileAnalysis p3 = new com.omniperform.system.domain.MemberProfileAnalysis();
            p3.setAnalysisDate(today);
            p3.setProfileType("价格敏感型");
            p3.setMemberCount(1250);
            p3.setPercentage(new java.math.BigDecimal("25.0"));
            p3.setAvgPurchaseAmount(new java.math.BigDecimal("200.0"));
            p3.setAvgInteractionFrequency(new java.math.BigDecimal("6.8"));
            p3.setRegionCode("CN");
            
            list.add(p1);
            list.add(p2);
            list.add(p3);
        } catch (Exception ignore) {
        }
        return list;
    }

    /**
     * 构造最佳触达时间示例数据（含monthYear字段）
     */
    private java.util.List<com.omniperform.system.domain.BestTouchTimeAnalysis> createBestTouchTimeSampleData() {
        java.util.List<com.omniperform.system.domain.BestTouchTimeAnalysis> list = new java.util.ArrayList<>();
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            com.omniperform.system.domain.BestTouchTimeAnalysis b1 = new com.omniperform.system.domain.BestTouchTimeAnalysis();
            b1.setAnalysisDate(sdf.parse("2025-01-15"));
            b1.setMonthYear("2025-01");
            b1.setTimeSlot("09:00-10:00");
            b1.setResponseRate(new java.math.BigDecimal("12.4"));
            b1.setConversionRate(new java.math.BigDecimal("3.1"));
            b1.setTotalTouches(1200);
            b1.setSuccessfulTouches(148);
            b1.setRegionCode("CN-East");

            com.omniperform.system.domain.BestTouchTimeAnalysis b2 = new com.omniperform.system.domain.BestTouchTimeAnalysis();
            b2.setAnalysisDate(sdf.parse("2025-02-12"));
            b2.setMonthYear("2025-02");
            b2.setTimeSlot("20:00-21:00");
            b2.setResponseRate(new java.math.BigDecimal("14.8"));
            b2.setConversionRate(new java.math.BigDecimal("3.9"));
            b2.setTotalTouches(980);
            b2.setSuccessfulTouches(145);
            b2.setRegionCode("CN-North");

            list.add(b1);
            list.add(b2);
        } catch (Exception ignore) {
        }
        return list;
    }

}