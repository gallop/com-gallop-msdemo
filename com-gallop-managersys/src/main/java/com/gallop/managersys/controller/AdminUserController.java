package com.gallop.managersys.controller;

import com.gallop.managersys.AdminUserVO;
import com.gallop.managersys.annotation.RequiresPermissionsDesc;
import com.gallop.managersys.pojo.AdminUser;
import com.gallop.managersys.service.AdminUserService;
import com.gallop.managersys.util.LogHelper;
import com.gallop.utils.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static com.gallop.utils.ResponseCode.*;

/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:
 * Date: Create in 15:10 2019/6/4
 * Modified By:
 */
@RestController
@RequestMapping("/admin/adminUser")
public class AdminUserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:admin:list")
    @RequiresPermissionsDesc(menu={"系统管理" , "管理员管理"}, button="查询")
    @GetMapping("/list")
    public Object list(String username,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(defaultValue = "add_time") String sort,
                       @RequestParam(defaultValue = "desc") String order) {
        PagedResult userList = adminUserService.querySelective(username, page, pageSize, sort, order);
        return JSONResult.ok(userList);
    }

    private Object validate(AdminUserVO adminUserVo) {
        logger.info("===========user: {}",adminUserVo.toString());
        String name = adminUserVo.getUsername();
        if (StringUtils.isEmpty(name)) {
            return JSONResult.badArgument();
        }
        if (!RegexUtil.isUsername(name)) {
            return JSONResult.errorMsg(ADMIN_INVALID_USERNAME, "用户名称不符合规定");
        }
        String password = adminUserVo.getPassword();
        if (StringUtils.isEmpty(password) || password.length() < 6) {
            return JSONResult.errorMsg(ADMIN_INVALID_PASSWORD, "用户密码长度不能小于6");
        }
        return null;
    }

    @RequiresPermissions("admin:admin:create")
    @RequiresPermissionsDesc(menu={"系统管理" , "管理员管理"}, button="添加")
    @PostMapping("/create")
    public Object create(@RequestBody AdminUserVO adminUserVo) {
        Object error = validate(adminUserVo);
        if (error != null) {
            return error;
        }

        String username = adminUserVo.getUsername();
        AdminUser admin_Db = adminUserService.findAdmin(username);
        if (admin_Db != null) {
            return JSONResult.errorMsg(ADMIN_NAME_EXIST, "用户已经存在");
        }

        String rawPassword = adminUserVo.getPassword();
        String password_md5 = "";
        try {
            password_md5 = MD5Utils.getMD5Str(rawPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(adminUserVo,adminUser);
        adminUser.setPassword(password_md5);
        adminUser.setDeleted(false);
        adminUser.setAddTime(LocalDateTime.now());

        adminUserService.add(adminUser);
        logHelper.logAuthSucceed("添加用户管理员", username);
        return JSONResult.ok(adminUserVo);
    }

    @RequiresPermissions("admin:admin:read")
    @RequiresPermissionsDesc(menu={"系统管理" , "管理员管理"}, button="详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        AdminUser admin = adminUserService.findById(id);
        return JSONResult.ok(admin);
    }

    @RequiresPermissions("admin:admin:update")
    @RequiresPermissionsDesc(menu={"系统管理" , "管理员管理"}, button="编辑")
    @PostMapping("/update")
    public Object update(@RequestBody AdminUser admin) {
        /*Object error = validate(admin);
        if (error != null) {
            return error;
        }*/

        Integer anotherAdminId = admin.getId();
        if (anotherAdminId == null) {
            return JSONResult.badArgument();
        }

        // 不允许管理员通过编辑接口修改密码
        admin.setPassword(null);

        if (adminUserService.updateById(admin) == 0) {
            return JSONResult.updatedDataFailed();
        }

        logHelper.logAuthSucceed("编辑管理员", admin.getUsername());
        return JSONResult.ok(admin);
    }


    @RequiresPermissions("admin:admin:delete")
    @RequiresPermissionsDesc(menu={"系统管理" , "管理员管理"}, button="删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody AdminUser admin) {
        Integer anotherAdminId = admin.getId();
        if (anotherAdminId == null) {
            return JSONResult.badArgument();
        }

        // 管理员不能删除自身账号
        Subject currentUser = SecurityUtils.getSubject();
        AdminUser currentAdmin = (AdminUser) currentUser.getPrincipal();
        if (currentAdmin.getId().equals(anotherAdminId)) {
            return JSONResult.errorMsg(ADMIN_DELETE_NOT_ALLOWED, "用户管理员不能删除自己账号");
        }

        adminUserService.deleteById(anotherAdminId);
        logHelper.logAuthSucceed("删除管理员", admin.getUsername());
        return JSONResult.ok();
    }


    @PostMapping("/password")
    public Object create(@RequestBody String body) {
        String oldPassword = JsonUtils.parseString(body, "oldPassword");
        String newPassword = JsonUtils.parseString(body, "newPassword");
        if (StringUtils.isEmpty(oldPassword)) {
            return JSONResult.badArgument();
        }
        if (StringUtils.isEmpty(newPassword)) {
            return JSONResult.badArgument();
        }

        Subject currentUser = SecurityUtils.getSubject();
        AdminUser admin = (AdminUser) currentUser.getPrincipal();

        String oldPassword_md5 = "";
        try {
            oldPassword_md5 = MD5Utils.getMD5Str(oldPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!oldPassword_md5.equals(admin.getPassword())) {
            return JSONResult.errorMsg(ADMIN_INVALID_ACCOUNT, "账号密码不对");
        }

        String newPassword_md5 = "";
        try {
            newPassword_md5 = MD5Utils.getMD5Str(newPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        admin.setPassword(newPassword_md5);

        adminUserService.updateById(admin);
        return JSONResult.ok();
    }
}
