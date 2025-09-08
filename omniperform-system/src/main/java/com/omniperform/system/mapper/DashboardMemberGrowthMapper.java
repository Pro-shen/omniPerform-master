package com.omniperform.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.DashboardMemberGrowth;

/**
 * 会员增长趋势数据 数据层
 * 
 * @author omniperform
 */
public interface DashboardMemberGrowthMapper
{
    /**
     * 查询会员增长趋势数据信息
     * 
     * @param growth 会员增长趋势数据信息
     * @return 会员增长趋势数据信息
     */
    public DashboardMemberGrowth selectDashboardMemberGrowth(DashboardMemberGrowth growth);

    /**
     * 通过ID查询会员增长趋势数据
     * 
     * @param id 主键ID
     * @return 会员增长趋势数据信息
     */
    public DashboardMemberGrowth selectDashboardMemberGrowthById(Long id);

    /**
     * 查询会员增长趋势数据列表
     * 
     * @param growth 会员增长趋势数据信息
     * @return 会员增长趋势数据集合
     */
    public List<DashboardMemberGrowth> selectDashboardMemberGrowthList(DashboardMemberGrowth growth);

    /**
     * 根据月份查询会员增长趋势数据
     * 
     * @param dataMonth 数据月份
     * @return 会员增长趋势数据信息
     */
    public DashboardMemberGrowth selectByDataMonth(String dataMonth);

    /**
     * 根据月份范围查询会员增长趋势数据列表
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员增长趋势数据集合
     */
    public List<DashboardMemberGrowth> selectByMonthRange(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth);

    /**
     * 新增会员增长趋势数据
     * 
     * @param growth 会员增长趋势数据信息
     * @return 结果
     */
    public int insertDashboardMemberGrowth(DashboardMemberGrowth growth);

    /**
     * 修改会员增长趋势数据
     * 
     * @param growth 会员增长趋势数据信息
     * @return 结果
     */
    public int updateDashboardMemberGrowth(DashboardMemberGrowth growth);

    /**
     * 删除会员增长趋势数据
     * 
     * @param id 主键ID
     * @return 结果
     */
    public int deleteDashboardMemberGrowthById(Long id);

    /**
     * 批量删除会员增长趋势数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDashboardMemberGrowthByIds(String[] ids);
}