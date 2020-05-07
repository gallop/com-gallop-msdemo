package com.gallop.managersys.service;

import com.gallop.managersys.pojo.SysLog;
import com.gallop.utils.PagedResult;

/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:
 * Date: Create in 11:09 2019/5/21
 * Modified By:
 */
public interface SysLogService {
    public void deleteById(Integer id);
    public void add(SysLog log);
    public PagedResult querySelective(String name, Integer page, Integer pageSize, String sort, String order);
}