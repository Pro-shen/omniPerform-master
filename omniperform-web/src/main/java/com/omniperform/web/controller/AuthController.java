package com.omniperform.web.controller;

import com.omniperform.web.common.Result;
import com.omniperform.common.core.domain.entity.SysUser;
import com.omniperform.framework.shiro.service.SysLoginService;
import com.omniperform.system.service.ISysUserService;
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
            log.info("用户退出成功");
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
}