package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.DashboardMemberSource;

/**
 * 会员来源分析数据 服务层
 * 
 * @author omniperform
 */
public interface IDashboardMemberSourceService
{
    /**
     * 查询会员来源分析数据信息
     * 
     * @param id 主键ID
     * @return 会员来源分析数据信息
     */
    public DashboardMemberSource selectDashboardMemberSourceById(Long id);

    /**
     * 根据月份查询会员来源分析数据
     * 
     * @param dataMonth 数据月份
     * @return 会员来源分析数据列表
     */
    public List<DashboardMemberSource> selectByDataMonth(String dataMonth);

    /**
     * 查询会员来源分析数据列表
     * 
     * @param dashboardMemberSource 会员来源分析数据信息
     * @return 会员来源分析数据集合
     */
    public List<DashboardMemberSource> selectDashboardMemberSourceList(DashboardMemberSource dashboardMemberSource);

    /**
     * 根据月份范围查询会员来源分析数据列表
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员来源分析数据集合
     */
    public List<DashboardMemberSource> selectByMonthRange(String startMonth, String endMonth);

    /**
     * 新增会员来源分析数据
     * 
     * @param dashboardMemberSource 会员来源分析数据信息
     * @return 结果
     */
    public int insertDashboardMemberSource(DashboardMemberSource dashboardMemberSource);

    /**
     * 修改会员来源分析数据
     * 
     * @param dashboardMemberSource 会员来源分析数据信息
     * @return 结果
     */
    public int updateDashboardMemberSource(DashboardMemberSource dashboardMemberSource);

    /**
     * 批量删除会员来源分析数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDashboardMemberSourceByIds(Long[] ids);

    /**
     * 删除会员来源分析数据信息
     * 
     * @param id 会员来源分析数据主键
     * @return 结果
     */
    public int deleteDashboardMemberSourceById(Long id);
}