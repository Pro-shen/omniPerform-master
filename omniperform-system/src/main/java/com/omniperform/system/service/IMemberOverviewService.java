package com.omniperform.system.service;

import java.util.List;
import java.util.Map;
import com.omniperform.system.domain.MemberInfo;
import com.omniperform.system.domain.MemberMonthlyStats;
import com.omniperform.system.domain.MemberStageStats;

/**
 * 会员概览Service接口
 * 
 * @author omniperform
 */
public interface IMemberOverviewService 
{
    /**
     * 查询会员概览统计数据
     * 
     * @param month 月份 (格式: YYYY-MM)
     * @return 会员概览统计数据
     */
    public Map<String, Object> getMemberOverview(String month);

    /**
     * 查询会员增长趋势数据
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员增长趋势数据
     */
    public List<Map<String, Object>> getMemberGrowthTrend(String startMonth, String endMonth);

    /**
     * 查询会员阶段分布数据
     * 
     * @param month 月份
     * @return 会员阶段分布数据
     */
    public List<Map<String, Object>> getMemberStageDistribution(String month);

    /**
     * 查询会员月度统计数据
     * 
     * @param month 月份
     * @return 会员月度统计数据
     */
    public MemberMonthlyStats getMemberMonthlyStats(String month);

    /**
     * 查询会员月度统计数据列表
     * 
     * @param memberMonthlyStats 会员月度统计
     * @return 会员月度统计数据集合
     */
    public List<MemberMonthlyStats> selectMemberMonthlyStatsList(MemberMonthlyStats memberMonthlyStats);

    /**
     * 查询会员阶段统计数据列表
     * 
     * @param memberStageStats 会员阶段统计
     * @return 会员阶段统计数据集合
     */
    public List<MemberStageStats> selectMemberStageStatsList(MemberStageStats memberStageStats);

    /**
     * 根据手机号查询会员信息
     * 
     * @param phone 手机号
     * @return 会员信息
     */
    public MemberInfo getMemberByPhone(String phone);

    /**
     * 查询会员信息列表
     * 
     * @param memberInfo 会员信息
     * @return 会员信息集合
     */
    public List<MemberInfo> selectMemberInfoList(MemberInfo memberInfo);

    /**
     * 新增会员信息
     * 
     * @param memberInfo 会员信息
     * @return 结果
     */
    public int insertMemberInfo(MemberInfo memberInfo);

    /**
     * 修改会员信息
     * 
     * @param memberInfo 会员信息
     * @return 结果
     */
    public int updateMemberInfo(MemberInfo memberInfo);

    /**
     * 批量删除会员信息
     * 
     * @param memberIds 需要删除的会员信息主键集合
     * @return 结果
     */
    public int deleteMemberInfoByMemberIds(Long[] memberIds);

    /**
     * 删除会员信息信息
     * 
     * @param memberId 会员信息主键
     * @return 结果
     */
    public int deleteMemberInfoByMemberId(Long memberId);

    /**
     * 计算会员CRFM-E评分
     * 
     * @param memberId 会员ID
     * @return CRFM-E评分数据
     */
    public Map<String, Object> calculateMemberCrfme(Long memberId);

    /**
     * 导入会员数据
     * 
     * @param memberList 会员数据列表
     * @param isUpdateSupport 是否更新支持
     * @param operName 操作用户
     * @return 导入结果
     */
    public Map<String, Object> importMemberInfo(List<MemberInfo> memberList, Boolean isUpdateSupport, String operName);
}