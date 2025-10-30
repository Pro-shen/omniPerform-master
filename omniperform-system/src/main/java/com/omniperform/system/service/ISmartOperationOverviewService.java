package com.omniperform.system.service;

import java.util.List;
import java.util.Date;
import java.util.Map;
import com.omniperform.system.domain.SmartOperationOverview;

/**
 * 智能运营概览统计Service接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface ISmartOperationOverviewService 
{
    /**
     * 查询智能运营概览统计
     * 
     * @param id 智能运营概览统计主键
     * @return 智能运营概览统计
     */
    public SmartOperationOverview selectSmartOperationOverviewById(Long id);

    /**
     * 根据日期查询智能运营概览统计
     * 
     * @param statDate 统计日期
     * @param regionCode 区域代码
     * @return 智能运营概览统计
     */
    public SmartOperationOverview selectSmartOperationOverviewByDate(Date statDate, String regionCode);

    /**
     * 查询最新的智能运营概览统计
     * 
     * @param regionCode 区域代码
     * @return 智能运营概览统计
     */
    public SmartOperationOverview selectLatestSmartOperationOverview(String regionCode);

    /**
     * 查询智能运营概览统计列表
     * 
     * @param smartOperationOverview 智能运营概览统计
     * @return 智能运营概览统计集合
     */
    public List<SmartOperationOverview> selectSmartOperationOverviewList(SmartOperationOverview smartOperationOverview);

    /**
     * 查询指定日期范围内的智能运营概览统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param regionCode 区域代码
     * @return 智能运营概览统计集合
     */
    public List<SmartOperationOverview> selectSmartOperationOverviewByDateRange(Date startDate, Date endDate, String regionCode);

    /**
     * 获取智能运营概览数据（前端接口）
     * 
     * @param regionCode 区域代码
     * @return 智能运营概览数据
     */
    public Map<String, Object> getSmartOperationOverview(String regionCode);

    /**
     * 获取智能运营概览数据（前端接口，支持月份参数）
     * 
     * @param regionCode 区域代码
     * @param monthYear 月份（格式：YYYY-MM，为空则查询最新数据）
     * @return 智能运营概览数据
     */
    public Map<String, Object> getSmartOperationOverview(String regionCode, String monthYear);

    /**
     * 获取智能运营趋势数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param regionCode 区域代码
     * @return 趋势数据
     */
    public List<Map<String, Object>> getSmartOperationTrend(Date startDate, Date endDate, String regionCode);

    /**
     * 新增智能运营概览统计
     * 
     * @param smartOperationOverview 智能运营概览统计
     * @return 结果
     */
    public int insertSmartOperationOverview(SmartOperationOverview smartOperationOverview);

    /**
     * 修改智能运营概览统计
     * 
     * @param smartOperationOverview 智能运营概览统计
     * @return 结果
     */
    public int updateSmartOperationOverview(SmartOperationOverview smartOperationOverview);

    /**
     * 批量删除智能运营概览统计
     * 
     * @param ids 需要删除的智能运营概览统计主键集合
     * @return 结果
     */
    public int deleteSmartOperationOverviewByIds(Long[] ids);

    /**
     * 删除智能运营概览统计信息
     * 
     * @param id 智能运营概览统计主键
     * @return 结果
     */
    public int deleteSmartOperationOverviewById(Long id);
}