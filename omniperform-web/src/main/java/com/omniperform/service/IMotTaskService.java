package com.omniperform.service;

import java.util.List;
import java.util.Map;
import com.omniperform.web.domain.MotTask;

/**
 * MOT任务Service接口
 * 
 * @author omniperform
 * @date 2025-01-27
 */
public interface IMotTaskService 
{
    /**
     * 查询MOT任务
     * 
     * @param id MOT任务主键
     * @return MOT任务
     */
    public MotTask selectMotTaskById(Long id);

    /**
     * 查询MOT任务列表
     * 
     * @param motTask MOT任务
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskList(MotTask motTask);

    /**
     * 分页查询MOT任务列表
     * 
     * @param motTask MOT任务查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskListWithPage(MotTask motTask, int pageNum, int pageSize);

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
     * @param ids 需要删除的MOT任务主键集合
     * @return 结果
     */
    public int deleteMotTaskByIds(String ids);

    /**
     * 删除MOT任务信息
     * 
     * @param id MOT任务主键
     * @return 结果
     */
    public int deleteMotTaskById(Long id);

    /**
     * 获取今日MOT任务概览
     * 
     * @return 概览数据
     */
    public Map<String, Object> getTodayOverview();

    /**
     * 获取MOT任务统计数据
     * 
     * @param params 查询参数
     * @return 统计数据
     */
    public Map<String, Object> getMotTaskStatistics(Map<String, Object> params);

    /**
     * 根据导购ID查询MOT任务列表
     * 
     * @param guideId 导购ID
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskListByGuideId(Long guideId);

    /**
     * 根据会员ID查询MOT任务列表
     * 
     * @param memberId 会员ID
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskListByMemberId(Long memberId);

    /**
     * 根据状态查询MOT任务列表
     * 
     * @param status 任务状态
     * @return MOT任务集合
     */
    public List<MotTask> selectMotTaskListByStatus(String status);

    /**
     * 更新任务状态
     * 
     * @param id 任务ID
     * @param status 新状态
     * @return 结果
     */
    public int updateMotTaskStatus(Long id, String status);

    /**
     * 完成MOT任务
     * 
     * @param id 任务ID
     * @param result 执行结果
     * @return 结果
     */
    public int completeMotTask(Long id, String result);

    /**
     * 获取MOT类型统计
     * 
     * @return MOT类型统计数据
     */
    public Map<String, Object> getMotTypeStatistics();

    /**
     * 获取导购任务分布统计
     * 
     * @return 导购任务分布数据
     */
    public Map<String, Object> getGuideTaskDistribution();
}