package com.omniperform.web.controller.smartoperation;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.omniperform.common.annotation.Anonymous;
import com.omniperform.common.core.controller.BaseController;
import com.omniperform.common.core.domain.AjaxResult;
import com.omniperform.system.domain.SmartOperationAlert;
import com.omniperform.system.service.ISmartOperationAlertService;

/**
 * 智能运营预警测试控制器
 * 
 * @author omniperform
 * @date 2024-10-04
 */
@Anonymous
@RestController
@RequestMapping("/test/smart-operation")
public class TestSmartOperationController extends BaseController
{
    @Autowired
    private ISmartOperationAlertService smartOperationAlertService;

    /**
     * 测试根据状态查询预警
     */
    @GetMapping("/alerts-by-status")
    public AjaxResult getAlertsByStatus(@RequestParam(required = false) String status, 
                                       @RequestParam(required = false) String region)
    {
        try {
            List<SmartOperationAlert> alerts = smartOperationAlertService.selectSmartOperationAlertByStatus(status, region);
            return AjaxResult.success(alerts);
        } catch (Exception e) {
            logger.error("测试根据状态查询预警失败", e);
            return AjaxResult.error("测试根据状态查询预警失败: " + e.getMessage());
        }
    }

    /**
     * 测试统计各状态预警数量
     */
    @GetMapping("/count-by-status")
    public AjaxResult countByStatus(@RequestParam(required = false) String region)
    {
        try {
            List<Map<String, Object>> result = smartOperationAlertService.countAlertsByStatus(region);
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("测试统计各状态预警数量失败", e);
            return AjaxResult.error("测试统计各状态预警数量失败: " + e.getMessage());
        }
    }

    /**
     * 测试统计各类型预警数量
     */
    @GetMapping("/count-by-type")
    public AjaxResult countByType(@RequestParam(required = false) String region)
    {
        try {
            List<Map<String, Object>> result = smartOperationAlertService.countAlertsByType(region);
            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("测试统计各类型预警数量失败", e);
            return AjaxResult.error("测试统计各类型预警数量失败: " + e.getMessage());
        }
    }

    /**
     * 测试根据状态查询预警（简化版，只传status）
     */
    @GetMapping("/alerts-by-status-simple")
    public AjaxResult getAlertsByStatusSimple(@RequestParam(required = false) String status)
    {
        try {
            List<SmartOperationAlert> alerts = smartOperationAlertService.selectSmartOperationAlertByStatus(status, null);
            return AjaxResult.success(alerts);
        } catch (Exception e) {
            logger.error("测试根据状态查询预警（简化版）失败", e);
            return AjaxResult.error("测试根据状态查询预警（简化版）失败: " + e.getMessage());
        }
    }
}