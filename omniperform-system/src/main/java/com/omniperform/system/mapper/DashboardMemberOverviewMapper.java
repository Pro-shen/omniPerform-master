package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.DashboardMemberOverview;

/**
 * 会员概览数据 数据层
 * 
 * @author omniperform
 */
public interface DashboardMemberOverviewMapper
{
    /**
     * 查询会员概览数据信息
     * 
     * @param overview 会员概览数据信息
     * @return 会员概览数据信息
     */
    public DashboardMemberOverview selectDashboardMemberOverview(DashboardMemberOverview overview);

    /**
     * 通过ID查询会员概览数据
     * 
     * @param id 主键ID
     * @return 会员概览数据信息
     */
    public DashboardMemberOverview selectDashboardMemberOverviewById(Long id);

    /**
     * 查询会员概览数据列表
     * 
     * @param overview 会员概览数据信息
     * @return 会员概览数据集合
     */
    public List<DashboardMemberOverview> selectDashboardMemberOverviewList(DashboardMemberOverview overview);

    /**
     * 根据月份查询会员概览数据
     * 
     * @param dataMonth 数据月份
     * @return 会员概览数据信息
     */
    public DashboardMemberOverview selectByDataMonth(String dataMonth);

    /**
     * 根据月份范围查询会员概览数据列表
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员概览数据集合
     */
    public List<DashboardMemberOverview> selectByMonthRange(String startMonth, String endMonth);

    /**
     * 新增会员概览数据
     * 
     * @param overview 会员概览数据信息
     * @return 结果
     */
    public int insertDashboardMemberOverview(DashboardMemberOverview overview);

    /**
     * 修改会员概览数据
     * 
     * @param overview 会员概览数据信息
     * @return 结果
     */
    public int updateDashboardMemberOverview(DashboardMemberOverview overview);

    /**
     * 删除会员概览数据
     * 
     * @param id 主键ID
     * @return 结果
     */
    public int deleteDashboardMemberOverviewById(Long id);

    /**
     * 批量删除会员概览数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDashboardMemberOverviewByIds(String[] ids);
}