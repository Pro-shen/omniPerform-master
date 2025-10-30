package com.omniperform.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.SmartOperationAlertMapper;
import com.omniperform.system.domain.SmartOperationAlert;
import com.omniperform.system.service.ISmartOperationAlertService;
import com.omniperform.common.utils.DateUtils;

/**
 * 智能运营告警Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-09
 */
@Service
public class SmartOperationAlertServiceImpl implements ISmartOperationAlertService 
{
    @Autowired
    private SmartOperationAlertMapper smartOperationAlertMapper;

    /**
     * 查询智能运营告警
     * 
     * @param id 智能运营告警主键
     * @return 智能运营告警
     */
    @Override
    public SmartOperationAlert selectSmartOperationAlertById(Long id)
    {
        return smartOperationAlertMapper.selectSmartOperationAlertById(id);
    }

    /**
     * 查询智能运营告警列表
     * 
     * @param smartOperationAlert 智能运营告警
     * @return 智能运营告警
     */
    @Override
    public List<SmartOperationAlert> selectSmartOperationAlertList(SmartOperationAlert smartOperationAlert)
    {
        return smartOperationAlertMapper.selectSmartOperationAlertList(smartOperationAlert);
    }

    /**
     * 查询所有智能运营告警（测试用）
     * 
     * @return 智能运营告警集合
     */
    @Override
    public List<SmartOperationAlert> selectAllSmartOperationAlerts()
    {
        return smartOperationAlertMapper.selectAllSmartOperationAlerts();
    }

    /**
     * 根据告警ID查询智能运营告警
     * 
     * @param alertId 告警ID
     * @return 智能运营告警
     */
    @Override
    public SmartOperationAlert selectSmartOperationAlertByAlertId(String alertId)
    {
        return smartOperationAlertMapper.selectSmartOperationAlertByAlertId(alertId);
    }

    /**
     * 根据状态查询智能运营告警
     * 
     * @param status 状态
     * @param region 区域
     * @return 智能运营告警集合
     */
    @Override
    public List<SmartOperationAlert> selectSmartOperationAlertByStatus(String status, String region)
    {
        return smartOperationAlertMapper.selectSmartOperationAlertByStatus(status, region);
    }

    /**
     * 查询指定日期范围内的智能运营告警
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param region 区域
     * @return 智能运营告警集合
     */
    @Override
    public List<SmartOperationAlert> selectSmartOperationAlertByDateRange(Date startDate, Date endDate, String region)
    {
        return smartOperationAlertMapper.selectSmartOperationAlertByDateRange(startDate, endDate, region);
    }

    /**
     * 获取告警数据（前端接口）
     * 
     * @param region 区域
     * @param status 状态
     * @return 告警数据
     */
    @Override
    public Map<String, Object> getAlertsData(String region, String status)
    {
        Map<String, Object> result = new HashMap<>();
        
        // 获取告警列表
        List<SmartOperationAlert> alertList = selectSmartOperationAlertByStatus(status, region);
        
        result.put("alertList", alertList);
        result.put("totalCount", alertList.size());
        
        // 统计各状态的告警数量
        List<Map<String, Object>> statusCount = countAlertsByStatus(region);
        result.put("statusCount", statusCount);
        
        // 统计各类型的告警数量
        List<Map<String, Object>> typeCount = countAlertsByType(region);
        result.put("typeCount", typeCount);
        
        return result;
    }

    /**
     * 获取告警数据（前端接口，支持分页）
     * 
     * @param region 区域
     * @param status 状态
     * @param page 页码
     * @param size 每页大小
     * @return 告警数据
     */
    @Override
    public Map<String, Object> getAlertsDataWithPagination(String region, String status, int page, int size)
    {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有告警列表用于计算总数
        List<SmartOperationAlert> allAlerts = selectSmartOperationAlertByStatus(status, region);
        int totalCount = allAlerts.size();
        
        // 计算分页参数
        int offset = (page - 1) * size;
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        // 获取当前页的数据
        List<SmartOperationAlert> alertList;
        if (offset >= totalCount) {
            alertList = new ArrayList<>();
        } else {
            int endIndex = Math.min(offset + size, totalCount);
            alertList = allAlerts.subList(offset, endIndex);
        }
        
        result.put("alertList", alertList);
        result.put("totalCount", totalCount);
        result.put("currentPage", page);
        result.put("pageSize", size);
        result.put("totalPages", totalPages);
        result.put("hasNext", page < totalPages);
        result.put("hasPrev", page > 1);
        
        // 统计各状态的告警数量
        List<Map<String, Object>> statusCount = countAlertsByStatus(region);
        result.put("statusCount", statusCount);
        
        // 统计各类型的告警数量
        List<Map<String, Object>> typeCount = countAlertsByType(region);
        result.put("typeCount", typeCount);
        
        return result;
    }

