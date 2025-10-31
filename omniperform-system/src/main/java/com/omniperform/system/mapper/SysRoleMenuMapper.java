package com.omniperform.system.mapper;

import java.util.List;
import com.omniperform.system.domain.SysRoleMenu;

/**
 * 角色与菜单关联表 数据层
 * 
 * @author omniperform
 */
public interface SysRoleMenuMapper
{
    /**
     * 通过角色ID删除角色和菜单关联
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleMenuByRoleId(Long roleId);
    
    /**
     * 批量删除角色菜单关联信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleMenu(Long[] ids);
    
    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    public int selectCountRoleMenuByMenuId(Long menuId);
    
    /**
     * 批量新增角色菜单信息
     * 
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);
    
    /**
     * 根据角色ID查询已分配的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    public List<Long> selectMenuIdsByRoleId(Long roleId);
}
