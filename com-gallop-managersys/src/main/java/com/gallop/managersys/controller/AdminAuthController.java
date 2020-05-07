package com.gallop.managersys.controller;

import com.gallop.managersys.pojo.AdminUser;
import com.gallop.managersys.service.AdminUserService;
import com.gallop.managersys.service.PermissionService;
import com.gallop.managersys.service.RoleService;
import com.gallop.managersys.util.LogHelper;
import com.gallop.managersys.util.PermissionUtil;
import com.gallop.managersys.util.PermissionWrap;
import com.gallop.utils.IpUtil;
import com.gallop.utils.JSONResult;
import com.gallop.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

import static com.gallop.utils.ResponseCode.*;

/**
 * author gallop
 * date 2020-04-19 17:24
 * Description:
 * Modified By:
 */
@RestController
@RequestMapping("/admin/auth")
@Validated
@Slf4j
public class AdminAuthController {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private LogHelper logHelper;

    @PostMapping("/login")
    public Object login(@RequestBody String body, HttpServletRequest request) {
        String username = JsonUtils.parseString(body, "username");
        String password = JsonUtils.parseString(body, "password");
        log.info("username = {}, password= {}",username,password);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return JSONResult.errorMsg(PARAMETER_VALUE_ERROR, "用户名和密码不能为空");
        }

        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(new UsernamePasswordToken(username, password));
        } catch (UnknownAccountException uae) {
            logHelper.logAuthFail("登录", "用户帐号或密码不正确");
            return JSONResult.errorMsg(ADMIN_INVALID_USERNAME, "用户帐号或密码不正确");
        } catch (LockedAccountException lae) {
            logHelper.logAuthFail("登录", "用户帐号已锁定不可用");
            return JSONResult.errorMsg(ADMIN_INVALID_ACCOUNT, "用户帐号已锁定不可用");

        } catch (AuthenticationException ae) {
            logHelper.logAuthFail("登录", "认证失败");
            return JSONResult.errorMsg(ADMIN_INVALID_ACCOUNT, "认证失败");
        }

        currentUser = SecurityUtils.getSubject();
        AdminUser admin = (AdminUser) currentUser.getPrincipal();
        admin.setLastLoginIp(IpUtil.getIpAddr(request));
        admin.setLastLoginTime(LocalDateTime.now());
        adminUserService.updateById(admin);

        logHelper.logAuthSucceed("登录");

        // userInfo
        Map<String, Object> adminUserInfo = new HashMap<String, Object>();
        adminUserInfo.put("nickName", admin.getUsername());
        adminUserInfo.put("avatar", admin.getAvatar());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", currentUser.getSession().getId());
        result.put("adminUserInfo", adminUserInfo);

        return  JSONResult.ok(result);
    }

    @RequiresAuthentication
    @PostMapping("/logout")
    public Object logout() {
        Subject currentUser = SecurityUtils.getSubject();
        /*String sessionid = (String)currentUser.getSession().getId();
        log.info("session:" + sessionid);*/
        logHelper.logAuthSucceed("退出");
        currentUser.logout();
        return JSONResult.ok();
    }

    @RequiresAuthentication
    @GetMapping("/info")
    public Object info() {
        Subject currentUser = SecurityUtils.getSubject();
        AdminUser adminUser = (AdminUser) currentUser.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("name", adminUser.getUsername());
        data.put("avatar", adminUser.getAvatar());

        log.info("==========adminUser:"+adminUser.toString());
        Integer[] roleIds = adminUser.getRoleIds();
        Set<String> roles = roleService.queryByIds(roleIds);
        Set<String> permissions = permissionService.queryByRoleIds(roleIds);
        data.put("roles", roles);
        // NOTE
        // 这里需要转换perms结构，因为对于前端而已API形式的权限更容易理解
        //todo
        data.put("perms", toApi(permissions));
        return JSONResult.ok(data);
    }

    @Autowired
    private ApplicationContext context;
    private HashMap<String, String> systemPermissionsMap = null;

    private Collection<String> toApi(Set<String> permissions) {
        if (systemPermissionsMap == null) {
            systemPermissionsMap = new HashMap<>();
            final String basicPackage = "com.gallop.managersys.controller";
            List<PermissionWrap> systemPermissions = PermissionUtil.listPermissionWrap(context, basicPackage);
            for (PermissionWrap permissionWrap : systemPermissions) {
                String perm = permissionWrap.getRequiresPermissions().value()[0];
                String api = permissionWrap.getApi();
                systemPermissionsMap.put(perm, api);
            }
        }

        Collection<String> apis = new HashSet<>();
        for (String perm : permissions) {
            String api = systemPermissionsMap.get(perm);
            apis.add(api);

            if (perm.equals("*")) {
                apis.clear();
                apis.add("*");
                return apis;
                // return systemPermissionsMap.values();
            }
        }
        return apis;
    }

    @GetMapping("/401")
    public Object page401() {
        log.info("进入/401 页面。。。。。");
        return JSONResult.unlogin();
    }

    @GetMapping("/index")
    public Object pageIndex() {
        return JSONResult.ok();
    }

    @GetMapping("/403")
    public Object page403() {
        return JSONResult.unauthz();
    }
}
