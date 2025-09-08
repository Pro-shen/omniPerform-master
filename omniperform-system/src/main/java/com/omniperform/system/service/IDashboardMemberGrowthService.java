package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.DashboardMemberGrowth;

/**
 * 会员增长趋势Service接口
 * 
 * @author omniperform
 * @date 2025-01-27
 */
public interface IDashboardMemberGrowthService 
{
    /**
     * 查询会员增长趋势
     * 
     * @param id 会员增长趋势主键
     * @return 会员增长趋势
     */
    public DashboardMemberGrowth selectDashboardMemberGrowthById(Long id);

    /**
     * 根据数据月份查询会员增长趋势
     * 
     * @param dataMonth 数据月份
     * @return 会员增长趋势
     */
    public DashboardMemberGrowth selectByDataMonth(String dataMonth);

    /**
     * 查询会员增长趋势列表
     * 
     * @param dashboardMemberGrowth 会员增长趋势
     * @return 会员增长趋势集合
     */
    public List<DashboardMemberGrowth> selectDashboardMemberGrowthList(DashboardMemberGrowth dashboardMemberGrowth);

    /**
     * 新增会员增长趋势
     * 
     * @param dashboardMemberGrowth 会员增长趋势
     * @return 结果
     */
    public int insertDashboardMemberGrowth(DashboardMemberGrowth dashboardMemberGrowth);

    /**
     * 修改会员增长趋势
     * 
     * @param dashboardMemberGrowth 会员增长趋势
     * @return 结果
     */
    public int updateDashboardMemberGrowth(DashboardMemberGrowth dashboardMemberGrowth);

    /**
     * 批量删除会员增长趋势
     * 
     * @param ids 需要删除的会员增长趋势主键集合
     * @return 结果
     */
    public int deleteDashboardMemberGrowthByIds(String ids);

    /**
     * 删除会员增长趋势信息
     * 
     * @param id 会员增长趋势主键
     * @return 结果
     */
    public int deleteDashboardMemberGrowthById(Long id);

    /**
     * 查询指定月份范围的会员增长趋势
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员增长趋势集合
     */
    public List<DashboardMemberGrowth> selectByMonthRange(String startMonth, String endMonth);
}