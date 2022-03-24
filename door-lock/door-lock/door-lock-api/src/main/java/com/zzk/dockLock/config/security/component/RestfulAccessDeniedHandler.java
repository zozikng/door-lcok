package com.zzk.dockLock.config.security.component;

import com.alibaba.fastjson.JSON;
import com.zzk.dockLock.vo.ErrorCodeEnum;
import com.zzk.dockLock.vo.ResBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 当访问接口没有权限时自定义返回结果
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/11 12:29
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding ("UTF-8");
        httpServletResponse.setContentType ("application/json");
        PrintWriter out = httpServletResponse.getWriter ();
        out.write(JSON.toJSONString (ResBean.fail (ErrorCodeEnum.NO_PERMISSION.getCode (), ErrorCodeEnum.NO_PERMISSION.getMsg ())));
        out.flush();
        out.close();
    }



}