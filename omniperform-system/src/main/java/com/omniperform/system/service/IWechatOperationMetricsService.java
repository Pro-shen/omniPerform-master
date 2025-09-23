package com.omniperform.system.service;

import java.util.Date;
import java.util.List;
import com.omniperform.system.domain.WechatOperationMetrics;

/**
 * 微信运营指标 业务层
 * 
 * @author omniperform
 */
public interface IWechatOperationMetricsService
{
    /**
     * 查询微信运营指标
     * 
     * @param metricId 微信运营指标主键
     * @return 微信运营指标
     */
    public WechatOperationMetrics selectWechatOperationMetricsByMetricId(Long metricId);

    /**
     * 根据统计日期和指标类型查询微信运营指标
     * 
     * @param statisticsDate 统计日期
     * @param metricType 指标类型
     * @return 微信运营指标
     */
    public WechatOperationMetrics selectWechatOperationMetricsByDateAndType(Date statisticsDate, Integer metricType);

    /**
     * 查询微信运营指标列表
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 微信运营指标集合
     */
    public List<WechatOperationMetrics> selectWechatOperationMetricsList(WechatOperationMetrics wechatOperationMetrics);

    /**
     * 根据导购ID查询微信运营指标列表
     * 
     * @param guideId 导购ID
     * @return 微信运营指标集合
     */
    public List<WechatOperationMetrics> selectWechatOperationMetricsListByGuideId(Long guideId);

    /**
     * 根据区域代码查询微信运营指标列表
     * 
     * @param regionCode 区域代码
     * @return 微信运营指标集合
     */
    public List<WechatOperationMetrics> selectWechatOperationMetricsListByRegionCode(String regionCode);

    /**
     * 根据日期范围查询微信运营指标列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 微信运营指标集合
     */
    public List<WechatOperationMetrics> selectWechatOperationMetricsListByDateRange(Date startDate, Date endDate);

    /**
     * 查询微信运营指标趋势数据
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 微信运营指标集合
     */
    public List<WechatOperationMetrics> selectWechatOperationMetricsTrendData(WechatOperationMetrics wechatOperationMetrics);

    /**
     * 查询微信运营指标汇总数据
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 微信运营指标集合
     */
    public List<WechatOperationMetrics> selectWechatOperationMetricsSummaryData(WechatOperationMetrics wechatOperationMetrics);

    /**
     * 新增微信运营指标
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 结果
     */
    public int insertWechatOperationMetrics(WechatOperationMetrics wechatOperationMetrics);

    /**
     * 修改微信运营指标
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 结果
     */
    public int updateWechatOperationMetrics(WechatOperationMetrics wechatOperationMetrics);

    /**
     * 批量删除微信运营指标
     * 
     * @param metricIds 需要删除的微信运营指标主键集合
     * @return 结果
     */
    public int deleteWechatOperationMetricsByMetricIds(String metricIds);

    /**
     * 删除微信运营指标信息
     * 
     * @param metricId 微信运营指标主键
     * @return 结果
     */
    public int deleteWechatOperationMetricsByMetricId(Long metricId);

    /**
     * 统计微信运营指标数量
     * 
     * @param wechatOperationMetrics 微信运营指标
     * @return 数量
     */
    public int countWechatOperationMetrics(WechatOperationMetrics wechatOperationMetrics);

    /**
     * 生成每日运营指标
     * 
     * @param statisticsDate 统计日期
     * @return 结果
     */
    public int generateDailyMetrics(Date statisticsDate);

    /**
     * 计算转化率
     * 
     * @param convertedUsers 转化用户数
     * @param totalUsers 总用户数
     * @return 转化率
     */
    public Double calculateConversionRate(Integer convertedUsers, Integer totalUsers);
}