package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.DashboardMemberStageMapper;
import com.omniperform.system.domain.DashboardMemberStage;
import com.omniperform.system.service.IDashboardMemberStageService;
import com.omniperform.common.core.text.Convert;

/**
 * 会员阶段分布Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-27
 */
@Service
public class DashboardMemberStageServiceImpl implements IDashboardMemberStageService 
{
    @Autowired
    private DashboardMemberStageMapper dashboardMemberStageMapper;

    /**
     * 查询会员阶段分布
     * 
     * @param id 会员阶段分布主键
     * @return 会员阶段分布
     */
    @Override
    public DashboardMemberStage selectDashboardMemberStageById(Long id)
    {
        return dashboardMemberStageMapper.selectDashboardMemberStageById(id);
    }

    /**
     * 根据数据月份查询会员阶段分布
     * 
     * @param dataMonth 数据月份
     * @return 会员阶段分布
     */
    @Override
    public DashboardMemberStage selectByDataMonth(String dataMonth)
    {
        return dashboardMemberStageMapper.selectByDataMonth(dataMonth);
    }

    /**
     * 查询会员阶段分布列表
     * 
     * @param dashboardMemberStage 会员阶段分布
     * @return 会员阶段分布
     */
    @Override
    public List<DashboardMemberStage> selectDashboardMemberStageList(DashboardMemberStage dashboardMemberStage)
    {
        return dashboardMemberStageMapper.selectDashboardMemberStageList(dashboardMemberStage);
    }

    /**
     * 新增会员阶段分布
     * 
     * @param dashboardMemberStage 会员阶段分布
     * @return 结果
     */
    @Override
    public int insertDashboardMemberStage(DashboardMemberStage dashboardMemberStage)
    {
        return dashboardMemberStageMapper.insertDashboardMemberStage(dashboardMemberStage);
    }

    /**
     * 修改会员阶段分布
     * 
     * @param dashboardMemberStage 会员阶段分布
     * @return 结果
     */
    @Override
    public int updateDashboardMemberStage(DashboardMemberStage dashboardMemberStage)
    {
        return dashboardMemberStageMapper.updateDashboardMemberStage(dashboardMemberStage);
    }

    /**
     * 批量删除会员阶段分布
     * 
     * @param ids 需要删除的会员阶段分布主键
     * @return 结果
     */
    @Override
    public int deleteDashboardMemberStageByIds(String ids)
    {
        return dashboardMemberStageMapper.deleteDashboardMemberStageByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除会员阶段分布信息
     * 
     * @param id 会员阶段分布主键
     * @return 结果
     */
    @Override
    public int deleteDashboardMemberStageById(Long id)
    {
        return dashboardMemberStageMapper.deleteDashboardMemberStageById(id);
    }

    /**
     * 查询指定月份范围的会员阶段分布
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员阶段分布集合
     */
    @Override
    public List<DashboardMemberStage> selectByMonthRange(String startMonth, String endMonth)
    {
        return dashboardMemberStageMapper.selectByMonthRange(startMonth, endMonth);
    }
}