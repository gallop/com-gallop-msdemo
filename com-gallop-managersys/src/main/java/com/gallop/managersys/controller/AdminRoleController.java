package com.gallop.managersys.controller;

import com.gallop.managersys.annotation.RequiresPermissionsDesc;
import com.gallop.managersys.pojo.AdminUser;
import com.gallop.managersys.pojo.Permission;
import com.gallop.managersys.pojo.Role;
import com.gallop.managersys.service.AdminUserService;
import com.gallop.managersys.service.PermissionService;
import com.gallop.managersys.service.RoleService;
import com.gallop.managersys.util.PermissionUtil;
import com.gallop.managersys.util.PermissionWrap;
import com.gallop.managersys.vo.PermVo;
import com.gallop.utils.JSONResult;
import com.gallop.utils.JsonUtils;
import com.gallop.utils.PagedResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;

import static com.gallop.utils.ResponseCode.*;

/**
 * author gallop
 * date 2020-04-19 17:53
 * Description:
 * Modified By:
 */
@RestController
@RequestMapping("/admin/role")
@Validated
@Slf4j
public class AdminRoleController {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @RequiresPermissions("admin:role:list")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色查询")
    @GetMapping("/list")
    public Object list(String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RequestParam(defaultValue = "add_time") String sort,
                       @RequestParam(defaultValue = "desc") String order) {
        PagedResult roleList = roleService.querySelective(name, page, limit, sort, order);
        return JSONResult.ok(roleList);
    }

    @GetMapping("/options")
    public Object options(){
        List<Role> roleList = roleService.queryAll();

        List<Map<String, Object>> options = new ArrayList<>(roleList.size());
        for (Role role : roleList) {
            Map<String, Object> option = new HashMap<>(2);
            option.put("value", role.getId());
            option.put("label", role.getName());
            options.add(option);
        }

        return JSONResult.ok(options);
    }

    @RequiresPermissions("admin:role:read")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        Role role = roleService.findById(id);
        return JSONResult.ok(role);
    }

    private Object validate(Role role) {
        String name = role.getName();
        if (StringUtils.isEmpty(name)) {
            return JSONResult.badArgument();
        }

        return null;
    }

    @RequiresPermissions("admin:role:create")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色添加")
    @PostMapping("/create")
    public Object create(@RequestBody Role role) {
        Object error = validate(role);
        if (error != null) {
            return error;
        }
        if (roleService.checkExist(role.getName())){
            return JSONResult.errorMsg(ROLE_NAME_EXIST, "角色已经存在");
        }

        roleService.add(role);
        return JSONResult.ok(role);
    }

    @RequiresPermissions("admin:role:update")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色编辑")
    @PostMapping("/update")
    public Object update(@RequestBody Role role) {
        Object error = validate(role);
        if (error != null) {
            return error;
        }

        roleService.updateById(role);
        return JSONResult.ok();
    }

    @RequiresPermissions("admin:role:delete")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody Role role) {
        Integer id = role.getId();
        if (id == null) {
            return JSONResult.badArgument();
        }

        // 如果当前角色所对应管理员仍存在，则拒绝删除角色。
        List<AdminUser> adminList = adminUserService.findAll();
        for(AdminUser adminUser : adminList){
            Integer[] roleIds = adminUser.getRoleIds();
            for(Integer roleId : roleIds){
                if(id.intValue()== roleId.intValue()){
                    return JSONResult.errorMsg(ROLE_USER_EXIST, "当前角色存在管理员，不能删除");
                }
            }
        }

        roleService.deleteById(id);
        return JSONResult.ok();
    }

    @Autowired
    private ApplicationContext context;
    private List<PermVo> systemPermissions = null;
    private Set<String> systemPermissionsString = null;

    private List<PermVo> getSystemPermissions(){
        final String basicPackage = "com.gallop.managersys.controller";
        if(systemPermissions == null || systemPermissions.size() == 0){
            List<PermissionWrap> permissionWrapLists = PermissionUtil.listPermissionWrap(context, basicPackage);
            systemPermissions = PermissionUtil.listPermVo(permissionWrapLists);
            systemPermissionsString = PermissionUtil.listPermissionString(permissionWrapLists);
        }
        return systemPermissions;
    }

    private Set<String> getAssignedPermissions(Integer roleId){
        // 这里需要注意的是，如果存在超级权限*，那么这里需要转化成当前所有系统权限。
        // 之所以这么做，是因为前端不能识别超级权限，所以这里需要转换一下。
        Set<String> assignedPermissions = null;
        if(permissionService.checkSuperPermission(roleId)){
            getSystemPermissions();
            assignedPermissions = systemPermissionsString;
        }else{
            assignedPermissions = permissionService.queryByRoleId(roleId);
        }

        return assignedPermissions;
    }

    /**
     * 管理员的权限情况
     *
     * @return 系统所有权限列表和管理员已分配权限
     */
    @RequiresPermissions("admin:role:permission:get")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="权限详情")
    @GetMapping("/getPermissions")
    public Object getPermissions(Integer roleId) {
        List<PermVo> systemPermissions = getSystemPermissions();
        Set<String> assignedPermissions = getAssignedPermissions(roleId);

        Map<String, Object> data = new HashMap<>();
        data.put("systemPermissions", systemPermissions);
        data.put("assignedPermissions", assignedPermissions);
        return JSONResult.ok(data);
    }

    /**
     * 更新管理员的权限
     *
     * @param body
     * @return
     */
    @RequiresPermissions("admin:role:permission:update")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="权限变更")
    @PostMapping("/updatePermissions")
    public Object updatePermissions(@RequestBody String body) {
        Integer roleId = JsonUtils.parseInteger(body, "roleId");
        List<String> permissionStrs = JsonUtils.parseStringList(body, "permissions");
        if(roleId == null || permissionStrs == null){
            return JSONResult.badArgument();
        }

        // 如果修改的角色是超级权限，则拒绝修改。
        if(permissionService.checkSuperPermission(roleId)){
            return JSONResult.errorMsg(ROLE_SUPER_SUPERMISSION, "当前角色的超级权限不能变更");
        }

        // 先删除旧的权限，再更新新的权限
        permissionService.deleteByRoleId(roleId);
        for(String permissionStr : permissionStrs){
            Permission permissionDb = new Permission();
            permissionDb.setRoleId(roleId);
            permissionDb.setPermission(permissionStr);
            permissionService.add(permissionDb);
        }
        return JSONResult.ok();
    }


}
