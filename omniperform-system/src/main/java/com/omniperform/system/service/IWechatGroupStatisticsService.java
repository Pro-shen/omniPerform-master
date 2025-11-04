package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.WechatGroupStatistics;

/**
 * 微信群组统计Service接口
 * 
 * @author omniperform
 */
public interface IWechatGroupStatisticsService 
{
    /**
     * 查询微信群组统计
     * 
     * @param statId 微信群组统计主键
     * @return 微信群组统计
     */
    public WechatGroupStatistics selectWechatGroupStatisticsByStatId(Long statId);

    /**
     * 查询微信群组统计列表
     * 
     * @param wechatGroupStatistics 微信群组统计
     * @return 微信群组统计集合
     */
    public List<WechatGroupStatistics> selectWechatGroupStatisticsList(WechatGroupStatistics wechatGroupStatistics);

    /**
     * 根据月份查询热门群组排行
     * 
     * @param statMonth 统计月份
     * @param limit 限制数量
     * @return 微信群组统计集合
     */
    public List<WechatGroupStatistics> selectHotGroupsByMonth(String statMonth, Integer limit);

    /**
     * 根据群组ID与统计月份查询唯一记录
     *
     * @param groupId 群组ID
     * @param statMonth 统计月份
     * @return 微信群组统计
     */
    public WechatGroupStatistics selectWechatGroupStatisticsByGroupAndMonth(Long groupId, String statMonth);

    /**
     * 新增微信群组统计
     * 
     * @param wechatGroupStatistics 微信群组统计
     * @return 结果
     */
    public int insertWechatGroupStatistics(WechatGroupStatistics wechatGroupStatistics);

    /**
     * 修改微信群组统计
     * 
     * @param wechatGroupStatistics 微信群组统计
     * @return 结果
     */
    public int updateWechatGroupStatistics(WechatGroupStatistics wechatGroupStatistics);

    /**
     * 批量删除微信群组统计
     * 
     * @param statIds 需要删除的微信群组统计主键集合
     * @return 结果
     */
    public int deleteWechatGroupStatisticsByStatIds(Long[] statIds);

    /**
     * 删除微信群组统计信息
     * 
     * @param statId 微信群组统计主键
     * @return 结果
     */
    public int deleteWechatGroupStatisticsByStatId(Long statId);

    /**
     * 查询数据库中去重后的统计月份列表（YYYY-MM），按时间倒序
     *
     * @return 月份字符串集合
     */
    public List<String> selectDistinctStatMonths();
}