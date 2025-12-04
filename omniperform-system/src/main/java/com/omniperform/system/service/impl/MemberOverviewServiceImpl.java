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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 会员概览Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class MemberOverviewServiceImpl implements IMemberOverviewService 
{
    private static final Logger log = LoggerFactory.getLogger(MemberOverviewServiceImpl.class);

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

    /**
     * 导入会员数据
     * 
     * @param memberList 会员数据列表
     * @param isUpdateSupport 是否更新支持
     * @param operName 操作用户
     * @return 导入结果
     */
    @Override
    public Map<String, Object> importMemberInfo(List<MemberInfo> memberList, Boolean isUpdateSupport, String operName)
    {
        if (memberList == null || memberList.isEmpty())
        {
            throw new RuntimeException("导入会员数据不能为空！");
        }
        
        log.info("开始导入会员数据，总条数: {}, 更新支持: {}", memberList.size(), isUpdateSupport);
        
        int successNum = 0;
        int failureNum = 0;
        int totalCount = memberList.size();
        List<String> errorMessages = new ArrayList<>();
        
        for (int i = 0; i < memberList.size(); i++)
        {
            MemberInfo memberInfo = memberList.get(i);
            try
            {
                log.info("处理第 {} 条数据: Phone={}, ID={}", i+1, memberInfo.getPhone(), memberInfo.getMemberId());
                
                // 验证必填字段
                if (memberInfo.getMemberName() == null || memberInfo.getMemberName().trim().isEmpty())
                {
                    failureNum++;
                    errorMessages.add("第" + (i + 1) + "行：会员姓名不能为空");
                    continue;
                }
                if (memberInfo.getPhone() == null || memberInfo.getPhone().trim().isEmpty())
                {
                    failureNum++;
                    errorMessages.add("第" + (i + 1) + "行：手机号不能为空");
                    continue;
                }
                
                // 检查手机号是否已存在
                MemberInfo existingMember = memberInfoMapper.selectMemberInfoByPhone(memberInfo.getPhone());
                if (existingMember != null)
                {
                    log.info("手机号 {} 已存在，现有ID: {}", memberInfo.getPhone(), existingMember.getMemberId());
                    
                    if (isUpdateSupport != null && isUpdateSupport)
                    {
                        // 如果Excel中指定了新的ID，且与现有ID不同，则需要删除原记录并重新插入
                        // 这是一个"替换"操作，以支持修改会员ID的需求
                        if (memberInfo.getMemberId() != null && !memberInfo.getMemberId().equals(existingMember.getMemberId())) {
                            log.info("检测到ID变更: 旧ID={} -> 新ID={}", existingMember.getMemberId(), memberInfo.getMemberId());
                            
                            // 1. 直接更新ID (避免删除操作可能导致的FK问题)
                            int updateRows = memberInfoMapper.updateMemberId(existingMember.getMemberId(), memberInfo.getMemberId());
                            log.info("更新ID结果: {} 行受影响", updateRows);
                            
                            // 2. 准备更新其他字段
                            // 尝试保留原记录的创建时间
                            if (memberInfo.getCreateTime() == null) {
                                memberInfo.setCreateTime(existingMember.getCreateTime());
                            }
                            // 设置更新时间
                            memberInfo.setUpdateTime(DateUtils.getNowDate());
                            
                            // 3. 更新其他字段
                            memberInfoMapper.updateMemberInfo(memberInfo);
                            log.info("更新会员详细信息完成, ID: {}", memberInfo.getMemberId());
                        } else {
                            log.info("ID未变更或未指定新ID，执行常规更新");
                            // ID相同或未指定ID，执行标准更新
                            // 保持原ID
                            memberInfo.setMemberId(existingMember.getMemberId());
                            memberInfo.setUpdateTime(DateUtils.getNowDate());
                            memberInfoMapper.updateMemberInfo(memberInfo);
                        }
                        successNum++;
                    }
                    else
                    {
                        failureNum++;
                        errorMessages.add("第" + (i + 1) + "行：手机号 " + memberInfo.getPhone() + " 已存在");
                        continue;
                    }
                }
                else
                {
                    log.info("手机号 {} 不存在，执行新增", memberInfo.getPhone());
                    // 新增会员信息
                    memberInfo.setCreateTime(DateUtils.getNowDate());
                    // 如果Excel中指定了memberId，MyBatis将使用它插入；如果未指定(null)，则使用自增ID
                    memberInfoMapper.insertMemberInfo(memberInfo);
                    log.info("新增记录完成, ID: {}", memberInfo.getMemberId());
                    successNum++;
                }
            }
            catch (Exception e)
            {
                log.error("处理第 {} 条数据失败", i+1, e);
                failureNum++;
                errorMessages.add("第" + (i + 1) + "行：导入失败 - " + e.getMessage());
            }
        }
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successNum);
        result.put("failCount", failureNum);
        result.put("totalCount", totalCount);
        result.put("errorMessages", errorMessages);
        
        return result;
    }
}