package com.zzk.dockLock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzk.dockLock.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzk
 * @since 2021-11-10
 */
@Repository
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * @Description: 通过当前用户id获取菜单
     * @Title: getMenuByUserId
     * @param id
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2021/11/14 14:29
     */
    List<Menu> getMenuByUserId(Long id);

    /**
     * @Description: 根据用户角色获取菜单列表
     * @Title: getMenuWithRole
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2021/11/14 21:22
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
     * @Date: 2022/1/6 19:06
     */

    List<Menu> getAllMenusDetail();
}
