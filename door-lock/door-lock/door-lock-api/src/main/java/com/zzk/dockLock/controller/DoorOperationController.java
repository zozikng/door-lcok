package com.zzk.dockLock.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzk.dockLock.netty.BusinessHandler;
import com.zzk.dockLock.pojo.Station;
import com.zzk.dockLock.service.IStationService;
import com.zzk.dockLock.vo.ResBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 控制门开关的接口
 * @program: door-lock
 * @author: zzk
 * @created: 2022/01/06 09:56
 */
@RestController
@Slf4j
@Api(tags = "DoorOperationController 控制门开关接口")
public class DoorOperationController {

    @Autowired
    private IStationService stationService;

    /**
     * @Description: 开关门的操作
     * @Title: DoorOperation
     * @param stationIp  @param deviceId  @param door  @param off
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 10:01
     */
    @ApiOperation ("控制门的开关")
    @PutMapping("/operateDoor/{stationIp}/{cabinetId}/{door}/{switch}")
    public ResBean DoorOperation(@PathVariable("stationIp") String stationIp, @PathVariable("cabinetId")int deviceId, @PathVariable("door")int door,  @PathVariable("switch")int off){
        try {
            LambdaQueryWrapper<Station> lambdaQueryWrapper=new LambdaQueryWrapper<> ();
            lambdaQueryWrapper.eq (Station::getStationIp,stationIp).eq (Station::getStationStatus,1);
            Station station = stationService.getOne (lambdaQueryWrapper);
            if (station!=null) {
                return ResBean.success (BusinessHandler.sendWriteMessage (stationIp, deviceId, door, off));
            }else{
                return ResBean.fail ("该站未添加或掉线了");
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
            return ResBean.fail ("开门失败");
        }
    }




}