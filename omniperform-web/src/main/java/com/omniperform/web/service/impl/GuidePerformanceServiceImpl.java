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
    public GuidePerformance selectGuidePerformanceByGuideIdAndMonth(String guideId, String dataMonth)
    {
        return guidePerformanceMapper.selectGuidePerformanceByGuideIdAndMonth(guideId, dataMonth);
    }

    /**
     * 查询所有不重复的数据月份
     * 
     * @return 月份列表
     */
    @Override
    public List<String> selectDistinctDataMonths()
    {
        return guidePerformanceMapper.selectDistinctDataMonths();
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

    /**
     * 查询九宫格分布统计
     * 
     * @param dataMonth 数据月份
     * @return 九宫格分布统计结果
     */
    @Override
    public List<Map<String, Object>> selectMatrixDistribution(String dataMonth)
    {
        return guidePerformanceMapper.selectMatrixDistribution(dataMonth);
    }

    /**
     * 根据九宫格位置查询导购列表
     * 
     * @param matrixPosition 九宫格位置
     * @param matrixType 九宫格类型
     * @param dataMonth 数据月份
     * @return 导购绩效列表
     */
    @Override
    public List<GuidePerformance> selectGuidesByMatrixPosition(String matrixPosition, String matrixType, String dataMonth)
    {
        return guidePerformanceMapper.selectGuidesByMatrixPosition(matrixPosition, matrixType, dataMonth);
    }

    /**
     * 查询CAI和RMV分数分布
     * 
     * @param dataMonth 数据月份
     * @return CAI和RMV分数分布
     */
    @Override
    public List<Map<String, Object>> selectCaiRmvDistribution(String dataMonth)
    {
        return guidePerformanceMapper.selectCaiRmvDistribution(dataMonth);
    }

    /**
     * 更新导购的九宫格位置和类型
     * 
     * @param performanceId 绩效ID
     * @param matrixPosition 九宫格位置
     * @param matrixType 九宫格类型
     * @return 结果
     */
    @Override
    public int updateMatrixPosition(Long performanceId, String matrixPosition, String matrixType)
    {
        return guidePerformanceMapper.updateMatrixPosition(performanceId, matrixPosition, matrixType);
    }

    /**
     * 批量更新导购的九宫格位置和类型
     * 
     * @param guidePerformances 导购绩效列表
     * @return 结果
     */
    @Override
    public int batchUpdateMatrixPosition(List<GuidePerformance> guidePerformances)
    {
        return guidePerformanceMapper.batchUpdateMatrixPosition(guidePerformances);
    }

    /**
     * 计算并更新所有导购的九宫格位置
     * 
     * @param dataMonth 数据月份
     * @return 更新的记录数
     */
    @Override
    public int calculateAndUpdateMatrixPositions(String dataMonth)
    {
        // 获取所有有CAI和RMV分数的导购绩效数据
        List<GuidePerformance> performances = guidePerformanceMapper.selectGuidePerformanceList(new GuidePerformance());
        
        int updateCount = 0;
        for (GuidePerformance performance : performances) {
            if (performance.getCaiScore() != null && performance.getRmvScore() != null 
                && performance.getCaiScore().doubleValue() > 0 && performance.getRmvScore().doubleValue() > 0) {
                
                // 计算九宫格位置和类型
                String[] result = calculateMatrixPositionAndType(performance.getCaiScore().doubleValue(), 
                                                                performance.getRmvScore().doubleValue());
                String matrixPosition = result[0];
                String matrixType = result[1];
                
                // 更新数据库
                int updated = guidePerformanceMapper.updateMatrixPosition(performance.getPerformanceId(), 
                                                                        matrixPosition, matrixType);
                updateCount += updated;
            }
        }
        
        return updateCount;
    }

    /**
     * 根据CAI和RMV分数计算九宫格位置和类型
     * 
     * @param caiScore CAI分数
     * @param rmvScore RMV分数
     * @return [位置, 类型]
     */
    private String[] calculateMatrixPositionAndType(double caiScore, double rmvScore) {
        String position;
        String type;
        
        // 根据分数范围确定位置
        if (caiScore >= 0.8 && rmvScore >= 0.8) {
            position = "3-3";
            type = "超级明星";
        } else if (caiScore >= 0.6 && rmvScore >= 0.8) {
            position = "2-3";
            type = "成长之星";
        } else if (caiScore >= 0.4 && rmvScore >= 0.8) {
            position = "1-3";
            type = "潜力新星";
        } else if (caiScore >= 0.8 && rmvScore >= 0.6) {
            position = "3-2";
            type = "骨干力量";
        } else if (caiScore >= 0.6 && rmvScore >= 0.6) {
            position = "2-2";
            type = "稳定发展";
        } else if (caiScore >= 0.4 && rmvScore >= 0.6) {
            position = "1-2";
            type = "待培养";
        } else if (caiScore >= 0.8 && rmvScore >= 0.4) {
            position = "3-1";
            type = "经验丰富";
        } else if (caiScore >= 0.6 && rmvScore >= 0.4) {
            position = "2-1";
            type = "需要提升";
        } else {
            position = "1-1";
            type = "新手入门";
        }
        
        return new String[]{position, type};
    }

    /**
     * 统计活跃导购数量
     * 
     * @param dataMonth 数据月份
     * @return 活跃导购数量
     */
    @Override
    public int countActiveGuides(String dataMonth)
    {
        return guidePerformanceMapper.countActiveGuides(dataMonth);
    }

    /**
     * 统计高绩效导购数量（绩效分数>=80）
     * 
     * @param dataMonth 数据月份
     * @return 高绩效导购数量
     */
    @Override
    public int countHighPerformanceGuides(String dataMonth)
    {
        return guidePerformanceMapper.countHighPerformanceGuides(dataMonth);
    }

    /**
     * 查询绩效趋势数据（按月份统计各等级导购数量）
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 绩效趋势数据
     */
    @Override
    public List<Map<String, Object>> selectPerformanceTrend(String startMonth, String endMonth)
    {
        return guidePerformanceMapper.selectPerformanceTrend(startMonth, endMonth);
    }

    /**
     * 查询绩效统计数据（总体、区域、等级分布）
     * 
     * @param dataMonth 数据月份
     * @return 绩效统计数据
     */
    @Override
    public Map<String, Object> selectPerformanceStatistics(String dataMonth)
    {
        return guidePerformanceMapper.selectPerformanceStatistics(dataMonth);
    }

    /**
     * 查询区域绩效统计
     * 
     * @param dataMonth 数据月份
     * @return 区域绩效统计
     */
    @Override
    public List<Map<String, Object>> selectRegionPerformanceStats(String dataMonth)
    {
        return guidePerformanceMapper.selectRegionPerformanceStats(dataMonth);
    }

    /**
     * 查询等级分布统计
     * 
     * @param dataMonth 数据月份
     * @return 等级分布统计
     */
    @Override
    public List<Map<String, Object>> selectGradeDistribution(String dataMonth)
    {
        return guidePerformanceMapper.selectGradeDistribution(dataMonth);
    }
}