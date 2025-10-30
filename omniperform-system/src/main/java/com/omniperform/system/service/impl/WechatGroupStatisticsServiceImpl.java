package com.omniperform.system.service.impl;

import java.util.List;
import com.omniperform.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.WechatGroupStatisticsMapper;
import com.omniperform.system.domain.WechatGroupStatistics;
import com.omniperform.system.service.IWechatGroupStatisticsService;

/**
 * 微信群组统计Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class WechatGroupStatisticsServiceImpl implements IWechatGroupStatisticsService 
{
    @Autowired
    private WechatGroupStatisticsMapper wechatGroupStatisticsMapper;

    /**
     * 查询微信群组统计
     * 
     * @param statId 微信群组统计主键
     * @return 微信群组统计
     */
    @Override
    public WechatGroupStatistics selectWechatGroupStatisticsByStatId(Long statId)
    {
        return wechatGroupStatisticsMapper.selectWechatGroupStatisticsByStatId(statId);
    }

    /**
     * 查询微信群组统计列表
     * 
     * @param wechatGroupStatistics 微信群组统计
     * @return 微信群组统计
     */
    @Override
    public List<WechatGroupStatistics> selectWechatGroupStatisticsList(WechatGroupStatistics wechatGroupStatistics)
    {
        return wechatGroupStatisticsMapper.selectWechatGroupStatisticsList(wechatGroupStatistics);
    }

    /**
     * 根据月份查询热门群组排行
     * 
     * @param statMonth 统计月份
     * @param limit 限制数量
     * @return 微信群组统计集合
     */
    @Override
    public List<WechatGroupStatistics> selectHotGroupsByMonth(String statMonth, Integer limit)
    {
        return wechatGroupStatisticsMapper.selectHotGroupsByMonth(statMonth, limit);
    }

    /**
     * 新增微信群组统计
     * 
     * @param wechatGroupStatistics 微信群组统计
     * @return 结果
     */
    @Override
    public int insertWechatGroupStatistics(WechatGroupStatistics wechatGroupStatistics)
    {
        wechatGroupStatistics.setCreateTime(DateUtils.getNowDate());
        return wechatGroupStatisticsMapper.insertWechatGroupStatistics(wechatGroupStatistics);
    }

    /**
     * 修改微信群组统计
     * 
     * @param wechatGroupStatistics 微信群组统计
     * @return 结果
     */
    @Override
    public int updateWechatGroupStatistics(WechatGroupStatistics wechatGroupStatistics)
    {
        wechatGroupStatistics.setUpdateTime(DateUtils.getNowDate());
        return wechatGroupStatisticsMapper.updateWechatGroupStatistics(wechatGroupStatistics);
    }

    /**
     * 批量删除微信群组统计
     * 
     * @param statIds 需要删除的微信群组统计主键
     * @return 结果
     */
    @Override
    public int deleteWechatGroupStatisticsByStatIds(Long[] statIds)
    {
        return wechatGroupStatisticsMapper.deleteWechatGroupStatisticsByStatIds(statIds);
    }

    /**
     * 删除微信群组统计信息
     * 
     * @param statId 微信群组统计主键
     * @return 结果
     */
    @Override
    public int deleteWechatGroupStatisticsByStatId(Long statId)
    {
        return wechatGroupStatisticsMapper.deleteWechatGroupStatisticsByStatId(statId);
    }
}