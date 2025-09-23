package com.omniperform.system.service;

import java.util.Date;
import java.util.List;
import com.omniperform.system.domain.WechatSopPlan;

/**
 * 微信SOP计划 业务层
 * 
 * @author omniperform
 */
public interface IWechatSopPlanService
{
    /**
     * 查询微信SOP计划
     * 
     * @param sopPlanId 微信SOP计划主键
     * @return 微信SOP计划
     */
    public WechatSopPlan selectWechatSopPlanBySopPlanId(Long sopPlanId);

    /**
     * 查询微信SOP计划列表
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 微信SOP计划集合
     */
    public List<WechatSopPlan> selectWechatSopPlanList(WechatSopPlan wechatSopPlan);

    /**
     * 根据导购ID查询微信SOP计划列表
     * 
     * @param guideId 导购ID
     * @return 微信SOP计划集合
     */
    public List<WechatSopPlan> selectWechatSopPlanListByGuideId(Long guideId);

    /**
     * 根据SOP类型查询微信SOP计划列表
     * 
     * @param sopType SOP类型
     * @return 微信SOP计划集合
     */
    public List<WechatSopPlan> selectWechatSopPlanListByType(Integer sopType);

    /**
     * 根据执行状态查询微信SOP计划列表
     * 
     * @param executionStatus 执行状态
     * @return 微信SOP计划集合
     */
    public List<WechatSopPlan> selectWechatSopPlanListByStatus(Integer executionStatus);

    /**
     * 查询待执行的微信SOP计划列表
     * 
     * @param currentTime 当前时间
     * @return 微信SOP计划集合
     */
    public List<WechatSopPlan> selectPendingWechatSopPlanList(Date currentTime);

    /**
     * 根据目标群组ID查询微信SOP计划列表
     * 
     * @param targetGroupId 目标群组ID
     * @return 微信SOP计划集合
     */
    public List<WechatSopPlan> selectWechatSopPlanListByTargetGroupId(Long targetGroupId);

    /**
     * 根据目标用户ID查询微信SOP计划列表
     * 
     * @param targetUserId 目标用户ID
     * @return 微信SOP计划集合
     */
    public List<WechatSopPlan> selectWechatSopPlanListByTargetUserId(Long targetUserId);

    /**
     * 查询需要重试的微信SOP计划列表
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 微信SOP计划集合
     */
    public List<WechatSopPlan> selectRetryWechatSopPlanList(WechatSopPlan wechatSopPlan);

    /**
     * 新增微信SOP计划
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 结果
     */
    public int insertWechatSopPlan(WechatSopPlan wechatSopPlan);

    /**
     * 修改微信SOP计划
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 结果
     */
    public int updateWechatSopPlan(WechatSopPlan wechatSopPlan);

    /**
     * 更新执行状态
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 结果
     */
    public int updateExecutionStatus(WechatSopPlan wechatSopPlan);

    /**
     * 更新重试次数
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 结果
     */
    public int updateRetryCount(WechatSopPlan wechatSopPlan);

    /**
     * 批量删除微信SOP计划
     * 
     * @param sopPlanIds 需要删除的微信SOP计划主键集合
     * @return 结果
     */
    public int deleteWechatSopPlanBySopPlanIds(String sopPlanIds);

    /**
     * 删除微信SOP计划信息
     * 
     * @param sopPlanId 微信SOP计划主键
     * @return 结果
     */
    public int deleteWechatSopPlanBySopPlanId(Long sopPlanId);

    /**
     * 统计微信SOP计划数量
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 数量
     */
    public int countWechatSopPlans(WechatSopPlan wechatSopPlan);

    /**
     * 执行SOP计划
     * 
     * @param sopPlanId SOP计划ID
     * @return 结果
     */
    public boolean executeSopPlan(Long sopPlanId);

    /**
     * 批量执行SOP计划
     * 
     * @param sopPlanIds SOP计划ID列表
     * @return 结果
     */
    public int batchExecuteSopPlans(List<Long> sopPlanIds);

    /**
     * 重试失败的SOP计划
     * 
     * @param sopPlanId SOP计划ID
     * @return 结果
     */
    public boolean retrySopPlan(Long sopPlanId);
}