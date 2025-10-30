package com.omniperform.system.mapper;

import java.util.List;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.SmartOperationOverview;

/**
 * 智能运营概览统计Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface SmartOperationOverviewMapper 
{
    /**
     * 查询智能运营概览统计
     * 
     * @param id 智能运营概览统计主键
     * @return 智能运营概览统计
     */
    public SmartOperationOverview selectSmartOperationOverviewById(Long id);

    /**
     * 根据日期查询智能运营概览统计
     * 
     * @param statDate 统计日期
     * @param regionCode 区域代码
     * @return 智能运营概览统计
     */
    public SmartOperationOverview selectSmartOperationOverviewByDate(@Param("statDate") Date statDate, @Param("regionCode") String regionCode);

    /**
     * 查询最新的智能运营概览统计
     * 
     * @param regionCode 区域代码
     * @return 智能运营概览统计
     */
    public SmartOperationOverview selectLatestSmartOperationOverview(@Param("regionCode") String regionCode);

    /**
     * 根据月份查询智能运营概览统计
     * 
     * @param monthYear 月份（格式：YYYY-MM）
     * @param regionCode 区域代码
     * @return 智能运营概览统计
     */
    public SmartOperationOverview selectSmartOperationOverviewByMonth(@Param("monthYear") String monthYear, @Param("regionCode") String regionCode);

    /**
     * 查询智能运营概览统计列表
     * 
     * @param smartOperationOverview 智能运营概览统计
     * @return 智能运营概览统计集合
     */
    public List<SmartOperationOverview> selectSmartOperationOverviewList(SmartOperationOverview smartOperationOverview);

    /**
     * 根据日期范围查询智能运营概览统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param regionCode 区域代码
     * @return 智能运营概览统计集合
     */
    public List<SmartOperationOverview> selectSmartOperationOverviewByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("regionCode") String regionCode);

    /**
     * 新增智能运营概览统计
     * 
     * @param smartOperationOverview 智能运营概览统计
     * @return 结果
     */
    public int insertSmartOperationOverview(SmartOperationOverview smartOperationOverview);

    /**
     * 修改智能运营概览统计
     * 
     * @param smartOperationOverview 智能运营概览统计
     * @return 结果
     */
    public int updateSmartOperationOverview(SmartOperationOverview smartOperationOverview);

    /**
     * 删除智能运营概览统计
     * 
     * @param id 智能运营概览统计主键
     * @return 结果
     */
    public int deleteSmartOperationOverviewById(Long id);

    /**
     * 批量删除智能运营概览统计
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSmartOperationOverviewByIds(Long[] ids);
}