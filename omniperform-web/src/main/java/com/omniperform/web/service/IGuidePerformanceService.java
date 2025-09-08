package com.omniperform.web.service;

import java.util.List;
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
}