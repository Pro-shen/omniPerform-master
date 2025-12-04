package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.WechatOperationStatistics;
import org.apache.ibatis.annotations.Param;

/**
 * 微信运营统计汇总Mapper接口
 * 
 * @author omniperform
 */
public interface WechatOperationStatisticsMapper 
{
    /**
     * 查询微信运营统计汇总
     * 
     * @param statId 微信运营统计汇总主键
     * @return 微信运营统计汇总
     */
    public WechatOperationStatistics selectWechatOperationStatisticsByStatId(Long statId);

    /**
     * 查询微信运营统计汇总列表
     * 
     * @param wechatOperationStatistics 微信运营统计汇总
     * @return 微信运营统计汇总集合
     */
    public List<WechatOperationStatistics> selectWechatOperationStatisticsList(WechatOperationStatistics wechatOperationStatistics);

    /**
     * 根据月份查询运营统计
     * 
     * @param statMonth 统计月份
     * @return 微信运营统计汇总
     */
    public WechatOperationStatistics selectWechatOperationStatisticsByMonth(@Param("statMonth") String statMonth);

    /**
     * 查询最近几个月的运营统计趋势
     * 
     * @param months 月份数量
     * @return 微信运营统计汇总集合
     */
    public List<WechatOperationStatistics> selectRecentMonthsStatistics(@Param("months") Integer months);

    /**
     * 查询所有统计月份（去重）
     * 
     * @return 统计月份集合
     */
    public List<String> selectDistinctStatMonths();

    /**
     * 新增微信运营统计汇总
     * 
     * @param wechatOperationStatistics 微信运营统计汇总
     * @return 结果
     */
    public int insertWechatOperationStatistics(WechatOperationStatistics wechatOperationStatistics);

    /**
     * 修改微信运营统计汇总
     * 
     * @param wechatOperationStatistics 微信运营统计汇总
     * @return 结果
     */
    public int updateWechatOperationStatistics(WechatOperationStatistics wechatOperationStatistics);

    /**
     * 删除微信运营统计汇总
     * 
     * @param statId 微信运营统计汇总主键
     * @return 结果
     */
    public int deleteWechatOperationStatisticsByStatId(Long statId);

    /**
     * 批量删除微信运营统计汇总
     * 
     * @param statIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWechatOperationStatisticsByStatIds(Long[] statIds);
}