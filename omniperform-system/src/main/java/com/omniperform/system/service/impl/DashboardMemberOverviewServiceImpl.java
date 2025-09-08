package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.common.core.text.Convert;
import com.omniperform.system.domain.DashboardMemberOverview;
import com.omniperform.system.mapper.DashboardMemberOverviewMapper;
import com.omniperform.system.service.IDashboardMemberOverviewService;

/**
 * 会员概览数据 服务层实现
 * 
 * @author omniperform
 */
@Service
public class DashboardMemberOverviewServiceImpl implements IDashboardMemberOverviewService
{
    @Autowired
    private DashboardMemberOverviewMapper dashboardMemberOverviewMapper;

    /**
     * 查询会员概览数据信息
     * 
     * @param id 主键ID
     * @return 会员概览数据信息
     */
    @Override
    public DashboardMemberOverview selectDashboardMemberOverviewById(Long id)
    {
        return dashboardMemberOverviewMapper.selectDashboardMemberOverviewById(id);
    }

    /**
     * 根据月份查询会员概览数据
     * 
     * @param dataMonth 数据月份
     * @return 会员概览数据信息
     */
    @Override
    public DashboardMemberOverview selectByDataMonth(String dataMonth)
    {
        return dashboardMemberOverviewMapper.selectByDataMonth(dataMonth);
    }

    /**
     * 查询会员概览数据列表
     * 
     * @param overview 会员概览数据信息
     * @return 会员概览数据集合
     */
    @Override
    public List<DashboardMemberOverview> selectDashboardMemberOverviewList(DashboardMemberOverview overview)
    {
        return dashboardMemberOverviewMapper.selectDashboardMemberOverviewList(overview);
    }

    /**
     * 根据月份范围查询会员概览数据列表
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员概览数据集合
     */
    @Override
    public List<DashboardMemberOverview> selectByMonthRange(String startMonth, String endMonth)
    {
        return dashboardMemberOverviewMapper.selectByMonthRange(startMonth, endMonth);
    }

    /**
     * 新增会员概览数据
     * 
     * @param overview 会员概览数据信息
     * @return 结果
     */
    @Override
    public int insertDashboardMemberOverview(DashboardMemberOverview overview)
    {
        return dashboardMemberOverviewMapper.insertDashboardMemberOverview(overview);
    }

    /**
     * 修改会员概览数据
     * 
     * @param overview 会员概览数据信息
     * @return 结果
     */
    @Override
    public int updateDashboardMemberOverview(DashboardMemberOverview overview)
    {
        return dashboardMemberOverviewMapper.updateDashboardMemberOverview(overview);
    }

    /**
     * 删除会员概览数据信息
     * 
     * @param id 主键ID
     * @return 结果
     */
    @Override
    public int deleteDashboardMemberOverviewById(Long id)
    {
        return dashboardMemberOverviewMapper.deleteDashboardMemberOverviewById(id);
    }

    /**
     * 批量删除会员概览数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDashboardMemberOverviewByIds(String ids)
    {
        return dashboardMemberOverviewMapper.deleteDashboardMemberOverviewByIds(Convert.toStrArray(ids));
    }
}