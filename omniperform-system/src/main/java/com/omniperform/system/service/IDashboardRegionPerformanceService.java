package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.DashboardRegionPerformance;

/**
 * 区域绩效Service接口
 * 
 * @author omniperform
 * @date 2025-01-27
 */
public interface IDashboardRegionPerformanceService 
{
    /**
     * 查询区域绩效
     * 
     * @param id 区域绩效主键
     * @return 区域绩效
     */
    public DashboardRegionPerformance selectDashboardRegionPerformanceById(Long id);

    /**
     * 根据数据月份查询区域绩效
     * 
     * @param dataMonth 数据月份
     * @return 区域绩效集合
     */
    public List<DashboardRegionPerformance> selectByDataMonth(String dataMonth);

    /**
     * 查询区域绩效列表
     * 
     * @param dashboardRegionPerformance 区域绩效
     * @return 区域绩效集合
     */
    public List<DashboardRegionPerformance> selectDashboardRegionPerformanceList(DashboardRegionPerformance dashboardRegionPerformance);

    /**
     * 新增区域绩效
     * 
     * @param dashboardRegionPerformance 区域绩效
     * @return 结果
     */
    public int insertDashboardRegionPerformance(DashboardRegionPerformance dashboardRegionPerformance);

    /**
     * 修改区域绩效
     * 
     * @param dashboardRegionPerformance 区域绩效
     * @return 结果
     */
    public int updateDashboardRegionPerformance(DashboardRegionPerformance dashboardRegionPerformance);

    /**
     * 批量删除区域绩效
     * 
     * @param ids 需要删除的区域绩效主键集合
     * @return 结果
     */
    public int deleteDashboardRegionPerformanceByIds(String ids);

    /**
     * 删除区域绩效信息
     * 
     * @param id 区域绩效主键
     * @return 结果
     */
    public int deleteDashboardRegionPerformanceById(Long id);

    /**
     * 查询指定月份范围的区域绩效
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 区域绩效集合
     */
    public List<DashboardRegionPerformance> selectByMonthRange(String startMonth, String endMonth);


}