package com.omniperform.system.mapper;

import java.util.List;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.MemberProfileAnalysis;

/**
 * 会员画像分析Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface MemberProfileAnalysisMapper 
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
    public List<MemberProfileAnalysis> selectMemberProfileAnalysisByDateAndType(@Param("analysisDate") Date analysisDate, @Param("profileType") String profileType, @Param("regionCode") String regionCode);

    /**
     * 查询最新的会员画像分析
     * 
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像分析集合
     */
    public List<MemberProfileAnalysis> selectLatestMemberProfileAnalysis(@Param("profileType") String profileType, @Param("regionCode") String regionCode);

    /**
     * 查询指定日期范围内的会员画像分析
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像分析集合
     */
    public List<MemberProfileAnalysis> selectMemberProfileAnalysisByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("profileType") String profileType, @Param("regionCode") String regionCode);

    /**
     * 统计各画像类型的会员数量
     * 
     * @param analysisDate 分析日期
     * @param regionCode 区域代码
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countMembersByProfileType(@Param("analysisDate") Date analysisDate, @Param("regionCode") String regionCode);

    /**
     * 统计各区域的会员画像分布
     * 
     * @param analysisDate 分析日期
     * @param profileType 画像类型
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countMembersByRegion(@Param("analysisDate") Date analysisDate, @Param("profileType") String profileType);

    /**
     * 根据月份查询会员画像分析
     * 
     * @param monthYear 月份（格式：YYYY-MM）
     * @param profileType 画像类型
     * @param regionCode 区域代码
     * @return 会员画像分析集合
     */
    public List<MemberProfileAnalysis> selectMemberProfileAnalysisByMonth(@Param("monthYear") String monthYear, @Param("profileType") String profileType, @Param("regionCode") String regionCode);

    /**
     * 根据月份统计各画像类型的会员数量
     * 
     * @param monthYear 月份（格式：YYYY-MM）
     * @param regionCode 区域代码
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countMembersByProfileTypeByMonth(@Param("monthYear") String monthYear, @Param("regionCode") String regionCode);

    /**
     * 根据月份统计各区域的会员画像分布
     * 
     * @param monthYear 月份（格式：YYYY-MM）
     * @param profileType 画像类型
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countMembersByRegionByMonth(@Param("monthYear") String monthYear, @Param("profileType") String profileType);

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
     * 删除会员画像分析
     * 
     * @param id 会员画像分析主键
     * @return 结果
     */
    public int deleteMemberProfileAnalysisById(Long id);

    /**
     * 批量删除会员画像分析
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberProfileAnalysisByIds(Long[] ids);
}