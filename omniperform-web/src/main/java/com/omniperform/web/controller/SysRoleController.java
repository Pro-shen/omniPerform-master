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
import java.util.ArrayList;
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
     * 获取角色权限菜单（包含已分配和可选菜单）
     */
    @Anonymous
    @GetMapping("/menus")
    @ApiOperation("获取角色权限菜单")
    public Result getRolePermissions(@RequestParam Long roleId) {
        try {
            log.info("请求获取角色菜单权限, roleId={}", roleId);
            
            SysRole role = roleService.selectRoleById(roleId);
            if (role == null) {
                log.warn("角色不存在, roleId={}", roleId);
                return Result.error("角色不存在");
            }
            
            // 获取所有菜单和角色已分配的菜单
            // 使用管理员权限(userId=1L)获取所有菜单
            List<SysMenu> allMenus = menuService.selectMenuAll(1L);
            List<Long> checkedMenuIds = roleService.selectMenuIdsByRoleId(roleId);

            log.info("系统全部菜单数量={}, 返回给前端的 checkedKeys 数量={}", 
                     allMenus.size(), checkedMenuIds.size());
            log.info("角色[{}] 已分配菜单ID: {}", role.getRoleName(), checkedMenuIds);
            
            // 打印前几个菜单的详细信息用于调试
            if (!allMenus.isEmpty()) {
                log.info("前5个菜单示例:");
                for (int i = 0; i < Math.min(5, allMenus.size()); i++) {
                    SysMenu menu = allMenus.get(i);
                    log.info("  菜单ID={}, 菜单名={}, 父级ID={}", 
                             menu.getMenuId(), menu.getMenuName(), menu.getParentId());
                }
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("role", role);
            data.put("menus", allMenus);
            data.put("checkedKeys", checkedMenuIds);
            
            log.info("成功返回角色菜单权限数据, data.keys={}", data.keySet());
            return Result.success("获取成功", data);
        } catch (Exception e) {
            log.error("获取角色菜单权限失败: {}", e.getMessage(), e);
            return Result.error("获取角色菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有可用权限菜单
     */
    @Anonymous
    @GetMapping("/menus/all")
    @ApiOperation("获取所有可用权限菜单")
    public Result getAllPermissions() {
        try {
            // 使用管理员权限(userId=1L)获取所有菜单
            List<SysMenu> allMenus = menuService.selectMenuAll(1L);
            log.info("获取所有权限菜单成功，共{}个菜单", allMenus.size());
            return Result.success("获取成功", allMenus);
        } catch (Exception e) {
            log.error("获取所有权限菜单失败: {}", e.getMessage(), e);
            return Result.error("获取所有权限菜单失败: " + e.getMessage());
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
     * 分配角色权限
     */
    @Anonymous
    @PutMapping("/menus")
    @ApiOperation("分配角色权限")
    public Result assignRolePermissions(@RequestParam Long roleId, @RequestBody Map<String, Object> permissionData) {
        try {
            log.info("开始分配角色权限, roleId={}, permissionData={}", roleId, permissionData);
            
            SysRole role = roleService.selectRoleById(roleId);
            if (role == null) {
                log.warn("角色不存在, roleId={}", roleId);
                return Result.error("角色不存在");
            }
            
            // 校验角色是否允许操作
            roleService.checkRoleAllowed(role);
            
            @SuppressWarnings("unchecked")
            List<Object> menuIdObjects = (List<Object>) permissionData.get("menuIds");
            log.info("接收到的menuIds原始数据: {}, 类型: {}", menuIdObjects, 
                     menuIdObjects != null ? menuIdObjects.getClass().getSimpleName() : "null");
            
            List<Long> menuIds = new ArrayList<>();
            if (menuIdObjects != null) {
                for (Object obj : menuIdObjects) {
                    log.debug("处理menuId对象: {}, 类型: {}", obj, obj != null ? obj.getClass().getSimpleName() : "null");
                    try {
                        if (obj instanceof Number) {
                            menuIds.add(((Number) obj).longValue());
                        } else if (obj instanceof String) {
                            // 处理字符串类型的menuId
                            String strValue = (String) obj;
                            if (!strValue.trim().isEmpty()) {
                                menuIds.add(Long.parseLong(strValue));
                            }
                        }
                    } catch (NumberFormatException e) {
                        log.warn("无法转换menuId: {}, 错误: {}", obj, e.getMessage());
                    }
                }
            }
            
            log.info("转换后的menuIds: {}, 数量: {}", menuIds, menuIds.size());
            role.setMenuIds(menuIds.toArray(new Long[0]));
            log.info("设置到role对象的menuIds: {}", java.util.Arrays.toString(role.getMenuIds()));
            
            int result = roleService.updateRole(role);
            log.info("updateRole执行结果: {}", result);
            
            if (result > 0) {
                log.info("为角色分配菜单权限成功: {} -> {}", role.getRoleName(), menuIds);
                return Result.success("分配成功");
            } else {
                log.error("updateRole返回结果为0，分配失败");
                return Result.error("分配失败");
            }
        } catch (Exception e) {
            log.error("为角色分配菜单权限失败: {}", e.getMessage(), e);
            return Result.error("分配菜单权限失败: " + e.getMessage());
        }
    }
}