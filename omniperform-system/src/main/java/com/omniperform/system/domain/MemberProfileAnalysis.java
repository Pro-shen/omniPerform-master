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
 * 会员画像分析表 member_profile_analysis
 * 
 * @author omniperform
 * @date 2025-01-09
 */
public class MemberProfileAnalysis extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 分析日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "分析日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date analysisDate;

    /** 画像类型：成长探索型、品质追求型、价格敏感型、社交分享型、忠诚依赖型 */
    @Excel(name = "画像类型")
    private String profileType;

    /** 该类型会员数量 */
    @Excel(name = "会员数量", cellType = ColumnType.NUMERIC)
    private Integer memberCount;

    /** 占比(%) */
    // @Excel(name = "占比", cellType = ColumnType.NUMERIC)
    private BigDecimal percentage;

    /** 平均购买金额 */
    // @Excel(name = "平均购买金额", cellType = ColumnType.NUMERIC)
    private BigDecimal avgPurchaseAmount;

    /** 平均互动频次 */
    // @Excel(name = "平均互动频次", cellType = ColumnType.NUMERIC)
    private BigDecimal avgInteractionFrequency;

    /** 区域代码，NULL表示全国统计 */
    @Excel(name = "区域代码")
    private String regionCode;

    /** 月份（格式：YYYY-MM） */
    private String monthYear;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getAnalysisDate()
    {
        return analysisDate;
    }

    public void setAnalysisDate(Date analysisDate)
    {
        this.analysisDate = analysisDate;
    }

    @NotBlank(message = "画像类型不能为空")
    @Size(min = 0, max = 50, message = "画像类型长度不能超过50个字符")
    public String getProfileType()
    {
        return profileType;
    }

    public void setProfileType(String profileType)
    {
        this.profileType = profileType;
    }

    public Integer getMemberCount()
    {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount)
    {
        this.memberCount = memberCount;
    }

    public BigDecimal getPercentage()
    {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage)
    {
        this.percentage = percentage;
    }

    public BigDecimal getAvgPurchaseAmount()
    {
        return avgPurchaseAmount;
    }

    public void setAvgPurchaseAmount(BigDecimal avgPurchaseAmount)
    {
        this.avgPurchaseAmount = avgPurchaseAmount;
    }

    public BigDecimal getAvgInteractionFrequency()
    {
        return avgInteractionFrequency;
    }

    public void setAvgInteractionFrequency(BigDecimal avgInteractionFrequency)
    {
        this.avgInteractionFrequency = avgInteractionFrequency;
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
            .append("analysisDate", getAnalysisDate())
            .append("profileType", getProfileType())
            .append("memberCount", getMemberCount())
            .append("percentage", getPercentage())
            .append("avgPurchaseAmount", getAvgPurchaseAmount())
            .append("avgInteractionFrequency", getAvgInteractionFrequency())
            .append("regionCode", getRegionCode())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}