package com.omniperform.web.service.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.web.domain.GuidePerformance;
import com.omniperform.web.mapper.GuidePerformanceMapper;
import com.omniperform.web.service.IGuidePerformanceService;

/**
 * 导购绩效Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-09
 */
@Service
public class GuidePerformanceServiceImpl implements IGuidePerformanceService
{
    @Autowired
    private GuidePerformanceMapper guidePerformanceMapper;

    /**
     * 查询导购绩效
     * 
     * @param performanceId 导购绩效主键
     * @return 导购绩效
     */
    @Override
    public GuidePerformance selectGuidePerformanceByPerformanceId(Long performanceId)
    {
        return guidePerformanceMapper.selectGuidePerformanceByPerformanceId(performanceId);
    }

    /**
     * 查询导购绩效列表
     * 
     * @param guidePerformance 导购绩效
     * @return 导购绩效
     */
    @Override
    public List<GuidePerformance> selectGuidePerformanceList(GuidePerformance guidePerformance)
    {
        return guidePerformanceMapper.selectGuidePerformanceList(guidePerformance);
    }

    /**
     * 新增导购绩效
     * 
     * @param guidePerformance 导购绩效
     * @return 结果
     */
    @Override
    public int insertGuidePerformance(GuidePerformance guidePerformance)
    {
        return guidePerformanceMapper.insertGuidePerformance(guidePerformance);
    }

    /**
     * 修改导购绩效
     * 
     * @param guidePerformance 导购绩效
     * @return 结果
     */
    @Override
    public int updateGuidePerformance(GuidePerformance guidePerformance)
    {
        return guidePerformanceMapper.updateGuidePerformance(guidePerformance);
    }

    /**
     * 批量删除导购绩效
     * 
     * @param performanceIds 需要删除的导购绩效主键
     * @return 结果
     */
    @Override
    public int deleteGuidePerformanceByPerformanceIds(Long[] performanceIds)
    {
        return guidePerformanceMapper.deleteGuidePerformanceByPerformanceIds(performanceIds);
    }

    /**
     * 删除导购绩效信息
     * 
     * @param performanceId 导购绩效主键
     * @return 结果
     */
    @Override
    public int deleteGuidePerformanceByPerformanceId(Long performanceId)
    {
        return guidePerformanceMapper.deleteGuidePerformanceByPerformanceId(performanceId);
    }

    /**
     * 根据月份查询导购绩效排行榜
     * 
     * @param dataMonth 数据月份
     * @param limit 限制条数
     * @return 导购绩效排行榜
     */
    @Override
    public List<GuidePerformance> selectGuidePerformanceRanking(String dataMonth, Integer limit)
    {
        return guidePerformanceMapper.selectGuidePerformanceRanking(dataMonth, limit);
    }

    /**
     * 根据导购ID和月份查询绩效
     * 
     * @param guideId 导购ID
     * @param dataMonth 数据月份
     * @return 导购绩效
     */
    @Override
    public GuidePerformance selectGuidePerformanceByGuideIdAndMonth(Long guideId, String dataMonth)
    {
        return guidePerformanceMapper.selectGuidePerformanceByGuideIdAndMonth(guideId, dataMonth);
    }

    /**
     * 根据区域查询导购绩效排行
     * 
     * @param regionCode 区域代码
     * @param dataMonth 数据月份
     * @param limit 限制条数
     * @return 导购绩效排行
     */
    @Override
    public List<GuidePerformance> selectGuidePerformanceByRegion(String regionCode, String dataMonth, Integer limit)
    {
        return guidePerformanceMapper.selectGuidePerformanceByRegion(regionCode, dataMonth, limit);
    }

    /**
     * 统计指定月份的导购总数
     * 
     * @param dataMonth 数据月份
     * @return 导购总数
     */
    @Override
    public int countGuidesByMonth(String dataMonth)
    {
        return guidePerformanceMapper.countGuidesByMonth(dataMonth);
    }

    /**
     * 统计指定月份的平均绩效分数
     * 
     * @param dataMonth 数据月份
     * @return 平均绩效分数
     */
    @Override
    public Double getAveragePerformanceScore(String dataMonth)
    {
        return guidePerformanceMapper.getAveragePerformanceScore(dataMonth);
    }
}