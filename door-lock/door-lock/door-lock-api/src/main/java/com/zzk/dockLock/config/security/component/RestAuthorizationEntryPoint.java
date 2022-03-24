package com.zzk.dockLock.config.security.component;

import com.alibaba.fastjson.JSON;
import com.zzk.dockLock.vo.ErrorCodeEnum;
import com.zzk.dockLock.vo.ResBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 当未登录或者token失效时访问接口时，自定义返回结果
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/11 12:17
 */
@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //设置编码格式
        httpServletResponse.setCharacterEncoding ("UTF-8");
        //设置返回类型
        httpServletResponse.setContentType ("application/json");
        PrintWriter out = httpServletResponse.getWriter ();
        out.write (JSON.toJSONString (ResBean.fail (ErrorCodeEnum.NO_LOGIN.getCode (), ErrorCodeEnum.NO_LOGIN.getMsg ())));
        out.flush ();
        out.close ();
    }
}