package com.omniperform.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 最佳触达时间分析表 best_touch_time_analysis
 */
public class BestTouchTimeAnalysis extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 分析日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "分析日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date analysisDate;

    /** 统计月份（格式：YYYY-MM） */
    @Excel(name = "统计月份", cellType = ColumnType.TEXT, sort = 1)
    private String monthYear;

    /** 时间段：09:00-10:00、10:00-11:00等 */
    @Excel(name = "时间段", sort = 2)
    private String timeSlot;

    /** 响应率(%) */
    @Excel(name = "响应率", cellType = ColumnType.NUMERIC, sort = 3)
    private BigDecimal responseRate;

    /** 转化率(%) */
    @Excel(name = "转化率", cellType = ColumnType.NUMERIC)
    private BigDecimal conversionRate;

    /** 总触达次数 */
    @Excel(name = "总触达次数", cellType = ColumnType.NUMERIC)
    private Integer totalTouches;

    /** 成功触达次数 */
    @Excel(name = "成功触达次数", cellType = ColumnType.NUMERIC)
    private Integer successfulTouches;

    /** 区域代码，NULL表示全国统计 */
    @Excel(name = "区域代码")
    private String regionCode;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getAnalysisDate() { return analysisDate; }
    public void setAnalysisDate(Date analysisDate) { this.analysisDate = analysisDate; }

    public String getMonthYear() { return monthYear; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public BigDecimal getResponseRate() { return responseRate; }
    public void setResponseRate(BigDecimal responseRate) { this.responseRate = responseRate; }

    public BigDecimal getConversionRate() { return conversionRate; }
    public void setConversionRate(BigDecimal conversionRate) { this.conversionRate = conversionRate; }

    public Integer getTotalTouches() { return totalTouches; }
    public void setTotalTouches(Integer totalTouches) { this.totalTouches = totalTouches; }

    public Integer getSuccessfulTouches() { return successfulTouches; }
    public void setSuccessfulTouches(Integer successfulTouches) { this.successfulTouches = successfulTouches; }

    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }
}