package com.zzk.dockLock.config.security.component;

import com.zzk.dockLock.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: Jwt登录过滤器
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/10 21:11
 */
public class JwtAuthencationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头
        String authHeader = request.getHeader (tokenHeader);
        //存在token（判读请求token的头是否以Bearer开头）
        if (null != authHeader && authHeader.startsWith (tokenHead)){
            //截取token
            String authToken=authHeader.substring (tokenHead.length ());
            String username = jwtTokenUtil.getUserNameFromToken (authToken);
            //token存在用户名但未登录
            if (null!=username &&null== SecurityContextHolder.getContext ().getAuthentication ()){
                //登录
                UserDetails userDetails = userDetailsService.loadUserByUsername (username);
                //验证token是否有效，重新设置用户对象
                if (jwtTokenUtil.validateToken (authToken,userDetails)){
                    //重新设置用户token
                    UsernamePasswordAuthenticationToken authenticationToken=
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails (new WebAuthenticationDetailsSource ().buildDetails (request));
                    //token存在用户名但未登录
                    SecurityContextHolder.getContext ().setAuthentication(authenticationToken);
                }
            }

        }

        filterChain.doFilter(request, response);
    }
}