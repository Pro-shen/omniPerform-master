package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.DashboardMemberSourceMapper;
import com.omniperform.system.domain.DashboardMemberSource;
import com.omniperform.system.service.IDashboardMemberSourceService;

/**
 * 会员来源分析数据 服务层实现
 * 
 * @author omniperform
 */
@Service
public class DashboardMemberSourceServiceImpl implements IDashboardMemberSourceService
{
    @Autowired
    private DashboardMemberSourceMapper dashboardMemberSourceMapper;

    /**
     * 查询会员来源分析数据信息
     * 
     * @param id 主键ID
     * @return 会员来源分析数据信息
     */
    @Override
    public DashboardMemberSource selectDashboardMemberSourceById(Long id)
    {
        return dashboardMemberSourceMapper.selectDashboardMemberSourceById(id);
    }

    /**
     * 根据月份查询会员来源分析数据
     * 
     * @param dataMonth 数据月份
     * @return 会员来源分析数据列表
     */
    @Override
    public List<DashboardMemberSource> selectByDataMonth(String dataMonth)
    {
        return dashboardMemberSourceMapper.selectByDataMonth(dataMonth);
    }

    /**
     * 查询会员来源分析数据列表
     * 
     * @param dashboardMemberSource 会员来源分析数据信息
     * @return 会员来源分析数据集合
     */
    @Override
    public List<DashboardMemberSource> selectDashboardMemberSourceList(DashboardMemberSource dashboardMemberSource)
    {
        return dashboardMemberSourceMapper.selectDashboardMemberSourceList(dashboardMemberSource);
    }

    /**
     * 根据月份范围查询会员来源分析数据列表
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员来源分析数据集合
     */
    @Override
    public List<DashboardMemberSource> selectByMonthRange(String startMonth, String endMonth)
    {
        return dashboardMemberSourceMapper.selectByMonthRange(startMonth, endMonth);
    }

    /**
     * 新增会员来源分析数据
     * 
     * @param dashboardMemberSource 会员来源分析数据信息
     * @return 结果
     */
    @Override
    public int insertDashboardMemberSource(DashboardMemberSource dashboardMemberSource)
    {
        return dashboardMemberSourceMapper.insertDashboardMemberSource(dashboardMemberSource);
    }

    /**
     * 修改会员来源分析数据
     * 
     * @param dashboardMemberSource 会员来源分析数据信息
     * @return 结果
     */
    @Override
    public int updateDashboardMemberSource(DashboardMemberSource dashboardMemberSource)
    {
        return dashboardMemberSourceMapper.updateDashboardMemberSource(dashboardMemberSource);
    }

    /**
     * 批量删除会员来源分析数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteDashboardMemberSourceByIds(Long[] ids)
    {
        return dashboardMemberSourceMapper.deleteDashboardMemberSourceByIds(ids);
    }

    /**
     * 删除会员来源分析数据信息
     * 
     * @param id 会员来源分析数据主键
     * @return 结果
     */
    @Override
    public int deleteDashboardMemberSourceById(Long id)
    {
        return dashboardMemberSourceMapper.deleteDashboardMemberSourceById(id);
    }
}