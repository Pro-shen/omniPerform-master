package com.omniperform.web.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 简化导购绩效对象 - 用于测试Excel导入
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public class GuidePerformanceSimple extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 绩效记录ID */
    private Long performanceId;

    /** 导购ID */
    @Excel(name = "导购ID")
    private String guideId;

    /** 数据月份(YYYY-MM) */
    @Excel(name = "数据月份", cellType = ColumnType.TEXT)
    private String dataMonth;

    /** 新增会员数 */
    @Excel(name = "新增会员数", cellType = ColumnType.NUMERIC)
    private Integer newMembers;

    /** 销售金额 */
    @Excel(name = "销售金额", cellType = ColumnType.NUMERIC)
    private BigDecimal salesAmount;

    /** 导购姓名 */
    @Excel(name = "导购姓名")
    private String guideName;

    public void setPerformanceId(Long performanceId) 
    {
        this.performanceId = performanceId;
    }

    public Long getPerformanceId() 
    {
        return performanceId;
    }
    
    public void setGuideId(String guideId) 
    {
        this.guideId = guideId;
    }

    public String getGuideId() 
    {
        return guideId;
    }
    
    public void setDataMonth(String dataMonth) 
    {
        this.dataMonth = dataMonth;
    }

    public String getDataMonth() 
    {
        return dataMonth;
    }
    
    public void setNewMembers(Integer newMembers) 
    {
        this.newMembers = newMembers;
    }

    public Integer getNewMembers() 
    {
        return newMembers;
    }
    
    public void setSalesAmount(BigDecimal salesAmount) 
    {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getSalesAmount() 
    {
        return salesAmount;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("performanceId", getPerformanceId())
            .append("guideId", getGuideId())
            .append("dataMonth", getDataMonth())
            .append("newMembers", getNewMembers())
            .append("salesAmount", getSalesAmount())
            .append("guideName", getGuideName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}