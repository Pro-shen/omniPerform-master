package com.omniperform.system.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.omniperform.system.mapper.SmartOperationOverviewMapper;
import com.omniperform.system.domain.SmartOperationOverview;
import com.omniperform.system.service.ISmartOperationOverviewService;
import com.omniperform.common.utils.DateUtils;

/**
 * 智能运营概览Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-09
 */
@Service
public class SmartOperationOverviewServiceImpl implements ISmartOperationOverviewService 
{
    private static final Logger log = LoggerFactory.getLogger(SmartOperationOverviewServiceImpl.class);
    
    @Autowired
    private SmartOperationOverviewMapper smartOperationOverviewMapper;

    /**
     * 查询智能运营概览
     * 
     * @param id 智能运营概览主键
     * @return 智能运营概览
     */
    @Override
    public SmartOperationOverview selectSmartOperationOverviewById(Long id)
    {
        return smartOperationOverviewMapper.selectSmartOperationOverviewById(id);
    }

    /**
     * 查询智能运营概览列表
     * 
     * @param smartOperationOverview 智能运营概览
     * @return 智能运营概览
     */
    @Override
    public List<SmartOperationOverview> selectSmartOperationOverviewList(SmartOperationOverview smartOperationOverview)
    {
        return smartOperationOverviewMapper.selectSmartOperationOverviewList(smartOperationOverview);
    }

    /**
     * 根据统计日期查询智能运营概览
     * 
     * @param statDate 统计日期
     * @param regionCode 区域代码
     * @return 智能运营概览
     */
    @Override
    public SmartOperationOverview selectSmartOperationOverviewByDate(Date statDate, String regionCode)
    {
        return smartOperationOverviewMapper.selectSmartOperationOverviewByDate(statDate, regionCode);
    }

    /**
     * 查询最新的智能运营概览
     * 
     * @param regionCode 区域代码
     * @return 智能运营概览
     */
    @Override
    public SmartOperationOverview selectLatestSmartOperationOverview(String regionCode)
    {
        return smartOperationOverviewMapper.selectLatestSmartOperationOverview(regionCode);
    }

    /**
     * 查询指定日期范围内的智能运营概览
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param regionCode 区域代码
     * @return 智能运营概览集合
     */
    @Override
    public List<SmartOperationOverview> selectSmartOperationOverviewByDateRange(Date startDate, Date endDate, String regionCode)
    {
        return smartOperationOverviewMapper.selectSmartOperationOverviewByDateRange(startDate, endDate, regionCode);
    }

    /**
     * 获取智能运营概览数据（前端接口）
     * 
     * @param regionCode 区域代码
     * @return 智能运营概览数据
     */
    @Override
    public Map<String, Object> getSmartOperationOverview(String regionCode)
    {
        log.info("开始获取智能运营概览数据，regionCode: {}", regionCode);
        Map<String, Object> result = new HashMap<>();
        
        // 获取最新的概览数据
        SmartOperationOverview overview = selectLatestSmartOperationOverview(regionCode);
        log.info("查询结果: {}", overview != null ? "找到数据" : "未找到数据");
        
        if (overview != null)
        {
            log.info("返回真实数据: statDate={}, todayAlerts={}, aiRecommendedTasks={}, motExecutionRate={}, memberActivityRate={}, regionCode={}", 
                overview.getStatDate(), overview.getTodayAlerts(), overview.getAiRecommendedTasks(), 
                overview.getMotExecutionRate(), overview.getMemberActivityRate(), overview.getRegionCode());
            
            result.put("statDate", overview.getStatDate());
            result.put("todayAlerts", overview.getTodayAlerts());
            result.put("aiRecommendedTasks", overview.getAiRecommendedTasks());
            result.put("motExecutionRate", overview.getMotExecutionRate());
            result.put("memberActivityRate", overview.getMemberActivityRate());
            result.put("regionCode", overview.getRegionCode());
        }
        else
        {
            log.warn("未找到数据，返回默认值");
            // 如果没有数据，返回默认值
            result.put("statDate", DateUtils.getNowDate());
            result.put("todayAlerts", 0);
            result.put("aiRecommendedTasks", 0);
            result.put("motExecutionRate", 0.0);
            result.put("memberActivityRate", 0.0);
            result.put("regionCode", regionCode);
        }
        
        return result;
    }

