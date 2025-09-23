package com.omniperform.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.common.utils.DateUtils;
import com.omniperform.common.core.text.Convert;
import com.omniperform.system.mapper.WechatSopPlanMapper;
import com.omniperform.system.domain.WechatSopPlan;
import com.omniperform.system.service.IWechatSopPlanService;

/**
 * 微信SOP计划Service业务层处理
 * 
 * @author omniperform
 */
@Service
public class WechatSopPlanServiceImpl implements IWechatSopPlanService 
{
    @Autowired
    private WechatSopPlanMapper wechatSopPlanMapper;

    /**
     * 查询微信SOP计划
     * 
     * @param sopPlanId 微信SOP计划主键
     * @return 微信SOP计划
     */
    @Override
    public WechatSopPlan selectWechatSopPlanBySopPlanId(Long sopPlanId)
    {
        return wechatSopPlanMapper.selectWechatSopPlanBySopPlanId(sopPlanId);
    }

    /**
     * 查询微信SOP计划列表
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 微信SOP计划集合
     */
    @Override
    public List<WechatSopPlan> selectWechatSopPlanList(WechatSopPlan wechatSopPlan)
    {
        return wechatSopPlanMapper.selectWechatSopPlanList(wechatSopPlan);
    }

    /**
     * 根据导购ID查询微信SOP计划列表
     * 
     * @param guideId 导购ID
     * @return 微信SOP计划集合
     */
    @Override
    public List<WechatSopPlan> selectWechatSopPlanListByGuideId(Long guideId)
    {
        return wechatSopPlanMapper.selectWechatSopPlanListByGuideId(guideId);
    }

    /**
     * 根据SOP类型查询微信SOP计划列表
     * 
     * @param sopType SOP类型
     * @return 微信SOP计划集合
     */
    @Override
    public List<WechatSopPlan> selectWechatSopPlanListByType(Integer sopType)
    {
        return wechatSopPlanMapper.selectWechatSopPlanListByType(sopType);
    }

    /**
     * 根据执行状态查询微信SOP计划列表
     * 
     * @param executionStatus 执行状态
     * @return 微信SOP计划集合
     */
    @Override
    public List<WechatSopPlan> selectWechatSopPlanListByStatus(Integer executionStatus)
    {
        return wechatSopPlanMapper.selectWechatSopPlanListByStatus(executionStatus);
    }

    /**
     * 根据目标群组ID查询微信SOP计划列表
     * 
     * @param targetGroupId 目标群组ID
     * @return 微信SOP计划集合
     */
    @Override
    public List<WechatSopPlan> selectWechatSopPlanListByTargetGroupId(Long targetGroupId)
    {
        return wechatSopPlanMapper.selectWechatSopPlanListByTargetGroupId(targetGroupId);
    }

    /**
     * 根据目标用户ID查询微信SOP计划列表
     * 
     * @param targetUserId 目标用户ID
     * @return 微信SOP计划集合
     */
    @Override
    public List<WechatSopPlan> selectWechatSopPlanListByTargetUserId(Long targetUserId)
    {
        return wechatSopPlanMapper.selectWechatSopPlanListByTargetUserId(targetUserId);
    }

    /**
     * 查询待执行的微信SOP计划列表
     * 
     * @param currentTime 当前时间
     * @return 微信SOP计划集合
     */
    @Override
    public List<WechatSopPlan> selectPendingWechatSopPlanList(Date currentTime)
    {
        return wechatSopPlanMapper.selectPendingWechatSopPlanList(currentTime);
    }

    /**
     * 查询需要重试的微信SOP计划列表
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 微信SOP计划集合
     */
    @Override
    public List<WechatSopPlan> selectRetryWechatSopPlanList(WechatSopPlan wechatSopPlan)
    {
        return wechatSopPlanMapper.selectRetryWechatSopPlanList(wechatSopPlan);
    }

    /**
     * 新增微信SOP计划
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 结果
     */
    @Override
    public int insertWechatSopPlan(WechatSopPlan wechatSopPlan)
    {
        wechatSopPlan.setCreateTime(DateUtils.getNowDate());
        return wechatSopPlanMapper.insertWechatSopPlan(wechatSopPlan);
    }

    /**
     * 修改微信SOP计划
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 结果
     */
    @Override
    public int updateWechatSopPlan(WechatSopPlan wechatSopPlan)
    {
        wechatSopPlan.setUpdateTime(DateUtils.getNowDate());
        return wechatSopPlanMapper.updateWechatSopPlan(wechatSopPlan);
    }

    /**
     * 更新执行状态
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 结果
     */
    @Override
    public int updateExecutionStatus(WechatSopPlan wechatSopPlan)
    {
        return wechatSopPlanMapper.updateExecutionStatus(wechatSopPlan);
    }

    /**
     * 更新重试次数
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 结果
     */
    @Override
    public int updateRetryCount(WechatSopPlan wechatSopPlan)
    {
        return wechatSopPlanMapper.updateRetryCount(wechatSopPlan);
    }

    /**
     * 批量删除微信SOP计划
     * 
     * @param sopPlanIds 需要删除的微信SOP计划主键集合
     * @return 结果
     */
    @Override
    public int deleteWechatSopPlanBySopPlanIds(String sopPlanIds)
    {
        return wechatSopPlanMapper.deleteWechatSopPlanBySopPlanIds(Convert.toLongArray(sopPlanIds));
    }

    /**
     * 删除微信SOP计划信息
     * 
     * @param sopPlanId 微信SOP计划主键
     * @return 结果
     */
    @Override
    public int deleteWechatSopPlanBySopPlanId(Long sopPlanId)
    {
        return wechatSopPlanMapper.deleteWechatSopPlanBySopPlanId(sopPlanId);
    }

    /**
     * 统计微信SOP计划数量
     * 
     * @param wechatSopPlan 微信SOP计划
     * @return 数量
     */
    @Override
    public int countWechatSopPlans(WechatSopPlan wechatSopPlan)
    {
        return wechatSopPlanMapper.countWechatSopPlans(wechatSopPlan);
    }

    /**
     * 执行单个SOP计划
     * 
     * @param sopPlanId SOP计划ID
     * @return 结果
     */
    @Override
    public boolean executeSopPlan(Long sopPlanId)
    {
        // 这里应该包含具体的SOP执行逻辑
        // 暂时只更新状态为执行中
        WechatSopPlan sopPlan = new WechatSopPlan();
        sopPlan.setSopPlanId(sopPlanId);
        sopPlan.setExecutionStatus(1); // 假设1表示执行中
        return updateExecutionStatus(sopPlan) > 0;
    }

    /**
     * 批量执行SOP计划
     * 
     * @param sopPlanIds SOP计划ID列表
     * @return 结果
     */
    @Override
    public int batchExecuteSopPlans(List<Long> sopPlanIds)
    {
        int result = 0;
        for (Long sopPlanId : sopPlanIds) {
            if (executeSopPlan(sopPlanId)) {
                result++;
            }
        }
        return result;
    }

    /**
     * 重试失败的SOP计划
     * 
     * @param sopPlanId SOP计划ID
     * @return 结果
     */
    @Override
    public boolean retrySopPlan(Long sopPlanId)
    {
        // 增加重试次数并重新执行
        WechatSopPlan sopPlan = selectWechatSopPlanBySopPlanId(sopPlanId);
        if (sopPlan != null) {
            sopPlan.setRetryCount(sopPlan.getRetryCount() + 1);
            updateRetryCount(sopPlan);
            return executeSopPlan(sopPlanId);
        }
        return false;
    }
}