    /**
     * 获取告警数据（前端接口，支持分页和月份过滤）
     * 
     * @param region 区域
     * @param status 状态
     * @param page 页码
     * @param size 每页大小
     * @param monthYear 月份（格式：YYYY-MM，为空则查询所有数据）
     * @return 告警数据
     */
    @Override
    public Map<String, Object> getAlertsDataWithPagination(String region, String status, int page, int size, String monthYear)
    {
        Map<String, Object> result = new HashMap<>();
        
        List<SmartOperationAlert> allAlerts;
        int totalCount;
        
        // 根据是否指定月份选择查询方法
        if (monthYear != null && !monthYear.trim().isEmpty())
        {
            // 按月份查询
            allAlerts = smartOperationAlertMapper.selectSmartOperationAlertByMonth(monthYear.trim(), region, status);
            totalCount = smartOperationAlertMapper.countSmartOperationAlertByMonth(monthYear.trim(), region, status);
        }
        else
        {
            // 查询所有数据
            allAlerts = selectSmartOperationAlertByStatus(status, region);
            totalCount = allAlerts.size();
        }
        
        // 计算分页参数
        int offset = (page - 1) * size;
        int totalPages = (int) Math.ceil((double) totalCount / size);
        
        // 获取当前页的数据
        List<SmartOperationAlert> alertList;
        if (offset >= totalCount) {
            alertList = new ArrayList<>();
        } else {
            int endIndex = Math.min(offset + size, totalCount);
            alertList = allAlerts.subList(offset, endIndex);
        }
        
        result.put("alertList", alertList);
        result.put("totalCount", totalCount);
        result.put("currentPage", page);
        result.put("pageSize", size);
        result.put("totalPages", totalPages);
        result.put("hasNext", page < totalPages);
        result.put("hasPrev", page > 1);
        result.put("monthYear", monthYear);
        
        // 统计各状态的告警数量
        List<Map<String, Object>> statusCount = countAlertsByStatus(region);
        result.put("statusCount", statusCount);
        
        // 统计各类型的告警数量
        List<Map<String, Object>> typeCount = countAlertsByType(region);
        result.put("typeCount", typeCount);
        
        return result;
    }

    /**
     * 统计各状态的告警数量
     * 
     * @param region 区域
     * @return 统计结果
     */
    @Override
    public List<Map<String, Object>> countAlertsByStatus(String region)
    {
        return smartOperationAlertMapper.countAlertsByStatus(region);
    }

    /**
     * 统计各类型的告警数量
     * 
     * @param region 区域
     * @return 统计结果
     */
    @Override
    public List<Map<String, Object>> countAlertsByType(String region)
    {
        return smartOperationAlertMapper.countAlertsByType(region);
    }

    /**
     * 处理告警
     * 
     * @param alertId 告警编号
     * @param processUser 处理人
     * @param processNote 处理备注
     * @return 结果
     */
    @Override
    public int processAlert(String alertId, String processUser, String processNote)
    {
        SmartOperationAlert alert = selectSmartOperationAlertByAlertId(alertId);
        if (alert != null)
        {
            alert.setStatus("已处理");
            alert.setProcessTime(DateUtils.getNowDate());
            alert.setProcessUser(processUser);
            alert.setProcessNote(processNote);
            alert.setUpdateTime(DateUtils.getNowDate());
            
            return smartOperationAlertMapper.updateSmartOperationAlert(alert);
        }
        return 0;
    }

    /**
     * 新增智能运营告警
     * 
     * @param smartOperationAlert 智能运营告警
     * @return 结果
     */
    @Override
    public int insertSmartOperationAlert(SmartOperationAlert smartOperationAlert)
    {
        smartOperationAlert.setCreateTime(DateUtils.getNowDate());
        return smartOperationAlertMapper.insertSmartOperationAlert(smartOperationAlert);
    }

    /**
     * 修改智能运营告警
     * 
     * @param smartOperationAlert 智能运营告警
     * @return 结果
     */
    @Override
    public int updateSmartOperationAlert(SmartOperationAlert smartOperationAlert)
    {
        smartOperationAlert.setUpdateTime(DateUtils.getNowDate());
        return smartOperationAlertMapper.updateSmartOperationAlert(smartOperationAlert);
    }

    /**
     * 批量删除智能运营告警
     * 
     * @param ids 需要删除的智能运营告警主键
     * @return 结果
     */
    @Override
    public int deleteSmartOperationAlertByIds(Long[] ids)
    {
        return smartOperationAlertMapper.deleteSmartOperationAlertByIds(ids);
    }

    /**
     * 删除智能运营告警信息
     * 
     * @param id 智能运营告警主键
     * @return 结果
     */
    @Override
    public int deleteSmartOperationAlertById(Long id)
    {
        return smartOperationAlertMapper.deleteSmartOperationAlertById(id);
    }
}