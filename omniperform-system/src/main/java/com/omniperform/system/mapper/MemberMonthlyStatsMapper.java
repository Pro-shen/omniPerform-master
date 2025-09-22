package com.omniperform.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.MemberMonthlyStats;

/**
 * 会员月度统计Mapper接口
 * 
 * @author omniperform
 */
public interface MemberMonthlyStatsMapper 
{
    /**
     * 查询会员月度统计
     * 
     * @param statsId 会员月度统计主键
     * @return 会员月度统计
     */
    public MemberMonthlyStats selectMemberMonthlyStatsByStatsId(Long statsId);

    /**
     * 根据月份查询会员月度统计
     * 
     * @param month 统计月份
     * @return 会员月度统计
     */
    public MemberMonthlyStats selectMemberMonthlyStatsByMonth(String month);

    /**
     * 根据日期范围查询会员月度统计列表
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 会员月度统计集合
     */
    public List<MemberMonthlyStats> selectMemberMonthlyStatsByDateRange(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth);

    /**
     * 查询会员月度统计列表
     * 
     * @param memberMonthlyStats 会员月度统计
     * @return 会员月度统计集合
     */
    public List<MemberMonthlyStats> selectMemberMonthlyStatsList(MemberMonthlyStats memberMonthlyStats);

    /**
     * 新增会员月度统计
     * 
     * @param memberMonthlyStats 会员月度统计
     * @return 结果
     */
    public int insertMemberMonthlyStats(MemberMonthlyStats memberMonthlyStats);

    /**
     * 修改会员月度统计
     * 
     * @param memberMonthlyStats 会员月度统计
     * @return 结果
     */
    public int updateMemberMonthlyStats(MemberMonthlyStats memberMonthlyStats);

    /**
     * 删除会员月度统计
     * 
     * @param statsId 会员月度统计主键
     * @return 结果
     */
    public int deleteMemberMonthlyStatsByStatsId(Long statsId);

    /**
     * 批量删除会员月度统计
     * 
     * @param statsIds 需要删除的会员月度统计主键集合
     * @return 结果
     */
    public int deleteMemberMonthlyStatsByStatsIds(Long[] statsIds);
}