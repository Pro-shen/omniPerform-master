package com.omniperform.system.mapper;

import java.util.List;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.SmartMarketingTask;

/**
 * AI推荐营销任务Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public interface SmartMarketingTaskMapper 
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
    public List<SmartMarketingTask> selectSmartMarketingTaskByStatus(@Param("status") String status, @Param("taskType") String taskType);

    /**
     * 查询指定日期范围内的任务列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param status 任务状态
     * @return AI推荐营销任务集合
     */
    public List<SmartMarketingTask> selectSmartMarketingTaskByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") String status);

    /**
     * 查询指定月份的任务列表
     * 
     * @param year 年份
     * @param month 月份
     * @param status 任务状态
     * @return AI推荐营销任务集合
     */
    public List<SmartMarketingTask> selectSmartMarketingTaskByMonth(@Param("year") Integer year, @Param("month") Integer month, @Param("status") String status);

    /**
     * 统计各状态任务数量
     * 
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countTasksByStatus();

    /**
     * 统计各类型任务数量
     * 
     * @return 统计结果
     */
    public List<java.util.Map<String, Object>> countTasksByType();

    /**
     * 查询待执行的高优先级任务
     * 
     * @param limit 限制数量
     * @return AI推荐营销任务集合
     */
    public List<SmartMarketingTask> selectPendingHighPriorityTasks(Integer limit);

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
     * 删除AI推荐营销任务
     * 
     * @param id AI推荐营销任务主键
     * @return 结果
     */
    public int deleteSmartMarketingTaskById(Long id);

    /**
     * 批量删除AI推荐营销任务
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSmartMarketingTaskByIds(Long[] ids);
}