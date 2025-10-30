package com.omniperform.system.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.SmartMarketingTaskMapper;
import com.omniperform.system.domain.SmartMarketingTask;
import com.omniperform.system.service.ISmartMarketingTaskService;
import com.omniperform.common.utils.DateUtils;

/**
 * 智能营销任务Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-09
 */
@Service
public class SmartMarketingTaskServiceImpl implements ISmartMarketingTaskService 
{
    @Autowired
    private SmartMarketingTaskMapper smartMarketingTaskMapper;

    /**
     * 查询智能营销任务
     * 
     * @param id 智能营销任务主键
     * @return 智能营销任务
     */
    @Override
    public SmartMarketingTask selectSmartMarketingTaskById(Long id)
    {
        return smartMarketingTaskMapper.selectSmartMarketingTaskById(id);
    }

    /**
     * 查询智能营销任务列表
     * 
     * @param smartMarketingTask 智能营销任务
     * @return 智能营销任务
     */
    @Override
    public List<SmartMarketingTask> selectSmartMarketingTaskList(SmartMarketingTask smartMarketingTask)
    {
        return smartMarketingTaskMapper.selectSmartMarketingTaskList(smartMarketingTask);
    }

    /**
     * 根据任务ID查询智能营销任务
     * 
     * @param taskId 任务ID
     * @return 智能营销任务
     */
    @Override
    public SmartMarketingTask selectSmartMarketingTaskByTaskId(String taskId)
    {
        return smartMarketingTaskMapper.selectSmartMarketingTaskByTaskId(taskId);
    }

    /**
     * 根据状态查询智能营销任务
     * 
     * @param status 状态
     * @param taskType 任务类型
     * @return 智能营销任务集合
     */
    @Override
    public List<SmartMarketingTask> selectSmartMarketingTaskByStatus(String status, String taskType)
    {
        return smartMarketingTaskMapper.selectSmartMarketingTaskByStatus(status, taskType);
    }

    /**
     * 查询指定日期范围内的智能营销任务
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param status 任务状态
     * @return 智能营销任务集合
     */
    @Override
    public List<SmartMarketingTask> selectSmartMarketingTaskByDateRange(Date startDate, Date endDate, String status)
    {
        return smartMarketingTaskMapper.selectSmartMarketingTaskByDateRange(startDate, endDate, status);
    }

    /**
     * 获取AI推荐任务数据（前端接口）
     * 
     * @param status 任务状态
     * @param taskType 任务类型
     * @return AI推荐任务数据
     */
    @Override
    public Map<String, Object> getAiTasksData(String status, String taskType)
    {
        Map<String, Object> result = new HashMap<>();
        
        // 获取待执行的高优先级任务
        List<SmartMarketingTask> pendingTasks = selectPendingHighPriorityTasks(10);
        result.put("pendingTasks", pendingTasks);
        result.put("pendingCount", pendingTasks.size());
        
        // 统计各状态的任务数量
        List<Map<String, Object>> statusCount = countTasksByStatus();
        result.put("statusCount", statusCount);
        
        // 统计各类型的任务数量
        List<Map<String, Object>> typeCount = countTasksByType();
        result.put("typeCount", typeCount);
        
        return result;
    }

    /**
     * 获取AI推荐任务数据（前端接口，支持月份过滤）
     * 
     * @param status 任务状态
     * @param taskType 任务类型
     * @param monthYear 月份（格式：YYYY-MM）
     * @return AI推荐任务数据
     */
    @Override
    public Map<String, Object> getAiTasksData(String status, String taskType, String monthYear)
    {
        Map<String, Object> result = new HashMap<>();
        
        List<SmartMarketingTask> pendingTasks;
        if (monthYear != null && !monthYear.isEmpty()) {
            // 解析年月
            String[] parts = monthYear.split("-");
            Integer year = Integer.parseInt(parts[0]);
            Integer month = Integer.parseInt(parts[1]);
            
            // 获取指定月份的高优先级任务
            pendingTasks = smartMarketingTaskMapper.selectSmartMarketingTaskByMonth(year, month, status);
            if (pendingTasks.size() > 10) {
                pendingTasks = pendingTasks.subList(0, 10);
            }
        } else {
            // 获取待执行的高优先级任务
            pendingTasks = selectPendingHighPriorityTasks(10);
        }
        
        result.put("pendingTasks", pendingTasks);
        result.put("pendingCount", pendingTasks.size());
        
        // 统计各状态的任务数量
        List<Map<String, Object>> statusCount = countTasksByStatus();
        result.put("statusCount", statusCount);
        
        // 统计各类型的任务数量
        List<Map<String, Object>> typeCount = countTasksByType();
        result.put("typeCount", typeCount);
        
        return result;
    }

