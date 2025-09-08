package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.DashboardMemberStage;

/**
 * 会员阶段分布Service接口
 * 
 * @author omniperform
 * @date 2025-01-27
 */
public interface IDashboardMemberStageService 
{
    /**
     * 查询会员阶段分布
     * 
     * @param id 会员阶段分布主键
     * @return 会员阶段分布
     */
    public DashboardMemberStage selectDashboardMemberStageById(Long id);

    /**
     * 根据数据月份查询会员阶段分布
     * 
     * @param dataMonth 数据月份
     * @return 会员阶段分布
     */
    public DashboardMemberStage selectByDataMonth(String dataMonth);

    /**
     * 查询会员阶段分布列表
     * 
     * @param dashboardMemberStage 会员阶段分布
     * @return 会员阶段分布集合
     */
    public List<DashboardMemberStage> selectDashboardMemberStageList(DashboardMemberStage dashboardMemberStage);

    /**
     * 新增会员阶段分布
     * 
     * @param dashboardMemberStage 会员阶段分布
     * @return 结果
     */
    public int insertDashboardMemberStage(DashboardMemberStage dashboardMemberStage);

    /**
     * 修改会员阶段分布
     * 
     * @param dashboardMemberStage 会员阶段分布
     * @return 结果
     */
    public int updateDashboardMemberStage(DashboardMemberStage dashboardMemberStage);

    /**
     * 批量删除会员阶段分布
     * 
     * @param ids 需要删除的会员阶段分布主键集合
     * @return 结果
     */
    public int deleteDashboardMemberStageByIds(String ids);

    /**
     * 删除会员阶段分布信息
     * 
     * @param id 会员阶段分布主键
     * @return 结果
     */
    public int deleteDashboardMemberStageById(Long id);

    /**
     * 查询指定月份范围的会员阶段分布
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员阶段分布集合
     */
    public List<DashboardMemberStage> selectByMonthRange(String startMonth, String endMonth);
}