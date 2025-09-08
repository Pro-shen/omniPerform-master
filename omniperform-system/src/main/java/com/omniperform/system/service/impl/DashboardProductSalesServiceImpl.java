package com.omniperform.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.mapper.DashboardProductSalesMapper;
import com.omniperform.system.domain.DashboardProductSales;
import com.omniperform.system.service.IDashboardProductSalesService;
import com.omniperform.common.core.text.Convert;

/**
 * 产品销售Service业务层处理
 * 
 * @author omniperform
 * @date 2025-01-27
 */
@Service
public class DashboardProductSalesServiceImpl implements IDashboardProductSalesService 
{
    @Autowired
    private DashboardProductSalesMapper dashboardProductSalesMapper;

    /**
     * 查询产品销售
     * 
     * @param id 产品销售主键
     * @return 产品销售
     */
    @Override
    public DashboardProductSales selectDashboardProductSalesById(Long id)
    {
        return dashboardProductSalesMapper.selectDashboardProductSalesById(id);
    }

    /**
     * 根据数据月份查询产品销售
     * 
     * @param dataMonth 数据月份
     * @return 产品销售集合
     */
    @Override
    public List<DashboardProductSales> selectByDataMonth(String dataMonth)
    {
        return dashboardProductSalesMapper.selectByDataMonth(dataMonth);
    }

    /**
     * 查询产品销售列表
     * 
     * @param dashboardProductSales 产品销售
     * @return 产品销售
     */
    @Override
    public List<DashboardProductSales> selectDashboardProductSalesList(DashboardProductSales dashboardProductSales)
    {
        return dashboardProductSalesMapper.selectDashboardProductSalesList(dashboardProductSales);
    }

    /**
     * 新增产品销售
     * 
     * @param dashboardProductSales 产品销售
     * @return 结果
     */
    @Override
    public int insertDashboardProductSales(DashboardProductSales dashboardProductSales)
    {
        return dashboardProductSalesMapper.insertDashboardProductSales(dashboardProductSales);
    }

    /**
     * 修改产品销售
     * 
     * @param dashboardProductSales 产品销售
     * @return 结果
     */
    @Override
    public int updateDashboardProductSales(DashboardProductSales dashboardProductSales)
    {
        return dashboardProductSalesMapper.updateDashboardProductSales(dashboardProductSales);
    }

    /**
     * 批量删除产品销售
     * 
     * @param ids 需要删除的产品销售主键
     * @return 结果
     */
    @Override
    public int deleteDashboardProductSalesByIds(String ids)
    {
        return dashboardProductSalesMapper.deleteDashboardProductSalesByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除产品销售信息
     * 
     * @param id 产品销售主键
     * @return 结果
     */
    @Override
    public int deleteDashboardProductSalesById(Long id)
    {
        return dashboardProductSalesMapper.deleteDashboardProductSalesById(id);
    }

    /**
     * 查询指定月份范围的产品销售
     * 
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 产品销售集合
     */
    @Override
    public List<DashboardProductSales> selectByMonthRange(String startMonth, String endMonth)
    {
        return dashboardProductSalesMapper.selectByMonthRange(startMonth, endMonth);
    }
}