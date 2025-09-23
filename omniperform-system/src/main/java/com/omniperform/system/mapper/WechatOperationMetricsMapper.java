package com.omniperform.system.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.omniperform.system.domain.WechatOperationMetrics;

/**
 * 微信运营指标Mapper接口
 * 
 * @author omniperform
 */
public interface WechatOperationMetricsMapper 
{
    /**
     * 查询微信运营指标
     * 
     * @param metricId 微信运营指标主键
     * @return 微信运营指标
     */
    public WechatOperationMetrics selectWechatOperationMetricsByMetricId(Long metricId);

    /**
     * 根据日期和类型查询微信运营指标
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
    public List<WechatOperationMetrics> selectWechatOperationMetricsListByRegion(String regionCode);

    /**
     * 根据日期范围查询微信运营指标列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param metricType 指标类型
     * @return 微信运营指标集合
     */
    public List<WechatOperationMetrics> selectWechatOperationMetricsListByDateRange(Date startDate, Date endDate, Integer metricType);

    /**
     * 查询运营指标趋势数据
     * 
     * @param params 查询参数
     * @return 趋势数据集合
     */
    public List<Map<String, Object>> selectOperationTrendData(Map<String, Object> params);

    /**
     * 查询运营指标汇总数据
     * 
     * @param params 查询参数
     * @return 汇总数据
     */
    public Map<String, Object> selectOperationSummaryData(Map<String, Object> params);

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
     * 删除微信运营指标
     * 
     * @param metricId 微信运营指标主键
     * @return 结果
     */
    public int deleteWechatOperationMetricsByMetricId(Long metricId);

    /**
     * 批量删除微信运营指标
     * 
     * @param metricIds 需要删除的微信运营指标主键集合
     * @return 结果
     */
    public int deleteWechatOperationMetricsByMetricIds(Long[] metricIds);

    /**
     * 统计指标数量
     * 
     * @param wechatOperationMetrics 查询条件
     * @return 指标数量
     */
    public int countWechatOperationMetrics(WechatOperationMetrics wechatOperationMetrics);
}