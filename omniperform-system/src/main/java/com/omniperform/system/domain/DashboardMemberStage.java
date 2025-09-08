package com.omniperform.system.domain;

import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 会员阶段分布数据表 dashboard_member_stage
 * 
 * @author omniperform
 */
public class DashboardMemberStage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 数据月份(格式:2025-05) */
    @Excel(name = "数据月份")
    private String dataMonth;

    /** 阶段名称 */
    @Excel(name = "阶段名称")
    private String stageName;

    /** 会员数量 */
    @Excel(name = "会员数量", cellType = ColumnType.NUMERIC)
    private Integer memberCount;

    /** 占比(%) */
    @Excel(name = "占比", cellType = ColumnType.NUMERIC)
    private BigDecimal percentage;

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

    @NotBlank(message = "阶段名称不能为空")
    @Size(min = 0, max = 50, message = "阶段名称长度不能超过50个字符")
    public String getStageName()
    {
        return stageName;
    }

    public void setStageName(String stageName)
    {
        this.stageName = stageName;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dataMonth", getDataMonth())
            .append("stageName", getStageName())
            .append("memberCount", getMemberCount())
            .append("percentage", getPercentage())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}