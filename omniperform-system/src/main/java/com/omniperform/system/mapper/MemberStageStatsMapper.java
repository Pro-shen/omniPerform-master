package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.MemberStageStats;

/**
 * 会员阶段统计Mapper接口
 * 
 * @author omniperform
 */
public interface MemberStageStatsMapper 
{
    /**
     * 查询会员阶段统计
     * 
     * @param statsId 会员阶段统计主键
     * @return 会员阶段统计
     */
    public MemberStageStats selectMemberStageStatsByStatsId(Long statsId);

    /**
     * 根据月份查询会员阶段统计列表
     * 
     * @param month 统计月份
     * @return 会员阶段统计集合
     */
    public List<MemberStageStats> selectMemberStageStatsByMonth(String month);

    /**
     * 查询会员阶段统计列表
     * 
     * @param memberStageStats 会员阶段统计
     * @return 会员阶段统计集合
     */
    public List<MemberStageStats> selectMemberStageStatsList(MemberStageStats memberStageStats);

    /**
     * 新增会员阶段统计
     * 
     * @param memberStageStats 会员阶段统计
     * @return 结果
     */
    public int insertMemberStageStats(MemberStageStats memberStageStats);

    /**
     * 修改会员阶段统计
     * 
     * @param memberStageStats 会员阶段统计
     * @return 结果
     */
    public int updateMemberStageStats(MemberStageStats memberStageStats);

    /**
     * 删除会员阶段统计
     * 
     * @param statsId 会员阶段统计主键
     * @return 结果
     */
    public int deleteMemberStageStatsByStatsId(Long statsId);

    /**
     * 批量删除会员阶段统计
     * 
     * @param statsIds 需要删除的会员阶段统计主键集合
     * @return 结果
     */
    public int deleteMemberStageStatsByStatsIds(Long[] statsIds);
}