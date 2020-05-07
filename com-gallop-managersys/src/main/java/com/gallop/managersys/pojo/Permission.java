package com.gallop.managersys.pojo;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
public class Permission {
    private Integer id;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 权限
     */
    private String permission;

    /**
     * 创建时间
     */
    @Column(name = "add_time")
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;
}