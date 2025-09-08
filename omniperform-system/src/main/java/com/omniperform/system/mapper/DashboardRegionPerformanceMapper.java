package com.omniperform.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.DashboardRegionPerformance;

/**
 * 区域绩效数据 数据层
 * 
 * @author omniperform
 */
public interface DashboardRegionPerformanceMapper
{
    public DashboardRegionPerformance selectDashboardRegionPerformance(DashboardRegionPerformance performance);
    public DashboardRegionPerformance selectDashboardRegionPerformanceById(Long id);
    public List<DashboardRegionPerformance> selectDashboardRegionPerformanceList(DashboardRegionPerformance performance);
    public List<DashboardRegionPerformance> selectByDataMonth(String dataMonth);
    public List<DashboardRegionPerformance> selectByMonthRange(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth);
    public int insertDashboardRegionPerformance(DashboardRegionPerformance performance);
    public int updateDashboardRegionPerformance(DashboardRegionPerformance performance);
    public int deleteDashboardRegionPerformanceById(Long id);
    public int deleteDashboardRegionPerformanceByIds(String[] ids);
}