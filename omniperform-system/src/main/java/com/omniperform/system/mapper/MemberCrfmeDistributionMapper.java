package com.omniperform.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.MemberCrfmeDistribution;

/**
 * 会员价值分层(CRFM-E)分布数据 数据层
 * 
 * @author omniperform
 */
public interface MemberCrfmeDistributionMapper
{
    public MemberCrfmeDistribution selectMemberCrfmeDistribution(MemberCrfmeDistribution distribution);
    public MemberCrfmeDistribution selectMemberCrfmeDistributionById(Long id);
    public List<MemberCrfmeDistribution> selectMemberCrfmeDistributionList(MemberCrfmeDistribution distribution);
    public List<MemberCrfmeDistribution> selectByDataMonth(String dataMonth);
    public List<MemberCrfmeDistribution> selectByMonthRange(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth);
    public int insertMemberCrfmeDistribution(MemberCrfmeDistribution distribution);
    public int updateMemberCrfmeDistribution(MemberCrfmeDistribution distribution);
    public int deleteMemberCrfmeDistributionById(Long id);
    public int deleteMemberCrfmeDistributionByIds(String[] ids);
}