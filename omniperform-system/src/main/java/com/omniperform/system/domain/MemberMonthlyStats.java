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
 * 会员月度统计表 member_monthly_stats
 * 
 * @author omniperform
 */
public class MemberMonthlyStats extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 统计ID */
    @Excel(name = "统计ID", cellType = ColumnType.NUMERIC)
    private Long statsId;

    /** 统计月份 */
    @Excel(name = "统计月份")
    private String statsMonth;

    /** 总会员数 */
    @Excel(name = "总会员数", cellType = ColumnType.NUMERIC)
    private Integer totalMembers;

    /** 新增会员数 */
    @Excel(name = "新增会员数", cellType = ColumnType.NUMERIC)
    private Integer newMembers;

    /** 活跃会员数 */
    @Excel(name = "活跃会员数", cellType = ColumnType.NUMERIC)
    private Integer activeMembers;

    /** 购买会员数 */
    @Excel(name = "购买会员数", cellType = ColumnType.NUMERIC)
    private Integer purchaseMembers;

    /** 流失会员数 */
    @Excel(name = "流失会员数", cellType = ColumnType.NUMERIC)
    private Integer churnMembers;

    /** 总购买金额 */
    @Excel(name = "总购买金额", cellType = ColumnType.NUMERIC)
    private BigDecimal totalPurchaseAmount;

    /** 平均客单价 */
    @Excel(name = "平均客单价", cellType = ColumnType.NUMERIC)
    private BigDecimal avgOrderValue;

    /** 会员活跃率 */
    @Excel(name = "会员活跃率", cellType = ColumnType.NUMERIC)
    private BigDecimal activeRate;

    /** 会员购买率 */
    @Excel(name = "会员购买率", cellType = ColumnType.NUMERIC)
    private BigDecimal purchaseRate;

    /** 会员流失率 */
    @Excel(name = "会员流失率", cellType = ColumnType.NUMERIC)
    private BigDecimal churnRate;

    /** 统计时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "统计时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date statsTime;

    public Long getStatsId()
    {
        return statsId;
    }

    public void setStatsId(Long statsId)
    {
        this.statsId = statsId;
    }

    @NotBlank(message = "统计月份不能为空")
    @Size(min = 0, max = 7, message = "统计月份长度不能超过7个字符")
    public String getStatsMonth()
    {
        return statsMonth;
    }

    public void setStatsMonth(String statsMonth)
    {
        this.statsMonth = statsMonth;
    }

    public Integer getTotalMembers()
    {
        return totalMembers;
    }

    public void setTotalMembers(Integer totalMembers)
    {
        this.totalMembers = totalMembers;
    }

    public Integer getNewMembers()
    {
        return newMembers;
    }

    public void setNewMembers(Integer newMembers)
    {
        this.newMembers = newMembers;
    }

    public Integer getActiveMembers()
    {
        return activeMembers;
    }

    public void setActiveMembers(Integer activeMembers)
    {
        this.activeMembers = activeMembers;
    }

    public Integer getPurchaseMembers()
    {
        return purchaseMembers;
    }

    public void setPurchaseMembers(Integer purchaseMembers)
    {
        this.purchaseMembers = purchaseMembers;
    }

    public Integer getChurnMembers()
    {
        return churnMembers;
    }

    public void setChurnMembers(Integer churnMembers)
    {
        this.churnMembers = churnMembers;
    }

    public BigDecimal getTotalPurchaseAmount()
    {
        return totalPurchaseAmount;
    }

    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount)
    {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public BigDecimal getAvgOrderValue()
    {
        return avgOrderValue;
    }

    public void setAvgOrderValue(BigDecimal avgOrderValue)
    {
        this.avgOrderValue = avgOrderValue;
    }

    public BigDecimal getActiveRate()
    {
        return activeRate;
    }

    public void setActiveRate(BigDecimal activeRate)
    {
        this.activeRate = activeRate;
    }

    public BigDecimal getPurchaseRate()
    {
        return purchaseRate;
    }

    public void setPurchaseRate(BigDecimal purchaseRate)
    {
        this.purchaseRate = purchaseRate;
    }

    public BigDecimal getChurnRate()
    {
        return churnRate;
    }

    public void setChurnRate(BigDecimal churnRate)
    {
        this.churnRate = churnRate;
    }

    public Date getStatsTime()
    {
        return statsTime;
    }

    public void setStatsTime(Date statsTime)
    {
        this.statsTime = statsTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("statsId", getStatsId())
            .append("statsMonth", getStatsMonth())
            .append("totalMembers", getTotalMembers())
            .append("newMembers", getNewMembers())
            .append("activeMembers", getActiveMembers())
            .append("purchaseMembers", getPurchaseMembers())
            .append("churnMembers", getChurnMembers())
            .append("totalPurchaseAmount", getTotalPurchaseAmount())
            .append("avgOrderValue", getAvgOrderValue())
            .append("activeRate", getActiveRate())
            .append("purchaseRate", getPurchaseRate())
            .append("churnRate", getChurnRate())
            .append("statsTime", getStatsTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}