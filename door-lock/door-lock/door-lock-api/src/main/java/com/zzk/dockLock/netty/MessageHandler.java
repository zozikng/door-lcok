package com.zzk.dockLock.netty;


import com.zzk.dockLock.dto.CabinetStateDTO;
import com.zzk.dockLock.service.IInfluxdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;


/**
 * @description: 对收到的数据进行处理
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/20 19:30
 */
@Component
public class MessageHandler {

    @Autowired
    private IInfluxdbService influxdbService;

    public static MessageHandler messageHandler;
    @PostConstruct
    public void init(){
       messageHandler=this;
       messageHandler.influxdbService=this.influxdbService;


    }

    /**
     * 存储柜门的信息
     * @param msg
     * @param stationIp
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public  boolean saveState(int[] msg,String stationIp) throws InvocationTargetException, IllegalAccessException {
        try {
            CabinetStateDTO cabinetStateDTO=new CabinetStateDTO ();
            cabinetStateDTO.setDeviceId (msg[0]);
            cabinetStateDTO.setStationIp (stationIp);
            //是否是读操作
            if (msg[1]==2){
                //前后门开关状态
                if (msg[6]==0){
                    cabinetStateDTO.setFontDoorState ("false");
                    cabinetStateDTO.setFontDoorStateName ("前门关闭");
                    cabinetStateDTO.setBackDoorState ("false");
                    cabinetStateDTO.setBackDoorStateName ("后门关闭");
                }
                if (msg[6]==1){
                    cabinetStateDTO.setFontDoorState ("false");
                    cabinetStateDTO.setFontDoorStateName ("前门关闭");
                    cabinetStateDTO.setBackDoorState ("true");
                    cabinetStateDTO.setBackDoorStateName ("后门打开");
                }
                if (msg[6]==2){
                    cabinetStateDTO.setFontDoorState ("true");
                    cabinetStateDTO.setFontDoorStateName ("前门打开");
                    cabinetStateDTO.setBackDoorState ("false");
                    cabinetStateDTO.setBackDoorStateName ("后门关闭");

                }
                if (msg[6]==3){
                    cabinetStateDTO.setFontDoorState ("true");
                    cabinetStateDTO.setFontDoorStateName ("前门打开");
                    cabinetStateDTO.setBackDoorState ("true");
                    cabinetStateDTO.setBackDoorStateName ("后门打开");
                }
                //门异常状态
                if (msg[8]==0){
                    cabinetStateDTO.setFontDoorExce ("false");
                    cabinetStateDTO.setFontDoorExceName ("前门正常");
                    cabinetStateDTO.setBackDoorExce ("false");
                    cabinetStateDTO.setBackDoorExceName ("后门正常");
                }if (msg[8]==1){
                    cabinetStateDTO.setFontDoorExce ("false");
                    cabinetStateDTO.setFontDoorExceName ("前门正常");
                    cabinetStateDTO.setBackDoorExce ("true");
                    cabinetStateDTO.setBackDoorExceName ("后门异常");

                }if (msg[8]==2){
                    cabinetStateDTO.setFontDoorExce ("true");
                    cabinetStateDTO.setFontDoorExceName ("前门异常");
                    cabinetStateDTO.setBackDoorExce ("false");
                    cabinetStateDTO.setBackDoorExceName ("后门正常");
                }if (msg[8]==3){
                    cabinetStateDTO.setFontDoorExce ("true");
                    cabinetStateDTO.setFontDoorExceName ("前门异常");
                    cabinetStateDTO.setBackDoorExce ("true");
                    cabinetStateDTO.setBackDoorExceName ("后门异常");
                }
            }
            //是否是写操作
            if (msg[1]==5) {
                cabinetStateDTO = messageHandler.influxdbService.findLastOneByIdAndIp (msg[0], stationIp);
                cabinetStateDTO.setDeviceId (msg[0]);
                cabinetStateDTO.setTime (null);
                //对前门进行操作
                if (msg[3] == 3) {
                    if (msg[5] == 1) {
                        cabinetStateDTO.setFontDoorState ("true");
                        cabinetStateDTO.setFontDoorStateName ("前门打开");
                    }
                    cabinetStateDTO.setFontDoorState ("false");
                    cabinetStateDTO.setFontDoorStateName ("前门关闭");

                }
                //对后门进行操作
                if (msg[3] == 4) {
                    if (msg[3] == 3) {
                        if (msg[5] == 1) {
                            cabinetStateDTO.setBackDoorState ("true");
                            cabinetStateDTO.setBackDoorStateName ("后门打开");
                        }
                        cabinetStateDTO.setBackDoorState ("false");
                        cabinetStateDTO.setBackDoorStateName ("后门关闭");
                    }

                }
            }
            messageHandler.influxdbService.saveCabinetState (cabinetStateDTO);
            return true;
        } catch (InvocationTargetException e) {
            e.printStackTrace ();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace ();
            return false;
        }
    }


}