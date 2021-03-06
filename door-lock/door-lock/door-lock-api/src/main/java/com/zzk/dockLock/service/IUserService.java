package com.zzk.dockLock.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.dockLock.pojo.Role;
import com.zzk.dockLock.pojo.SysUser;
import com.zzk.dockLock.vo.ResBean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: 用户服务类
 * @program: door-lock
 * @author: zzk
 * @since: 2021-11-10
 */
public interface IUserService extends IService<SysUser> {

    /**
     * @Description: 登录
     * @Title: login
     * @param username   @param password  @param request
     * @param code
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/10 15:39
     */
    ResBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * @Description: 根据用户名获取用户信息
     * @Title: getUserbyUserName
     * @param username
     * @return: com.zzk.dockLock.pojo.User
     * @author: zzk
     * @Date: 2021/11/10 19:32
     */
    SysUser getUserbyUserName(String username);


    /**
     * @Description: 根据用户id去查角色
     * @Title: getRoles
     * @param userId
     * @return: java.util.List<com.zzk.dockLock.pojo.Role>
     * @author: zzk
     * @Date: 2021/11/15 17:32
     */
    List<Role> getRoles(Long userId);

    /**
     * @Description: 获取所有用户
     * @Title: getAllUsers
     * @param keyWords
     * @return: java.util.List<com.zzk.dockLock.pojo.SysUser>
     * @author: zzk
     * @Date: 2021/11/20 20:59
     */
    List<SysUser> getAllUsers(String keyWords);


    IPage<SysUser> getAllUsersPage(Long currentPage, Long pageSize,String keyWords);

    /**
     * @Description: 根据id获取用户密码
     * @Title: findUserPassWord
     * @param id
     * @return: com.zzk.dockLock.pojo.SysUser
     * @author: zzk
     * @Date: 2022/3/9 16:46
     */
    SysUser findUserPassWord(Long id);
}
