package com.gallop.managersys.service.impl;

import com.gallop.managersys.mapper.SysLogMapper;
import com.gallop.managersys.pojo.SysLog;
import com.gallop.managersys.service.SysLogService;
import com.gallop.utils.PagedResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

import static java.time.LocalDateTime.now;

/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:
 * Date: Create in 11:25 2019/5/21
 * Modified By:
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public void deleteById(Integer id) {
        sysLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void add(SysLog log) {
        log.setAddTime(now());
        log.setUpdateTime(now());
        sysLogMapper.insertSelective(log);
    }

    @Override
    public PagedResult querySelective(String name, Integer page, Integer pageSize, String sort, String order) {
        Example sysLogExample = new Example(SysLog.class);
        Example.Criteria criteria = sysLogExample.createCriteria();
        //criteria.andEqualTo("username", username);

        if (!StringUtils.isEmpty(name)) {
            //criteria.andEqualTo("nickname",nickname);
            criteria.andLike("admin",name);
        }
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            sysLogExample.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, pageSize);

        List<SysLog> list = sysLogMapper.selectByExample(sysLogExample);

        PageInfo<SysLog> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }
}
