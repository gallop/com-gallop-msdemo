package com.gallop.managersys.controller;

import com.gallop.managersys.annotation.RequiresPermissionsDesc;
import com.gallop.managersys.service.SysLogService;
import com.gallop.utils.JSONResult;
import com.gallop.utils.PagedResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:
 * Date: Create in 15:41 2019/6/4
 * Modified By:
 */
@RestController
@RequestMapping("/admin/log")
public class AdminLogController {

    @Autowired
    private SysLogService sysLogService;

    @RequiresPermissions("admin:log:list")
    @RequiresPermissionsDesc(menu={"系统管理" , "操作日志"}, button="查询")
    @GetMapping("/list")
    public Object list(String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(defaultValue = "add_time") String sort,
                       @RequestParam(defaultValue = "desc") String order) {
        PagedResult logList = sysLogService.querySelective(name, page, pageSize, sort, order);
        return JSONResult.ok(logList);
    }
}
