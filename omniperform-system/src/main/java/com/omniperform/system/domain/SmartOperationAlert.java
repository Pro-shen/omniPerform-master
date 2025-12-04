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
 * 智能运营预警表 smart_operation_alerts
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public class SmartOperationAlert extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 预警编号 */
    @Excel(name = "预警编号")
    private String alertId;

    /** 预警类型：会员流失风险、销售异常、库存预警、服务质量、系统异常 */
    @Excel(name = "预警类型")
    private String alertType;

    /** 预警内容描述 */
    @Excel(name = "预警内容")
    private String alertContent;

    /** 严重程度：高、中、低 */
    @Excel(name = "严重程度")
    private String severity;

    /** 处理状态：待处理、处理中、已处理 */
    @Excel(name = "处理状态")
    private String status;

    /** 所属区域 */
    @Excel(name = "所属区域")
    private String region;

    /** 关联会员ID（如果适用） */
    @Excel(name = "关联会员ID", cellType = ColumnType.NUMERIC)
    private Long memberId;

    /** 关联导购ID（如果适用） */
    @Excel(name = "关联导购ID", cellType = ColumnType.NUMERIC)
    private Long guideId;

    /** 触发预警的数据 */
    private String triggerData;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date processTime;

    /** 处理人 */
    @Excel(name = "处理人")
    private String processUser;

    /** 处理备注 */
    @Excel(name = "处理备注")
    private String processNote;

    /** 月份（格式：YYYY-MM），用于按月查询 */
    @Excel(name = "月份", width = 30, cellType = ColumnType.TEXT)
    private String monthYear;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @NotBlank(message = "预警编号不能为空")
    @Size(min = 0, max = 50, message = "预警编号长度不能超过50个字符")
    public String getAlertId()
    {
        return alertId;
    }

    public void setAlertId(String alertId)
    {
        this.alertId = alertId;
    }

    @NotBlank(message = "预警类型不能为空")
    @Size(min = 0, max = 50, message = "预警类型长度不能超过50个字符")
    public String getAlertType()
    {
        return alertType;
    }

    public void setAlertType(String alertType)
    {
        this.alertType = alertType;
    }

    @NotBlank(message = "预警内容不能为空")
    public String getAlertContent()
    {
        return alertContent;
    }

    public void setAlertContent(String alertContent)
    {
        this.alertContent = alertContent;
    }

    @NotBlank(message = "严重程度不能为空")
    public String getSeverity()
    {
        return severity;
    }

    public void setSeverity(String severity)
    {
        this.severity = severity;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Size(min = 0, max = 50, message = "所属区域长度不能超过50个字符")
    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public Long getGuideId()
    {
        return guideId;
    }

    public void setGuideId(Long guideId)
    {
        this.guideId = guideId;
    }

    public String getTriggerData()
    {
        return triggerData;
    }

    public void setTriggerData(String triggerData)
    {
        this.triggerData = triggerData;
    }

    public Date getProcessTime()
    {
        return processTime;
    }

    public void setProcessTime(Date processTime)
    {
        this.processTime = processTime;
    }

    @Size(min = 0, max = 100, message = "处理人长度不能超过100个字符")
    public String getProcessUser()
    {
        return processUser;
    }

    public void setProcessUser(String processUser)
    {
        this.processUser = processUser;
    }

    public String getProcessNote()
    {
        return processNote;
    }

    public void setProcessNote(String processNote)
    {
        this.processNote = processNote;
    }

    public String getMonthYear()
    {
        return monthYear;
    }

    public void setMonthYear(String monthYear)
    {
        this.monthYear = monthYear;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("alertId", getAlertId())
            .append("alertType", getAlertType())
            .append("alertContent", getAlertContent())
            .append("severity", getSeverity())
            .append("status", getStatus())
            .append("region", getRegion())
            .append("memberId", getMemberId())
            .append("guideId", getGuideId())
            .append("triggerData", getTriggerData())
            .append("processTime", getProcessTime())
            .append("processUser", getProcessUser())
            .append("processNote", getProcessNote())
            .append("monthYear", getMonthYear())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}