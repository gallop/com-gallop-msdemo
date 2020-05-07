package com.gallop.managersys.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.gallop.managersys.mapper.RoleMapper;
import com.gallop.managersys.pojo.FileStorage;
import com.gallop.managersys.pojo.Role;
import com.gallop.managersys.service.RoleService;
import com.gallop.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
 * date 2020-04-19 11:11
 * Description:
 * Modified By:
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if (roleIds.length == 0) {
            return roles;
        }

        Example roleExample = new Example(Role.class);
        Example.Criteria criteria = roleExample.createCriteria();
        criteria.andEqualTo("enabled", true)
                .andEqualTo("deleted", false)
                .andIn("id", Arrays.asList(roleIds));

        List<Role> roleList = roleMapper.selectByExample(roleExample);
        for (Role role : roleList) {
            roles.add(role.getName());
        }

        return roles;
    }

    @Override
    public PagedResult querySelective(String name, Integer page, Integer limit, String sort, String order) {
        Example roleExample = new Example(Role.class);
        Example.Criteria criteria = roleExample.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andLike("name","%" + name + "%");
        }
        criteria.andEqualTo("deleted",false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            roleExample.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        List<Role> list = roleMapper.selectByExample(roleExample);

        PageInfo<Role> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }

    @Override
    public Role findById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Role role) {
        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.insertSelective(role);
    }

    @Override
    public void deleteById(Integer id) {
        roleMapper.logicalDeleteByPrimaryKey(id);
    }

    @Override
    public void updateById(Role role) {
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public boolean checkExist(String name) {
        Example example = new Example(Role.class);
        example.or().andEqualTo("name",name).andEqualTo("deleted",false);
        return roleMapper.selectCountByExample(example) != 0;
    }

    @Override
    public List<Role> queryAll() {
        Example example = new Example(Role.class);
        example.or().andEqualTo("deleted",false);
        return roleMapper.selectByExample(example);
    }
}
