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
 * 会员月度阶段统计表 member_monthly_stage_stats
 * 
 * @author omniperform
 */
public class MemberStageStats extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 统计ID */
    @Excel(name = "统计ID", cellType = ColumnType.NUMERIC)
    private Long statsId;

    /** 统计月份 */
    @Excel(name = "统计月份")
    private String statsMonth;

    /** 宝宝阶段 */
    @Excel(name = "宝宝阶段")
    private String babyStage;

    /** 该阶段会员数 */
    @Excel(name = "该阶段会员数", cellType = ColumnType.NUMERIC)
    private Integer memberCount;

    /** 该阶段新增会员数 */
    @Excel(name = "该阶段新增会员数", cellType = ColumnType.NUMERIC)
    private Integer newMemberCount;

    /** 该阶段活跃会员数 */
    @Excel(name = "该阶段活跃会员数", cellType = ColumnType.NUMERIC)
    private Integer activeMemberCount;

    /** 该阶段购买会员数 */
    @Excel(name = "该阶段购买会员数", cellType = ColumnType.NUMERIC)
    private Integer purchaseMemberCount;

    /** 该阶段总购买金额 */
    @Excel(name = "该阶段总购买金额", cellType = ColumnType.NUMERIC)
    private BigDecimal totalPurchaseAmount;

    /** 该阶段平均客单价 */
    @Excel(name = "该阶段平均客单价", cellType = ColumnType.NUMERIC)
    private BigDecimal avgOrderValue;

    /** 该阶段占比 */
    @Excel(name = "该阶段占比", cellType = ColumnType.NUMERIC)
    private BigDecimal stageRatio;

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

    @NotBlank(message = "宝宝阶段不能为空")
    @Size(min = 0, max = 20, message = "宝宝阶段长度不能超过20个字符")
    public String getBabyStage()
    {
        return babyStage;
    }

    public void setBabyStage(String babyStage)
    {
        this.babyStage = babyStage;
    }

    public Integer getMemberCount()
    {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount)
    {
        this.memberCount = memberCount;
    }

    public Integer getNewMemberCount()
    {
        return newMemberCount;
    }

    public void setNewMemberCount(Integer newMemberCount)
    {
        this.newMemberCount = newMemberCount;
    }

    public Integer getActiveMemberCount()
    {
        return activeMemberCount;
    }

    public void setActiveMemberCount(Integer activeMemberCount)
    {
        this.activeMemberCount = activeMemberCount;
    }

    public Integer getPurchaseMemberCount()
    {
        return purchaseMemberCount;
    }

    public void setPurchaseMemberCount(Integer purchaseMemberCount)
    {
        this.purchaseMemberCount = purchaseMemberCount;
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

    public BigDecimal getStageRatio()
    {
        return stageRatio;
    }

    public void setStageRatio(BigDecimal stageRatio)
    {
        this.stageRatio = stageRatio;
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
            .append("babyStage", getBabyStage())
            .append("memberCount", getMemberCount())
            .append("newMemberCount", getNewMemberCount())
            .append("activeMemberCount", getActiveMemberCount())
            .append("purchaseMemberCount", getPurchaseMemberCount())
            .append("totalPurchaseAmount", getTotalPurchaseAmount())
            .append("avgOrderValue", getAvgOrderValue())
            .append("stageRatio", getStageRatio())
            .append("statsTime", getStatsTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}