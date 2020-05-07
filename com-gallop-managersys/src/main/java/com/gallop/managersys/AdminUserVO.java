package com.gallop.managersys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * author gallop
 * date 2020-04-24 15:56
 * Description:
 * Modified By:
 */
@Data
public class AdminUserVO {
    private String username;
    private String password;
    private String avatar;
    private Integer[] roleIds;
}
