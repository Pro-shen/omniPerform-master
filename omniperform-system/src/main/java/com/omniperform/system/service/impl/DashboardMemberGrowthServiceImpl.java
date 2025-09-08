package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.DashboardMemberGrowthMapper;
import com.omniperform.system.domain.DashboardMemberGrowth;
import com.omniperform.system.service.IDashboardMemberGrowthService;
import com.omniperform.common.core.text.Convert;

/**
 * 会员增长趋势Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-27
 */
@Service
public class DashboardMemberGrowthServiceImpl implements IDashboardMemberGrowthService 
{
    @Autowired
    private DashboardMemberGrowthMapper dashboardMemberGrowthMapper;

    /**
     * 查询会员增长趋势
     * 
     * @param id 会员增长趋势主键
     * @return 会员增长趋势
     */
    @Override
    public DashboardMemberGrowth selectDashboardMemberGrowthById(Long id)
    {
        return dashboardMemberGrowthMapper.selectDashboardMemberGrowthById(id);
    }

    /**
     * 根据数据月份查询会员增长趋势
     * 
     * @param dataMonth 数据月份
     * @return 会员增长趋势
     */
    @Override
    public DashboardMemberGrowth selectByDataMonth(String dataMonth)
    {
        return dashboardMemberGrowthMapper.selectByDataMonth(dataMonth);
    }

    /**
     * 查询会员增长趋势列表
     * 
     * @param dashboardMemberGrowth 会员增长趋势
     * @return 会员增长趋势
     */
    @Override
    public List<DashboardMemberGrowth> selectDashboardMemberGrowthList(DashboardMemberGrowth dashboardMemberGrowth)
    {
        return dashboardMemberGrowthMapper.selectDashboardMemberGrowthList(dashboardMemberGrowth);
    }

    /**
     * 新增会员增长趋势
     * 
     * @param dashboardMemberGrowth 会员增长趋势
     * @return 结果
     */
    @Override
    public int insertDashboardMemberGrowth(DashboardMemberGrowth dashboardMemberGrowth)
    {
        return dashboardMemberGrowthMapper.insertDashboardMemberGrowth(dashboardMemberGrowth);
    }

    /**
     * 修改会员增长趋势
     * 
     * @param dashboardMemberGrowth 会员增长趋势
     * @return 结果
     */
    @Override
    public int updateDashboardMemberGrowth(DashboardMemberGrowth dashboardMemberGrowth)
    {
        return dashboardMemberGrowthMapper.updateDashboardMemberGrowth(dashboardMemberGrowth);
    }

    /**
     * 批量删除会员增长趋势
     * 
     * @param ids 需要删除的会员增长趋势主键
     * @return 结果
     */
    @Override
    public int deleteDashboardMemberGrowthByIds(String ids)
    {
        return dashboardMemberGrowthMapper.deleteDashboardMemberGrowthByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除会员增长趋势信息
     * 
     * @param id 会员增长趋势主键
     * @return 结果
     */
    @Override
    public int deleteDashboardMemberGrowthById(Long id)
    {
        return dashboardMemberGrowthMapper.deleteDashboardMemberGrowthById(id);
    }

    /**
     * 查询指定月份范围的会员增长趋势
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员增长趋势集合
     */
    @Override
    public List<DashboardMemberGrowth> selectByMonthRange(String startMonth, String endMonth)
    {
        return dashboardMemberGrowthMapper.selectByMonthRange(startMonth, endMonth);
    }
}