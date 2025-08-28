package com.omniperform.framework.shiro.service;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.omniperform.common.constant.Constants;
import com.omniperform.common.constant.ShiroConstants;
import com.omniperform.common.constant.UserConstants;
import com.omniperform.common.core.domain.entity.SysRole;
import com.omniperform.common.core.domain.entity.SysUser;
import com.omniperform.common.enums.UserStatus;
import com.omniperform.common.exception.user.BlackListException;
import com.omniperform.common.exception.user.CaptchaException;
import com.omniperform.common.exception.user.UserBlockedException;
import com.omniperform.common.exception.user.UserDeleteException;
import com.omniperform.common.exception.user.UserNotExistsException;
import com.omniperform.common.exception.user.UserPasswordNotMatchException;
import com.omniperform.common.utils.DateUtils;
import com.omniperform.common.utils.IpUtils;
import com.omniperform.common.utils.MessageUtils;
import com.omniperform.common.utils.ServletUtils;
import com.omniperform.common.utils.ShiroUtils;
import com.omniperform.common.utils.StringUtils;
import com.omniperform.framework.manager.AsyncManager;
import com.omniperform.framework.manager.factory.AsyncFactory;
import com.omniperform.system.service.ISysConfigService;
import com.omniperform.system.service.ISysMenuService;
import com.omniperform.system.service.ISysUserService;

/**
 * 登录校验方法
 * 
 * @author omniperform
 */
@Component
public class SysLoginService
{
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录
     */
    public SysUser login(String username, String password)
    {
        // 验证码校验
        if (ShiroConstants.CAPTCHA_ERROR.equals(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA)))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, ShiroUtils.getIp()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }

        // 查询用户信息
        SysUser user = userService.selectUserByLoginName(username);

        /**
        if (user == null && maybeMobilePhoneNumber(username))
        {
            user = userService.selectUserByPhoneNumber(username);
        }

        if (user == null && maybeEmail(username))
        {
            user = userService.selectUserByEmail(username);
        }
        */

        if (user == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }
        
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete")));
            throw new UserDeleteException();
        }
        
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
            throw new UserBlockedException();
        }

        passwordService.validate(user, password);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        setRolePermission(user);
        recordLoginInfo(user.getUserId());
        return user;
    }

    /**
    private boolean maybeEmail(String username)
    {
        if (!username.matches(UserConstants.EMAIL_PATTERN))
        {
            return false;
        }
        return true;
    }

    private boolean maybeMobilePhoneNumber(String username)
    {
        if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN))
        {
            return false;
        }
        return true;
    }
    */

    /**
     * 设置角色权限
     *
     * @param user 用户信息
     */
    public void setRolePermission(SysUser user)
    {
        List<SysRole> roles = user.getRoles();
        if (!roles.isEmpty())
        {
            // 设置permissions属性，以便数据权限匹配权限
            for (SysRole role : roles)
            {
                if (StringUtils.equals(role.getStatus(), UserConstants.ROLE_NORMAL) && !role.isAdmin())
                {
                    Set<String> rolePerms = menuService.selectPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                }
            }
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setLoginIp(ShiroUtils.getIp());
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserInfo(user);
    }
}
