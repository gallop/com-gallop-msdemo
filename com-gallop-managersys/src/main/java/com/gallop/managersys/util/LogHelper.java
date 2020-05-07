package com.gallop.managersys.util;

import com.gallop.managersys.pojo.AdminUser;
import com.gallop.managersys.pojo.SysLog;
import com.gallop.managersys.service.SysLogService;
import com.gallop.utils.IpUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * author gallop
 * date 2020-04-19 17:15
 * Description:
 * 这里的日志类型设计成四种（当然开发者需要可以自己扩展）
 *  一般日志：用户觉得需要查看的一般操作日志，建议是默认的日志级别
 *  安全日志：用户安全相关的操作日志，例如登录、删除管理员
 *  订单日志：用户交易相关的操作日志，例如订单发货、退款
 *  其他日志：如果以上三种不合适，可以选择其他日志，建议是优先级最低的日志级别
 *
 *  当然可能很多操作是不需要记录到数据库的，例如编辑商品、编辑广告品之类。
 * Modified By:
 */
@Component
public class LogHelper {
    public final static Integer LOG_TYPE_GENERAL = 0;
    public final static Integer LOG_TYPE_AUTH = 1;
    public final static Integer LOG_TYPE_ORDER = 2;
    public final static Integer LOG_TYPE_OTHER = 3;

    @Autowired
    private SysLogService sysLogService;

    public void logAdmin (Integer type, String action, Boolean succeed, String result, String comment){
        SysLog sysLog = new SysLog();

        Subject currentUser = SecurityUtils.getSubject();
        if(currentUser != null) {
            AdminUser adminUser = (AdminUser) currentUser.getPrincipal();
            if(adminUser != null) {
                sysLog.setAdmin(adminUser.getUsername());
            }
            else{
                sysLog.setAdmin("匿名用户");
            }
        }
        else{
            sysLog.setAdmin("匿名用户");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request != null) {
            sysLog.setIp(IpUtil.getIpAddr(request));
        }

        sysLog.setType(type);
        sysLog.setAction(action);
        sysLog.setStatus(succeed);
        sysLog.setResult(result);
        sysLog.setComment(comment);
        sysLogService.add(sysLog);
    }

    public void logGeneralSucceed(String action){
        logAdmin(LOG_TYPE_GENERAL, action, true, "", "");
    }

    public void logGeneralSucceed(String action, String result){
        logAdmin(LOG_TYPE_GENERAL, action, true, result, "");
    }

    public void logGeneralFail(String action, String error){
        logAdmin(LOG_TYPE_GENERAL, action, false, error, "");
    }

    public void logAuthSucceed(String action){
        logAdmin(LOG_TYPE_AUTH, action, true, "", "");
    }

    public void logAuthSucceed(String action, String result){
        logAdmin(LOG_TYPE_AUTH, action, true, result, "");
    }

    public void logAuthFail(String action, String error){
        logAdmin(LOG_TYPE_AUTH, action, false, error, "");
    }
}
