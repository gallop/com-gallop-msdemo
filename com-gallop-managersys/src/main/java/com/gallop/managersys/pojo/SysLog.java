package com.gallop.managersys.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "sys_log")
@Data
public class SysLog {
    private Integer id;

    /**
     * 管理员
     */
    private String admin;

    /**
     * 登入ip
     */
    private String ip;

    /**
     * 操作分类
     */
    private Integer type;

    /**
     * 操作动作
     */
    private String action;

    /**
     * 操作状态
     */
    private Boolean status;

    /**
     * 操作结果，或者成功消息，或者失败消息
     */
    private String result;

    /**
     * 补充信息
     */
    private String comment;

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