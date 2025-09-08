package com.omniperform.web.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.web.domain.MotTask;

/**
 * MOT任务Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-13
 */
public interface MotTaskMapper 
{
    /**
     * 查询MOT任务
     * 
     * @param taskId MOT任务主键
     * @return MOT任务
     */
    public MotTask selectMotTaskByTaskId(Long taskId);

    /**
     * 查询MOT任务列表
     * 
     * @param motTask MOT任务
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskList(MotTask motTask);

    /**
     * 新增MOT任务
     * 
     * @param motTask MOT任务
     * @return 结果
     */
    public int insertMotTask(MotTask motTask);

    /**
     * 修改MOT任务
     * 
     * @param motTask MOT任务
     * @return 结果
     */
    public int updateMotTask(MotTask motTask);

    /**
     * 删除MOT任务
     * 
     * @param taskId MOT任务主键
     * @return 结果
     */
    public int deleteMotTaskByTaskId(Long taskId);

    /**
     * 批量删除MOT任务
     * 
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMotTaskByTaskIds(Long[] taskIds);

    /**
     * 查询待执行MOT任务列表
     * 
     * @param month 月份
     * @param limit 限制数量
     * @return MOT任务集合
     */
    public List<MotTask> selectPendingMotTasks(@Param("month") String month, @Param("limit") Integer limit);

    /**
     * 查询MOT任务统计
     * 
     * @param month 月份
     * @return 统计结果
     */
    public List<MotTask> selectMotTaskStatistics(@Param("month") String month);

    /**
     * 根据会员ID查询MOT任务
     * 
     * @param memberId 会员ID
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据导购ID查询MOT任务
     * 
     * @param guideId 导购ID
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskByGuideId(@Param("guideId") Long guideId);

    /**
     * 查询MOT任务执行率统计
     * 
     * @param month 月份
     * @return 执行率统计
     */
    public List<MotTask> selectMotTaskExecutionRate(@Param("month") String month);

    /**
     * 更新MOT任务状态
     * 
     * @param taskId 任务ID
     * @param status 状态
     * @return 结果
     */
    public int updateMotTaskStatus(@Param("taskId") Long taskId, @Param("status") String status);

    /**
     * 执行MOT任务
     * 
     * @param taskId 任务ID
     * @param executeResult 执行结果
     * @param executeNote 执行备注
     * @return 结果
     */
    public int executeMotTask(@Param("taskId") Long taskId, @Param("executeResult") String executeResult, @Param("executeNote") String executeNote);
}