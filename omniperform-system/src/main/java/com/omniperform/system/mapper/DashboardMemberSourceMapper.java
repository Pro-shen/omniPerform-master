package com.omniperform.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.DashboardMemberSource;

/**
 * 会员来源分析数据Mapper接口
 * 
 * @author omniperform
 */
public interface DashboardMemberSourceMapper 
{
    /**
     * 查询会员来源分析数据
     * 
     * @param id 会员来源分析数据主键
     * @return 会员来源分析数据
     */
    public DashboardMemberSource selectDashboardMemberSourceById(Long id);

    /**
     * 根据月份查询会员来源分析数据
     * 
     * @param dataMonth 数据月份
     * @return 会员来源分析数据列表
     */
    public List<DashboardMemberSource> selectByDataMonth(@Param("dataMonth") String dataMonth);

    /**
     * 查询会员来源分析数据列表
     * 
     * @param dashboardMemberSource 会员来源分析数据
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
    public List<DashboardMemberSource> selectByMonthRange(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth);

    /**
     * 新增会员来源分析数据
     * 
     * @param dashboardMemberSource 会员来源分析数据
     * @return 结果
     */
    public int insertDashboardMemberSource(DashboardMemberSource dashboardMemberSource);

    /**
     * 修改会员来源分析数据
     * 
     * @param dashboardMemberSource 会员来源分析数据
     * @return 结果
     */
    public int updateDashboardMemberSource(DashboardMemberSource dashboardMemberSource);

    /**
     * 删除会员来源分析数据
     * 
     * @param id 会员来源分析数据主键
     * @return 结果
     */
    public int deleteDashboardMemberSourceById(Long id);

    /**
     * 批量删除会员来源分析数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDashboardMemberSourceByIds(Long[] ids);
}