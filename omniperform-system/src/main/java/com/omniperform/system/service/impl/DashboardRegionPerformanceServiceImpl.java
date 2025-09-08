package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.DashboardRegionPerformanceMapper;
import com.omniperform.system.domain.DashboardRegionPerformance;
import com.omniperform.system.service.IDashboardRegionPerformanceService;
import com.omniperform.common.core.text.Convert;

/**
 * 区域绩效Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-27
 */
@Service
public class DashboardRegionPerformanceServiceImpl implements IDashboardRegionPerformanceService 
{
    @Autowired
    private DashboardRegionPerformanceMapper dashboardRegionPerformanceMapper;

    /**
     * 查询区域绩效
     * 
     * @param id 区域绩效主键
     * @return 区域绩效
     */
    @Override
    public DashboardRegionPerformance selectDashboardRegionPerformanceById(Long id)
    {
        return dashboardRegionPerformanceMapper.selectDashboardRegionPerformanceById(id);
    }

    /**
     * 根据数据月份查询区域绩效
     * 
     * @param dataMonth 数据月份
     * @return 区域绩效集合
     */
    @Override
    public List<DashboardRegionPerformance> selectByDataMonth(String dataMonth)
    {
        return dashboardRegionPerformanceMapper.selectByDataMonth(dataMonth);
    }

    /**
     * 查询区域绩效列表
     * 
     * @param dashboardRegionPerformance 区域绩效
     * @return 区域绩效
     */
    @Override
    public List<DashboardRegionPerformance> selectDashboardRegionPerformanceList(DashboardRegionPerformance dashboardRegionPerformance)
    {
        return dashboardRegionPerformanceMapper.selectDashboardRegionPerformanceList(dashboardRegionPerformance);
    }

    /**
     * 新增区域绩效
     * 
     * @param dashboardRegionPerformance 区域绩效
     * @return 结果
     */
    @Override
    public int insertDashboardRegionPerformance(DashboardRegionPerformance dashboardRegionPerformance)
    {
        return dashboardRegionPerformanceMapper.insertDashboardRegionPerformance(dashboardRegionPerformance);
    }

    /**
     * 修改区域绩效
     * 
     * @param dashboardRegionPerformance 区域绩效
     * @return 结果
     */
    @Override
    public int updateDashboardRegionPerformance(DashboardRegionPerformance dashboardRegionPerformance)
    {
        return dashboardRegionPerformanceMapper.updateDashboardRegionPerformance(dashboardRegionPerformance);
    }

    /**
     * 批量删除区域绩效
     * 
     * @param ids 需要删除的区域绩效主键
     * @return 结果
     */
    @Override
    public int deleteDashboardRegionPerformanceByIds(String ids)
    {
        return dashboardRegionPerformanceMapper.deleteDashboardRegionPerformanceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除区域绩效信息
     * 
     * @param id 区域绩效主键
     * @return 结果
     */
    @Override
    public int deleteDashboardRegionPerformanceById(Long id)
    {
        return dashboardRegionPerformanceMapper.deleteDashboardRegionPerformanceById(id);
    }

    /**
     * 查询指定月份范围的区域绩效
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 区域绩效集合
     */
    @Override
    public List<DashboardRegionPerformance> selectByMonthRange(String startMonth, String endMonth)
    {
        return dashboardRegionPerformanceMapper.selectByMonthRange(startMonth, endMonth);
    }


}