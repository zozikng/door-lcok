package com.zzk.dockLock.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.dockLock.mapper.StationMapper;
import com.zzk.dockLock.pojo.Station;
import com.zzk.dockLock.service.IStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: service层
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/18 16:00
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements IStationService {

    @Autowired
    private StationMapper stationMapper;

    /**
     * @Description: 根据站的ip查找该站下的所有柜门
     * @Title: findCabinetListByStationIp
     * @param stationIp
     * @return: com.zzk.dockLock.pojo.Station
     * @author: zzk
     * @Date: 2022/1/5 21:40
     */
    @Override
    public Station findCabinetListByStationIp(String stationIp) {
       return stationMapper.findCabinetListByStationIp(stationIp);
    }
}