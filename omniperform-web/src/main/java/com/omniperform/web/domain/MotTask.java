package com.omniperform.web.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * MOT任务对象 mot_task
 * 
 * @author omniperform
 * @date 2025-01-13
 */
public class MotTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long taskId;

    /** 会员ID */
    @Excel(name = "会员ID")
    private Long memberId;

    /** 会员姓名 */
    @Excel(name = "会员姓名")
    private String memberName;

    /** 会员手机号 */
    @Excel(name = "会员手机号")
    private String memberPhone;

    /** MOT类型 */
    @Excel(name = "MOT类型")
    private String motType;

    /** 任务优先级 */
    @Excel(name = "任务优先级")
    private String priority;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private String status;

    /** 负责导购ID */
    @Excel(name = "负责导购ID")
    private String guideId;

    /** 负责导购姓名 */
    @Excel(name = "负责导购姓名")
    private String guideName;

    /** 计划执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划执行时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dueDate;

    /** 实际执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际执行时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date executeDate;

    /** 执行结果 */
    @Excel(name = "执行结果")
    private String executeResult;

    /** 执行备注 */
    @Excel(name = "执行备注")
    private String executeNote;

    /** 任务描述 */
    @Excel(name = "任务描述")
    private String description;

    /** 区域ID */
    @Excel(name = "区域ID")
    private Long regionId;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String regionName;

    /** 门店ID */
    @Excel(name = "门店ID")
    private Long storeId;

    /** 门店名称 */
    @Excel(name = "门店名称")
    private String storeName;

    /** 数据月份 */
    @Excel(name = "数据月份", cellType = ColumnType.TEXT)
    private String dataMonth;

    public void setTaskId(Long taskId) 
    {
        this.taskId = taskId;
    }

    public Long getTaskId() 
    {
        return taskId;
    }
    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }
    public void setMemberName(String memberName) 
    {
        this.memberName = memberName;
    }

    public String getMemberName() 
    {
        return memberName;
    }
    public void setMemberPhone(String memberPhone) 
    {
        this.memberPhone = memberPhone;
    }

    public String getMemberPhone() 
    {
        return memberPhone;
    }
    public void setMotType(String motType) 
    {
        this.motType = motType;
    }

    public String getMotType() 
    {
        return motType;
    }
    public void setPriority(String priority) 
    {
        this.priority = priority;
    }

    public String getPriority() 
    {
        return priority;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setGuideId(String guideId) 
    {
        this.guideId = guideId;
    }

    public String getGuideId() 
    {
        return guideId;
    }
    public void setGuideName(String guideName) 
    {
        this.guideName = guideName;
    }

    public String getGuideName() 
    {
        return guideName;
    }
    public void setDueDate(Date dueDate) 
    {
        this.dueDate = dueDate;
    }

    public Date getDueDate() 
    {
        return dueDate;
    }
    public void setExecuteDate(Date executeDate) 
    {
        this.executeDate = executeDate;
    }

    public Date getExecuteDate() 
    {
        return executeDate;
    }
    public void setExecuteResult(String executeResult) 
    {
        this.executeResult = executeResult;
    }

    public String getExecuteResult() 
    {
        return executeResult;
    }
    public void setExecuteNote(String executeNote) 
    {
        this.executeNote = executeNote;
    }

    public String getExecuteNote() 
    {
        return executeNote;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setRegionId(Long regionId) 
    {
        this.regionId = regionId;
    }

    public Long getRegionId() 
    {
        return regionId;
    }
    public void setRegionName(String regionName) 
    {
        this.regionName = regionName;
    }

    public String getRegionName() 
    {
        return regionName;
    }
    public void setStoreId(Long storeId) 
    {
        this.storeId = storeId;
    }

    public Long getStoreId() 
    {
        return storeId;
    }
    public void setStoreName(String storeName) 
    {
        this.storeName = storeName;
    }

    public String getStoreName() 
    {
        return storeName;
    }
    public void setDataMonth(String dataMonth) 
    {
        this.dataMonth = dataMonth;
    }

    public String getDataMonth() 
    {
        return dataMonth;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("taskId", getTaskId())
            .append("memberId", getMemberId())
            .append("memberName", getMemberName())
            .append("memberPhone", getMemberPhone())
            .append("motType", getMotType())
            .append("priority", getPriority())
            .append("status", getStatus())
            .append("guideId", getGuideId())
            .append("guideName", getGuideName())
            .append("dueDate", getDueDate())
            .append("executeDate", getExecuteDate())
            .append("executeResult", getExecuteResult())
            .append("executeNote", getExecuteNote())
            .append("description", getDescription())
            .append("regionId", getRegionId())
            .append("regionName", getRegionName())
            .append("storeId", getStoreId())
            .append("storeName", getStoreName())
            .append("dataMonth", getDataMonth())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}