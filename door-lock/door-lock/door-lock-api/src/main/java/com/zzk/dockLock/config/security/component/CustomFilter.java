package com.zzk.dockLock.config.security.component;

import com.zzk.dockLock.pojo.Menu;
import com.zzk.dockLock.pojo.Role;
import com.zzk.dockLock.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * @description: 权限控制   根据请求url分析请求所需角色
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/14 21:54
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IMenuService menuService;

    //Ant 样式路径模式的实现。
    AntPathMatcher antPathMatcher=new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        //获取请求的url
        String requestUrl = ((FilterInvocation) object).getRequestUrl ();
        //查询出来了每一个菜单及其对应所需要的角色
        List<Menu> menus = menuService.getMenuWithRole ();
        //遍历资源，查找用户访问的资源
        for (Menu menu : menus) {
            //判断请求的url与角色是否匹配
            if (antPathMatcher.match (menu.getUrl (),requestUrl)){
                /*
                    Stream 不是集合元素，它不是数据结构并不保存数据，它是有关算法和计算的，它更像一个高级版本的 Iterator。
                原始版本的 Iterator，用户只能显式地一个一个遍历元素并对其执行某些操作；高级版本的 Stream，用户只要给出
                需要对其包含的元素执行什么操作，比如 “过滤掉长度大于 10 的字符串”、“获取每个字符串的首字母”等，Stream
                会隐式地在内部进行遍历，做出相应的数据转换。
                    Stream 就如同一个迭代器（Iterator），单向，不可往复，数据只能遍历一次，遍历过一次后即用尽了，就好比流
                水从面前流过，一去不复返。
                */
                String[] str = menu.getRoles ().stream ().map (Role::getName).toArray (String[]::new);
                //把匹配上的角色放入list
                return SecurityConfig.createList (str);
            }
        }
        //没匹配的url默认登录即可访问
        return SecurityConfig.createList ("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}