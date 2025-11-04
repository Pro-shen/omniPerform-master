package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.WechatGroupStatistics;
import org.apache.ibatis.annotations.Param;

/**
 * 微信群组统计Mapper接口
 * 
 * @author omniperform
 */
public interface WechatGroupStatisticsMapper 
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
    public List<WechatGroupStatistics> selectHotGroupsByMonth(@Param("statMonth") String statMonth, @Param("limit") Integer limit);

    /**
     * 根据群组ID与统计月份查询唯一记录
     *
     * @param groupId 群组ID
     * @param statMonth 统计月份
     * @return 微信群组统计
     */
    public WechatGroupStatistics selectWechatGroupStatisticsByGroupAndMonth(@Param("groupId") Long groupId, @Param("statMonth") String statMonth);

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
     * 删除微信群组统计
     * 
     * @param statId 微信群组统计主键
     * @return 结果
     */
    public int deleteWechatGroupStatisticsByStatId(Long statId);

    /**
     * 批量删除微信群组统计
     * 
     * @param statIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWechatGroupStatisticsByStatIds(Long[] statIds);

    /**
     * 查询数据库中去重后的统计月份列表（YYYY-MM），按时间倒序
     *
     * @return 月份字符串集合
     */
    public List<String> selectDistinctStatMonths();
}