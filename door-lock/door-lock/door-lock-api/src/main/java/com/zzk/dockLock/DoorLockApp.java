package com.zzk.dockLock;

import com.zzk.dockLock.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author zzk
 * @date 2021/11/2 19:38
 * 启动类
 */
@SpringBootApplication
public class DoorLockApp {
    public static void main(String[] args) {
        ApplicationContext context=SpringApplication.run (DoorLockApp.class,args);
        NettyServer nettyServer = context.getBean (NettyServer.class);
        nettyServer.run ();
    }

}