    /**
     * 获取智能运营概览数据（前端接口，支持月份参数）
     * 
     * @param regionCode 区域代码
     * @param monthYear 月份（格式：YYYY-MM，为空则查询最新数据）
     * @return 智能运营概览数据
     */
    @Override
    public Map<String, Object> getSmartOperationOverview(String regionCode, String monthYear)
    {
        log.info("开始获取智能运营概览数据，regionCode: {}, monthYear: {}", regionCode, monthYear);
        Map<String, Object> result = new HashMap<>();
        
        SmartOperationOverview overview = null;
        
        // 如果指定了月份，按月份查询；否则查询最新数据
        if (monthYear != null && !monthYear.trim().isEmpty())
        {
            overview = smartOperationOverviewMapper.selectSmartOperationOverviewByMonth(monthYear.trim(), regionCode);
            log.info("按月份查询结果: {}", overview != null ? "找到数据" : "未找到数据");
        }
        else
        {
            overview = selectLatestSmartOperationOverview(regionCode);
            log.info("查询最新数据结果: {}", overview != null ? "找到数据" : "未找到数据");
        }
        
        if (overview != null)
        {
            log.info("返回真实数据: statDate={}, todayAlerts={}, aiRecommendedTasks={}, motExecutionRate={}, memberActivityRate={}, regionCode={}", 
                overview.getStatDate(), overview.getTodayAlerts(), overview.getAiRecommendedTasks(), 
                overview.getMotExecutionRate(), overview.getMemberActivityRate(), overview.getRegionCode());
            
            result.put("statDate", overview.getStatDate());
            result.put("todayAlerts", overview.getTodayAlerts());
            result.put("aiRecommendedTasks", overview.getAiRecommendedTasks());
            result.put("motExecutionRate", overview.getMotExecutionRate());
            result.put("memberActivityRate", overview.getMemberActivityRate());
            result.put("regionCode", overview.getRegionCode());
        }
        else
        {
            log.warn("未找到数据，返回默认值");
            // 如果没有数据，返回默认值
            result.put("statDate", DateUtils.getNowDate());
            result.put("todayAlerts", 0);
            result.put("aiRecommendedTasks", 0);
            result.put("motExecutionRate", 0.0);
            result.put("memberActivityRate", 0.0);
            result.put("regionCode", regionCode);
        }
        
        return result;
    }

    /**
     * 获取智能运营趋势数据
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param regionCode 区域代码
     * @return 趋势数据
     */
    @Override
    public List<Map<String, Object>> getSmartOperationTrend(Date startDate, Date endDate, String regionCode)
    {
        List<SmartOperationOverview> overviewList = selectSmartOperationOverviewByDateRange(startDate, endDate, regionCode);
        List<Map<String, Object>> trendData = new java.util.ArrayList<>();
        
        for (SmartOperationOverview overview : overviewList)
        {
            Map<String, Object> data = new HashMap<>();
            data.put("statDate", overview.getStatDate());
            data.put("todayAlerts", overview.getTodayAlerts());
            data.put("aiRecommendedTasks", overview.getAiRecommendedTasks());
            data.put("motExecutionRate", overview.getMotExecutionRate());
            data.put("memberActivityRate", overview.getMemberActivityRate());
            trendData.add(data);
        }
        
        return trendData;
    }

    /**
     * 新增智能运营概览
     * 
     * @param smartOperationOverview 智能运营概览
     * @return 结果
     */
    @Override
    public int insertSmartOperationOverview(SmartOperationOverview smartOperationOverview)
    {
        smartOperationOverview.setCreateTime(DateUtils.getNowDate());
        return smartOperationOverviewMapper.insertSmartOperationOverview(smartOperationOverview);
    }

    /**
     * 修改智能运营概览
     * 
     * @param smartOperationOverview 智能运营概览
     * @return 结果
     */
    @Override
    public int updateSmartOperationOverview(SmartOperationOverview smartOperationOverview)
    {
        smartOperationOverview.setUpdateTime(DateUtils.getNowDate());
        return smartOperationOverviewMapper.updateSmartOperationOverview(smartOperationOverview);
    }

    /**
     * 批量删除智能运营概览
     * 
     * @param ids 需要删除的智能运营概览主键
     * @return 结果
     */
    @Override
    public int deleteSmartOperationOverviewByIds(Long[] ids)
    {
        return smartOperationOverviewMapper.deleteSmartOperationOverviewByIds(ids);
    }

    /**
     * 删除智能运营概览信息
     * 
     * @param id 智能运营概览主键
     * @return 结果
     */
    @Override
    public int deleteSmartOperationOverviewById(Long id)
    {
        return smartOperationOverviewMapper.deleteSmartOperationOverviewById(id);
    }
}