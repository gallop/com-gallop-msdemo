package com.gallop.managersys.shiro;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:重写session 管理，主要是重写getSessionId（）方法，从header中获取token(shiro 中的)sessionId
 *             改变shiro默认使用cookie来管理有状态的session，这样可以使用无状态的session，使之可以应用在前后端分离的项目中，
 * Date: Create in 18:44 2019/5/20
 * Modified By:
 */
@Slf4j
public class AdminWebSessionManager extends DefaultWebSessionManager{
    public static final String LOGIN_TOKEN_KEY = "X-msdemo-Admin-Token";
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    /***
     *
     * 重写getSessionId方法，shiro默认将sessionid存储在cookie中，现改成从header中获取，
     * 这样客户端可以在每次请求中的header中加上sessionid（token），用于服务端的session管理
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = WebUtils.toHttp(request).getHeader(LOGIN_TOKEN_KEY);
        log.info("=================the token is ="+id);

        if (!StringUtils.isEmpty(id)) {//referenced
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        } else {
            return super.getSessionId(request, response);
        }
    }
}
