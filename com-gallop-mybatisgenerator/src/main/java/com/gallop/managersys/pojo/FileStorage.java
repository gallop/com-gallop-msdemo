package com.gallop.managersys.pojo;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Table(name = "file_storage")
@Data
public class FileStorage {
    private Integer id;

    /**
     * 文件的唯一索引
     */
    private String key;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件访问链接
     */
    private String url;

    /**
     * 创建时间
     */
    @Column(name = "add_time")
    private Date addTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;
}