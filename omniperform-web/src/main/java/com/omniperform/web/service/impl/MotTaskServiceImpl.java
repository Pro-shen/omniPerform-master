package com.omniperform.web.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.web.mapper.MotTaskMapper;
import com.omniperform.web.domain.MotTask;
import com.omniperform.web.service.IMotTaskService;

/**
 * MOT任务Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-13
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
        return motTaskMapper.updateMotTask(motTask);
    }

    /**
     * 批量删除MOT任务
     * 
     * @param taskIds 需要删除的MOT任务主键
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
     * 查询待执行MOT任务列表
     * 
     * @param month 月份
     * @param limit 限制数量
     * @return MOT任务集合
     */
    @Override
    public List<MotTask> selectPendingMotTasks(String month, Integer limit)
    {
        return motTaskMapper.selectPendingMotTasks(month, limit);
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
        return motTaskMapper.selectMotTaskByMemberId(memberId);
    }

    /**
     * 根据导购ID查询MOT任务
     * 
     * @param guideId 导购ID
     * @return MOT任务集合
     */
    @Override
    public List<MotTask> selectMotTaskByGuideId(Long guideId)
    {
        return motTaskMapper.selectMotTaskByGuideId(guideId);
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
        return motTaskMapper.selectMotTaskExecutionRate(month);
    }

    /**
     * 更新MOT任务状态
     * 
     * @param taskId 任务ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateMotTaskStatus(Long taskId, String status)
    {
        return motTaskMapper.updateMotTaskStatus(taskId, status);
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
        return motTaskMapper.executeMotTask(taskId, executeResult, executeNote);
    }
}