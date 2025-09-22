package com.omniperform.web.mapper;

import java.util.List;
import java.util.Map;
import com.omniperform.web.domain.GuidePerformance;
import org.apache.ibatis.annotations.Param;

/**
 * 导购绩效Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface GuidePerformanceMapper 
{
    /**
     * 查询导购绩效
     * 
     * @param performanceId 导购绩效主键
     * @return 导购绩效
     */
    public GuidePerformance selectGuidePerformanceByPerformanceId(Long performanceId);

    /**
     * 查询导购绩效列表
     * 
     * @param guidePerformance 导购绩效
     * @return 导购绩效集合
     */
    public List<GuidePerformance> selectGuidePerformanceList(GuidePerformance guidePerformance);

    /**
     * 根据月份查询导购绩效排行榜
     * 
     * @param dataMonth 数据月份
     * @param limit 限制条数
     * @return 导购绩效排行榜
     */
    public List<GuidePerformance> selectGuidePerformanceRanking(@Param("dataMonth") String dataMonth, @Param("limit") Integer limit);

    /**
     * 根据导购ID和月份查询绩效
     * 
     * @param guideId 导购ID
     * @param dataMonth 数据月份
     * @return 导购绩效
     */
    public GuidePerformance selectGuidePerformanceByGuideIdAndMonth(@Param("guideId") Long guideId, @Param("dataMonth") String dataMonth);

    /**
     * 根据区域查询导购绩效排行
     * 
     * @param regionCode 区域代码
     * @param dataMonth 数据月份
     * @param limit 限制条数
     * @return 导购绩效排行
     */
    public List<GuidePerformance> selectGuidePerformanceByRegion(@Param("regionCode") String regionCode, @Param("dataMonth") String dataMonth, @Param("limit") Integer limit);

    /**
     * 新增导购绩效
     * 
     * @param guidePerformance 导购绩效
     * @return 结果
     */
    public int insertGuidePerformance(GuidePerformance guidePerformance);

    /**
     * 修改导购绩效
     * 
     * @param guidePerformance 导购绩效
     * @return 结果
     */
    public int updateGuidePerformance(GuidePerformance guidePerformance);

    /**
     * 删除导购绩效
     * 
     * @param performanceId 导购绩效主键
     * @return 结果
     */
    public int deleteGuidePerformanceByPerformanceId(Long performanceId);

    /**
     * 批量删除导购绩效
     * 
     * @param performanceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGuidePerformanceByPerformanceIds(Long[] performanceIds);

    /**
     * 统计指定月份的导购总数
     * 
     * @param dataMonth 数据月份
     * @return 导购总数
     */
    public int countGuidesByMonth(@Param("dataMonth") String dataMonth);

    /**
     * 统计指定月份的平均绩效分数
     * 
     * @param dataMonth 数据月份
     * @return 平均绩效分数
     */
    public Double getAveragePerformanceScore(@Param("dataMonth") String dataMonth);

    /**
     * 查询九宫格分布统计
     * 
     * @param dataMonth 数据月份
     * @return 九宫格分布统计结果
     */
    public List<Map<String, Object>> selectMatrixDistribution(@Param("dataMonth") String dataMonth);

    /**
     * 根据九宫格位置查询导购列表
     * 
     * @param matrixPosition 九宫格位置
     * @param matrixType 九宫格类型
     * @param dataMonth 数据月份
     * @return 导购绩效列表
     */
    public List<GuidePerformance> selectGuidesByMatrixPosition(@Param("matrixPosition") String matrixPosition, 
                                                               @Param("matrixType") String matrixType, 
                                                               @Param("dataMonth") String dataMonth);

    /**
     * 查询CAI和RMV分数分布
     * 
     * @param dataMonth 数据月份
     * @return CAI和RMV分数分布
     */
    public List<Map<String, Object>> selectCaiRmvDistribution(@Param("dataMonth") String dataMonth);

    /**
     * 更新导购的九宫格位置和类型
     * 
     * @param performanceId 绩效记录ID
     * @param matrixPosition 九宫格位置
     * @param matrixType 九宫格类型
     * @return 更新结果
     */
    public int updateMatrixPosition(@Param("performanceId") Long performanceId, 
                                   @Param("matrixPosition") String matrixPosition, 
                                   @Param("matrixType") String matrixType);

    /**
     * 批量更新导购的九宫格位置和类型
     * 
     * @param guidePerformances 导购绩效列表
     * @return 结果
     */
    public int batchUpdateMatrixPosition(@Param("list") List<GuidePerformance> guidePerformances);

    /**
     * 统计活跃导购数量
     * 
     * @param dataMonth 数据月份
     * @return 活跃导购数量
     */
    public int countActiveGuides(@Param("dataMonth") String dataMonth);

    /**
     * 统计高绩效导购数量（绩效分数>=80）
     * 
     * @param dataMonth 数据月份
     * @return 高绩效导购数量
     */
    public int countHighPerformanceGuides(@Param("dataMonth") String dataMonth);

    /**
     * 查询绩效趋势数据（按月份统计各等级导购数量）
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 绩效趋势数据
     */
    public List<Map<String, Object>> selectPerformanceTrend(@Param("startMonth") String startMonth, 
                                                            @Param("endMonth") String endMonth);

    /**
     * 查询绩效统计数据（总体、区域、等级分布）
     * 
     * @param dataMonth 数据月份
     * @return 绩效统计数据
     */
    public Map<String, Object> selectPerformanceStatistics(@Param("dataMonth") String dataMonth);

    /**
     * 查询区域绩效统计
     * 
     * @param dataMonth 数据月份
     * @return 区域绩效统计
     */
    public List<Map<String, Object>> selectRegionPerformanceStats(@Param("dataMonth") String dataMonth);

    /**
     * 查询等级分布统计
     * 
     * @param dataMonth 数据月份
     * @return 等级分布统计
     */
    public List<Map<String, Object>> selectGradeDistribution(@Param("dataMonth") String dataMonth);
}