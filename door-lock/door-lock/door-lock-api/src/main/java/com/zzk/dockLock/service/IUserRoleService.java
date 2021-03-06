package com.zzk.dockLock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.dockLock.pojo.UserRole;
import com.zzk.dockLock.vo.ResBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zzk
 * @since 2021-11-10
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * @Description: 更新用户角色
     * @Title: updateUserRole
     * @param userId  @param rids
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/21 13:13
     */
    ResBean updateUserRole(Long userId, Integer[] rids);
}
