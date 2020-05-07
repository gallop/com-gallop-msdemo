package com.gallop.managersys.service;

import com.gallop.managersys.pojo.AdminUser;
import com.gallop.utils.PagedResult;

import java.util.List;

/**
 * author gallop
 * date 2020-04-19 10:47
 * Description:
 * Modified By:
 */
public interface AdminUserService {
    public AdminUser findAdmin(String userName);
    public AdminUser findById(Integer id);
    public void add(AdminUser adminUser);
    public int updateById(AdminUser adminUser);
    public PagedResult querySelective(String username, Integer page, Integer pageSize, String sort, String order);
    public int deleteById(Integer id);
    public List<AdminUser> findAll();
}
