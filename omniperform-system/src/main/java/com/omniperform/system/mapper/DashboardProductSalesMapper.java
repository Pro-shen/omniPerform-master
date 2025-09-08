package com.omniperform.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.DashboardProductSales;

/**
 * 产品销售数据 数据层
 * 
 * @author omniperform
 */
public interface DashboardProductSalesMapper
{
    public DashboardProductSales selectDashboardProductSales(DashboardProductSales sales);
    public DashboardProductSales selectDashboardProductSalesById(Long id);
    public List<DashboardProductSales> selectDashboardProductSalesList(DashboardProductSales sales);
    public List<DashboardProductSales> selectByDataMonth(String dataMonth);
    public List<DashboardProductSales> selectByMonthRange(@Param("startMonth") String startMonth, @Param("endMonth") String endMonth);
    public int insertDashboardProductSales(DashboardProductSales sales);
    public int updateDashboardProductSales(DashboardProductSales sales);
    public int deleteDashboardProductSalesById(Long id);
    public int deleteDashboardProductSalesByIds(String[] ids);
}