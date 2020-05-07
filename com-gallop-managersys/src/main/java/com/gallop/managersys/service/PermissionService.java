package com.gallop.managersys.service;

import com.gallop.managersys.pojo.Permission;

import java.util.Set;

/**
 * author gallop
 * date 2020-04-19 11:46
 * Description:
 * Modified By:
 */
public interface PermissionService {
    public Set<String> queryByRoleIds(Integer[] roleIds);
    public Set<String> queryByRoleId(Integer roleId);
    public boolean checkSuperPermission(Integer roleId);
    public void deleteByRoleId(Integer roleId);
    public void add(Permission permission);
}
