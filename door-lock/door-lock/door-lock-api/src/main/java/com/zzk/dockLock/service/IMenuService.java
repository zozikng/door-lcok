package com.zzk.dockLock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.dockLock.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzk
 * @since 2021-11-10
 */
public interface IMenuService extends IService<Menu> {

    /**
     * @Description: 通过当前用户id获取菜单
     * @Title: getMenuByUserId
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2021/11/14 14:21
     */
    List<Menu> getMenuByUserId();


    /**
     * @Description: 根据角色获取菜单列表
     * @Title: getMenuWithRole
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2021/11/14 21:18
     */
    List<Menu> getMenuWithRole();

    /**
     * @Description: 获取所有菜单
     * @Title: getAllMenus
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2021/11/20 19:01
     */
    List<Menu> getAllMenus();

    /**
     * @Description: 获取所有的菜单有父子关系且详细
     * @Title: getAllMenusDetail
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2022/1/6 19:05
     */
    List<Menu> getAllMenusDetail();

}
