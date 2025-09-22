package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.MemberCrfmeDistribution;

/**
 * 会员价值分层(CRFM-E)分布Service接口
 * 
 * @author omniperform
 * @date 2025-01-27
 */
public interface IMemberCrfmeDistributionService 
{
    /**
     * 查询会员价值分层分布
     * 
     * @param id 会员价值分层分布主键
     * @return 会员价值分层分布
     */
    public MemberCrfmeDistribution selectMemberCrfmeDistributionById(Long id);

    /**
     * 根据数据月份查询会员价值分层分布
     * 
     * @param dataMonth 数据月份
     * @return 会员价值分层分布集合
     */
    public List<MemberCrfmeDistribution> selectByDataMonth(String dataMonth);

    /**
     * 查询会员价值分层分布列表
     * 
     * @param memberCrfmeDistribution 会员价值分层分布
     * @return 会员价值分层分布集合
     */
    public List<MemberCrfmeDistribution> selectMemberCrfmeDistributionList(MemberCrfmeDistribution memberCrfmeDistribution);

    /**
     * 新增会员价值分层分布
     * 
     * @param memberCrfmeDistribution 会员价值分层分布
     * @return 结果
     */
    public int insertMemberCrfmeDistribution(MemberCrfmeDistribution memberCrfmeDistribution);

    /**
     * 修改会员价值分层分布
     * 
     * @param memberCrfmeDistribution 会员价值分层分布
     * @return 结果
     */
    public int updateMemberCrfmeDistribution(MemberCrfmeDistribution memberCrfmeDistribution);

    /**
     * 批量删除会员价值分层分布
     * 
     * @param ids 需要删除的会员价值分层分布主键集合
     * @return 结果
     */
    public int deleteMemberCrfmeDistributionByIds(String ids);

    /**
     * 删除会员价值分层分布信息
     * 
     * @param id 会员价值分层分布主键
     * @return 结果
     */
    public int deleteMemberCrfmeDistributionById(Long id);

    /**
     * 查询指定月份范围的会员价值分层分布
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员价值分层分布集合
     */
    public List<MemberCrfmeDistribution> selectByMonthRange(String startMonth, String endMonth);
}