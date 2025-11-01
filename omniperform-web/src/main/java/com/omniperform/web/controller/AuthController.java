package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.core.domain.entity.SysUser;
import com.omniperform.common.core.domain.entity.SysMenu;
import com.omniperform.framework.shiro.service.SysLoginService;
import com.omniperform.system.service.ISysUserService;
import com.omniperform.system.service.ISysMenuService;
import com.omniperform.common.utils.StringUtils;
import com.omniperform.common.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * 用户认证控制器
 * 
 * @author omniperform
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "用户认证管理")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private SysLoginService loginService;
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysMenuService menuService;

    /**
     * 用户登录
     */
    @Anonymous
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result login(@RequestBody Map<String, String> loginForm) {
        try {
            String username = loginForm.get("username");
            String password = loginForm.get("password");
            
            log.info("用户登录请求: username={}", username);
            
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                return Result.error("用户名和密码不能为空");
            }
            
            // 调用现有的登录服务进行验证
            SysUser user = loginService.login(username, password);
            
            if (user != null) {
                // 生成简单的token
                String token = generateToken(user);
                
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                data.put("user", createUserInfo(user));
                
                log.info("用户登录成功: username={}, userId={}", username, user.getUserId());
                
//                // 获取并打印用户可访问的菜单列表
//                printUserMenuPermissions(user);
                
                return Result.success("登录成功", data);
            } else {
                log.warn("用户登录失败: username={}", username);
                return Result.error("用户名或密码错误");
            }
        } catch (Exception e) {
            log.error("登录异常: {}", e.getMessage(), e);
            return Result.error("登录失败: " + e.getMessage());
        }
    }

    /**
     * 用户退出
     */
    @Anonymous
    @PostMapping("/logout")
    @ApiOperation("用户退出")
    public Result logout() {
        try {
            // 这里可以添加清除session等逻辑
            return Result.success("退出成功");
        } catch (Exception e) {
            log.error("退出异常: {}", e.getMessage(), e);
            return Result.error("退出失败");
        }
    }

    /**
     * 验证Token有效性
     */
    @Anonymous
    @GetMapping("/validate-token")
    @ApiOperation("验证Token有效性")
    public Result validateToken(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (StringUtils.isEmpty(token)) {
                return Result.error("Token不能为空");
            }
            
            // 简单的token验证逻辑
            if (isValidToken(token)) {
                return Result.success("Token有效", true);
            } else {
                return Result.error("Token无效或已过期");
            }
        } catch (Exception e) {
            log.error("Token验证异常: {}", e.getMessage(), e);
            return Result.error("Token验证失败");
        }
    }

    /**
     * 刷新Token
     */
    @Anonymous
    @PostMapping("/refresh-token")
    @ApiOperation("刷新Token")
    public Result refreshToken(@RequestHeader(value = "Authorization", required = false) String oldToken) {
        try {
            if (StringUtils.isEmpty(oldToken)) {
                return Result.error("原Token不能为空");
            }
            
            if (isValidToken(oldToken)) {
                // 生成新token
                String newToken = generateRefreshToken(oldToken);
                
                Map<String, Object> data = new HashMap<>();
                data.put("token", newToken);
                
                return Result.success("Token刷新成功", data);
            } else {
                return Result.error("原Token无效，无法刷新");
            }
        } catch (Exception e) {
            log.error("Token刷新异常: {}", e.getMessage(), e);
            return Result.error("Token刷新失败");
        }
    }

    /**
     * 生成Token
     */
    private String generateToken(SysUser user) {
        // 简单的token生成逻辑：base64(username:timestamp:userId)
        long timestamp = System.currentTimeMillis();
        String tokenData = user.getLoginName() + ":" + timestamp + ":" + user.getUserId();
        return java.util.Base64.getEncoder().encodeToString(tokenData.getBytes());
    }

    /**
     * 验证Token有效性
     */
    private boolean isValidToken(String token) {
        try {
            // 移除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            String tokenData = new String(java.util.Base64.getDecoder().decode(token));
            String[] parts = tokenData.split(":");
            
            if (parts.length != 3) {
                return false;
            }
            
            String username = parts[0];
            long timestamp = Long.parseLong(parts[1]);
            Long userId = Long.parseLong(parts[2]);
            
            // 检查token是否过期（24小时）
            long currentTime = System.currentTimeMillis();
            long maxAge = 24 * 60 * 60 * 1000; // 24小时
            
            if (currentTime - timestamp > maxAge) {
                log.warn("Token已过期: username={}", username);
                return false;
            }
            
            // 验证用户是否存在
            SysUser user = userService.selectUserByLoginName(username);
            if (user == null || !user.getUserId().equals(userId)) {
                log.warn("Token对应用户不存在或ID不匹配: username={}", username);
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("Token验证解析异常: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 生成刷新后的Token
     */
    private String generateRefreshToken(String oldToken) {
        try {
            if (oldToken.startsWith("Bearer ")) {
                oldToken = oldToken.substring(7);
            }
            
            String tokenData = new String(java.util.Base64.getDecoder().decode(oldToken));
            String[] parts = tokenData.split(":");
            
            String username = parts[0];
            Long userId = Long.parseLong(parts[2]);
            
            // 生成新的token
            long newTimestamp = System.currentTimeMillis();
            String newTokenData = username + ":" + newTimestamp + ":" + userId;
            return java.util.Base64.getEncoder().encodeToString(newTokenData.getBytes());
        } catch (Exception e) {
            log.error("Token刷新生成异常: {}", e.getMessage());
            throw new RuntimeException("Token刷新失败");
        }
    }

    /**
     * 创建用户信息
     */
    private Map<String, Object> createUserInfo(SysUser user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getUserId());
        userInfo.put("username", user.getLoginName());
        userInfo.put("nickName", user.getUserName());
        userInfo.put("email", user.getEmail());
        userInfo.put("phonenumber", user.getPhonenumber());
        userInfo.put("status", user.getStatus());
        userInfo.put("deptId", user.getDeptId());
        return userInfo;
    }
    
    /**
     * 打印用户菜单权限信息
     */
    private void printUserMenuPermissions(SysUser user) {
        try {
            log.info("========== 用户菜单权限信息 ==========");
            log.info("用户ID: {}", user.getUserId());
            log.info("用户名: {} ({})", user.getUserName(), user.getLoginName());
            
            // 直接获取用户的所有菜单权限（不经过层级过滤）
            List<SysMenu> allUserMenus;
            if (user.isAdmin()) {
                allUserMenus = menuService.selectMenuAll(user.getUserId());
            } else {
                // 直接调用mapper获取完整的菜单列表
                allUserMenus = menuService.selectMenuList(new SysMenu(), user.getUserId());
            }
            
            if (allUserMenus == null || allUserMenus.isEmpty()) {
                log.info("该用户没有分配任何菜单权限");
                log.info("=====================================");
                return;
            }
            
            log.info("用户可访问的界面列表 (共{}个):", allUserMenus.size());
            log.info("-------------------------------------");
            
            // 按层级打印菜单，包括孤儿菜单
            printMenuHierarchyWithOrphans(allUserMenus);
            
            // 打印所有可访问的URL
            log.info("-------------------------------------");
            log.info("可访问的URL列表:");
            for (SysMenu menu : allUserMenus) {
                if (StringUtils.isNotEmpty(menu.getUrl()) && !"#".equals(menu.getUrl())) {
                    log.info("  - {} ({}) [ID:{}]", menu.getUrl(), menu.getMenuName(), menu.getMenuId());
                }
            }
            
            log.info("=====================================");
            
        } catch (Exception e) {
            log.error("打印用户菜单权限信息时发生异常: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 打印菜单层级结构，包括孤儿菜单处理
     */
    private void printMenuHierarchyWithOrphans(List<SysMenu> menus) {
        // 创建一个Set来存储所有存在的菜单ID
        java.util.Set<Long> existingMenuIds = new java.util.HashSet<>();
        for (SysMenu menu : menus) {
            existingMenuIds.add(menu.getMenuId());
        }
        
        // 首先打印所有顶级菜单（parentId = 0）
        printMenuHierarchy(menus, 0, "");
        
        // 然后找出并打印孤儿菜单（父级不在用户权限中的菜单）
        for (SysMenu menu : menus) {
            if (menu.getParentId() != null && menu.getParentId() != 0 && !existingMenuIds.contains(menu.getParentId())) {
                // 这是一个孤儿菜单，将其作为顶级菜单显示
                if ("M".equals(menu.getMenuType()) || "C".equals(menu.getMenuType())) {
                    String menuInfo = String.format("├─ %s [ID:%d]", menu.getMenuName(), menu.getMenuId());
                    if (StringUtils.isNotEmpty(menu.getUrl()) && !"#".equals(menu.getUrl())) {
                        menuInfo += String.format(" [%s]", menu.getUrl());
                    }
                    if (StringUtils.isNotEmpty(menu.getMenuType())) {
                        String typeDesc = getMenuTypeDescription(menu.getMenuType());
                        menuInfo += String.format(" (%s)", typeDesc);
                    }
                    log.info(menuInfo);
                    
                    // 递归打印这个孤儿菜单的子菜单
                    printMenuHierarchy(menus, menu.getMenuId(), "│  ");
                }
            }
        }
    }
    
    /**
     * 递归打印菜单层级结构
     */
    private void printMenuHierarchy(List<SysMenu> menus, long parentId, String prefix) {
        for (SysMenu menu : menus) {
            if (menu.getParentId() != null && menu.getParentId().equals(parentId)) {
                // 只显示目录(M)和菜单(C)，过滤掉按钮(F)
                if ("M".equals(menu.getMenuType()) || "C".equals(menu.getMenuType())) {
                    String menuInfo = String.format("%s├─ %s [ID:%d]", prefix, menu.getMenuName(), menu.getMenuId());
                    if (StringUtils.isNotEmpty(menu.getUrl()) && !"#".equals(menu.getUrl())) {
                        menuInfo += String.format(" [%s]", menu.getUrl());
                    }
                    if (StringUtils.isNotEmpty(menu.getMenuType())) {
                        String typeDesc = getMenuTypeDescription(menu.getMenuType());
                        menuInfo += String.format(" (%s)", typeDesc);
                    }
                    log.info(menuInfo);
                }
                
                // 递归打印子菜单（包括按钮的子菜单，但按钮本身不显示）
                printMenuHierarchy(menus, menu.getMenuId(), prefix + "│  ");
            }
        }
    }
    
    /**
     * 获取菜单类型描述
     */
    private String getMenuTypeDescription(String menuType) {
        switch (menuType) {
            case "M":
                return "目录";
            case "C":
                return "菜单";
            case "F":
                return "按钮";
            default:
                return "未知";
        }
    }
    
    /**
     * 获取用户权限信息
     */
    @Anonymous
    @GetMapping("/user-permissions/{userId}")
    @ApiOperation("获取用户权限信息")
    public Result getUserPermissions(@PathVariable Long userId) {
        log.info("=== 开始处理用户权限请求 ===");
        log.info("请求路径: /auth/user-permissions/{}", userId);
        log.info("@Anonymous注解已添加");
        log.info("用户ID参数: {}", userId);
        
        try {
            log.info("开始获取用户权限信息，用户ID: {}", userId);
            
            if (userId == null) {
                log.warn("用户ID为空");
                return Result.error("用户ID不能为空");
            }
            
            // 根据用户ID查询用户信息
            SysUser user = userService.selectUserById(userId);
            if (user == null) {
                log.warn("用户不存在，用户ID: {}", userId);
                return Result.error("用户不存在");
            }
            
            log.info("找到用户: {}, 登录名: {}", user.getUserName(), user.getLoginName());
            
            // 获取用户权限数据
            Map<String, Object> permissionData = getUserMenuPermissionsData(user);
            
            log.info("成功获取用户权限信息，菜单数量: {}", permissionData.get("menuCount"));
            log.info("=== 用户权限请求处理完成 ===");
            
            return Result.success("获取用户权限信息成功", permissionData);
            
        } catch (Exception e) {
            log.error("获取用户权限信息失败，用户ID: {}", userId, e);
            log.error("=== 用户权限请求处理失败 ===");
            return Result.error("获取用户权限信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户菜单权限数据（返回数据而不仅仅是打印日志）
     */
    private Map<String, Object> getUserMenuPermissionsData(SysUser user) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> menuList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        
        try {
            log.info("========== 用户菜单权限信息 ==========");
            log.info("用户ID: {}", user.getUserId());
            log.info("用户名: {} ({})", user.getUserName(), user.getLoginName());
            
            // 直接获取用户的所有菜单权限（不经过层级过滤）
            List<SysMenu> allUserMenus;
            if (user.isAdmin()) {
                allUserMenus = menuService.selectMenuAll(user.getUserId());
            } else {
                // 直接调用mapper获取完整的菜单列表
                allUserMenus = menuService.selectMenuList(new SysMenu(), user.getUserId());
            }
            
            if (allUserMenus == null || allUserMenus.isEmpty()) {
                log.info("该用户没有分配任何菜单权限");
                log.info("=====================================");
                result.put("userId", user.getUserId());
                result.put("userName", user.getUserName());
                result.put("loginName", user.getLoginName());
                result.put("menuCount", 0);
                result.put("menus", menuList);
                result.put("urls", urlList);
                return result;
            }
            
            log.info("用户可访问的界面列表 (共{}个):", allUserMenus.size());
            log.info("-------------------------------------");
            
            // 构建菜单数据
            for (SysMenu menu : allUserMenus) {
                Map<String, Object> menuData = new HashMap<>();
                menuData.put("menuId", menu.getMenuId());
                menuData.put("menuName", menu.getMenuName());
                menuData.put("parentId", menu.getParentId());
                menuData.put("url", menu.getUrl());
                menuData.put("menuType", menu.getMenuType());
                menuData.put("visible", menu.getVisible());
                menuData.put("orderNum", menu.getOrderNum());
                menuList.add(menuData);
                
                // 收集URL
                if (StringUtils.isNotEmpty(menu.getUrl()) && !"#".equals(menu.getUrl())) {
                    urlList.add(menu.getUrl());
                    log.info("  - {} ({}) [ID:{}]", menu.getUrl(), menu.getMenuName(), menu.getMenuId());
                }
            }
            
            // 按层级打印菜单，包括孤儿菜单
            printMenuHierarchyWithOrphans(allUserMenus);
            
            log.info("-------------------------------------");
            log.info("可访问的URL列表:");
            for (String url : urlList) {
                log.info("  - {}", url);
            }
            log.info("=====================================");
            
            result.put("userId", user.getUserId());
            result.put("userName", user.getUserName());
            result.put("loginName", user.getLoginName());
            result.put("menuCount", allUserMenus.size());
            result.put("menus", menuList);
            result.put("urls", urlList);
            
        } catch (Exception e) {
            log.error("获取用户菜单权限数据时发生异常: {}", e.getMessage(), e);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}