package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.WechatOperationStatistics;

/**
 * 企业微信运营统计Service接口
 * 
 * @author omniperform
 */
public interface IWechatOperationStatisticsService 
{
    /**
     * 查询企业微信运营统计
     * 
     * @param statId 企业微信运营统计主键
     * @return 企业微信运营统计
     */
    public WechatOperationStatistics selectWechatOperationStatisticsByStatId(Long statId);

    /**
     * 查询企业微信运营统计列表
     * 
     * @param wechatOperationStatistics 企业微信运营统计
     * @return 企业微信运营统计集合
     */
    public List<WechatOperationStatistics> selectWechatOperationStatisticsList(WechatOperationStatistics wechatOperationStatistics);

    /**
     * 根据月份查询企业微信运营统计
     * 
     * @param statMonth 统计月份
     * @return 企业微信运营统计
     */
    public WechatOperationStatistics selectWechatOperationStatisticsByMonth(String statMonth);

    /**
     * 查询最近几个月的统计数据
     * 
     * @param months 月份数
     * @return 企业微信运营统计集合
     */
    public List<WechatOperationStatistics> selectRecentMonthsStatistics(Integer months);

    /**
     * 查询所有统计月份（去重）
     * 
     * @return 统计月份集合
     */
    public List<String> selectDistinctStatMonths();

    /**
     * 新增企业微信运营统计
     * 
     * @param wechatOperationStatistics 企业微信运营统计
     * @return 结果
     */
    public int insertWechatOperationStatistics(WechatOperationStatistics wechatOperationStatistics);

    /**
     * 修改企业微信运营统计
     * 
     * @param wechatOperationStatistics 企业微信运营统计
     * @return 结果
     */
    public int updateWechatOperationStatistics(WechatOperationStatistics wechatOperationStatistics);

    /**
     * 批量删除企业微信运营统计
     * 
     * @param statIds 需要删除的企业微信运营统计主键集合
     * @return 结果
     */
    public int deleteWechatOperationStatisticsByStatIds(Long[] statIds);

    /**
     * 删除企业微信运营统计信息
     * 
     * @param statId 企业微信运营统计主键
     * @return 结果
     */
    public int deleteWechatOperationStatisticsByStatId(Long statId);
}