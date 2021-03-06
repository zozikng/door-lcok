package com.zzk.dockLock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.dockLock.pojo.MenuRole;
import com.zzk.dockLock.vo.ResBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzk
 * @since 2021-11-10
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * @Description: 更新角色菜单
     * @Title: updateMenuRole
     * @param rid  @param mids
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/20 19:44
     */
    ResBean updateMenuRole(Integer rid, Integer[] mids);
}
