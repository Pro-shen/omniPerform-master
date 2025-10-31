package com.omniperform.web.controller;

import com.omniperform.common.utils.ShiroUtils;
import com.omniperform.framework.shiro.service.SysPasswordService;
import com.omniperform.web.common.Result;
import com.omniperform.common.core.domain.entity.SysUser;
import com.omniperform.common.core.domain.entity.SysRole;
import com.omniperform.common.core.domain.entity.SysMenu;
import com.omniperform.system.service.ISysUserService;
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
import java.util.Set;

/**
 * 用户管理控制器
 * 
 * @author omniperform
 */
@RestController
@RequestMapping("/system/user")
@Api(tags = "用户管理")
public class SysUserController {

    private static final Logger log = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysRoleService roleService;
    
    @Autowired
    private ISysMenuService menuService;

    // 新增: 注入密码工具服务
    @Autowired
    private SysPasswordService passwordService;

    /**
     * 获取用户列表
     */
    @Anonymous
    @GetMapping("/list")
    @ApiOperation("获取用户列表")
    public Result getUserList() {
        try {
            SysUser user = new SysUser();
            List<SysUser> users = userService.selectUserList(user);
            log.info("获取用户列表成功，共{}个用户", users.size());
            return Result.success("获取成功", users);
        } catch (Exception e) {
            log.error("获取用户列表失败: {}", e.getMessage(), e);
            return Result.error("获取用户列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取用户详情
     */
    @Anonymous
    @GetMapping("/{userId}")
    @ApiOperation("获取用户详情")
    public Result getUserById(@PathVariable Long userId) {
        try {
            SysUser user = userService.selectUserById(userId);
            if (user != null) {
                // 获取用户角色
                List<SysRole> roles = roleService.selectRolesByUserId(userId);
                user.setRoles(roles);
                
                Map<String, Object> data = new HashMap<>();
                data.put("user", user);
                data.put("roles", roles);
                
                return Result.success("获取成功", data);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户详情失败: {}", e.getMessage(), e);
            return Result.error("获取用户详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @Anonymous
    @GetMapping("/profile")
    @ApiOperation("获取当前用户信息")
    public Result getCurrentUser(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 这里应该从token中解析用户信息，暂时返回默认用户
            // 实际项目中需要实现token解析逻辑
            SysUser user = userService.selectUserById(1L); // 默认返回admin用户
            if (user != null) {
                // 获取用户角色
                List<SysRole> roles = roleService.selectRolesByUserId(user.getUserId());
                user.setRoles(roles);
                
                // 获取用户菜单权限
                List<SysMenu> menus = menuService.selectMenusByUser(user);
                
                // 获取用户权限标识
                Set<String> permissions = menuService.selectPermsByUserId(user.getUserId());
                
                Map<String, Object> data = new HashMap<>();
                data.put("user", createUserInfo(user));
                data.put("roles", roles);
                data.put("menus", menus);
                data.put("permissions", permissions);
                
                return Result.success("获取成功", data);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取当前用户信息失败: {}", e.getMessage(), e);
            return Result.error("获取当前用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 创建新用户
     */
    @Anonymous
    @PostMapping
    @ApiOperation("创建用户")
    public Result createUser(@RequestBody SysUser user) {
        try {
            // 校验用户名唯一性
            if (!userService.checkLoginNameUnique(user)) {
                return Result.error("用户名已存在");
            }
            
            // 校验邮箱唯一性
            if (!userService.checkEmailUnique(user)) {
                return Result.error("邮箱已存在");
            }
            
            // 校验手机号唯一性
            if (!userService.checkPhoneUnique(user)) {
                return Result.error("手机号已存在");
            }

            // ================= 若依模式: 生成盐并加密密码 =================
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String rawPassword = user.getPassword();
                String salt = ShiroUtils.randomSalt();
                user.setSalt(salt);
                user.setPassword(passwordService.encryptPassword(user.getLoginName(), rawPassword, salt));
            }
            // ==========================================================

            int result = userService.insertUser(user);
            
            if (result > 0) {
                return Result.success("创建成功", user);
            } else {
                return Result.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建用户失败: {}", e.getMessage(), e);
            return Result.error("创建用户失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @Anonymous
    @PutMapping("/{userId}")
    @ApiOperation("更新用户")
    public Result updateUser(@PathVariable Long userId, @RequestBody SysUser user) {
        try {
            user.setUserId(userId);
            
            // 校验用户是否允许操作
            userService.checkUserAllowed(user);
            
            // 校验用户名唯一性
            if (!userService.checkLoginNameUnique(user)) {
                return Result.error("用户名已存在");
            }
            
            // 校验邮箱唯一性
            if (!userService.checkEmailUnique(user)) {
                return Result.error("邮箱已存在");
            }
            
            // 校验手机号唯一性
            if (!userService.checkPhoneUnique(user)) {
                return Result.error("手机号已存在");
            }
            
            int result = userService.updateUser(user);
            if (result > 0) {
                return Result.success("更新成功", user);
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage(), e);
            return Result.error("更新用户失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @Anonymous
    @DeleteMapping("/{userId}")
    @ApiOperation("删除用户")
    public Result deleteUser(@PathVariable Long userId) {
        try {
            SysUser user = userService.selectUserById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 校验用户是否允许操作
            userService.checkUserAllowed(user);
            
            int result = userService.deleteUserById(userId);
            if (result > 0) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage(), e);
            return Result.error("删除用户失败: " + e.getMessage());
        }
    }

    /**
     * 修改用户状态
     */
    @Anonymous
    @PutMapping("/{userId}/status")
    @ApiOperation("修改用户状态")
    public Result changeStatus(@PathVariable Long userId, @RequestBody Map<String, String> statusData) {
        try {
            SysUser user = userService.selectUserById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            user.setStatus(statusData.get("status"));
            
            int result = userService.changeStatus(user);
            if (result > 0) {
                return Result.success("修改成功");
            } else {
                return Result.error("修改失败");
            }
        } catch (Exception e) {
            log.error("修改用户状态失败: {}", e.getMessage(), e);
            return Result.error("修改用户状态失败: " + e.getMessage());
        }
    }

    /**
     * 重置用户密码
     */
    @Anonymous
    @PutMapping("/{userId}/reset-password")
    @ApiOperation("重置用户密码")
    public Result resetPassword(@PathVariable Long userId, @RequestBody Map<String, String> passwordData) {
        try {
            SysUser user = userService.selectUserById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            user.setPassword(passwordData.get("password"));
            
            int result = userService.resetUserPwd(user);
            if (result > 0) {
                return Result.success("重置成功");
            } else {
                return Result.error("重置失败");
            }
        } catch (Exception e) {
            log.error("重置用户密码失败: {}", e.getMessage(), e);
            return Result.error("重置用户密码失败: " + e.getMessage());
        }
    }

    /**
     * 分配用户角色
     */
    @Anonymous
    @PutMapping("/{userId}/roles")
    @ApiOperation("分配用户角色")
    public Result assignRoles(@PathVariable Long userId, @RequestBody Map<String, Object> roleData) {
        try {
            SysUser user = userService.selectUserById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            @SuppressWarnings("unchecked")
            List<Long> roleIds = (List<Long>) roleData.get("roleIds");
            user.setRoleIds(roleIds.toArray(new Long[0]));
            
            int result = userService.updateUser(user);
            if (result > 0) {
                return Result.success("分配成功");
            } else {
                return Result.error("分配失败");
            }
        } catch (Exception e) {
            log.error("分配用户角色失败: {}", e.getMessage(), e);
            return Result.error("分配用户角色失败: " + e.getMessage());
        }
    }

    /**
     * 创建用户信息对象（去除敏感信息）
     */
    private Map<String, Object> createUserInfo(SysUser user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", user.getUserId());
        userInfo.put("loginName", user.getLoginName());
        userInfo.put("userName", user.getUserName());
        userInfo.put("email", user.getEmail());
        userInfo.put("phonenumber", user.getPhonenumber());
        userInfo.put("sex", user.getSex());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("status", user.getStatus());
        userInfo.put("createTime", user.getCreateTime());
        userInfo.put("loginDate", user.getLoginDate());
        return userInfo;
    }
}