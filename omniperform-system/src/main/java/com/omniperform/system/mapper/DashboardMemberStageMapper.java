package com.omniperform.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.DashboardMemberStage;

/**
 * 会员阶段分布数据 数据层
 * 
 * @author omniperform
 */
public interface DashboardMemberStageMapper
{
    public DashboardMemberStage selectDashboardMemberStage(DashboardMemberStage stage);
    public DashboardMemberStage selectDashboardMemberStageById(Long id);
    public List<DashboardMemberStage> selectDashboardMemberStageList(DashboardMemberStage stage);
    public DashboardMemberStage selectByDataMonth(String dataMonth);
    public List<DashboardMemberStage> selectByMonthRange(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth);
    public int insertDashboardMemberStage(DashboardMemberStage stage);
    public int updateDashboardMemberStage(DashboardMemberStage stage);
    public int deleteDashboardMemberStageById(Long id);
    public int deleteDashboardMemberStageByIds(String[] ids);
}