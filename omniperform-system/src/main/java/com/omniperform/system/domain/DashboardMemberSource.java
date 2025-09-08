package com.omniperform.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 会员来源分析数据表 dashboard_member_source
 * 
 * @author omniperform
 */
public class DashboardMemberSource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 数据月份(格式:2025-05) */
    @Excel(name = "数据月份")
    private String dataMonth;

    /** 来源渠道 */
    @Excel(name = "来源渠道")
    private String sourceChannel;

    /** 会员数量 */
    @Excel(name = "会员数量", cellType = ColumnType.NUMERIC)
    private Integer memberCount;

    /** 占比(%) */
    @Excel(name = "占比", cellType = ColumnType.NUMERIC)
    private BigDecimal percentage;

    /** 转化率(%) */
    @Excel(name = "转化率", cellType = ColumnType.NUMERIC)
    private BigDecimal conversionRate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDataMonth(String dataMonth) 
    {
        this.dataMonth = dataMonth;
    }

    public String getDataMonth() 
    {
        return dataMonth;
    }
    public void setSourceChannel(String sourceChannel) 
    {
        this.sourceChannel = sourceChannel;
    }

    public String getSourceChannel() 
    {
        return sourceChannel;
    }
    public void setMemberCount(Integer memberCount) 
    {
        this.memberCount = memberCount;
    }

    public Integer getMemberCount() 
    {
        return memberCount;
    }
    public void setPercentage(BigDecimal percentage) 
    {
        this.percentage = percentage;
    }

    public BigDecimal getPercentage() 
    {
        return percentage;
    }
    public void setConversionRate(BigDecimal conversionRate) 
    {
        this.conversionRate = conversionRate;
    }

    public BigDecimal getConversionRate() 
    {
        return conversionRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dataMonth", getDataMonth())
            .append("sourceChannel", getSourceChannel())
            .append("memberCount", getMemberCount())
            .append("percentage", getPercentage())
            .append("conversionRate", getConversionRate())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}