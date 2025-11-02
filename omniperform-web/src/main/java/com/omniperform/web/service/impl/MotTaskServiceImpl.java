package com.omniperform.web.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.web.mapper.MotTaskMapper;
import com.omniperform.web.domain.MotTask;
import com.omniperform.web.service.IMotTaskService;
import com.omniperform.common.core.text.Convert;
import com.omniperform.common.utils.DateUtils;

/**
 * MOT任务Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-27
 */
@Service
public class MotTaskServiceImpl implements IMotTaskService 
{
    @Autowired
    private MotTaskMapper motTaskMapper;

    /**
     * 查询MOT任务
     * 
     * @param taskId MOT任务主键
     * @return MOT任务
     */
    @Override
    public MotTask selectMotTaskByTaskId(Long taskId)
    {
        return motTaskMapper.selectMotTaskByTaskId(taskId);
    }

    /**
     * 查询MOT任务列表
     * 
     * @param motTask MOT任务
     * @return MOT任务
     */
    @Override
    public List<MotTask> selectMotTaskList(MotTask motTask)
    {
        return motTaskMapper.selectMotTaskList(motTask);
    }



    /**
     * 新增MOT任务
     * 
     * @param motTask MOT任务
     * @return 结果
     */
    @Override
    public int insertMotTask(MotTask motTask)
    {
        motTask.setCreateTime(DateUtils.getNowDate());
        return motTaskMapper.insertMotTask(motTask);
    }

    /**
     * 修改MOT任务
     * 
     * @param motTask MOT任务
     * @return 结果
     */
    @Override
    public int updateMotTask(MotTask motTask)
    {
        motTask.setUpdateTime(DateUtils.getNowDate());
        return motTaskMapper.updateMotTask(motTask);
    }

    /**
     * 批量删除MOT任务
     * 
     * @param taskIds 需要删除的MOT任务主键集合
     * @return 结果
     */
    @Override
    public int deleteMotTaskByTaskIds(Long[] taskIds)
    {
        return motTaskMapper.deleteMotTaskByTaskIds(taskIds);
    }

    /**
     * 删除MOT任务信息
     * 
     * @param taskId MOT任务主键
     * @return 结果
     */
    @Override
    public int deleteMotTaskByTaskId(Long taskId)
    {
        return motTaskMapper.deleteMotTaskByTaskId(taskId);
    }

    /**
     * 获取今日MOT任务概览
     * 
     * @return 概览数据
     */
    @Override
    public Map<String, Object> getTodayOverview()
    {
        Map<String, Object> overview = new HashMap<>();
        
        // 获取所有任务统计 - 移除日期过滤，显示所有任务
        MotTask queryTask = new MotTask();
        List<MotTask> allTasks = motTaskMapper.selectMotTaskList(queryTask);
        
        // 统计各种状态的任务数量
        int totalTasks = allTasks.size();
        int pendingTasks = 0;
        int inProgressTasks = 0;
        int completedTasks = 0;
        int cancelledTasks = 0;
        
        for (MotTask task : allTasks) {
            String status = task.getStatus();
            if ("待执行".equals(status)) {
                pendingTasks++;
            } else if ("执行中".equals(status)) {
                inProgressTasks++;
            } else if ("已完成".equals(status)) {
                completedTasks++;
            } else if ("已取消".equals(status)) {
                cancelledTasks++;
            }
        }
        
        overview.put("totalTasks", totalTasks);
        overview.put("pendingTasks", pendingTasks);
        overview.put("inProgressTasks", inProgressTasks);
        overview.put("completedTasks", completedTasks);
        overview.put("cancelledTasks", cancelledTasks);
        
        // 计算完成率
        double completionRate = totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0;
        overview.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
        
        // 获取所有任务统计（用于计算各种率）
        List<MotTask> allTasksForStats = motTaskMapper.selectMotTaskList(new MotTask());
        int totalAllTasks = allTasksForStats.size();
        
        // 计算发送率（已发送 + 已沟通 + 已完成的任务）
        int sentTasks = 0;
        int communicatedTasks = 0;
        int successfulTasks = 0;
        
        for (MotTask task : allTasksForStats) {
            String status = task.getStatus();
            if ("已发送".equals(status) || "已沟通".equals(status) || "已完成".equals(status)) {
                sentTasks++;
            }
            if ("已沟通".equals(status) || "已完成".equals(status)) {
                communicatedTasks++;
            }
            if ("已完成".equals(status)) {
                successfulTasks++;
            }
        }
        
        // 计算各种率
        double sendRate = totalAllTasks > 0 ? (double) sentTasks / totalAllTasks * 100 : 0;
        double communicationRate = totalAllTasks > 0 ? (double) communicatedTasks / totalAllTasks * 100 : 0;
        double successRate = totalAllTasks > 0 ? (double) successfulTasks / totalAllTasks * 100 : 0;
        
        overview.put("sendRate", Math.round(sendRate * 100.0) / 100.0);
        overview.put("communicationRate", Math.round(communicationRate * 100.0) / 100.0);
        overview.put("successRate", Math.round(successRate * 100.0) / 100.0);
        
        return overview;
    }

