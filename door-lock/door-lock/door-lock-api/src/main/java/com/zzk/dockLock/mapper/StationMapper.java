package com.zzk.dockLock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzk.dockLock.pojo.Station;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @description: mapper接口
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/18 15:56
 */
@Mapper
@Repository
public interface StationMapper extends BaseMapper<Station> {
    Station findCabinetListByStationIp(String stationIp);
}
