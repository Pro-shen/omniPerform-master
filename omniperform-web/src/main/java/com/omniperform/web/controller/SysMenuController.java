package com.omniperform.web.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.omniperform.common.annotation.Anonymous;
import com.omniperform.web.common.Result;
import com.omniperform.common.core.domain.entity.SysMenu;
import com.omniperform.common.core.domain.entity.SysUser;
import com.omniperform.system.service.ISysMenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 菜单管理控制器
 * 
 * @author omniperform
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/api/menu")
public class SysMenuController {
    
    private static final Logger log = LoggerFactory.getLogger(SysMenuController.class);
    
    @Autowired
    private ISysMenuService menuService;
    
    /**
     * 测试匿名访问
     */
    @Anonymous
    @GetMapping("/test")
    @ApiOperation("测试匿名访问")
    public Result testAnonymous() {
        log.info("匿名访问测试成功");
        return Result.success("匿名访问测试成功", "Hello Anonymous!");
    }
    
    /**
     * 获取菜单列表
     */
    @Anonymous
    @GetMapping("/list")
    @ApiOperation("获取菜单列表")
    public Result getMenuList() {
        try {
            List<SysMenu> menus = menuService.selectMenuAll(null);
            log.info("获取菜单列表成功，共{}个菜单", menus.size());
            return Result.success("获取成功", menus);
        } catch (Exception e) {
            log.error("获取菜单列表失败: {}", e.getMessage(), e);
            return Result.error("获取菜单列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据用户获取菜单
     */
    @Anonymous
    @GetMapping("/user/{userId}")
    @ApiOperation("根据用户ID获取菜单")
    public Result getMenusByUser(@PathVariable Long userId) {
        try {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysMenu> menus = menuService.selectMenusByUser(user);
            log.info("获取用户{}菜单成功，共{}个菜单", userId, menus.size());
            return Result.success("获取成功", menus);
        } catch (Exception e) {
            log.error("获取用户菜单失败: {}", e.getMessage(), e);
            return Result.error("获取用户菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据菜单ID获取菜单详情
     */
    @Anonymous
    @GetMapping("/{menuId}")
    @ApiOperation("根据菜单ID获取菜单详情")
    public Result getMenuById(@PathVariable Long menuId) {
        try {
            SysMenu menu = menuService.selectMenuById(menuId);
            if (menu == null) {
                return Result.error("菜单不存在");
            }
            return Result.success("获取成功", menu);
        } catch (Exception e) {
            log.error("获取菜单详情失败: {}", e.getMessage(), e);
            return Result.error("获取菜单详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增菜单
     */
    @Anonymous
    @PostMapping
    @ApiOperation("新增菜单")
    public Result addMenu(@RequestBody SysMenu menu) {
        try {
            if (menuService.checkMenuNameUnique(menu)) {
                return Result.error("菜单名称已存在");
            }
            
            int result = menuService.insertMenu(menu);
            if (result > 0) {
                log.info("新增菜单成功: {}", menu.getMenuName());
                return Result.success("新增成功");
            } else {
                return Result.error("新增失败");
            }
        } catch (Exception e) {
            log.error("新增菜单失败: {}", e.getMessage(), e);
            return Result.error("新增菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 修改菜单
     */
    @Anonymous
    @PutMapping
    @ApiOperation("修改菜单")
    public Result updateMenu(@RequestBody SysMenu menu) {
        try {
            if (menuService.checkMenuNameUnique(menu)) {
                return Result.error("菜单名称已存在");
            }
            
            int result = menuService.updateMenu(menu);
            if (result > 0) {
                log.info("修改菜单成功: {}", menu.getMenuName());
                return Result.success("修改成功");
            } else {
                return Result.error("修改失败");
            }
        } catch (Exception e) {
            log.error("修改菜单失败: {}", e.getMessage(), e);
            return Result.error("修改菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除菜单
     */
    @Anonymous
    @DeleteMapping("/{menuId}")
    @ApiOperation("删除菜单")
    public Result deleteMenu(@PathVariable Long menuId) {
        try {
            // 检查是否有子菜单
            if (menuService.selectCountMenuByParentId(menuId) > 0) {
                return Result.error("存在子菜单，不允许删除");
            }
            
            // 检查菜单是否被角色使用
            if (menuService.selectCountRoleMenuByMenuId(menuId) > 0) {
                return Result.error("菜单已分配给角色，不允许删除");
            }
            
            int result = menuService.deleteMenuById(menuId);
            if (result > 0) {
                log.info("删除菜单成功，菜单ID: {}", menuId);
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除菜单失败: {}", e.getMessage(), e);
            return Result.error("删除菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取菜单树形结构
     */
    @Anonymous
    @GetMapping("/tree")
    @ApiOperation("获取菜单树形结构")
    public Result getMenuTree() {
        try {
            List<SysMenu> menus = menuService.selectMenuAll(null);
            
            // 构建树形结构
            Map<String, Object> data = new HashMap<>();
            data.put("menus", menus);
            
            return Result.success("获取成功", data);
        } catch (Exception e) {
            log.error("获取菜单树失败: {}", e.getMessage(), e);
            return Result.error("获取菜单树失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据用户ID获取有权限访问的菜单（用于前端权限控制）
     */
    @Anonymous
    @GetMapping("/permissions/{userId}")
    @ApiOperation("根据用户ID获取有权限访问的菜单")
    public Result getUserMenuPermissions(@PathVariable Long userId) {
        try {
            log.info("获取用户{}的菜单权限", userId);
            
            SysUser user = new SysUser();
            user.setUserId(userId);
            
            // 获取用户有权限的菜单
            List<SysMenu> userMenus = menuService.selectMenusByUser(user);
            
            // 构建权限数据，包含菜单ID、名称、URL等信息
            Map<String, Object> permissionData = new HashMap<>();
            permissionData.put("userId", userId);
            permissionData.put("menus", userMenus);
            
            // 提取菜单URL列表，用于前端快速权限检查
            List<String> allowedUrls = new java.util.ArrayList<>();
            List<Long> allowedMenuIds = new java.util.ArrayList<>();
            
            for (SysMenu menu : userMenus) {
                if (menu.getUrl() != null && !menu.getUrl().isEmpty()) {
                    allowedUrls.add(menu.getUrl());
                }
                allowedMenuIds.add(menu.getMenuId());
            }
            
            permissionData.put("allowedUrls", allowedUrls);
            permissionData.put("allowedMenuIds", allowedMenuIds);
            
            log.info("用户{}有权限访问{}个菜单，包含{}个URL", userId, userMenus.size(), allowedUrls.size());
            
            return Result.success("获取成功", permissionData);
        } catch (Exception e) {
            log.error("获取用户菜单权限失败: {}", e.getMessage(), e);
            return Result.error("获取用户菜单权限失败: " + e.getMessage());
        }
    }
}