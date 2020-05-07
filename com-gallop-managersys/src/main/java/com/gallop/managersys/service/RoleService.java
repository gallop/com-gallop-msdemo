package com.gallop.managersys.service;

import com.gallop.managersys.pojo.Role;
import com.gallop.utils.PagedResult;

import java.util.List;
import java.util.Set;

/**
 * author gallop
 * date 2020-04-19 11:03
 * Description:
 * Modified By:
 */
public interface RoleService {
    public Set<String> queryByIds(Integer[] roleIds);
    public PagedResult querySelective(String name, Integer page, Integer limit, String sort, String order);
    public Role findById(Integer id);
    public void add(Role role);
    public void deleteById(Integer id);
    public void updateById(Role role);
    public boolean checkExist(String name);
    public List<Role> queryAll();
}
