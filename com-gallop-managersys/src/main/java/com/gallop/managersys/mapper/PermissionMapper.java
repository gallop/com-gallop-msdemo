package com.gallop.managersys.mapper;

import com.gallop.managersys.pojo.Permission;
import com.gallop.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Example;

public interface PermissionMapper extends MyMapper<Permission> {
     /**
      * date 2020/4/24 12:29
      * Description: mybatis中的xml文件是无法识别这里的参数名称的，
      * 这里必须使用@Param 指定参数的名称，
      * 因为在map xml 的sql语句会使用到
      * example 参数，如果这里不指定，会报找不到example的异常
      * Param:
      * return:
      **/
    public void logicalDeleteByExample(@Param("example") Example example);
}