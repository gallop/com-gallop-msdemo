package com.gallop.utils;

import java.io.Serializable;

/**
 * @Description: 自定义响应数据结构
 * 这个类是提供给门户，ios，安卓，微信商城用的
 * 门户接受此类数据后需要使用本类的方法转换成对于的数据类型格式（类，或者list）
 * 其他自行处理
 * 200：表示成功
 * 500：表示错误，错误信息在msg字段中
 * 501：bean验证错误，不管多少个错误都以map形式返回
 * 502：拦截器拦截到用户token出错
 * 555：异常抛出信息
 */
public class JSONResult implements Serializable{
    private static final long serialVersionUID = 1L;

    // 响应业务状态
    private Integer status;
    // 响应消息
    private String msg;
    // 响应中的数据
    private Object data;

    public static JSONResult build(Integer status, String msg, Object data) {
        return new JSONResult(status, msg, data);
    }

    public JSONResult() {
    }

    public JSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public JSONResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }


    public static JSONResult ok(Object data) {
        return new JSONResult(data);
    }

    public static JSONResult ok() {
        return new JSONResult(null);
    }

    public static JSONResult errorMsg(Integer status, String msg) {
        return new JSONResult(status, msg, null);
    }

    public static JSONResult blacklist() {
        return new JSONResult(ResponseCode.ACCOUNT_BLACKLIST, "此帐号已经被大耳朵列为黑名单！", null);
    }

    public static JSONResult unlogin() {
        return new JSONResult(ResponseCode.ADMIN_UNLOGIN, "请登录", null);
    }

    public static JSONResult tokenExpires () {
        return new JSONResult(ResponseCode.TOKEN_EXPIRES, "token 过期", null);
    }

    public static JSONResult unauthz() {
        return new JSONResult(ResponseCode.ADMIN_UNAUTHZ, "无操作权限", null);
    }

    public static Object badArgument() {
        return new JSONResult(ResponseCode.PARAMETER_ERROR, "参数不对", null);
    }

    public static JSONResult badArgumentValue() {
        return new JSONResult(ResponseCode.PARAMETER_VALUE_ERROR, "参数值不对", null);
    }

    public static Object updatedDataFailed() {
        return new JSONResult(ResponseCode.UPDATE_DATAFAILED, "更新数据失败", null);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GallopJSONResult{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
