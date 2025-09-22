package com.omniperform.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.MemberInfoMapper;
import com.omniperform.system.mapper.MemberMonthlyStatsMapper;
import com.omniperform.system.mapper.MemberStageStatsMapper;
import com.omniperform.system.domain.MemberInfo;
import com.omniperform.system.domain.MemberMonthlyStats;
import com.omniperform.system.domain.MemberStageStats;
import com.omniperform.system.service.IMemberOverviewService;
import com.omniperform.common.utils.DateUtils;

/**
 * 会员概览Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class MemberOverviewServiceImpl implements IMemberOverviewService 
{
    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Autowired
    private MemberMonthlyStatsMapper memberMonthlyStatsMapper;

    @Autowired
    private MemberStageStatsMapper memberStageStatsMapper;

    /**
     * 查询会员概览统计数据
     * 
     * @param month 月份 (格式: YYYY-MM)
     * @return 会员概览统计数据
     */
    @Override
    public Map<String, Object> getMemberOverview(String month)
    {
        Map<String, Object> result = new HashMap<>();
        
        // 查询月度统计数据
        MemberMonthlyStats monthlyStats = memberMonthlyStatsMapper.selectMemberMonthlyStatsByMonth(month);
        if (monthlyStats != null)
        {
            result.put("totalMembers", monthlyStats.getTotalMembers());
            result.put("newMembers", monthlyStats.getNewMembers());
            result.put("activeMembers", monthlyStats.getActiveMembers());
            result.put("purchaseMembers", monthlyStats.getPurchaseMembers());
            result.put("churnMembers", monthlyStats.getChurnMembers());
            result.put("totalPurchaseAmount", monthlyStats.getTotalPurchaseAmount());
            result.put("avgOrderValue", monthlyStats.getAvgOrderValue());
            result.put("activeRate", monthlyStats.getActiveRate());
            result.put("purchaseRate", monthlyStats.getPurchaseRate());
            result.put("churnRate", monthlyStats.getChurnRate());
        }
        else
        {
            // 如果没有统计数据，返回默认值
            result.put("totalMembers", 0);
            result.put("newMembers", 0);
            result.put("activeMembers", 0);
            result.put("purchaseMembers", 0);
            result.put("churnMembers", 0);
            result.put("totalPurchaseAmount", BigDecimal.ZERO);
            result.put("avgOrderValue", BigDecimal.ZERO);
            result.put("activeRate", BigDecimal.ZERO);
            result.put("purchaseRate", BigDecimal.ZERO);
            result.put("churnRate", BigDecimal.ZERO);
        }
        
        return result;
    }

    /**
     * 查询会员增长趋势数据
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员增长趋势数据
     */
    @Override
    public List<Map<String, Object>> getMemberGrowthTrend(String startMonth, String endMonth)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询指定时间范围内的月度统计数据
        List<MemberMonthlyStats> statsList = memberMonthlyStatsMapper.selectMemberMonthlyStatsByDateRange(startMonth, endMonth);
        
        for (MemberMonthlyStats stats : statsList)
        {
            Map<String, Object> item = new HashMap<>();
            item.put("month", stats.getStatsMonth());
            item.put("totalMembers", stats.getTotalMembers());
            item.put("newMembers", stats.getNewMembers());
            item.put("activeMembers", stats.getActiveMembers());
            item.put("purchaseMembers", stats.getPurchaseMembers());
            item.put("churnMembers", stats.getChurnMembers());
            result.add(item);
        }
        
        return result;
    }

    /**
     * 查询会员阶段分布数据
     * 
     * @param month 月份
     * @return 会员阶段分布数据
     */
    @Override
    public List<Map<String, Object>> getMemberStageDistribution(String month)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询指定月份的阶段统计数据
        List<MemberStageStats> stageStatsList = memberStageStatsMapper.selectMemberStageStatsByMonth(month);
        
        for (MemberStageStats stageStats : stageStatsList)
        {
            Map<String, Object> item = new HashMap<>();
            item.put("stage", stageStats.getBabyStage());
            item.put("memberCount", stageStats.getMemberCount());
            item.put("newMemberCount", stageStats.getNewMemberCount());
            item.put("activeMemberCount", stageStats.getActiveMemberCount());
            item.put("purchaseMemberCount", stageStats.getPurchaseMemberCount());
            item.put("totalPurchaseAmount", stageStats.getTotalPurchaseAmount());
            item.put("avgOrderValue", stageStats.getAvgOrderValue());
            item.put("stageRatio", stageStats.getStageRatio());
            result.add(item);
        }
        
        return result;
    }

    /**
     * 查询会员月度统计数据
     * 
     * @param month 月份
     * @return 会员月度统计数据
     */
    @Override
    public MemberMonthlyStats getMemberMonthlyStats(String month)
    {
        return memberMonthlyStatsMapper.selectMemberMonthlyStatsByMonth(month);
    }

    /**
     * 查询会员月度统计数据列表
     * 
     * @param memberMonthlyStats 会员月度统计
     * @return 会员月度统计数据集合
     */
    @Override
    public List<MemberMonthlyStats> selectMemberMonthlyStatsList(MemberMonthlyStats memberMonthlyStats)
    {
        return memberMonthlyStatsMapper.selectMemberMonthlyStatsList(memberMonthlyStats);
    }

    /**
     * 查询会员阶段统计数据列表
     * 
     * @param memberStageStats 会员阶段统计
     * @return 会员阶段统计数据集合
     */
    @Override
    public List<MemberStageStats> selectMemberStageStatsList(MemberStageStats memberStageStats)
    {
        return memberStageStatsMapper.selectMemberStageStatsList(memberStageStats);
    }

    /**
     * 根据手机号查询会员信息
     * 
     * @param phone 手机号
     * @return 会员信息
     */
    @Override
    public MemberInfo getMemberByPhone(String phone)
    {
        return memberInfoMapper.selectMemberInfoByPhone(phone);
    }

    /**
     * 查询会员信息列表
     * 
     * @param memberInfo 会员信息
     * @return 会员信息集合
     */
    @Override
    public List<MemberInfo> selectMemberInfoList(MemberInfo memberInfo)
    {
        return memberInfoMapper.selectMemberInfoList(memberInfo);
    }

    /**
     * 新增会员信息
     * 
     * @param memberInfo 会员信息
     * @return 结果
     */
    @Override
    public int insertMemberInfo(MemberInfo memberInfo)
    {
        memberInfo.setCreateTime(DateUtils.getNowDate());
        return memberInfoMapper.insertMemberInfo(memberInfo);
    }

    /**
     * 修改会员信息
     * 
     * @param memberInfo 会员信息
     * @return 结果
     */
    @Override
    public int updateMemberInfo(MemberInfo memberInfo)
    {
        memberInfo.setUpdateTime(DateUtils.getNowDate());
        return memberInfoMapper.updateMemberInfo(memberInfo);
    }

    /**
     * 批量删除会员信息
     * 
     * @param memberIds 需要删除的会员信息主键
     * @return 结果
     */
    @Override
    public int deleteMemberInfoByMemberIds(Long[] memberIds)
    {
        return memberInfoMapper.deleteMemberInfoByMemberIds(memberIds);
    }

    /**
     * 删除会员信息信息
     * 
     * @param memberId 会员信息主键
     * @return 结果
     */
    @Override
    public int deleteMemberInfoByMemberId(Long memberId)
    {
        return memberInfoMapper.deleteMemberInfoByMemberId(memberId);
    }

    /**
     * 计算会员CRFM-E评分
     * 
     * @param memberId 会员ID
     * @return CRFM-E评分数据
     */
    @Override
    public Map<String, Object> calculateMemberCrfme(Long memberId)
    {
        Map<String, Object> result = new HashMap<>();
        
        // 查询会员信息
        MemberInfo memberInfo = memberInfoMapper.selectMemberInfoByMemberId(memberId);
        if (memberInfo != null)
        {
            // 计算CRFM-E评分（这里使用简化的计算逻辑）
            Map<String, Object> crfmeScore = memberInfoMapper.calculateMemberCrfme(memberId);
            if (crfmeScore != null)
            {
                result.putAll(crfmeScore);
            }
            else
            {
                // 默认评分
                result.put("consumptionScore", 60);
                result.put("frequencyScore", 60);
                result.put("monetaryScore", 60);
                result.put("engagementScore", 60);
                result.put("totalScore", 240);
                result.put("level", "普通会员");
            }
            
            result.put("memberInfo", memberInfo);
        }
        
        return result;
    }
}