    /**
     * 获取营销任务数据（前端接口）
     * 
     * @param status 状态
     * @param taskType 任务类型
     * @return 营销任务数据
     */
    @Override
    public Map<String, Object> getMarketingTasksData(String status, String taskType)
    {
        Map<String, Object> result = new HashMap<>();
        
        // 获取任务列表
        List<SmartMarketingTask> taskList = selectSmartMarketingTaskByStatus(status, taskType);
        result.put("taskList", taskList);
        result.put("totalCount", taskList.size());
        
        // 统计各状态的任务数量
        List<Map<String, Object>> statusCount = countTasksByStatus();
        result.put("statusCount", statusCount);
        
        return result;
    }

    /**
     * 获取营销任务数据（前端接口，支持月份过滤）
     * 
     * @param status 状态
     * @param taskType 任务类型
     * @param monthYear 月份（格式：YYYY-MM）
     * @return 营销任务数据
     */
    @Override
    public Map<String, Object> getMarketingTasksData(String status, String taskType, String monthYear)
    {
        Map<String, Object> result = new HashMap<>();
        
        List<SmartMarketingTask> taskList;
        if (monthYear != null && !monthYear.isEmpty()) {
            // 解析年月
            String[] parts = monthYear.split("-");
            Integer year = Integer.parseInt(parts[0]);
            Integer month = Integer.parseInt(parts[1]);
            
            // 获取指定月份的任务列表
            taskList = smartMarketingTaskMapper.selectSmartMarketingTaskByMonth(year, month, status);
        } else {
            // 获取任务列表
            taskList = selectSmartMarketingTaskByStatus(status, taskType);
        }
        
        result.put("taskList", taskList);
        result.put("totalCount", taskList.size());
        
        // 统计各状态的任务数量
        List<Map<String, Object>> statusCount = countTasksByStatus();
        result.put("statusCount", statusCount);
        
        return result;
    }

    /**
     * 统计各状态的任务数量
     * 
     * @return 统计结果
     */
    @Override
    public List<Map<String, Object>> countTasksByStatus()
    {
        return smartMarketingTaskMapper.countTasksByStatus();
    }

    /**
     * 统计各类型的任务数量
     * 
     * @return 统计结果
     */
    @Override
    public List<Map<String, Object>> countTasksByType()
    {
        return smartMarketingTaskMapper.countTasksByType();
    }

    /**
     * 查询待执行的高优先级任务
     * 
     * @param limit 限制数量
     * @return 智能营销任务集合
     */
    @Override
    public List<SmartMarketingTask> selectPendingHighPriorityTasks(Integer limit)
    {
        return smartMarketingTaskMapper.selectPendingHighPriorityTasks(limit);
    }

    /**
     * 执行任务
     * 
     * @param taskId 任务ID
     * @param executeUser 执行人
     * @return 结果
     */
    @Override
    public int executeTask(String taskId, String executeUser)
    {
        SmartMarketingTask task = selectSmartMarketingTaskByTaskId(taskId);
        if (task != null)
        {
            task.setStatus("执行中");
            task.setExecuteTime(DateUtils.getNowDate());
            task.setUpdateTime(DateUtils.getNowDate());
            
            return smartMarketingTaskMapper.updateSmartMarketingTask(task);
        }
        return 0;
    }

    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param executionResult 执行结果
     * @return 结果
     */
    @Override
    public int completeTask(String taskId, String executionResult)
    {
        SmartMarketingTask task = selectSmartMarketingTaskByTaskId(taskId);
        if (task != null)
        {
            task.setStatus("已完成");
            task.setExecutionResult(executionResult);
            task.setCompleteTime(DateUtils.getNowDate());
            task.setUpdateTime(DateUtils.getNowDate());
            
            return smartMarketingTaskMapper.updateSmartMarketingTask(task);
        }
        return 0;
    }

    /**
     * 新增智能营销任务
     * 
     * @param smartMarketingTask 智能营销任务
     * @return 结果
     */
    @Override
    public int insertSmartMarketingTask(SmartMarketingTask smartMarketingTask)
    {
        smartMarketingTask.setCreateTime(DateUtils.getNowDate());
        return smartMarketingTaskMapper.insertSmartMarketingTask(smartMarketingTask);
    }

    /**
     * 修改智能营销任务
     * 
     * @param smartMarketingTask 智能营销任务
     * @return 结果
     */
    @Override
    public int updateSmartMarketingTask(SmartMarketingTask smartMarketingTask)
    {
        smartMarketingTask.setUpdateTime(DateUtils.getNowDate());
        return smartMarketingTaskMapper.updateSmartMarketingTask(smartMarketingTask);
    }

    /**
     * 批量删除智能营销任务
     * 
     * @param ids 需要删除的智能营销任务主键
     * @return 结果
     */
    @Override
    public int deleteSmartMarketingTaskByIds(Long[] ids)
    {
        return smartMarketingTaskMapper.deleteSmartMarketingTaskByIds(ids);
    }

    /**
     * 删除智能营销任务信息
     * 
     * @param id 智能营销任务主键
     * @return 结果
     */
    @Override
    public int deleteSmartMarketingTaskById(Long id)
    {
        return smartMarketingTaskMapper.deleteSmartMarketingTaskById(id);
    }
}