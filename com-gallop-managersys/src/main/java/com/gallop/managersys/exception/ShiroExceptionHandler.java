package com.gallop.managersys.exception;

import com.gallop.utils.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;

import static com.gallop.utils.ResponseCode.UNKONWN_ERROR;

/**
 * Author: gallop
 * E-Mail: 39100782@qq.com
 * Description:
 * Date: Create in 21:05 2019/5/20
 * Modified By:
 */
@ControllerAdvice
@Order( value = Ordered.HIGHEST_PRECEDENCE )
@Slf4j
public class ShiroExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Object unauthenticatedHandler(AuthenticationException e) {
        e.printStackTrace();
        log.info("进入unauthenticatedHandler。。。。。。");
        log.error(e.getMessage());
        return JSONResult.unlogin();
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public Object unauthorizedHandler(AuthorizationException e) {
        e.printStackTrace();
        return JSONResult.unauthz();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Object unkonwExceptionHandler(RuntimeException e){
        e.printStackTrace();
        return JSONResult.errorMsg(UNKONWN_ERROR,e.getMessage());
    }
}
