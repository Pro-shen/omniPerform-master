package com.omniperform.web.service;

import java.util.List;
import java.util.Map;
import com.omniperform.web.domain.GuidePerformance;

/**
 * 导购绩效Service接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface IGuidePerformanceService 
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
    public List<GuidePerformance> selectGuidePerformanceRanking(String dataMonth, Integer limit);

    /**
     * 根据导购ID和月份查询绩效
     * 
     * @param guideId 导购ID
     * @param dataMonth 数据月份
     * @return 导购绩效
     */
    public GuidePerformance selectGuidePerformanceByGuideIdAndMonth(Long guideId, String dataMonth);

    /**
     * 根据区域查询导购绩效排行
     * 
     * @param regionCode 区域代码
     * @param dataMonth 数据月份
     * @param limit 限制条数
     * @return 导购绩效排行
     */
    public List<GuidePerformance> selectGuidePerformanceByRegion(String regionCode, String dataMonth, Integer limit);

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
     * 批量删除导购绩效
     * 
     * @param performanceIds 需要删除的导购绩效主键集合
     * @return 结果
     */
    public int deleteGuidePerformanceByPerformanceIds(Long[] performanceIds);

    /**
     * 删除导购绩效信息
     * 
     * @param performanceId 导购绩效主键
     * @return 结果
     */
    public int deleteGuidePerformanceByPerformanceId(Long performanceId);

    /**
     * 统计指定月份的导购总数
     * 
     * @param dataMonth 数据月份
     * @return 导购总数
     */
    public int countGuidesByMonth(String dataMonth);

    /**
     * 统计指定月份的平均绩效分数
     * 
     * @param dataMonth 数据月份
     * @return 平均绩效分数
     */
    public Double getAveragePerformanceScore(String dataMonth);

    /**
     * 查询九宫格分布统计
     * 
     * @param dataMonth 数据月份
     * @return 九宫格分布统计结果
     */
    public List<Map<String, Object>> selectMatrixDistribution(String dataMonth);

    /**
     * 根据九宫格位置查询导购列表
     * 
     * @param matrixPosition 九宫格位置
     * @param matrixType 九宫格类型
     * @param dataMonth 数据月份
     * @return 导购绩效列表
     */
    public List<GuidePerformance> selectGuidesByMatrixPosition(String matrixPosition, String matrixType, String dataMonth);

    /**
     * 查询CAI和RMV分数分布
     * 
     * @param dataMonth 数据月份
     * @return CAI和RMV分数分布
     */
    public List<Map<String, Object>> selectCaiRmvDistribution(String dataMonth);

    /**
     * 更新导购的九宫格位置和类型
     * 
     * @param performanceId 绩效ID
     * @param matrixPosition 九宫格位置
     * @param matrixType 九宫格类型
     * @return 结果
     */
    public int updateMatrixPosition(Long performanceId, String matrixPosition, String matrixType);

    /**
     * 批量更新导购的九宫格位置和类型
     * 
     * @param guidePerformances 导购绩效列表
     * @return 结果
     */
    public int batchUpdateMatrixPosition(List<GuidePerformance> guidePerformances);

    /**
     * 计算并更新所有导购的九宫格位置
     * 
     * @param dataMonth 数据月份
     * @return 更新的记录数
     */
    public int calculateAndUpdateMatrixPositions(String dataMonth);

    /**
     * 统计活跃导购数量
     * 
     * @param dataMonth 数据月份
     * @return 活跃导购数量
     */
    public int countActiveGuides(String dataMonth);

    /**
     * 统计高绩效导购数量（绩效分数>=80）
     * 
     * @param dataMonth 数据月份
     * @return 高绩效导购数量
     */
    public int countHighPerformanceGuides(String dataMonth);

    /**
     * 查询绩效趋势数据（按月份统计各等级导购数量）
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 绩效趋势数据
     */
    public List<Map<String, Object>> selectPerformanceTrend(String startMonth, String endMonth);

    /**
     * 查询绩效统计数据（总体、区域、等级分布）
     * 
     * @param dataMonth 数据月份
     * @return 绩效统计数据
     */
    public Map<String, Object> selectPerformanceStatistics(String dataMonth);

    /**
     * 查询区域绩效统计
     * 
     * @param dataMonth 数据月份
     * @return 区域绩效统计
     */
    public List<Map<String, Object>> selectRegionPerformanceStats(String dataMonth);

    /**
     * 查询等级分布统计
     * 
     * @param dataMonth 数据月份
     * @return 等级分布统计
     */
    public List<Map<String, Object>> selectGradeDistribution(String dataMonth);
}