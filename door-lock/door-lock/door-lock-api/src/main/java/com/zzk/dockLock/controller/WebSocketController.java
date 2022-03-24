package com.zzk.dockLock.controller;

import com.alibaba.fastjson.JSON;
import com.zzk.dockLock.dto.CabinetStateDTO;
import com.zzk.dockLock.service.IInfluxdbService;
import com.zzk.dockLock.vo.ResBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: websocket推送接口
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/25 12:13
 */
@ServerEndpoint(value = "/ws/{ip}")
@Slf4j
@RestController
public class WebSocketController {
    private static Session session;

    @Autowired
    private IInfluxdbService influxdbService;

    public static WebSocketController webSocketController;

    @PostConstruct
    public void init(){
        webSocketController=this;
        webSocketController.influxdbService=this.influxdbService;
    }

    /**
     * 连接集合
     */
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session> ();
    private static Map<String, Integer> lengthMap = new ConcurrentHashMap<String, Integer>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("ip") String stationIp) {
        //添加到集合中
        sessionMap.put(session.getId(), session);
        lengthMap.put(session.getId(), 1);//默认从第一行开始

        //for (CabinetStateDTO cabinetStateDTO : lastListByIdAndIp) {
        //    System.out.println (cabinetStateDTO);
        //}
        //获取日志信息
        new Thread(() -> {
            log.info("LoggingWebSocketServer 任务开始");
            boolean first = true;
            while (sessionMap.get(session.getId()) != null) {

                try {

                    List<CabinetStateDTO> lastListByStationIp = webSocketController.influxdbService.findLastListByStationIp (stationIp);

                    //发送
                    if (lastListByStationIp!=null) {

                        send (session, JSON.toJSONString(lastListByStationIp));
                    }else {
                        send (session, JSON.toJSONString (ResBean.fail ("没有查询到该站或该站下没有柜门")));
                        break;
                    }
                    //休眠一秒
                    Thread.sleep (1000);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }/*finally {
                    Thread.currentThread ().interrupt ();
                }*/
            }
            log.info("LoggingWebSocketServer 任务结束");
        }).start();
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        //从集合中删除
        sessionMap.remove(session.getId());
        lengthMap.remove(session.getId());
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info (message);
    }

    /**
     * 封装一个send方法，发送消息到前端
     */
    private void send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}