package com.omniperform.system.service;

import java.util.List;
import java.util.Date;
import java.util.Map;
import com.omniperform.system.domain.SmartMarketingTask;

/**
 * AI推荐营销任务Service接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface ISmartMarketingTaskService 
{
    /**
     * 查询AI推荐营销任务
     * 
     * @param id AI推荐营销任务主键
     * @return AI推荐营销任务
     */
    public SmartMarketingTask selectSmartMarketingTaskById(Long id);

    /**
     * 根据任务编号查询AI推荐营销任务
     * 
     * @param taskId 任务编号
     * @return AI推荐营销任务
     */
    public SmartMarketingTask selectSmartMarketingTaskByTaskId(String taskId);

    /**
     * 查询AI推荐营销任务列表
     * 
     * @param smartMarketingTask AI推荐营销任务
     * @return AI推荐营销任务集合
     */
    public List<SmartMarketingTask> selectSmartMarketingTaskList(SmartMarketingTask smartMarketingTask);

    /**
     * 查询指定状态的任务列表
     * 
     * @param status 任务状态
     * @param taskType 任务类型
     * @return AI推荐营销任务集合
     */
    public List<SmartMarketingTask> selectSmartMarketingTaskByStatus(String status, String taskType);

    /**
     * 查询指定日期范围内的任务列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param status 任务状态
     * @return AI推荐营销任务集合
     */
    public List<SmartMarketingTask> selectSmartMarketingTaskByDateRange(Date startDate, Date endDate, String status);

    /**
     * 获取AI推荐任务数据（前端接口）
     * 
     * @param status 任务状态
     * @param taskType 任务类型
     * @return AI推荐任务数据
     */
    public Map<String, Object> getAiTasksData(String status, String taskType);

    /**
     * 获取AI推荐任务数据（前端接口，支持月份过滤）
     * 
     * @param status 任务状态
     * @param taskType 任务类型
     * @param monthYear 月份（格式：YYYY-MM）
     * @return AI推荐任务数据
     */
    public Map<String, Object> getAiTasksData(String status, String taskType, String monthYear);

    /**
     * 获取营销任务数据（前端接口）
     * 
     * @param status 任务状态
     * @param taskType 任务类型
     * @return 营销任务数据
     */
    public Map<String, Object> getMarketingTasksData(String status, String taskType);

    /**
     * 获取营销任务数据（前端接口，支持月份过滤）
     * 
     * @param status 任务状态
     * @param taskType 任务类型
     * @param monthYear 月份（格式：YYYY-MM）
     * @return 营销任务数据
     */
    public Map<String, Object> getMarketingTasksData(String status, String taskType, String monthYear);

    /**
     * 统计各状态任务数量
     * 
     * @return 统计结果
     */
    public List<Map<String, Object>> countTasksByStatus();

    /**
     * 统计各类型任务数量
     * 
     * @return 统计结果
     */
    public List<Map<String, Object>> countTasksByType();

    /**
     * 查询待执行的高优先级任务
     * 
     * @param limit 限制数量
     * @return AI推荐营销任务集合
     */
    public List<SmartMarketingTask> selectPendingHighPriorityTasks(Integer limit);

    /**
     * 执行任务
     * 
     * @param taskId 任务编号
     * @param executeUser 执行人
     * @return 结果
     */
    public int executeTask(String taskId, String executeUser);

    /**
     * 完成任务
     * 
     * @param taskId 任务编号
     * @param executionResult 执行结果
     * @return 结果
     */
    public int completeTask(String taskId, String executionResult);

    /**
     * 新增AI推荐营销任务
     * 
     * @param smartMarketingTask AI推荐营销任务
     * @return 结果
     */
    public int insertSmartMarketingTask(SmartMarketingTask smartMarketingTask);

    /**
     * 修改AI推荐营销任务
     * 
     * @param smartMarketingTask AI推荐营销任务
     * @return 结果
     */
    public int updateSmartMarketingTask(SmartMarketingTask smartMarketingTask);

    /**
     * 批量删除AI推荐营销任务
     * 
     * @param ids 需要删除的AI推荐营销任务主键集合
     * @return 结果
     */
    public int deleteSmartMarketingTaskByIds(Long[] ids);

    /**
     * 删除AI推荐营销任务信息
     * 
     * @param id AI推荐营销任务主键
     * @return 结果
     */
    public int deleteSmartMarketingTaskById(Long id);
}