package com.gallop.managersys.util;

import com.gallop.managersys.annotation.RequiresPermissionsDesc;
import lombok.Data;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * author gallop
 * date 2020-04-19 16:42
 * Description:
 * Modified By:
 */
@Data
public class PermissionWrap {
    private RequiresPermissions requiresPermissions;
    private RequiresPermissionsDesc requiresPermissionsDesc;
    private String api;
}
