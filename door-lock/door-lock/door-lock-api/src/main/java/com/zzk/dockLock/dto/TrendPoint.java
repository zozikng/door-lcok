package com.zzk.dockLock.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;

/**
 * @description: 柜门的状态趋势类
 * @program: door-lock
 * @author: zzk
 * @created: 2022/03/02 20:49
 */
@Data
@Measurement (name = "cabinetstate")
public class TrendPoint implements Serializable {


    @Column (name = "pointValue")
    private Double pointValue;

    @Column(name="time")
    private String time;

}