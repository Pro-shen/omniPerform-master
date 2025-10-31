package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.core.domain.entity.SysRole;
import com.omniperform.common.core.domain.entity.SysMenu;
import com.omniperform.system.service.ISysRoleService;
import com.omniperform.system.service.ISysMenuService;
import com.omniperform.common.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 角色管理控制器
 * 
 * @author omniperform
 */
@RestController
@RequestMapping("/system/role")
@Api(tags = "角色管理")
public class SysRoleController {

    private static final Logger log = LoggerFactory.getLogger(SysRoleController.class);

    @Autowired
    private ISysRoleService roleService;
    
    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色列表
     */
    @Anonymous
    @GetMapping("/list")
    @ApiOperation("获取角色列表")
    public Result getRoleList() {
        try {
            List<SysRole> roles = roleService.selectRoleAll();
            log.info("获取角色列表成功，共{}个角色", roles.size());
            return Result.success("获取成功", roles);
        } catch (Exception e) {
            log.error("获取角色列表失败: {}", e.getMessage(), e);
            return Result.error("获取角色列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取角色详情
     */
    @Anonymous
    @GetMapping("/{roleId}")
    @ApiOperation("获取角色详情")
    public Result getRoleById(@PathVariable Long roleId) {
        try {
            SysRole role = roleService.selectRoleById(roleId);
            if (role != null) {
                return Result.success("获取成功", role);
            } else {
                return Result.error("角色不存在");
            }
        } catch (Exception e) {
            log.error("获取角色详情失败: {}", e.getMessage(), e);
            return Result.error("获取角色详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建新角色
     */
    @Anonymous
    @PostMapping
    @ApiOperation("创建角色")
    public Result createRole(@RequestBody SysRole role) {
        try {
            // 校验角色名称唯一性
            if (!roleService.checkRoleNameUnique(role)) {
                return Result.error("角色名称已存在");
            }
            
            // 校验角色权限标识唯一性
            if (!roleService.checkRoleKeyUnique(role)) {
                return Result.error("角色权限标识已存在");
            }
            
            int result = roleService.insertRole(role);
            if (result > 0) {
                log.info("创建角色成功: {}", role.getRoleName());
                return Result.success("创建成功", role);
            } else {
                return Result.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建角色失败: {}", e.getMessage(), e);
            return Result.error("创建角色失败: " + e.getMessage());
        }
    }

    /**
     * 更新角色
     */
    @Anonymous
    @PutMapping("/{roleId}")
    @ApiOperation("更新角色")
    public Result updateRole(@PathVariable Long roleId, @RequestBody SysRole role) {
        try {
            role.setRoleId(roleId);
            
            // 校验角色是否允许操作
            roleService.checkRoleAllowed(role);
            
            // 校验角色名称唯一性
            if (!roleService.checkRoleNameUnique(role)) {
                return Result.error("角色名称已存在");
            }
            
            // 校验角色权限标识唯一性
            if (!roleService.checkRoleKeyUnique(role)) {
                return Result.error("角色权限标识已存在");
            }
            
            int result = roleService.updateRole(role);
            if (result > 0) {
                log.info("更新角色成功: {}", role.getRoleName());
                return Result.success("更新成功", role);
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新角色失败: {}", e.getMessage(), e);
            return Result.error("更新角色失败: " + e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @Anonymous
    @DeleteMapping("/{roleId}")
    @ApiOperation("删除角色")
    public Result deleteRole(@PathVariable Long roleId) {
        try {
            SysRole role = roleService.selectRoleById(roleId);
            if (role == null) {
                return Result.error("角色不存在");
            }
            
            // 校验角色是否允许操作
            roleService.checkRoleAllowed(role);
            
            // 检查角色是否被使用
            int userCount = roleService.countUserRoleByRoleId(roleId);
            if (userCount > 0) {
                return Result.error("角色已分配给用户，不能删除");
            }
            
            boolean result = roleService.deleteRoleById(roleId);
            if (result) {
                log.info("删除角色成功: {}", role.getRoleName());
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除角色失败: {}", e.getMessage(), e);
            return Result.error("删除角色失败: " + e.getMessage());
        }
    }

    /**
     * 获取角色菜单权限
     */
    @Anonymous
    @GetMapping("/{roleId}/menus")
    @ApiOperation("获取角色菜单权限")
    public Result getRoleMenus(@PathVariable Long roleId) {
        try {
            SysRole role = roleService.selectRoleById(roleId);
            if (role == null) {
                return Result.error("角色不存在");
            }
            
            // 获取所有菜单和角色已分配的菜单
            List<SysMenu> allMenus = menuService.selectMenuAll(null);
            List<Long> checkedMenuIds = roleService.selectMenuIdsByRoleId(roleId);

            log.info("角色[{}] 已分配菜单ID: {}", role.getRoleName(), checkedMenuIds);
            
            Map<String, Object> data = new HashMap<>();
            data.put("role", role);
            data.put("menus", allMenus);
            data.put("checkedKeys", checkedMenuIds);
            
            return Result.success("获取成功", data);
        } catch (Exception e) {
            log.error("获取角色菜单权限失败: {}", e.getMessage(), e);
            return Result.error("获取角色菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 修改角色状态
     */
    @Anonymous
    @PutMapping("/{roleId}/status")
    @ApiOperation("修改角色状态")
    public Result changeStatus(@PathVariable Long roleId, @RequestBody Map<String, String> statusData) {
        try {
            SysRole role = roleService.selectRoleById(roleId);
            if (role == null) {
                return Result.error("角色不存在");
            }
            
            role.setStatus(statusData.get("status"));
            
            int result = roleService.changeStatus(role);
            if (result > 0) {
                log.info("修改角色状态成功: {} -> {}", role.getRoleName(), role.getStatus());
                return Result.success("修改成功");
            } else {
                return Result.error("修改失败");
            }
        } catch (Exception e) {
            log.error("修改角色状态失败: {}", e.getMessage(), e);
            return Result.error("修改角色状态失败: " + e.getMessage());
        }
    }

    /**
     * 为角色分配菜单权限
     */
    @Anonymous
    @PutMapping("/{roleId}/menus")
    @ApiOperation("为角色分配菜单权限")
    public Result assignMenusToRole(@PathVariable Long roleId, @RequestBody Map<String, Object> menuData) {
        try {
            SysRole role = roleService.selectRoleById(roleId);
            if (role == null) {
                return Result.error("角色不存在");
            }
            
            // 校验角色是否允许操作
            roleService.checkRoleAllowed(role);
            
            @SuppressWarnings("unchecked")
            List<Long> menuIds = (List<Long>) menuData.get("menuIds");
            role.setMenuIds(menuIds.toArray(new Long[0]));
            
            int result = roleService.updateRole(role);
            if (result > 0) {
                log.info("为角色分配菜单权限成功: {} -> {}", role.getRoleName(), menuIds);
                return Result.success("分配成功");
            } else {
                return Result.error("分配失败");
            }
        } catch (Exception e) {
            log.error("为角色分配菜单权限失败: {}", e.getMessage(), e);
            return Result.error("分配菜单权限失败: " + e.getMessage());
        }
    }
}