    /**
     * 获取MOT任务统计数据
     * 
     * @param params 查询参数
     * @return 统计数据
     */
    @Override
    public Map<String, Object> getMotTaskStatistics(Map<String, Object> params)
    {
        Map<String, Object> statistics = new HashMap<>();
        
        // 根据参数构建查询条件
        MotTask queryTask = new MotTask();
        if (params.containsKey("dataMonth")) {
            queryTask.setDataMonth((String) params.get("dataMonth"));
        }
        if (params.containsKey("guideId")) {
            queryTask.setGuideId(params.get("guideId").toString());
        }
        if (params.containsKey("regionId")) {
            queryTask.setRegionId(Long.valueOf(params.get("regionId").toString()));
        }
        
        List<MotTask> tasks = motTaskMapper.selectMotTaskList(queryTask);
        
        // 按MOT类型统计
        Map<String, Integer> motTypeStats = new HashMap<>();
        // 按状态统计
        Map<String, Integer> statusStats = new HashMap<>();
        // 按导购统计
        Map<String, Integer> guideStats = new HashMap<>();
        
        for (MotTask task : tasks) {
            // MOT类型统计
            String motType = task.getMotType();
            motTypeStats.put(motType, motTypeStats.getOrDefault(motType, 0) + 1);
            
            // 状态统计
            String status = task.getStatus();
            statusStats.put(status, statusStats.getOrDefault(status, 0) + 1);
            
            // 导购统计
            String guideName = task.getGuideName();
            if (guideName != null) {
                guideStats.put(guideName, guideStats.getOrDefault(guideName, 0) + 1);
            }
        }
        
        statistics.put("motTypeStats", motTypeStats);
        statistics.put("statusStats", statusStats);
        statistics.put("guideStats", guideStats);
        statistics.put("totalCount", tasks.size());
        
        return statistics;
    }





    /**
     * 更新任务状态
     * 
     * @param taskId 任务ID
     * @param status 新状态
     * @return 结果
     */
    @Override
    public int updateMotTaskStatus(Long taskId, String status)
    {
        MotTask motTask = new MotTask();
        motTask.setTaskId(taskId);
        motTask.setStatus(status);
        motTask.setUpdateTime(DateUtils.getNowDate());
        return motTaskMapper.updateMotTask(motTask);
    }

    /**
     * 获取导购任务分布统计
     * 
     * @return 导购任务分布数据
     */
    @Override
    public Map<String, Object> getGuideTaskDistribution()
    {
        Map<String, Object> distribution = new HashMap<>();
        
        List<MotTask> allTasks = motTaskMapper.selectMotTaskList(new MotTask());
        Map<String, Map<String, Integer>> guideTaskStats = new HashMap<>();
        
        for (MotTask task : allTasks) {
            String guideName = task.getGuideName();
            if (guideName != null) {
                guideTaskStats.putIfAbsent(guideName, new HashMap<>());
                Map<String, Integer> guideStats = guideTaskStats.get(guideName);
                
                String status = task.getStatus();
                guideStats.put(status, guideStats.getOrDefault(status, 0) + 1);
                guideStats.put("total", guideStats.getOrDefault("total", 0) + 1);
            }
        }
        
        distribution.put("guideTaskStats", guideTaskStats);
        distribution.put("totalGuides", guideTaskStats.size());
        
        return distribution;
    }

    /**
     * 查询待执行MOT任务列表
     * 
     * @param month 月份
     * @param limit 限制数量
     * @return MOT任务集合
     */
    @Override
    public List<MotTask> selectPendingMotTasks(String month, Integer limit)
    {
        MotTask queryTask = new MotTask();
        queryTask.setStatus("待执行");
        if (month != null) {
            queryTask.setDataMonth(month);
        }
        return motTaskMapper.selectMotTaskList(queryTask);
    }

    /**
     * 查询MOT任务统计
     * 
     * @param month 月份
     * @return 统计结果
     */
    @Override
    public List<MotTask> selectMotTaskStatistics(String month)
    {
        return motTaskMapper.selectMotTaskStatistics(month);
    }

    /**
     * 根据会员ID查询MOT任务
     * 
     * @param memberId 会员ID
     * @return MOT任务集合
     */
    @Override
    public List<MotTask> selectMotTaskByMemberId(Long memberId)
    {
        MotTask queryTask = new MotTask();
        queryTask.setMemberId(memberId);
        return motTaskMapper.selectMotTaskList(queryTask);
    }

    /**
     * 根据导购ID查询MOT任务
     * 
     * @param guideId 导购ID
     * @return MOT任务集合
     */
    @Override
    public List<MotTask> selectMotTaskByGuideId(String guideId)
    {
        MotTask queryTask = new MotTask();
        queryTask.setGuideId(guideId);
        return motTaskMapper.selectMotTaskList(queryTask);
    }

    /**
     * 查询MOT任务执行率统计
     * 
     * @param month 月份
     * @return 执行率统计
     */
    @Override
    public List<MotTask> selectMotTaskExecutionRate(String month)
    {
        MotTask queryTask = new MotTask();
        if (month != null) {
            queryTask.setDataMonth(month);
        }
        return motTaskMapper.selectMotTaskList(queryTask);
    }

    /**
     * 执行MOT任务
     * 
     * @param taskId 任务ID
     * @param executeResult 执行结果
     * @param executeNote 执行备注
     * @return 结果
     */
    @Override
    public int executeMotTask(Long taskId, String executeResult, String executeNote)
    {
        MotTask motTask = new MotTask();
        motTask.setTaskId(taskId);
        motTask.setExecuteResult(executeResult);
        motTask.setExecuteNote(executeNote);
        motTask.setExecuteDate(new Date());
        motTask.setStatus("已完成");
        motTask.setUpdateTime(new Date());
        return motTaskMapper.updateMotTask(motTask);
    }
}