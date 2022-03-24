package com.zzk.dockLock.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.dockLock.mapper.MenuMapper;
import com.zzk.dockLock.pojo.Menu;
import com.zzk.dockLock.pojo.SysUser;
import com.zzk.dockLock.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzk
 * @since 2021-11-10
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * @Description: 通过当前用户id获取菜单
     * @Title: getMenuByUserId
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2021/11/14 14:22
     */
    @Override
    public List<Menu> getMenuByUserId() {
        //通过security的全局上下文获取当前用户的id
        return menuMapper.getMenuByUserId(((SysUser)SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ()).getId ());
    }

    /**
     * @Description: 根据角色获取菜单列表
     * @Title: getMenuWithRole
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2021/11/14 21:19
     */
    @Override
    public List<Menu> getMenuWithRole() {


        return  menuMapper.getMenuWithRole();
    }

    /**
     * @Description: 获取所有菜单
     * @Title: getAllMenus
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2021/11/20 19:01
     */
    @Override
    public List<Menu> getAllMenus() {

        return menuMapper.getAllMenus ();
    }

    /**
     * @Description: 获取所有的菜单有父子关系且详细
     * @Title: getAllMenusDetail
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2022/1/6 19:05
     */
    @Override
    public List<Menu> getAllMenusDetail() {
        return menuMapper.getAllMenusDetail ();
    }
}
