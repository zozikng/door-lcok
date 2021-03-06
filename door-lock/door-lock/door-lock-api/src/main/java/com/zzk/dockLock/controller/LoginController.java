package com.zzk.dockLock.controller;

import com.zzk.dockLock.pojo.SysUser;
import com.zzk.dockLock.service.IUserService;
import com.zzk.dockLock.vo.ResBean;
import com.zzk.dockLock.vo.params.LoginParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @description: 用户登录接口
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/10 14:34
 */

@RestController
@Slf4j
@Api(tags = "LoginController 登录接口")
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * @Description: 登录
     * @Title: login
     * @param loginParams  @param request
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/10 16:27
     */
    @ApiOperation ("登录之后返回token")
    @PostMapping("/login")
    public ResBean login(@RequestBody LoginParams loginParams, HttpServletRequest request){
        System.out.println  ("用户名："+loginParams.getUsername ());
        System.out.println ("密码："+loginParams.getPassword ());
        System.out.println ("验证码："+loginParams.getCode ());

        return userService.login(loginParams.getUsername (),loginParams.getPassword (),loginParams.getCode (),request);
    }

    /**
     * @Description: 获取当前登录用户信息
     * @Title: getUserInfo
     * @param principal
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/10 19:28
     */
    @ApiOperation ("获取当前登录用户信息")
    @GetMapping("/user/info")
    public ResBean getUserInfo(Principal principal){
        if (null==principal){
            return null;
        }
        //获取用户名
        String username = principal.getName ();
        SysUser sysUser =userService.getUserbyUserName(username);
        sysUser.setPassword (null);
        sysUser.setRoles (userService.getRoles (sysUser.getId ()));
        return ResBean.success (sysUser);

    }



    /**
     * @Description: 退出登录
     * @Title: loginOut
     * @param
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/10 19:25
     */
    @ApiOperation ("退出登录")
    @PostMapping("/logout")
    public ResBean loginOut(){
        return ResBean.success ("注销成功!!!");
    }


    @ApiOperation ("首页")
    @PostMapping("/")
    public ResBean index(){
        return ResBean.success ("访问成功");
    }


}