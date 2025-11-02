package com.omniperform.system.domain;

import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 会员概览数据表 dashboard_member_overview
 * 
 * @author omniperform
 */
public class DashboardMemberOverview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 数据月份(格式:2025-05) */
    @Excel(name = "数据月份")
    private String dataMonth;

    /** 总会员数 */
    @Excel(name = "总会员数", cellType = ColumnType.NUMERIC)
    private Integer totalMembers;

    /** 新增会员数 */
    @Excel(name = "新增会员数", cellType = ColumnType.NUMERIC)
    private Integer newMembers;

    /** 活跃会员数 */
    @Excel(name = "活跃会员数", cellType = ColumnType.NUMERIC)
    private Integer activeMembers;

    /** 会员增长率(%) */
    @Excel(name = "会员增长率", cellType = ColumnType.NUMERIC)
    private BigDecimal memberGrowthRate;

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

    public BigDecimal getMemberGrowthRate()
    {
        return memberGrowthRate;
    }

    public void setMemberGrowthRate(BigDecimal memberGrowthRate)
    {
        this.memberGrowthRate = memberGrowthRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dataMonth", getDataMonth())
            .append("totalMembers", getTotalMembers())
            .append("newMembers", getNewMembers())
            .append("activeMembers", getActiveMembers())
            .append("memberGrowthRate", getMemberGrowthRate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}