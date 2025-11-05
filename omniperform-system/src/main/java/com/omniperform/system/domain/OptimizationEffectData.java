package com.omniperform.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 数据闭环优化效果数据表 optimization_effect_data
 */
public class OptimizationEffectData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 统计日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date statDate;

    /** 统计月份（格式：YYYY-MM） */
    @Excel(name = "统计月份")
    private String monthYear;

    /** 指标名称：MOT执行率、会员活跃度、复购率、客单价等 */
    @Excel(name = "指标名称")
    private String metricName;

    /** 指标值(%)或金额等 */
    @Excel(name = "指标值", cellType = ColumnType.NUMERIC)
    private BigDecimal metricValue;

    /** 环比(%) */
    @Excel(name = "环比", cellType = ColumnType.NUMERIC)
    private BigDecimal momRate;

    /** 同比(%) */
    @Excel(name = "同比", cellType = ColumnType.NUMERIC)
    private BigDecimal yoyRate;

    /** 区域代码，NULL表示全国统计 */
    @Excel(name = "区域代码")
    private String regionCode;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getStatDate() { return statDate; }
    public void setStatDate(Date statDate) { this.statDate = statDate; }

    public String getMonthYear() { return monthYear; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }

    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }

    public BigDecimal getMetricValue() { return metricValue; }
    public void setMetricValue(BigDecimal metricValue) { this.metricValue = metricValue; }

    public BigDecimal getMomRate() { return momRate; }
    public void setMomRate(BigDecimal momRate) { this.momRate = momRate; }

    public BigDecimal getYoyRate() { return yoyRate; }
    public void setYoyRate(BigDecimal yoyRate) { this.yoyRate = yoyRate; }

    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
}