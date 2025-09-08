package com.omniperform.system.domain;

import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 会员增长趋势数据表 dashboard_member_growth
 * 
 * @author omniperform
 */
public class DashboardMemberGrowth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 数据月份(格式:2025-05) */
    @Excel(name = "数据月份")
    private String dataMonth;

    /** 新增会员数 */
    @Excel(name = "新增会员数", cellType = ColumnType.NUMERIC)
    private Integer newMembers;

    /** 累计会员数 */
    @Excel(name = "累计会员数", cellType = ColumnType.NUMERIC)
    private Integer totalMembers;

    /** 增长率(%) */
    @Excel(name = "增长率", cellType = ColumnType.NUMERIC)
    private BigDecimal growthRate;

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

    public Integer getNewMembers()
    {
        return newMembers;
    }

    public void setNewMembers(Integer newMembers)
    {
        this.newMembers = newMembers;
    }

    public Integer getTotalMembers()
    {
        return totalMembers;
    }

    public void setTotalMembers(Integer totalMembers)
    {
        this.totalMembers = totalMembers;
    }

    public BigDecimal getGrowthRate()
    {
        return growthRate;
    }

    public void setGrowthRate(BigDecimal growthRate)
    {
        this.growthRate = growthRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dataMonth", getDataMonth())
            .append("newMembers", getNewMembers())
            .append("totalMembers", getTotalMembers())
            .append("growthRate", getGrowthRate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}