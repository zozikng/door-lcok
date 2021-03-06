package com.zzk.dockLock.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zzk.dockLock.pojo.Role;
import com.zzk.dockLock.pojo.SysUser;
import com.zzk.dockLock.service.IRoleService;
import com.zzk.dockLock.service.IUserRoleService;
import com.zzk.dockLock.service.IUserService;
import com.zzk.dockLock.vo.ResBean;
import com.zzk.dockLock.vo.params.SysUserParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 用户管理
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/20 20:40
 */
@RestController
@RequestMapping("system/sysuser")
@Api(tags = "UserController 用户接口")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;

    /**
     * @Description: 获取所有用户
     * @Title: getAllUsers
     * @param KeyWords
     * @return: java.util.List<com.zzk.dockLock.pojo.SysUser>
     * @author: zzk
     * @Date: 2021/11/20 20:58
     */
    @ApiOperation ("获取所有的用户(除登录用户外)")
    @GetMapping("/getAllUser")
    public List<SysUser> getAllUsers(String KeyWords){
        return userService.getAllUsers(KeyWords);
    }

    /**
     * @Description: 更新用户信息
     * @Title: updateUser
     * @param userParam
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/20 21:58
     */
    @ApiOperation ("更新用户信息")
    @PostMapping("/updateUser")
    public ResBean updateUser(@RequestBody SysUserParam userParam){
        SysUser sysUser=new SysUser ();
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder ();
        if (userParam.getPassword ()!=null) {
            userParam.setPassword (passwordEncoder.encode (userParam.getPassword ()));
        }
        BeanUtils.copyProperties ( userParam,sysUser);

        if (userService.updateById (sysUser)){
            return ResBean.success ("更新成功");
        }
        return ResBean.fail ("更新失败");
    }


    /**
     * @Description: 更新用户信息
     * @Title: updateUser
     * @param userParam
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/20 21:58
     */
    @ApiOperation ("更新当前用户密码")
    @PutMapping("/currentUserUpdateInfo/{id}/{oldPass}/{pass}")
    public ResBean pcurrentUserUpdateInfo(@PathVariable("id")Long id,@PathVariable("oldPass") String oldPass,@PathVariable("pass") String newPass){

        //获取编码
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder ();
        //如果有密码
        if (oldPass!=null&&newPass!=null) {
            //找到这个用户
            SysUser currentUser=userService.findUserPassWord(id);

            //判断是否和旧密码相等
            if (passwordEncoder.matches (oldPass,currentUser.getPassword ())){
                currentUser.setPassword (passwordEncoder.encode (newPass));
                if (userService.updateById (currentUser)){
                    return ResBean.success ("更新成功");
                }
            }
        }

        return ResBean.fail ("更新失败，旧密码错误");
    }

    /**
     * @Description: 删除用户
     * @Title: deleteUser
     * @param id
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/20 22:01
     */
    @ApiOperation ("删除用户")
    @DeleteMapping("/deleteUser/{id}")
    public ResBean deleteUser(@PathVariable Long id){
        if (userService.removeById (id)){
            return ResBean.success ("删除成功");
        }
        return ResBean.fail ("删除失败");
    }


    /**
     * @Description: 获取所有角色
     * @Title: getAllRoles
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Role>
     * @author: zzk
     * @Date: 2021/11/21 13:03
     */
    @ApiOperation ("获取所有角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.list ();
    }


    /**
     * @Description: 更新用户角色
     * @Title: updateUserRole
     * @param userId  @param rids
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/21 13:57
     */
    @ApiOperation ("更新用户角色")
    @PutMapping("/userRole")
    public ResBean updateUserRole(Long userId,Integer [] rids){
        return userRoleService.updateUserRole(userId,rids);
    }


    /**
     * @Description: 添加用户
     * @Title: addUser
     * @param userParam
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/20 18:22
     */
    @ApiOperation ("添加用户")
    @PostMapping("/addUser")
    public ResBean addUser(@RequestBody SysUserParam userParam){
        SysUser sysUser=new SysUser ();
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder ();
        userParam.setPassword (passwordEncoder.encode (userParam.getPassword ()));

        BeanUtils.copyProperties ( userParam,sysUser);

        if (userService.save (sysUser)){
            Long id = sysUser.getId ();
            System.out.println (id);
            return ResBean.success ("添加成功",id);
        }
        return ResBean.fail ("添加失败");
    }

    /**
     * 分页获取用户除超级管理员外
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation ("分页获取所有用户信息")
    @GetMapping("/getAllUsers")
    public IPage<SysUser> getAllUsersPage(@RequestParam(required = false,defaultValue = "1") Long currentPage,
                                          @RequestParam(required = false,defaultValue = "10") Long pageSize,
                                          String keyWords){
        return userService.getAllUsersPage(currentPage,pageSize,keyWords);
    }



}