package com.zzk.dockLock.service;


import com.zzk.dockLock.dto.CabinetStateDTO;
import com.zzk.dockLock.dto.TrendPoint;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @description: inservice业务接口类
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/20 20:13
 */
public interface IInfluxdbService {

    /**
     * 存入柜门的状态
     * @param cabinetStateDTO
     * @return
     */
    boolean saveCabinetState(CabinetStateDTO cabinetStateDTO);

    /**
     * 根据柜门id和站ip查询最新的单个柜门的状态信息
     * @param deviceId
     * @param stationIp
     * @return
     */
    CabinetStateDTO findLastOneByIdAndIp(int deviceId, String stationIp) throws InvocationTargetException, IllegalAccessException;

    /**
     * 根据柜门id和站ip查询最新的站下所有的柜状态信息
     * @param stationIp
     * @return
     */
    List<CabinetStateDTO> findLastListByStationIp(String stationIp);

    /**
     * @Description: 获取柜门状态日志
     * @Title: getCabinetStateLog
     * @param start 开始时间 
     * @param end 结束时间 
     * @param door 门状态类型（fontDoorState:，backDoorState，fontDoorExce，backDoorExce: ） 
     * @param type 状态（true，false）
     * @return: java.util.List<com.zzk.dockLock.dto.TrendPoint>
     * @author: zzk
     * @Date: 2022/3/2 21:22
     */
    List<TrendPoint> getCabinetStateLog(int deviceId, String stationIp,String start, String end, String door, boolean state, Integer type);
}
