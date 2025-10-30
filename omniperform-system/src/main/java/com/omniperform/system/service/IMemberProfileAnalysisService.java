package com.omniperform.system.service;

import java.util.List;
import java.util.Date;
import java.util.Map;
import com.omniperform.system.domain.MemberProfileAnalysis;

/**
 * 会员画像分析Service接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface IMemberProfileAnalysisService 
{
    /**
     * 查询会员画像分析
     * 
     * @param id 会员画像分析主键
     * @return 会员画像分析
     */
    public MemberProfileAnalysis selectMemberProfileAnalysisById(Long id);

    /**
     * 查询会员画像分析列表
     * 
     * @param memberProfileAnalysis 会员画像分析
     * @return 会员画像分析集合
     */
    public List<MemberProfileAnalysis> selectMemberProfileAnalysisList(MemberProfileAnalysis memberProfileAnalysis);

    /**
     * 根据分析日期和画像类型查询
     * 
     * @param analysisDate 分析日期
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像分析集合
     */
    public List<MemberProfileAnalysis> selectMemberProfileAnalysisByDateAndType(Date analysisDate, String profileType, String regionCode);

    /**
     * 查询最新的会员画像分析
     * 
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像分析集合
     */
    public List<MemberProfileAnalysis> selectLatestMemberProfileAnalysis(String profileType, String regionCode);

    /**
     * 查询指定日期范围内的会员画像分析
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像分析集合
     */
    public List<MemberProfileAnalysis> selectMemberProfileAnalysisByDateRange(Date startDate, Date endDate, String profileType, String regionCode);

    /**
     * 获取会员画像数据（前端接口）
     * 
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像数据
     */
    public Map<String, Object> getMemberProfileData(String profileType, String regionCode);

    /**
     * 获取会员画像数据（前端接口，支持月份参数）
     * 
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @param monthYear 月份（格式：YYYY-MM）
     * @return 会员画像数据
     */
    public Map<String, Object> getMemberProfileData(String profileType, String regionCode, String monthYear);

    /**
     * 获取会员画像趋势数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 趋势数据
     */
    public List<Map<String, Object>> getMemberProfileTrend(Date startDate, Date endDate, String profileType, String regionCode);

    /**
     * 统计各画像类型的会员数量
     * 
     * @param analysisDate 分析日期
     * @param regionCode 区域代码
     * @return 统计结果
     */
    public List<Map<String, Object>> countMembersByProfileType(Date analysisDate, String regionCode);

    /**
     * 统计各区域的会员画像分布
     * 
     * @param analysisDate 分析日期
     * @param profileType 画像类型
     * @return 统计结果
     */
    public List<Map<String, Object>> countMembersByRegion(Date analysisDate, String profileType);

    /**
     * 新增会员画像分析
     * 
     * @param memberProfileAnalysis 会员画像分析
     * @return 结果
     */
    public int insertMemberProfileAnalysis(MemberProfileAnalysis memberProfileAnalysis);

    /**
     * 修改会员画像分析
     * 
     * @param memberProfileAnalysis 会员画像分析
     * @return 结果
     */
    public int updateMemberProfileAnalysis(MemberProfileAnalysis memberProfileAnalysis);

    /**
     * 批量删除会员画像分析
     * 
     * @param ids 需要删除的会员画像分析主键集合
     * @return 结果
     */
    public int deleteMemberProfileAnalysisByIds(Long[] ids);

    /**
     * 删除会员画像分析信息
     * 
     * @param id 会员画像分析主键
     * @return 结果
     */
    public int deleteMemberProfileAnalysisById(Long id);
}