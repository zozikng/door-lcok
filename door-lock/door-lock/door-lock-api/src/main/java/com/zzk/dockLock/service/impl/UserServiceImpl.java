package com.zzk.dockLock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.dockLock.mapper.RoleMapper;
import com.zzk.dockLock.mapper.SysUserMapper;
import com.zzk.dockLock.pojo.Role;
import com.zzk.dockLock.pojo.SysUser;
import com.zzk.dockLock.service.IUserService;
import com.zzk.dockLock.utils.JwtTokenUtil;
import com.zzk.dockLock.vo.ErrorCodeEnum;
import com.zzk.dockLock.vo.ResBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 用户服务类
 * @program: door-lock
 * @author: zzk
 * @since 2021-11-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements IUserService {

    //springsecurity的用户服务类
    @Autowired
    private UserDetailsService userDetailsService;

    ///重写springsecuroty的校验密码服务类
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    //token的头部
    @Value ("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RoleMapper roleMapper;


    /**
     * @Description: 登录之后返回token
     * @Title: login
     * @param username   @param password  @param request
     * @param code
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/10 15:08
     */
    @Override
    public ResBean login(String username, String password, String code, HttpServletRequest request) {

        //判断验证码是否正确
        String captcha = (String) request.getSession ().getAttribute ("captcha");
        if (StringUtils.isEmpty (captcha)||!captcha.equalsIgnoreCase (code)){
            return ResBean.fail (ErrorCodeEnum.CODE_ERROR.getCode (),ErrorCodeEnum.CODE_ERROR.getMsg ());
        }
        request.removeAttribute ("captcha");
        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername (username);
        } catch (UsernameNotFoundException e) {
            return ResBean.fail (ErrorCodeEnum.ACCOUNT_PWD_NOT_EXIST.getCode (), ErrorCodeEnum.ACCOUNT_PWD_NOT_EXIST.getMsg ());
        }

        //登录
        //判断用户名或密码是否正确或存在
        if (null==userDetails||!passwordEncoder.matches (password,userDetails.getPassword ())){
            return ResBean.fail (ErrorCodeEnum.ACCOUNT_PWD_NOT_EXIST.getCode (), ErrorCodeEnum.ACCOUNT_PWD_NOT_EXIST.getMsg ());
        }
        //判断用户是否被禁用
        if (!userDetails.isEnabled ()){
            return ResBean.fail (ErrorCodeEnum.ACCOUNT_IS_NO_ENABLED.getCode (), ErrorCodeEnum.ACCOUNT_IS_NO_ENABLED.getMsg ());
        }

        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails
                , null, userDetails.getAuthorities());//用户，凭证为null，权限

        //将token设置springsecurity全局
        SecurityContextHolder.getContext ().setAuthentication (authenticationToken);

        //生成token
        String token = jwtTokenUtil.generateToken (userDetails);

        Map<String,String> tokenMap = new HashMap<>();
         tokenMap.put ("token",token);
         tokenMap.put ("tokenHead",tokenHead);

        return ResBean.success ("登录成功",tokenMap);
    }

    /**
     * @Description: 获取当前用户
     * @Title: getUserbyUserName
     * @param username
     * @return: com.zzk.dockLock.pojo.User
     * @author: zzk
     * @Date: 2021/11/10 19:38
     */
    @Override
    public SysUser getUserbyUserName(String username) {
        return sysUserMapper.selectOne (new LambdaQueryWrapper<SysUser> ().eq (SysUser::getUsername,username).eq (SysUser::isEnabled,true));
    }

    /**
     * @Description: 根据用户id去查角色
     * @Title: getRoles
     * @param userId
     * @return: java.util.List<com.zzk.dockLock.pojo.Role>
     * @author: zzk
     * @Date: 2021/11/15 17:33
     */
    @Override
    public List<Role> getRoles(Long userId) {

        return roleMapper.getRoles(userId);
    }

    /**
     * @Description: 获取所有用户（可以有关键词）
     * @Title: getAllUsers
     * @param keyWords
     * @return: java.util.List<com.zzk.dockLock.pojo.SysUser>
     * @author: zzk
     * @Date: 2021/11/20 21:02
     */
    @Override
    public List<SysUser> getAllUsers(String keyWords) {

        return sysUserMapper.getAllUsers(((SysUser)SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ())
        .getId (),keyWords);
    }
    /**
     * @Description: 分页获取用户信息
     * @Title: getAllUsersPage
     * @param currentPage  @param pageSize  @param keyWords
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.zzk.dockLock.pojo.SysUser>
     * @author: zzk
     * @Date: 2022/2/13 16:35
     */
    @Override
    public IPage<SysUser> getAllUsersPage(Long currentPage, Long pageSize,String keyWords) {

        Page<SysUser> page =new Page<> (currentPage,pageSize);
        SysUser sysUser=new SysUser ();
        return sysUserMapper.getAllUsersPage(page,sysUser,keyWords);

    }

    @Override
    public SysUser findUserPassWord(Long id) {


        return sysUserMapper.selectById (id);
    }
}
