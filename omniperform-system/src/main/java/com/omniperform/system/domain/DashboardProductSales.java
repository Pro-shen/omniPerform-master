package com.omniperform.system.domain;

import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.omniperform.common.annotation.Excel;
import com.omniperform.common.annotation.Excel.ColumnType;
import com.omniperform.common.core.domain.BaseEntity;

/**
 * 产品销售数据表 dashboard_product_sales
 * 
 * @author omniperform
 */
public class DashboardProductSales extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "主键ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 数据月份(格式:2025-05) */
    @Excel(name = "数据月份", cellType = ColumnType.TEXT)
    private String dataMonth;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 销售金额 */
    @Excel(name = "销售金额", cellType = ColumnType.NUMERIC)
    private BigDecimal salesAmount;

    /** 销售数量 */
    @Excel(name = "销售数量", cellType = ColumnType.NUMERIC)
    private Integer salesQuantity;

    /** 市场份额(%) */
    @Excel(name = "市场份额", cellType = ColumnType.NUMERIC)
    private BigDecimal marketShare;

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

    @NotBlank(message = "产品名称不能为空")
    @Size(min = 0, max = 100, message = "产品名称长度不能超过100个字符")
    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public BigDecimal getSalesAmount()
    {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount)
    {
        this.salesAmount = salesAmount;
    }

    public Integer getSalesQuantity()
    {
        return salesQuantity;
    }

    public void setSalesQuantity(Integer salesQuantity)
    {
        this.salesQuantity = salesQuantity;
    }

    public BigDecimal getMarketShare()
    {
        return marketShare;
    }

    public void setMarketShare(BigDecimal marketShare)
    {
        this.marketShare = marketShare;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dataMonth", getDataMonth())
            .append("productName", getProductName())
            .append("salesAmount", getSalesAmount())
            .append("salesQuantity", getSalesQuantity())
            .append("marketShare", getMarketShare())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}