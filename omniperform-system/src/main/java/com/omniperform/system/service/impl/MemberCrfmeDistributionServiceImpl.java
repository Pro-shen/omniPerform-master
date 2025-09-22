package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.common.core.text.Convert;
import com.omniperform.system.domain.MemberCrfmeDistribution;
import com.omniperform.system.mapper.MemberCrfmeDistributionMapper;
import com.omniperform.system.service.IMemberCrfmeDistributionService;

/**
 * 会员价值分层(CRFM-E)分布Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-27
 */
@Service
public class MemberCrfmeDistributionServiceImpl implements IMemberCrfmeDistributionService 
{
    @Autowired
    private MemberCrfmeDistributionMapper memberCrfmeDistributionMapper;

    /**
     * 查询会员价值分层分布
     * 
     * @param id 会员价值分层分布主键
     * @return 会员价值分层分布
     */
    @Override
    public MemberCrfmeDistribution selectMemberCrfmeDistributionById(Long id)
    {
        return memberCrfmeDistributionMapper.selectMemberCrfmeDistributionById(id);
    }

    /**
     * 根据数据月份查询会员价值分层分布
     * 
     * @param dataMonth 数据月份
     * @return 会员价值分层分布集合
     */
    @Override
    public List<MemberCrfmeDistribution> selectByDataMonth(String dataMonth)
    {
        return memberCrfmeDistributionMapper.selectByDataMonth(dataMonth);
    }

    /**
     * 查询会员价值分层分布列表
     * 
     * @param memberCrfmeDistribution 会员价值分层分布
     * @return 会员价值分层分布
     */
    @Override
    public List<MemberCrfmeDistribution> selectMemberCrfmeDistributionList(MemberCrfmeDistribution memberCrfmeDistribution)
    {
        return memberCrfmeDistributionMapper.selectMemberCrfmeDistributionList(memberCrfmeDistribution);
    }

    /**
     * 新增会员价值分层分布
     * 
     * @param memberCrfmeDistribution 会员价值分层分布
     * @return 结果
     */
    @Override
    public int insertMemberCrfmeDistribution(MemberCrfmeDistribution memberCrfmeDistribution)
    {
        return memberCrfmeDistributionMapper.insertMemberCrfmeDistribution(memberCrfmeDistribution);
    }

    /**
     * 修改会员价值分层分布
     * 
     * @param memberCrfmeDistribution 会员价值分层分布
     * @return 结果
     */
    @Override
    public int updateMemberCrfmeDistribution(MemberCrfmeDistribution memberCrfmeDistribution)
    {
        return memberCrfmeDistributionMapper.updateMemberCrfmeDistribution(memberCrfmeDistribution);
    }

    /**
     * 批量删除会员价值分层分布
     * 
     * @param ids 需要删除的会员价值分层分布主键
     * @return 结果
     */
    @Override
    public int deleteMemberCrfmeDistributionByIds(String ids)
    {
        return memberCrfmeDistributionMapper.deleteMemberCrfmeDistributionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除会员价值分层分布信息
     * 
     * @param id 会员价值分层分布主键
     * @return 结果
     */
    @Override
    public int deleteMemberCrfmeDistributionById(Long id)
    {
        return memberCrfmeDistributionMapper.deleteMemberCrfmeDistributionById(id);
    }

    /**
     * 查询指定月份范围的会员价值分层分布
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员价值分层分布集合
     */
    @Override
    public List<MemberCrfmeDistribution> selectByMonthRange(String startMonth, String endMonth)
    {
        return memberCrfmeDistributionMapper.selectByMonthRange(startMonth, endMonth);
    }
}