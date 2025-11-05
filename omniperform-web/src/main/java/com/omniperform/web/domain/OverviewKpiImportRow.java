package com.omniperform.web.domain;

import java.math.BigDecimal;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;

/**
 * 首页KPI导入Excel行（仅用于Excel解析，不做持久化）
 */
public class OverviewKpiImportRow {
    @Excel(name = "月份")
    private String month;

    @Excel(name = "新会员数量", cellType = ColumnType.NUMERIC)
    private Integer newMembers;

    @Excel(name = "复购率", cellType = ColumnType.NUMERIC)
    private BigDecimal repeatPurchaseRate;

    @Excel(name = "MOT成功率", cellType = ColumnType.NUMERIC)
    private BigDecimal motSuccessRate;

    @Excel(name = "会员活跃度", cellType = ColumnType.NUMERIC)
    private BigDecimal memberActivityRate;

    @Excel(name = "新会员数量环比", cellType = ColumnType.NUMERIC)
    private BigDecimal newMembersGrowth;

    @Excel(name = "复购率环比", cellType = ColumnType.NUMERIC)
    private BigDecimal repeatPurchaseGrowth;

    @Excel(name = "MOT成功率环比", cellType = ColumnType.NUMERIC)
    private BigDecimal motSuccessGrowth;

    @Excel(name = "会员活跃度环比", cellType = ColumnType.NUMERIC)
    private BigDecimal memberActivityGrowth;

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public Integer getNewMembers() { return newMembers; }
    public void setNewMembers(Integer newMembers) { this.newMembers = newMembers; }
    public BigDecimal getRepeatPurchaseRate() { return repeatPurchaseRate; }
    public void setRepeatPurchaseRate(BigDecimal repeatPurchaseRate) { this.repeatPurchaseRate = repeatPurchaseRate; }
    public BigDecimal getMotSuccessRate() { return motSuccessRate; }
    public void setMotSuccessRate(BigDecimal motSuccessRate) { this.motSuccessRate = motSuccessRate; }
    public BigDecimal getMemberActivityRate() { return memberActivityRate; }
    public void setMemberActivityRate(BigDecimal memberActivityRate) { this.memberActivityRate = memberActivityRate; }
    public BigDecimal getNewMembersGrowth() { return newMembersGrowth; }
    public void setNewMembersGrowth(BigDecimal newMembersGrowth) { this.newMembersGrowth = newMembersGrowth; }
    public BigDecimal getRepeatPurchaseGrowth() { return repeatPurchaseGrowth; }
    public void setRepeatPurchaseGrowth(BigDecimal repeatPurchaseGrowth) { this.repeatPurchaseGrowth = repeatPurchaseGrowth; }
    public BigDecimal getMotSuccessGrowth() { return motSuccessGrowth; }
    public void setMotSuccessGrowth(BigDecimal motSuccessGrowth) { this.motSuccessGrowth = motSuccessGrowth; }
    public BigDecimal getMemberActivityGrowth() { return memberActivityGrowth; }
    public void setMemberActivityGrowth(BigDecimal memberActivityGrowth) { this.memberActivityGrowth = memberActivityGrowth; }
}

