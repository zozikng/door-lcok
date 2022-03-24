package com.zzk.dockLock.config.security;

import com.zzk.dockLock.config.security.component.*;
import com.zzk.dockLock.pojo.SysUser;
import com.zzk.dockLock.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletMapping;
import java.time.Duration;
import java.util.Arrays;

/**
 * @description: SpringSecurity配置类
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/10 20:11
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private IUserService userService;
    //自定义未登录返回结果
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    //自定义无权限返回结果
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    //获取单前角色
    @Autowired
    private CustomFilter customFilter;

    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;


    /**
     * @Description: 登录的时候走自己重写后的userDetails逻辑
     * @Title: configure
     * @param auth
     * @return: void
     * @author: zzk
     * @Date: 2021/11/10 20:41
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService (userDetailsService ()).passwordEncoder (passwordEncoder ());
    }

    /**
     * @Description: 过滤路径
     * @Title: configure
     * @param web
     * @return: void
     * @author: zzk
     * @Date: 2021/11/11 13:26
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring ().antMatchers ("/captcha","/login",
                "/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha",
                "/ws/**");
    }

    /**
     * @Description: springsecurity完整的配置
     * @Title: configure
     * @param http
     * @return: void
     * @author: zzk
     * @Date: 2021/11/10 20:53
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       //使用jwt，不需要csrf
        http
                .csrf ()
                .disable ()
                //不需要session
                .sessionManagement ()
                .sessionCreationPolicy (SessionCreationPolicy.STATELESS)
                .and ()
                .authorizeRequests ()
                //所有请求都要求要认证
                .anyRequest ()
                .authenticated ()
                //动态权限配置
                .withObjectPostProcessor (new ObjectPostProcessor<FilterSecurityInterceptor> () {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager (customUrlDecisionManager);
                        object.setSecurityMetadataSource (customFilter);
                        return object;
                    }
                })
                .and ()
                //缓存用不到
                .headers ()
                .cacheControl ();

        //添加jwt 登录授权过滤器
        http.addFilterBefore (jwtAuthencationTokenFilter (), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling ()
                .accessDeniedHandler (restfulAccessDeniedHandler)
                .authenticationEntryPoint (restAuthorizationEntryPoint);

    }


    /**
     * @Description: 重写父类获取用户信息
     * @Title: userDetailsService
     * @param
     * @return: org.springframework.security.core.userdetails.UserDetailsService
     * @author: zzk
     * @Date: 2021/11/10 20:19
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        //匿名内部类的简写
        return username->{
            SysUser sysUser = userService.getUserbyUserName (username);
            if (null!= sysUser){
                sysUser.setRoles (userService.getRoles (sysUser.getId ()));
                return  sysUser;
            }
            throw new UsernameNotFoundException ("用户名或密码不正确");
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder ();
    }

    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthencationTokenFilter ();
    }

}