package com.zzk.dockLock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: WebMVCConfig配置类
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/19 16:32
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 再次加入前端Origin  localhost！=127.0.0.1
                .allowedOrigins("http://localhost")
                // 是否允许证书（cookies）
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                //允许请求头
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}