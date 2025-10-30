package com.omniperform.system.mapper;

import java.util.List;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.SmartOperationAlert;

/**
 * 智能运营预警Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface SmartOperationAlertMapper 
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
     * 查询所有智能运营预警（测试用）
     * 
     * @return 智能运营预警集合
     */
    public List<SmartOperationAlert> selectAllSmartOperationAlerts();

    /**
     * 查询智能运营预警列表
     * 
     * @param smartOperationAlert 智能运营预警
     * @return 智能运营预警集合
     */
    public List<SmartOperationAlert> selectSmartOperationAlertList(SmartOperationAlert smartOperationAlert);

    /**
     * 查询指定状态的预警列表
     * 
     * @param status 预警状态
     * @param region 区域
     * @return 智能运营预警集合
     */
    public List<SmartOperationAlert> selectSmartOperationAlertByStatus(@Param("status") String status, @Param("region") String region);

    /**
     * 查询指定日期范围内的预警列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param region 区域
     * @return 智能运营预警集合
     */
    public List<SmartOperationAlert> selectSmartOperationAlertByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("region") String region);

    /**
     * 统计各状态预警数量
     * 
     * @param region 区域
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countAlertsByStatus(@Param("region") String region);

    /**
     * 统计各类型预警数量
     * 
     * @param region 区域
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countAlertsByType(@Param("region") String region);

    /**
     * 根据月份查询智能运营预警列表
     * 
     * @param monthYear 月份（格式：YYYY-MM）
     * @param region 区域
     * @param status 状态
     * @return 智能运营预警集合
     */
    public List<SmartOperationAlert> selectSmartOperationAlertByMonth(@Param("monthYear") String monthYear, @Param("region") String region, @Param("status") String status);

    /**
     * 根据月份统计预警数量
     * 
     * @param monthYear 月份（格式：YYYY-MM）
     * @param region 区域
     * @param status 状态
     * @return 预警数量
     */
    public int countSmartOperationAlertByMonth(@Param("monthYear") String monthYear, @Param("region") String region, @Param("status") String status);

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
     * 删除智能运营预警
     * 
     * @param id 智能运营预警主键
     * @return 结果
     */
    public int deleteSmartOperationAlertById(Long id);

    /**
     * 批量删除智能运营预警
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSmartOperationAlertByIds(Long[] ids);
}