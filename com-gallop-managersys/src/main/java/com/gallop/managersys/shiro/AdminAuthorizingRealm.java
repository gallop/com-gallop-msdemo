package com.gallop.managersys.shiro;


import com.gallop.managersys.pojo.AdminUser;
import com.gallop.managersys.service.AdminUserService;
import com.gallop.managersys.service.PermissionService;
import com.gallop.managersys.service.RoleService;
import com.gallop.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;


/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:
 * Date: Create in 18:02 2019/5/20
 * Modified By:
 */
@Slf4j
public class AdminAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (principalCollection == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        AdminUser admin = (AdminUser) getAvailablePrincipal(principalCollection);
        Integer[] roleIds = admin.getRoleIds();
        Set<String> roles = roleService.queryByIds(roleIds);
        Set<String> permissions = permissionService.queryByRoleIds(roleIds);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username = upToken.getUsername();
        String password=new String(upToken.getPassword());

        if (StringUtils.isEmpty(username)) {
            throw new AccountException("用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new AccountException("密码不能为空");
        }

        AdminUser adminUser = adminUserService.findAdmin(username);
        //log.info("adminUser is: {}",adminUser.toString());

        if (adminUser == null) {
            throw new UnknownAccountException("找不到用户（"+username+"）的帐号信息");
        }
        String password_md5 = "";
        try {
            password_md5 =MD5Utils.getMD5Str(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!StringUtils.equals(password_md5,adminUser.getPassword())){
            throw new UnknownAccountException("找不到用户（"+username+"）的帐号信息");
        }

        return new SimpleAuthenticationInfo(adminUser,password,getName());
    }
}
