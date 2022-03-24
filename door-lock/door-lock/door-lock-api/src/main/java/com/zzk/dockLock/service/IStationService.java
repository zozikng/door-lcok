package com.zzk.dockLock.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.dockLock.pojo.Station;

/**
 * @description: service接口
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/18 15:58
 */
public interface IStationService extends IService<Station> {


    /**
     * 找到该站下所有的柜门
     * @param stationIp
     * @return
     */
    Station findCabinetListByStationIp(String stationIp);
}
