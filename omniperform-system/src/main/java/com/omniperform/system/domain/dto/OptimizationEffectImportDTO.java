package com.omniperform.system.domain.dto;

import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据闭环优化效果导入DTO
 * 用于Excel导入时的宽表结构，对应界面图表的三个主要指标
 */
public class OptimizationEffectImportDTO {
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计日期", width = 30, dateFormat = "yyyy-MM-dd", sort = 1)
    private Date statDate;

    @Excel(name = "MOT执行率(%)", cellType = ColumnType.NUMERIC, sort = 2)
    private BigDecimal motExecutionRate;

    @Excel(name = "会员活跃度(%)", cellType = ColumnType.NUMERIC, sort = 3)
    private BigDecimal memberActivityRate;

    @Excel(name = "复购率(%)", cellType = ColumnType.NUMERIC, sort = 4)
    private BigDecimal repurchaseRate;

    private String regionCode;

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public BigDecimal getMotExecutionRate() {
        return motExecutionRate;
    }

    public void setMotExecutionRate(BigDecimal motExecutionRate) {
        this.motExecutionRate = motExecutionRate;
    }

    public BigDecimal getMemberActivityRate() {
        return memberActivityRate;
    }

    public void setMemberActivityRate(BigDecimal memberActivityRate) {
        this.memberActivityRate = memberActivityRate;
    }

    public BigDecimal getRepurchaseRate() {
        return repurchaseRate;
    }

    public void setRepurchaseRate(BigDecimal repurchaseRate) {
        this.repurchaseRate = repurchaseRate;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }
}
