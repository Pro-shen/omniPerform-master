package com.omniperform.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * AI推荐营销任务表 smart_marketing_tasks
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public class SmartMarketingTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 任务编号 */
    @Excel(name = "任务编号")
    private String taskId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 任务类型：个性化推荐、触达优化、内容推送、活动邀请、关怀提醒 */
    @Excel(name = "任务类型")
    private String taskType;

    /** 目标群体 */
    @Excel(name = "目标群体")
    private String targetGroup;

    /** 目标会员数量 */
    @Excel(name = "目标会员数量", cellType = ColumnType.NUMERIC)
    private Integer memberCount;

    /** 预期效果 */
    @Excel(name = "预期效果")
    private String expectedEffect;

    /** 推荐执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "推荐执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recommendTime;

    /** 任务状态：待执行、执行中、已完成、已取消 */
    @Excel(name = "任务状态")
    private String status;

    /** 优先级：高、中、低 */
    @Excel(name = "优先级")
    private String priority;

    /** AI推荐置信度(%) */
    @Excel(name = "AI推荐置信度", cellType = ColumnType.NUMERIC)
    private BigDecimal aiConfidence;

    /** 目标会员ID列表 */
    private String targetMembers;

    /** 任务配置参数 */
    private String taskConfig;

    /** 执行结果数据 */
    private String executionResult;

    /** 执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date executeTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @NotBlank(message = "任务编号不能为空")
    @Size(min = 0, max = 50, message = "任务编号长度不能超过50个字符")
    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    @NotBlank(message = "任务名称不能为空")
    @Size(min = 0, max = 200, message = "任务名称长度不能超过200个字符")
    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    @NotBlank(message = "任务类型不能为空")
    @Size(min = 0, max = 50, message = "任务类型长度不能超过50个字符")
    public String getTaskType()
    {
        return taskType;
    }

    public void setTaskType(String taskType)
    {
        this.taskType = taskType;
    }

    @NotBlank(message = "目标群体不能为空")
    @Size(min = 0, max = 100, message = "目标群体长度不能超过100个字符")
    public String getTargetGroup()
    {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup)
    {
        this.targetGroup = targetGroup;
    }

    public Integer getMemberCount()
    {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount)
    {
        this.memberCount = memberCount;
    }

    @Size(min = 0, max = 100, message = "预期效果长度不能超过100个字符")
    public String getExpectedEffect()
    {
        return expectedEffect;
    }

    public void setExpectedEffect(String expectedEffect)
    {
        this.expectedEffect = expectedEffect;
    }

    public Date getRecommendTime()
    {
        return recommendTime;
    }

    public void setRecommendTime(Date recommendTime)
    {
        this.recommendTime = recommendTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public BigDecimal getAiConfidence()
    {
        return aiConfidence;
    }

    public void setAiConfidence(BigDecimal aiConfidence)
    {
        this.aiConfidence = aiConfidence;
    }

    public String getTargetMembers()
    {
        return targetMembers;
    }

    public void setTargetMembers(String targetMembers)
    {
        this.targetMembers = targetMembers;
    }

    public String getTaskConfig()
    {
        return taskConfig;
    }

    public void setTaskConfig(String taskConfig)
    {
        this.taskConfig = taskConfig;
    }

    public String getExecutionResult()
    {
        return executionResult;
    }

    public void setExecutionResult(String executionResult)
    {
        this.executionResult = executionResult;
    }

    public Date getExecuteTime()
    {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime)
    {
        this.executeTime = executeTime;
    }

    public Date getCompleteTime()
    {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime)
    {
        this.completeTime = completeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
            .append("taskName", getTaskName())
            .append("taskType", getTaskType())
            .append("targetGroup", getTargetGroup())
            .append("memberCount", getMemberCount())
            .append("expectedEffect", getExpectedEffect())
            .append("recommendTime", getRecommendTime())
            .append("status", getStatus())
            .append("priority", getPriority())
            .append("aiConfidence", getAiConfidence())
            .append("targetMembers", getTargetMembers())
            .append("taskConfig", getTaskConfig())
            .append("executionResult", getExecutionResult())
            .append("executeTime", getExecuteTime())
            .append("completeTime", getCompleteTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}