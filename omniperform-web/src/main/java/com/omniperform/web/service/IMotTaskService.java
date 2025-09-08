package com.omniperform.web.service;

import java.util.List;
import com.omniperform.web.domain.MotTask;

/**
 * MOT任务Service接口
 * 
 * @author omniperform
 * @date 2025-01-13
 */
public interface IMotTaskService 
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
     * 批量删除MOT任务
     * 
     * @param taskIds 需要删除的MOT任务主键集合
     * @return 结果
     */
    public int deleteMotTaskByTaskIds(Long[] taskIds);

    /**
     * 删除MOT任务信息
     * 
     * @param taskId MOT任务主键
     * @return 结果
     */
    public int deleteMotTaskByTaskId(Long taskId);

    /**
     * 查询待执行MOT任务列表
     * 
     * @param month 月份
     * @param limit 限制数量
     * @return MOT任务集合
     */
    public List<MotTask> selectPendingMotTasks(String month, Integer limit);

    /**
     * 查询MOT任务统计
     * 
     * @param month 月份
     * @return 统计结果
     */
    public List<MotTask> selectMotTaskStatistics(String month);

    /**
     * 根据会员ID查询MOT任务
     * 
     * @param memberId 会员ID
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskByMemberId(Long memberId);

    /**
     * 根据导购ID查询MOT任务
     * 
     * @param guideId 导购ID
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskByGuideId(Long guideId);

    /**
     * 查询MOT任务执行率统计
     * 
     * @param month 月份
     * @return 执行率统计
     */
    public List<MotTask> selectMotTaskExecutionRate(String month);

    /**
     * 更新MOT任务状态
     * 
     * @param taskId 任务ID
     * @param status 状态
     * @return 结果
     */
    public int updateMotTaskStatus(Long taskId, String status);

    /**
     * 执行MOT任务
     * 
     * @param taskId 任务ID
     * @param executeResult 执行结果
     * @param executeNote 执行备注
     * @return 结果
     */
    public int executeMotTask(Long taskId, String executeResult, String executeNote);
}