package com.zzk.dockLock.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

/**
 * @description: 柜门转态信息类
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/19 21:41
 */
@Data
@Measurement(name = "cabinetstate")
public class CabinetStateDTO {

    //设备id
    @Column (name = "deviceId",tag = true)
    private Integer deviceId;

    //前门状态0是关1是开
    @Column (name = "fontDoorState")
    private String fontDoorState;

    //前门状态名字
    @Column (name = "fontDoorStateName")
    private String  fontDoorStateName;

    //后门状态0是关1是开
    @Column (name = "backDoorState")
    private String backDoorState;

  //后门状态名字
    @Column (name = "backDoorStateName")
    private String backDoorStateName;

    //前异常状态0是非异常1是异常
    @Column (name = "fontDoorExce")
    private String fontDoorExce;

    //前异常状态名字
    @Column (name = "fontDoorExceName")
    private String fontDoorExceName;

    //后门异常状态0是非异常1是异常
    @Column (name = "backDoorExce")
    private String backDoorExce;

    //后门异常状态0是非异常1是异常
    @Column (name = "backDoorExceName")
    private String  backDoorExceName;

    @Column (name = "stationIp")
    private String stationIp;

    @Column(name="time")
    private String time;

}
