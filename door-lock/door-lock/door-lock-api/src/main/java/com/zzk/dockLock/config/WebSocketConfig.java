package com.zzk.dockLock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * @description: Websocket的配置类，开启 WebSocket 支持
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/21 21:53
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}