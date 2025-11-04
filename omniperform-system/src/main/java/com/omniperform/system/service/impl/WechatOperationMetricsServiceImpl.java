package com.omniperform.system.service.impl;

import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.common.utils.DateUtils;
import com.omniperform.common.core.text.Convert;
import com.omniperform.system.mapper.WechatOperationMetricsMapper;
import com.omniperform.system.domain.WechatOperationMetrics;
import com.omniperform.system.service.IWechatOperationMetricsService;

/**
 * 微信运营指标Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class WechatOperationMetricsServiceImpl implements IWechatOperationMetricsService 
{
    @Autowired
    private WechatOperationMetricsMapper wechatOperationMetricsMapper;

    /**
     * 查询微信运营指标
     * 
     * @param metricId 微信运营指标主键
     * @return 微信运营指标
     */
    @Override
    public WechatOperationMetrics selectWechatOperationMetricsByMetricId(Long metricId)
    {
        return wechatOperationMetricsMapper.selectWechatOperationMetricsByMetricId(metricId);
    }

    /**
     * 根据统计日期和指标类型查询微信运营指标
     * 
     * @param statisticsDate 统计日期
     * @param metricType 指标类型
     * @return 微信运营指标
     */
    @Override
    public WechatOperationMetrics selectWechatOperationMetricsByDateAndType(Date statisticsDate, Integer metricType)
    {
        return wechatOperationMetricsMapper.selectWechatOperationMetricsByDateAndType(statisticsDate, metricType);
    }

    /**
     * 查询微信运营指标列表
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 微信运营指标集合
     */
    @Override
    public List<WechatOperationMetrics> selectWechatOperationMetricsList(WechatOperationMetrics wechatOperationMetrics)
    {
        return wechatOperationMetricsMapper.selectWechatOperationMetricsList(wechatOperationMetrics);
    }

    /**
     * 根据导购ID查询微信运营指标列表
     * 
     * @param guideId 导购ID
     * @return 微信运营指标集合
     */
    @Override
    public List<WechatOperationMetrics> selectWechatOperationMetricsListByGuideId(Long guideId)
    {
        return wechatOperationMetricsMapper.selectWechatOperationMetricsListByGuideId(guideId);
    }

    /**
     * 根据区域代码查询微信运营指标列表
     * 
     * @param regionCode 区域代码
     * @return 微信运营指标集合
     */
    @Override
    public List<WechatOperationMetrics> selectWechatOperationMetricsListByRegionCode(String regionCode)
    {
        return wechatOperationMetricsMapper.selectWechatOperationMetricsListByRegion(regionCode);
    }

    /**
     * 根据日期范围查询微信运营指标列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 微信运营指标集合
     */
    @Override
    public List<WechatOperationMetrics> selectWechatOperationMetricsListByDateRange(Date startDate, Date endDate)
    {
        return wechatOperationMetricsMapper.selectWechatOperationMetricsListByDateRange(startDate, endDate, null);
    }

    /**
     * 查询微信运营指标趋势数据
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 微信运营指标集合
     */
    @Override
    public List<WechatOperationMetrics> selectWechatOperationMetricsTrendData(WechatOperationMetrics wechatOperationMetrics)
    {
        // 这里需要调用Mapper的selectOperationTrendData方法，但返回类型不匹配
        // 暂时返回空列表，需要后续优化
        return null;
    }

    /**
     * 查询微信运营指标汇总数据
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 微信运营指标集合
     */
    @Override
    public List<WechatOperationMetrics> selectWechatOperationMetricsSummaryData(WechatOperationMetrics wechatOperationMetrics)
    {
        // 这里需要调用Mapper的selectOperationSummaryData方法，但返回类型不匹配
        // 暂时返回空列表，需要后续优化
        return null;
    }

    /**
     * 新增微信运营指标
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 结果
     */
    @Override
    public int insertWechatOperationMetrics(WechatOperationMetrics wechatOperationMetrics)
    {
        wechatOperationMetrics.setCreateTime(DateUtils.getNowDate());
        return wechatOperationMetricsMapper.insertWechatOperationMetrics(wechatOperationMetrics);
    }

    /**
     * 修改微信运营指标
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 结果
     */
    @Override
    public int updateWechatOperationMetrics(WechatOperationMetrics wechatOperationMetrics)
    {
        wechatOperationMetrics.setUpdateTime(DateUtils.getNowDate());
        return wechatOperationMetricsMapper.updateWechatOperationMetrics(wechatOperationMetrics);
    }

    /**
     * 批量删除微信运营指标
     * 
     * @param metricIds 需要删除的微信运营指标主键集合
     * @return 结果
     */
    @Override
    public int deleteWechatOperationMetricsByMetricIds(String metricIds)
    {
        return wechatOperationMetricsMapper.deleteWechatOperationMetricsByMetricIds(Convert.toLongArray(metricIds));
    }

    /**
     * 删除微信运营指标信息
     * 
     * @param metricId 微信运营指标主键
     * @return 结果
     */
    @Override
    public int deleteWechatOperationMetricsByMetricId(Long metricId)
    {
        return wechatOperationMetricsMapper.deleteWechatOperationMetricsByMetricId(metricId);
    }

    /**
     * 统计微信运营指标数量
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 数量
     */
    @Override
    public int countWechatOperationMetrics(WechatOperationMetrics wechatOperationMetrics)
    {
        return wechatOperationMetricsMapper.countWechatOperationMetrics(wechatOperationMetrics);
    }

    /**
     * 生成每日指标数据
     * 
     * @param statisticsDate 统计日期
     * @return 结果
     */
    @Override
    public int generateDailyMetrics(Date statisticsDate)
    {
        // Mapper中没有此方法，暂时返回0，需要后续实现
        return 0;
    }

    /**
     * 计算转化率
     * 
     * @param convertedUsers 转化用户数
     * @param totalUsers 总用户数
     * @return 转化率
     */
    @Override
    public Double calculateConversionRate(Integer convertedUsers, Integer totalUsers)
    {
        if (totalUsers == null || totalUsers == 0) {
            return 0.0;
        }
        if (convertedUsers == null) {
            convertedUsers = 0;
        }
        return (double) convertedUsers / totalUsers * 100;
    }

    /**
     * 查询数据库中的去重月份列表（YYYY-MM），按时间倒序
     *
     * @return 月份字符串集合
     */
    @Override
    public List<String> selectDistinctStatMonths()
    {
        return wechatOperationMetricsMapper.selectDistinctStatMonths();
    }
}