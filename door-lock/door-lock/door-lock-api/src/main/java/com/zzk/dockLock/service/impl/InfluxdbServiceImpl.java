package com.zzk.dockLock.service.impl;

import com.zzk.dockLock.dto.CabinetStateDTO;
import com.zzk.dockLock.dto.TrendPoint;
import com.zzk.dockLock.pojo.Cabinet;
import com.zzk.dockLock.pojo.Station;
import com.zzk.dockLock.service.IInfluxdbService;
import com.zzk.dockLock.service.IStationService;
import com.zzk.dockLock.utils.InfluxdbUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: influxdbservic实现类
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/20 20:14
 */
@Slf4j
@Service
public class InfluxdbServiceImpl implements IInfluxdbService {

    @Autowired
    private InfluxdbUtil influxdbUtil;

    @Autowired
    private IStationService stationService;

    /**
     * 存入柜门的状态
     * @param cabinetStateDTO
     * @return
     */
    @Override
    public boolean saveCabinetState(CabinetStateDTO cabinetStateDTO) {



        return influxdbUtil.insertOne (cabinetStateDTO);
    }

    /**
     * 根据柜门id和站ip查询最新的状态信息
     * @param deviceId
     * @param stationIp
     * @return
     */
    @Override
    public CabinetStateDTO findLastOneByIdAndIp(int deviceId, String stationIp) throws InvocationTargetException, IllegalAccessException {
        //select * from cabinetstate where deviceId='1' and stationIp='127.0.0.1' group by "deviceId"  order by time desc limit 1
        int pageSize = 1;
        // InfluxDB支持分页查询,因此可以设置分页查询条件
        String pageQuery = " LIMIT " + pageSize;

        String queryCondition = " WhERE deviceId = \'"+ deviceId +"\' and stationIp = \'"+ stationIp +"\' group by \"deviceId\"";  //查询条件暂且为空
        // 此处查询所有内容,如果
        String queryCmd = "SELECT * FROM cabinetstate"
                // 查询指定设备下的日志信息
                // 要指定从 RetentionPolicyName.measurement中查询指定数据,默认的策略可以不加；
                // + 策略name + "." + measurement
                // 添加查询条件(注意查询条件选择tag值,选择field数值会严重拖慢查询速度)
                + queryCondition
                // 查询结果需要按照时间排序
                + " ORDER BY time DESC"
                // 添加分页查询条件
                + pageQuery;
        System.out.println (queryCmd);
        List<CabinetStateDTO> stateDTOList = influxdbUtil.fetchResults (queryCmd, CabinetStateDTO.class);
        //List<Sensor> sensorList = influxdbUtils.fetchResults("*", "sensor", Sensor.class);
        //获取第一个
        CabinetStateDTO cabinetStateDTOresult = null;
        if (stateDTOList.size ()==1 &&stateDTOList!=null) {
            for (CabinetStateDTO cabinetStateDTO : stateDTOList) {
                cabinetStateDTOresult=cabinetStateDTO;
            }
        }



        return cabinetStateDTOresult;
    }

    /**
     * 根据根据站的Ip找到最新柜门的所有消息
     * @param stationIp
     * @return
     */
    @Override
    public List<CabinetStateDTO> findLastListByStationIp(String stationIp) {
        Station station = stationService.findCabinetListByStationIp (stationIp);

        if (station!=null) {
            List<Cabinet> cabinets = station.getCabinets ();
            List<CabinetStateDTO> cabinetDTOList = new ArrayList<>();
            for (Cabinet cabinet : cabinets) {

                int pageSize = 1;
                // InfluxDB支持分页查询,因此可以设置分页查询条件
                String pageQuery = " LIMIT " + pageSize;

                String queryCondition = " WhERE deviceId = \'" + cabinet.getCabinetId () + "\' and stationIp = \'" + stationIp + "\' group by \"deviceId\"";  //查询条件暂且为空
                // 此处查询所有内容,如果
                String queryCmd = "SELECT * FROM cabinetstate"
                        // 查询指定设备下的日志信息
                        // 要指定从 RetentionPolicyName.measurement中查询指定数据,默认的策略可以不加；
                        // + 策略name + "." + measurement
                        // 添加查询条件(注意查询条件选择tag值,选择field数值会严重拖慢查询速度)
                        + queryCondition
                        // 查询结果需要按照时间排序
                        + " ORDER BY time DESC"
                        // 添加分页查询条件
                        + pageQuery;
                log.info (queryCmd);

                List<CabinetStateDTO> stateDTOList = influxdbUtil.fetchResults (queryCmd, CabinetStateDTO.class);
                if (stateDTOList.size () == 1 && stateDTOList != null) {
                    for (CabinetStateDTO cabinetStateDTO : stateDTOList) {
                        cabinetDTOList.add (cabinetStateDTO);

                    }
                }

            }
            return cabinetDTOList;
        }

        return null;
    }


    /**
     * @Description: 获取柜门某段时间的状态
     * @Title: getCabinetStateLog
     * @param deviceId  @param stationIpString  @param end  @param door  @param type
     * @return: java.util.List<com.zzk.dockLock.dto.TrendPoint>
     * @author: zzk
     * @Date: 2022/3/3 11:19
     */
    @Override
    public List<TrendPoint> getCabinetStateLog(int deviceId, String stationIp,String start, String end, String door,boolean state, Integer type) {

        StringBuffer queryCmd = new StringBuffer("SELECT count("+door+") as pointValue FROM cabinetstate ");
        queryCmd.append (" WhERE  "+door+" = \'"+state+"\' and time > \'"+start+"\' and time < \'"+end+"\' and deviceId = \'"+deviceId+"\' and stationIp= \'"+stationIp+"\' ");

        if (type==1){
            queryCmd.append (" group by time(1m)" );
        }else if (type==2){
            queryCmd.append (" group by time(1h) ");
        } else{
            queryCmd.append (" group by time(1d) ");
        }

        System.out.println (String.valueOf (queryCmd));
        log.info (String.valueOf (queryCmd));

        List<TrendPoint> trendPointList = influxdbUtil.fetchResults (String.valueOf (queryCmd), TrendPoint.class);


        return trendPointList;

    }
}