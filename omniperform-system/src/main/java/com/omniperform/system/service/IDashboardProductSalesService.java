package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.DashboardProductSales;

/**
 * 产品销售Service接口
 * 
 * @author omniperform
 * @date 2025-01-27
 */
public interface IDashboardProductSalesService 
{
    /**
     * 查询产品销售
     * 
     * @param id 产品销售主键
     * @return 产品销售
     */
    public DashboardProductSales selectDashboardProductSalesById(Long id);

    /**
     * 根据数据月份查询产品销售
     * 
     * @param dataMonth 数据月份
     * @return 产品销售集合
     */
    public List<DashboardProductSales> selectByDataMonth(String dataMonth);

    /**
     * 查询产品销售列表
     * 
     * @param dashboardProductSales 产品销售
     * @return 产品销售集合
     */
    public List<DashboardProductSales> selectDashboardProductSalesList(DashboardProductSales dashboardProductSales);

    /**
     * 新增产品销售
     * 
     * @param dashboardProductSales 产品销售
     * @return 结果
     */
    public int insertDashboardProductSales(DashboardProductSales dashboardProductSales);

    /**
     * 修改产品销售
     * 
     * @param dashboardProductSales 产品销售
     * @return 结果
     */
    public int updateDashboardProductSales(DashboardProductSales dashboardProductSales);

    /**
     * 批量删除产品销售
     * 
     * @param ids 需要删除的产品销售主键集合
     * @return 结果
     */
    public int deleteDashboardProductSalesByIds(String ids);

    /**
     * 删除产品销售信息
     * 
     * @param id 产品销售主键
     * @return 结果
     */
    public int deleteDashboardProductSalesById(Long id);

    /**
     * 查询指定月份范围的产品销售
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 产品销售集合
     */
    public List<DashboardProductSales> selectByMonthRange(String startMonth, String endMonth);
}