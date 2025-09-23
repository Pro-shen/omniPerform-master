package com.omniperform.system.domain;

import java.util.Date;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 微信SOP计划表 wechat_sop_plan
 * 
 * @author omniperform
 */
public class WechatSopPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** SOP计划ID */
    @Excel(name = "SOP计划ID", cellType = ColumnType.NUMERIC)
    private Long sopPlanId;

    /** SOP名称 */
    @Excel(name = "SOP名称")
    private String sopName;

    /** SOP类型：1-欢迎SOP，2-跟进SOP，3-活动SOP，4-关怀SOP */
    @Excel(name = "SOP类型", readConverterExp = "1=欢迎SOP,2=跟进SOP,3=活动SOP,4=关怀SOP")
    private Integer sopType;

    /** 目标群组ID */
    @Excel(name = "目标群组ID", cellType = ColumnType.NUMERIC)
    private Long targetGroupId;

    /** 目标用户ID */
    @Excel(name = "目标用户ID", cellType = ColumnType.NUMERIC)
    private Long targetUserId;

    /** 执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date executionTime;

    /** 执行状态：1-待执行，2-执行中，3-已完成，4-已取消 */
    @Excel(name = "执行状态", readConverterExp = "1=待执行,2=执行中,3=已完成,4=已取消")
    private Integer executionStatus;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String messageContent;

    /** 消息类型：1-文本，2-图片，3-链接，4-小程序 */
    @Excel(name = "消息类型", readConverterExp = "1=文本,2=图片,3=链接,4=小程序")
    private Integer messageType;

    /** 媒体URL */
    @Excel(name = "媒体URL")
    private String mediaUrl;

    /** 实际执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date actualExecutionTime;

    /** 执行结果：1-成功，2-失败，3-部分成功 */
    @Excel(name = "执行结果", readConverterExp = "1=成功,2=失败,3=部分成功")
    private Integer executionResult;

    /** 失败原因 */
    @Excel(name = "失败原因")
    private String failureReason;

    /** 负责导购ID */
    @Excel(name = "负责导购ID", cellType = ColumnType.NUMERIC)
    private Long guideId;

    /** 重试次数 */
    @Excel(name = "重试次数", cellType = ColumnType.NUMERIC)
    private Integer retryCount;

    /** 最大重试次数 */
    @Excel(name = "最大重试次数", cellType = ColumnType.NUMERIC)
    private Integer maxRetries;

    public Long getSopPlanId()
    {
        return sopPlanId;
    }

    public void setSopPlanId(Long sopPlanId)
    {
        this.sopPlanId = sopPlanId;
    }

    @NotBlank(message = "SOP名称不能为空")
    @Size(min = 0, max = 100, message = "SOP名称长度不能超过100个字符")
    public String getSopName()
    {
        return sopName;
    }

    public void setSopName(String sopName)
    {
        this.sopName = sopName;
    }

    public Integer getSopType()
    {
        return sopType;
    }

    public void setSopType(Integer sopType)
    {
        this.sopType = sopType;
    }

    public Long getTargetGroupId()
    {
        return targetGroupId;
    }

    public void setTargetGroupId(Long targetGroupId)
    {
        this.targetGroupId = targetGroupId;
    }

    public Long getTargetUserId()
    {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId)
    {
        this.targetUserId = targetUserId;
    }

    public Date getExecutionTime()
    {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime)
    {
        this.executionTime = executionTime;
    }

    public Integer getExecutionStatus()
    {
        return executionStatus;
    }

    public void setExecutionStatus(Integer executionStatus)
    {
        this.executionStatus = executionStatus;
    }

    @Size(min = 0, max = 2000, message = "消息内容长度不能超过2000个字符")
    public String getMessageContent()
    {
        return messageContent;
    }

    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }

    public Integer getMessageType()
    {
        return messageType;
    }

    public void setMessageType(Integer messageType)
    {
        this.messageType = messageType;
    }

    @Size(min = 0, max = 500, message = "媒体URL长度不能超过500个字符")
    public String getMediaUrl()
    {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl)
    {
        this.mediaUrl = mediaUrl;
    }

    public Date getActualExecutionTime()
    {
        return actualExecutionTime;
    }

    public void setActualExecutionTime(Date actualExecutionTime)
    {
        this.actualExecutionTime = actualExecutionTime;
    }

    public Integer getExecutionResult()
    {
        return executionResult;
    }

    public void setExecutionResult(Integer executionResult)
    {
        this.executionResult = executionResult;
    }

    @Size(min = 0, max = 500, message = "失败原因长度不能超过500个字符")
    public String getFailureReason()
    {
        return failureReason;
    }

    public void setFailureReason(String failureReason)
    {
        this.failureReason = failureReason;
    }

    public Long getGuideId()
    {
        return guideId;
    }

    public void setGuideId(Long guideId)
    {
        this.guideId = guideId;
    }

    public Integer getRetryCount()
    {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount)
    {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetries()
    {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries)
    {
        this.maxRetries = maxRetries;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("sopPlanId", getSopPlanId())
            .append("sopName", getSopName())
            .append("sopType", getSopType())
            .append("targetGroupId", getTargetGroupId())
            .append("targetUserId", getTargetUserId())
            .append("executionTime", getExecutionTime())
            .append("executionStatus", getExecutionStatus())
            .append("messageContent", getMessageContent())
            .append("messageType", getMessageType())
            .append("mediaUrl", getMediaUrl())
            .append("actualExecutionTime", getActualExecutionTime())
            .append("executionResult", getExecutionResult())
            .append("failureReason", getFailureReason())
            .append("guideId", getGuideId())
            .append("retryCount", getRetryCount())
            .append("maxRetries", getMaxRetries())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}