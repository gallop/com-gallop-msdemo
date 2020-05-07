package com.gallop.managersys.service.impl;

import com.gallop.managersys.mapper.PermissionMapper;
import com.gallop.managersys.pojo.Permission;
import com.gallop.managersys.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author gallop
 * date 2020-04-19 11:50
 * Description:
 * Modified By:
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();
        if(roleIds.length == 0){
            return permissions;
        }
        Example example = new Example(Permission.class);
        example.or().andIn("roleId", Arrays.asList(roleIds)).andEqualTo("deleted",false);
        List<Permission> permissionList = permissionMapper.selectByExample(example);

        for(Permission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    @Override
    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if(roleId == null){
            return permissions;
        }
        Example example = new Example(Permission.class);
        example.or().andEqualTo("roleId", roleId).andEqualTo("deleted",false);
        List<Permission> permissionList = permissionMapper.selectByExample(example);

        for(Permission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    @Override
    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }

        Example example = new Example(Permission.class);
        example.or().andEqualTo("roleId",roleId)
                .andEqualTo("permission","*")
                .andEqualTo("deleted",false);
        return permissionMapper.selectCountByExample(example) != 0;
    }

    @Override
    public void deleteByRoleId(Integer roleId) {
        Example permissionExample = new Example(Permission.class);
        Example.Criteria criteria = permissionExample.createCriteria();
        criteria.andEqualTo("roleId",roleId)
                .andEqualTo("deleted",false);
        permissionMapper.logicalDeleteByExample(permissionExample);
        //permissionMapper.deleteByExample(permissionExample);
    }

    @Override
    public void add(Permission permission) {
        permission.setAddTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        permissionMapper.insertSelective(permission);
    }
}
