package com.zzk.dockLock.config;

import com.google.common.collect.Lists;
import com.zzk.dockLock.vo.ErrorCodeEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: swagger接口文档配置类
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/07 19:54
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {


    @Bean
    public Docket createRestApi(Environment environment){
        //加载springboot的配置环境
        Profiles profiles = Profiles.of ("dev","test");
        // 判断当前是否处于该环境
        // 通过 enable() 接收此参数判断是否要显示
        boolean b = environment.acceptsProfiles (profiles);

        return new Docket (DocumentationType.SWAGGER_2)
                .apiInfo (apiInfo())//配置文档信息
                .enable (b)//判断是否是开发环境
                .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
                .apis (RequestHandlerSelectors.basePackage ("com.zzk.dockLock.controller"))
                .paths (PathSelectors.any ())//后期可以配置过滤的接口
                .build ()
                //配置全局错误异常转态码
                .globalResponseMessage(RequestMethod.GET, Lists.newArrayList(
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.ACCOUNT_PWD_NOT_EXIST.getCode ())
                                .message (ErrorCodeEnum.ACCOUNT_PWD_NOT_EXIST.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.CODE_ERROR.getCode ())
                                .message (ErrorCodeEnum.CODE_ERROR.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.ACCOUNT_IS_NO_ENABLED.getCode ())
                                .message (ErrorCodeEnum.ACCOUNT_IS_NO_ENABLED.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.NO_PERMISSION.getCode ())
                                .message (ErrorCodeEnum.NO_PERMISSION.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.NO_LOGIN.getCode ())
                                .message (ErrorCodeEnum.NO_LOGIN.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (200)
                                .message ("成功")
                                .build (),
                        new ResponseMessageBuilder()
                                .code (404)
                                .message ("错误")
                                .build (),
                        new ResponseMessageBuilder()
                                .code (-999)
                                .message ("系统异常")
                                .build ()
                        ))
                .globalResponseMessage(RequestMethod.POST, Lists.newArrayList(
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.ACCOUNT_PWD_NOT_EXIST.getCode ())
                                .message (ErrorCodeEnum.ACCOUNT_PWD_NOT_EXIST.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.CODE_ERROR.getCode ())
                                .message (ErrorCodeEnum.CODE_ERROR.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.ACCOUNT_IS_NO_ENABLED.getCode ())
                                .message (ErrorCodeEnum.ACCOUNT_IS_NO_ENABLED.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.NO_PERMISSION.getCode ())
                                .message (ErrorCodeEnum.NO_PERMISSION.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (ErrorCodeEnum.NO_LOGIN.getCode ())
                                .message (ErrorCodeEnum.NO_LOGIN.getMsg ())
                                .build (),
                        new ResponseMessageBuilder()
                                .code (200)
                                .message ("成功")
                                .build (),
                        new ResponseMessageBuilder()
                                .code (404)
                                .message ("错误")
                                .build (),
                        new ResponseMessageBuilder()
                                .code (-999)
                                .message ("系统异常")
                                .build ()
                ))
                .securityContexts (securityContext ())
                .securitySchemes (securitySchemes ());


    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact ("zzk", "#", "811400940");
        return new ApiInfo(
                " 环网柜门禁管理系统", // 标题
                "A301工作室提供", // 描述
                "v1.0", // 版本
                "A301工作室提供", // 组织链接
                contact, // 联系人信息
                "Apach 2.0 许可", // 许可
                "许可链接", // 许可连接
                new ArrayList<> ()// 扩展
        );

    }

    private List<ApiKey> securitySchemes(){
        //设置请求头信息
        List<ApiKey> result= new ArrayList<>();
        ApiKey apiKey = new ApiKey("Authorization","Authorization","Header");
        result.add(apiKey);
        return result;
    }

    public List<SecurityContext> securityContext(){
        //设置需要登录的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add (getContextByPath("/hello/.*"));
        return  result;

    }

    private SecurityContext getContextByPath(String pathRegex) {

        return SecurityContext.builder ()
                .securityReferences (defultAuth())
                .forPaths (PathSelectors.regex (pathRegex))
                .build ();
    }

    private List<SecurityReference> defultAuth() {

        List<SecurityReference> result = new ArrayList<>();
        //授权范围                                                      //全局                    //描述
        AuthorizationScope authorizationScope = new AuthorizationScope("global","accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization",authorizationScopes));
        return result;
    }


}