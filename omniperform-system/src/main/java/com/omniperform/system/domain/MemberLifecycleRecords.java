package com.omniperform.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 会员生命周期记录对象 member_lifecycle_records
 * 
 * @author omniperform
 * @date 2024-01-15
 */
public class MemberLifecycleRecords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;

    /** 数据月份 */
    @Excel(name = "数据月份")
    private String dataMonth;

    /** 会员ID */
    @Excel(name = "会员ID")
    private Long memberId;

    /** 生命周期阶段 */
    @Excel(name = "生命周期阶段")
    private String lifecycleStage;

    /** 阶段开始时间 */
    @Excel(name = "阶段开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date stageStartTime;

    /** 阶段结束时间 */
    @Excel(name = "阶段结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date stageEndTime;

    /** 阶段持续天数 */
    @Excel(name = "阶段持续天数")
    private Integer stageDuration;

    /** 阶段描述 */
    @Excel(name = "阶段描述")
    private String stageDescription;

    /** 触发事件 */
    @Excel(name = "触发事件")
    private String triggerEvent;

    /** 记录时间 */
    @Excel(name = "记录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setLifecycleStage(String lifecycleStage) 
    {
        this.lifecycleStage = lifecycleStage;
    }

    public String getLifecycleStage() 
    {
        return lifecycleStage;
    }
    public void setStageStartTime(Date stageStartTime) 
    {
        this.stageStartTime = stageStartTime;
    }

    public Date getStageStartTime() 
    {
        return stageStartTime;
    }
    public void setStageEndTime(Date stageEndTime) 
    {
        this.stageEndTime = stageEndTime;
    }

    public Date getStageEndTime() 
    {
        return stageEndTime;
    }
    public void setStageDuration(Integer stageDuration) 
    {
        this.stageDuration = stageDuration;
    }

    public Integer getStageDuration() 
    {
        return stageDuration;
    }
    public void setStageDescription(String stageDescription) 
    {
        this.stageDescription = stageDescription;
    }

    public String getStageDescription() 
    {
        return stageDescription;
    }
    public void setTriggerEvent(String triggerEvent) 
    {
        this.triggerEvent = triggerEvent;
    }

    public String getTriggerEvent() 
    {
        return triggerEvent;
    }
    public void setRecordTime(Date recordTime) 
    {
        this.recordTime = recordTime;
    }

    public Date getRecordTime() 
    {
        return recordTime;
    }

    public String getDataMonth() {
        return dataMonth;
    }
    public void setDataMonth(String dataMonth) {
        this.dataMonth = dataMonth;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("memberId", getMemberId())
            .append("lifecycleStage", getLifecycleStage())
            .append("stageStartTime", getStageStartTime())
            .append("stageEndTime", getStageEndTime())
            .append("stageDuration", getStageDuration())
            .append("stageDescription", getStageDescription())
            .append("triggerEvent", getTriggerEvent())
            .append("recordTime", getRecordTime())
            .toString();
    }
}
