package com.gallop.utils;

/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:
 * Date: Create in 10:06 2019/5/21
 * Modified By:
 */
public class ResponseCode {
    /**
     * 4xx，前端错误，说明前端开发者需要重新了解后端接口使用规范：
     *
     * 401，参数错误，即前端没有传递后端需要的参数；
     * 402，参数值错误，即前端传递的参数值不符合后端接收范围。
     */
    public static final Integer PARAMETER_ERROR=401;
    public static final Integer PARAMETER_VALUE_ERROR=402; //参数值不对
    public static final Integer ADMIN_INVALID_USERNAME = 601;//不可用的账号名称
    public static final Integer ADMIN_INVALID_PASSWORD = 602;//密码错误
    public static final Integer ADMIN_NAME_EXIST = 603;//账号不存在
    public static final Integer ADMIN_UNLOGIN = 604;//未登入
    public static final Integer ADMIN_UNAUTHZ =605;//未授权
    //public static final Integer ADMIN_ALTER_NOT_ALLOWED = 604;
    public static final Integer ADMIN_DELETE_NOT_ALLOWED = 606;
    public static final Integer ADMIN_INVALID_ACCOUNT = 606;//不可用账号
    public static final Integer ACCOUNT_BLACKLIST = 607;//黑名单
    public static final Integer TOKEN_EXPIRES = 609;//token过期
    public static final Integer UPDATE_DATAFAILED = 505; //更新数据失败
    public static final Integer ROLE_NAME_EXIST = 640;
    public static final Integer ROLE_SUPER_SUPERMISSION = 641;
    public static final Integer ROLE_USER_EXIST = 642;
    public static final Integer UNKONWN_ERROR = 1007;
}
