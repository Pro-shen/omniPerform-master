package com.omniperform.system.domain;

import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 会员价值分层(CRFM-E)分布数据表 member_crfme_distribution
 * 
 * @author omniperform
 */
public class MemberCrfmeDistribution extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 数据月份(格式:2025-05) */
    @Excel(name = "数据月份", cellType = ColumnType.TEXT)
    @NotBlank(message = "数据月份不能为空")
    @Size(min = 0, max = 7, message = "数据月份长度不能超过7个字符")
    private String dataMonth;

    /** 评分区间 */
    @Excel(name = "评分区间")
    @NotBlank(message = "评分区间不能为空")
    @Size(min = 0, max = 20, message = "评分区间长度不能超过20个字符")
    private String scoreRange;

    /** 会员数量 */
    @Excel(name = "会员数量", cellType = ColumnType.NUMERIC)
    @NotNull(message = "会员数量不能为空")
    private Integer count;

    /** 占比(%) */
    @Excel(name = "占比", cellType = ColumnType.NUMERIC)
    private BigDecimal percentage;

    /** 平均评分 */
    @Excel(name = "平均评分", cellType = ColumnType.NUMERIC)
    private BigDecimal avgScore;

    /** 分层等级 */
    @Excel(name = "分层等级")
    @Size(min = 0, max = 20, message = "分层等级长度不能超过20个字符")
    private String tier;

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

    public void setScoreRange(String scoreRange) 
    {
        this.scoreRange = scoreRange;
    }

    public String getScoreRange() 
    {
        return scoreRange;
    }

    public void setCount(Integer count) 
    {
        this.count = count;
    }

    public Integer getCount() 
    {
        return count;
    }

    public void setPercentage(BigDecimal percentage) 
    {
        this.percentage = percentage;
    }

    public BigDecimal getPercentage() 
    {
        return percentage;
    }

    public void setAvgScore(BigDecimal avgScore) 
    {
        this.avgScore = avgScore;
    }

    public BigDecimal getAvgScore() 
    {
        return avgScore;
    }

    public void setTier(String tier) 
    {
        this.tier = tier;
    }

    public String getTier() 
    {
        return tier;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dataMonth", getDataMonth())
            .append("scoreRange", getScoreRange())
            .append("count", getCount())
            .append("percentage", getPercentage())
            .append("avgScore", getAvgScore())
            .append("tier", getTier())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}