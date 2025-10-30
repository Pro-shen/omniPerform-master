package com.omniperform.system.service;

import java.util.List;
import java.util.Date;
import java.util.Map;
import com.omniperform.system.domain.SmartOperationAlert;

/**
 * 智能运营预警Service接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface ISmartOperationAlertService 
{
    /**
     * 查询智能运营预警
     * 
     * @param id 智能运营预警主键
     * @return 智能运营预警
     */
    public SmartOperationAlert selectSmartOperationAlertById(Long id);

    /**
     * 根据预警编号查询智能运营预警
     * 
     * @param alertId 预警编号
     * @return 智能运营预警
     */
    public SmartOperationAlert selectSmartOperationAlertByAlertId(String alertId);

    /**
     * 查询智能运营预警列表
     * 
     * @param smartOperationAlert 智能运营预警
     * @return 智能运营预警集合
     */
    public List<SmartOperationAlert> selectSmartOperationAlertList(SmartOperationAlert smartOperationAlert);

    /**
     * 查询所有智能运营预警（测试用）
     * 
     * @return 智能运营预警集合
     */
    public List<SmartOperationAlert> selectAllSmartOperationAlerts();

    /**
     * 查询指定状态的预警列表
     * 
     * @param status 预警状态
     * @param region 区域
     * @return 智能运营预警集合
     */
    public List<SmartOperationAlert> selectSmartOperationAlertByStatus(String status, String region);

    /**
     * 查询指定日期范围内的预警列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param region 区域
     * @return 智能运营预警集合
     */
    public List<SmartOperationAlert> selectSmartOperationAlertByDateRange(Date startDate, Date endDate, String region);

    /**
     * 获取预警数据（前端接口）
     * 
     * @param region 区域
     * @param status 状态
     * @return 预警数据
     */
    public Map<String, Object> getAlertsData(String region, String status);

    /**
     * 获取预警数据（前端接口，支持分页）
     * 
     * @param region 区域
     * @param status 状态
     * @param page 页码
     * @param size 每页大小
     * @return 预警数据
     */
    public Map<String, Object> getAlertsDataWithPagination(String region, String status, int page, int size);

    /**
     * 获取预警数据（前端接口，支持分页和月份过滤）
     * 
     * @param region 区域
     * @param status 状态
     * @param page 页码
     * @param size 每页大小
     * @param monthYear 月份（格式：YYYY-MM，为空则查询所有数据）
     * @return 预警数据
     */
    public Map<String, Object> getAlertsDataWithPagination(String region, String status, int page, int size, String monthYear);

    /**
     * 统计各状态预警数量
     * 
     * @param region 区域
     * @return 统计结果
     */
    public List<Map<String, Object>> countAlertsByStatus(String region);

    /**
     * 统计各类型预警数量
     * 
     * @param region 区域
     * @return 统计结果
     */
    public List<Map<String, Object>> countAlertsByType(String region);

    /**
     * 处理预警
     * 
     * @param alertId 预警编号
     * @param processUser 处理人
     * @param processNote 处理备注
     * @return 结果
     */
    public int processAlert(String alertId, String processUser, String processNote);

    /**
     * 新增智能运营预警
     * 
     * @param smartOperationAlert 智能运营预警
     * @return 结果
     */
    public int insertSmartOperationAlert(SmartOperationAlert smartOperationAlert);

    /**
     * 修改智能运营预警
     * 
     * @param smartOperationAlert 智能运营预警
     * @return 结果
     */
    public int updateSmartOperationAlert(SmartOperationAlert smartOperationAlert);

    /**
     * 批量删除智能运营预警
     * 
     * @param ids 需要删除的智能运营预警主键集合
     * @return 结果
     */
    public int deleteSmartOperationAlertByIds(Long[] ids);

    /**
     * 删除智能运营预警信息
     * 
     * @param id 智能运营预警主键
     * @return 结果
     */
    public int deleteSmartOperationAlertById(Long id);
}