package com.gallop.managersys.pojo;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
public class Role {
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    @Column(name = "`desc`")
    private String desc;

    /**
     * 是否启用
     */
    private Boolean enabled;

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