package com.omniperform.system.service.impl;

import java.util.List;
import com.omniperform.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.WechatOperationStatisticsMapper;
import com.omniperform.system.domain.WechatOperationStatistics;
import com.omniperform.system.service.IWechatOperationStatisticsService;

/**
 * 企业微信运营统计Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class WechatOperationStatisticsServiceImpl implements IWechatOperationStatisticsService 
{
    @Autowired
    private WechatOperationStatisticsMapper wechatOperationStatisticsMapper;

    /**
     * 查询企业微信运营统计
     * 
     * @param statId 企业微信运营统计主键
     * @return 企业微信运营统计
     */
    @Override
    public WechatOperationStatistics selectWechatOperationStatisticsByStatId(Long statId)
    {
        return wechatOperationStatisticsMapper.selectWechatOperationStatisticsByStatId(statId);
    }

    /**
     * 查询企业微信运营统计列表
     * 
     * @param wechatOperationStatistics 企业微信运营统计
     * @return 企业微信运营统计
     */
    @Override
    public List<WechatOperationStatistics> selectWechatOperationStatisticsList(WechatOperationStatistics wechatOperationStatistics)
    {
        return wechatOperationStatisticsMapper.selectWechatOperationStatisticsList(wechatOperationStatistics);
    }

    /**
     * 根据月份查询企业微信运营统计
     * 
     * @param statMonth 统计月份
     * @return 企业微信运营统计
     */
    @Override
    public WechatOperationStatistics selectWechatOperationStatisticsByMonth(String statMonth)
    {
        return wechatOperationStatisticsMapper.selectWechatOperationStatisticsByMonth(statMonth);
    }

    /**
     * 查询最近几个月的统计数据
     * 
     * @param months 月份数
     * @return 企业微信运营统计集合
     */
    @Override
    public List<WechatOperationStatistics> selectRecentMonthsStatistics(Integer months)
    {
        return wechatOperationStatisticsMapper.selectRecentMonthsStatistics(months);
    }

    /**
     * 新增企业微信运营统计
     * 
     * @param wechatOperationStatistics 企业微信运营统计
     * @return 结果
     */
    @Override
    public int insertWechatOperationStatistics(WechatOperationStatistics wechatOperationStatistics)
    {
        wechatOperationStatistics.setCreateTime(DateUtils.getNowDate());
        return wechatOperationStatisticsMapper.insertWechatOperationStatistics(wechatOperationStatistics);
    }

    /**
     * 修改企业微信运营统计
     * 
     * @param wechatOperationStatistics 企业微信运营统计
     * @return 结果
     */
    @Override
    public int updateWechatOperationStatistics(WechatOperationStatistics wechatOperationStatistics)
    {
        wechatOperationStatistics.setUpdateTime(DateUtils.getNowDate());
        return wechatOperationStatisticsMapper.updateWechatOperationStatistics(wechatOperationStatistics);
    }

    /**
     * 批量删除企业微信运营统计
     * 
     * @param statIds 需要删除的企业微信运营统计主键
     * @return 结果
     */
    @Override
    public int deleteWechatOperationStatisticsByStatIds(Long[] statIds)
    {
        return wechatOperationStatisticsMapper.deleteWechatOperationStatisticsByStatIds(statIds);
    }

    /**
     * 删除企业微信运营统计信息
     * 
     * @param statId 企业微信运营统计主键
     * @return 结果
     */
    @Override
    public int deleteWechatOperationStatisticsByStatId(Long statId)
    {
        return wechatOperationStatisticsMapper.deleteWechatOperationStatisticsByStatId(statId);
    }
}