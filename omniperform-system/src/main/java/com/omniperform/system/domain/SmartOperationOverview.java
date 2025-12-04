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
 * 智能运营概览统计表 smart_operation_overview
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public class SmartOperationOverview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 统计日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date statDate;

    /** 统计月份（格式：YYYY-MM） */
    @Excel(name = "统计月份", sort = 1, cellType = ColumnType.TEXT)
    private String monthYear;

    /** 今日待处理预警数 */
    @Excel(name = "今日待处理预警数", cellType = ColumnType.NUMERIC, sort = 2)
    private Integer todayAlerts;

    /** AI推荐任务数 */
    @Excel(name = "AI推荐任务数", cellType = ColumnType.NUMERIC, sort = 3)
    private Integer aiRecommendedTasks;

    /** MOT执行率(%) */
    @Excel(name = "MOT执行率", cellType = ColumnType.NUMERIC, sort = 4)
    private BigDecimal motExecutionRate;

    /** 会员活跃度(%) */
    @Excel(name = "会员活跃度", cellType = ColumnType.NUMERIC, sort = 5)
    private BigDecimal memberActivityRate;

    /** 区域代码，NULL表示全国统计 */
    private String regionCode;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getStatDate()
    {
        return statDate;
    }

    public void setStatDate(Date statDate)
    {
        this.statDate = statDate;
    }

    public Integer getTodayAlerts()
    {
        return todayAlerts;
    }

    public void setTodayAlerts(Integer todayAlerts)
    {
        this.todayAlerts = todayAlerts;
    }

    public Integer getAiRecommendedTasks()
    {
        return aiRecommendedTasks;
    }

    public void setAiRecommendedTasks(Integer aiRecommendedTasks)
    {
        this.aiRecommendedTasks = aiRecommendedTasks;
    }

    public String getMonthYear()
    {
        return monthYear;
    }

    public void setMonthYear(String monthYear)
    {
        this.monthYear = monthYear;
    }

    public BigDecimal getMotExecutionRate()
    {
        return motExecutionRate;
    }

    public void setMotExecutionRate(BigDecimal motExecutionRate)
    {
        this.motExecutionRate = motExecutionRate;
    }

    public BigDecimal getMemberActivityRate()
    {
        return memberActivityRate;
    }

    public void setMemberActivityRate(BigDecimal memberActivityRate)
    {
        this.memberActivityRate = memberActivityRate;
    }

    @Size(min = 0, max = 20, message = "区域代码长度不能超过20个字符")
    public String getRegionCode()
    {
        return regionCode;
    }

    public void setRegionCode(String regionCode)
    {
        this.regionCode = regionCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("statDate", getStatDate())
            .append("todayAlerts", getTodayAlerts())
            .append("aiRecommendedTasks", getAiRecommendedTasks())
            .append("motExecutionRate", getMotExecutionRate())
            .append("memberActivityRate", getMemberActivityRate())
            .append("regionCode", getRegionCode())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}