package com.zzk.dockLock.config.security.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @description: 判断用户角色过滤器
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/15 19:52
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {

    /**
     * 该方法的参数Collection<ConfigAttribute>
     * 通过拦截Request传入FilterInvocationSecurityMetadataSource的getAttributes获取到的角色集合
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : collection) {
            //当前url所需角色
            String needRole = configAttribute.getAttribute ();
            //判断角色是否登录即可访问的角色，此角色在CustomFilter中配置
            if ("ROLE_LOGIN".equals (needRole)){
                //判断是否登录
                if (authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException ("尚未登录请登录");
                }else {
                    return;
                }
            }
            //判断角色是否为url角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities ();

            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority ().equals (needRole)){
                    return;
                }
            }
        }
        throw new AccessDeniedException ("权限不足，请联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}