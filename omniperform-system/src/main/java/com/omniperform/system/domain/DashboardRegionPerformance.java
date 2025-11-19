package com.omniperform.system.domain;

import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 区域绩效数据表 dashboard_region_performance
 * 
 * @author omniperform
 */
public class DashboardRegionPerformance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 数据月份(格式:2025-05) */
    @Excel(name = "数据月份", cellType = ColumnType.TEXT)
    private String dataMonth;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String regionName;

    /** 销售金额 */
    @Excel(name = "销售金额", cellType = ColumnType.NUMERIC)
    private BigDecimal salesAmount;

    /** 会员数量 */
    @Excel(name = "会员数量", cellType = ColumnType.NUMERIC)
    private Integer memberCount;

    /** 绩效得分 */
    @Excel(name = "绩效得分", cellType = ColumnType.NUMERIC)
    private BigDecimal performanceScore;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @NotBlank(message = "数据月份不能为空")
    @Size(min = 0, max = 7, message = "数据月份长度不能超过7个字符")
    public String getDataMonth()
    {
        return dataMonth;
    }

    public void setDataMonth(String dataMonth)
    {
        this.dataMonth = dataMonth;
    }

    @NotBlank(message = "区域名称不能为空")
    @Size(min = 0, max = 50, message = "区域名称长度不能超过50个字符")
    public String getRegionName()
    {
        return regionName;
    }

    public void setRegionName(String regionName)
    {
        this.regionName = regionName;
    }

    public BigDecimal getSalesAmount()
    {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount)
    {
        this.salesAmount = salesAmount;
    }

    public Integer getMemberCount()
    {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount)
    {
        this.memberCount = memberCount;
    }

    public BigDecimal getPerformanceScore()
    {
        return performanceScore;
    }

    public void setPerformanceScore(BigDecimal performanceScore)
    {
        this.performanceScore = performanceScore;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dataMonth", getDataMonth())
            .append("regionName", getRegionName())
            .append("salesAmount", getSalesAmount())
            .append("memberCount", getMemberCount())
            .append("performanceScore", getPerformanceScore())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}