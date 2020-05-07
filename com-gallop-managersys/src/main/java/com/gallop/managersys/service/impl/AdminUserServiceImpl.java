package com.gallop.managersys.service.impl;

import com.gallop.managersys.mapper.AdminUserMapper;
import com.gallop.managersys.pojo.AdminUser;
import com.gallop.managersys.service.AdminUserService;
import com.gallop.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * author gallop
 * date 2020-04-19 10:48
 * Description:
 * Modified By:
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser findAdmin(String userName) {
        /*Example adminUserExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminUserExample.createCriteria();
        criteria.andEqualTo("username", userName);*/
        return adminUserMapper.selectOneByUserName(userName);
    }

    @Override
    public AdminUser findById(Integer id) {
        if(id == null)
            return null;
        return adminUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(AdminUser adminUser) {
        adminUserMapper.insertSelective(adminUser);
    }

    @Override
    public int updateById(AdminUser adminUser) {
        adminUser.setUpdateTime(LocalDateTime.now());
        return adminUserMapper.updateByPrimaryKeySelective(adminUser);
    }

    @Override
    public PagedResult querySelective(String username, Integer page, Integer pageSize, String sort, String order) {
        Example adminUserExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminUserExample.createCriteria();
        //criteria.andEqualTo("username", username);

        if (!StringUtils.isEmpty(username)) {
            //criteria.andEqualTo("nickname",nickname);
            criteria.andLike("username",username);
        }
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            adminUserExample.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, pageSize);

        List<AdminUser> list = adminUserMapper.selectByExample(adminUserExample);

        PageInfo<AdminUser> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Override
    public int deleteById(Integer id) {
        if(id == null)
            return -1;
        return adminUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<AdminUser> findAll() {
        return adminUserMapper.selectAll();
    }
}
