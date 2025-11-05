package com.omniperform.system.domain;

import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 首页概览KPI数据表 dashboard_overview_kpi
 */
public class DashboardOverviewKpi extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 数据月份(格式:YYYY-MM) */
    @Excel(name = "数据月份")
    private String dataMonth;

    /** 区域代码，可为空表示全国 */
    @Excel(name = "区域代码")
    private String regionCode;

    /** 新会员数量 */
    @Excel(name = "新会员数量", cellType = ColumnType.NUMERIC)
    private Integer newMembers;

    /** 新会员环比(%) */
    @Excel(name = "新会员环比", cellType = ColumnType.NUMERIC)
    private BigDecimal newMembersGrowth;

    /** 复购率(%) */
    @Excel(name = "复购率", cellType = ColumnType.NUMERIC)
    private BigDecimal repeatPurchaseRate;

    /** 复购率环比(%) */
    @Excel(name = "复购率环比", cellType = ColumnType.NUMERIC)
    private BigDecimal repeatPurchaseGrowth;

    /** MOT成功率(%) */
    @Excel(name = "MOT成功率", cellType = ColumnType.NUMERIC)
    private BigDecimal motSuccessRate;

    /** MOT成功率环比(%) */
    @Excel(name = "MOT成功率环比", cellType = ColumnType.NUMERIC)
    private BigDecimal motSuccessGrowth;

    /** 会员活跃度(%) */
    @Excel(name = "会员活跃度", cellType = ColumnType.NUMERIC)
    private BigDecimal memberActivityRate;

    /** 会员活跃度环比(%) */
    @Excel(name = "会员活跃度环比", cellType = ColumnType.NUMERIC)
    private BigDecimal memberActivityGrowth;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @NotBlank(message = "数据月份不能为空")
    @Size(min = 0, max = 7, message = "数据月份长度不能超过7个字符")
    public String getDataMonth() { return dataMonth; }
    public void setDataMonth(String dataMonth) { this.dataMonth = dataMonth; }

    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String regionCode) { this.regionCode = regionCode; }

    public Integer getNewMembers() { return newMembers; }
    public void setNewMembers(Integer newMembers) { this.newMembers = newMembers; }

    public BigDecimal getNewMembersGrowth() { return newMembersGrowth; }
    public void setNewMembersGrowth(BigDecimal newMembersGrowth) { this.newMembersGrowth = newMembersGrowth; }

    public BigDecimal getRepeatPurchaseRate() { return repeatPurchaseRate; }
    public void setRepeatPurchaseRate(BigDecimal repeatPurchaseRate) { this.repeatPurchaseRate = repeatPurchaseRate; }

    public BigDecimal getRepeatPurchaseGrowth() { return repeatPurchaseGrowth; }
    public void setRepeatPurchaseGrowth(BigDecimal repeatPurchaseGrowth) { this.repeatPurchaseGrowth = repeatPurchaseGrowth; }

    public BigDecimal getMotSuccessRate() { return motSuccessRate; }
    public void setMotSuccessRate(BigDecimal motSuccessRate) { this.motSuccessRate = motSuccessRate; }

    public BigDecimal getMotSuccessGrowth() { return motSuccessGrowth; }
    public void setMotSuccessGrowth(BigDecimal motSuccessGrowth) { this.motSuccessGrowth = motSuccessGrowth; }

    public BigDecimal getMemberActivityRate() { return memberActivityRate; }
    public void setMemberActivityRate(BigDecimal memberActivityRate) { this.memberActivityRate = memberActivityRate; }

    public BigDecimal getMemberActivityGrowth() { return memberActivityGrowth; }
    public void setMemberActivityGrowth(BigDecimal memberActivityGrowth) { this.memberActivityGrowth = memberActivityGrowth; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("dataMonth", getDataMonth())
                .append("regionCode", getRegionCode())
                .append("newMembers", getNewMembers())
                .append("newMembersGrowth", getNewMembersGrowth())
                .append("repeatPurchaseRate", getRepeatPurchaseRate())
                .append("repeatPurchaseGrowth", getRepeatPurchaseGrowth())
                .append("motSuccessRate", getMotSuccessRate())
                .append("motSuccessGrowth", getMotSuccessGrowth())
                .append("memberActivityRate", getMemberActivityRate())
                .append("memberActivityGrowth", getMemberActivityGrowth())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}