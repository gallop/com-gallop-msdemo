package com.gallop.managersys.mapper;

import com.gallop.managersys.pojo.AdminUser;
import com.gallop.utils.MyMapper;

import java.util.List;

public interface AdminUserMapper extends MyMapper<AdminUser> {
    List<AdminUser> selectByExample(Object var1);
    AdminUser selectOneByUserName(String userName);
    int insertSelective(AdminUser adminUser);
    int updateByPrimaryKeySelective(AdminUser adminUser);
}