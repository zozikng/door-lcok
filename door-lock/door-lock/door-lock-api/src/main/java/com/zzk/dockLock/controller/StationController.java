package com.zzk.dockLock.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzk.dockLock.pojo.Station;
import com.zzk.dockLock.service.IStationService;
import com.zzk.dockLock.vo.ResBean;
import com.zzk.dockLock.vo.StationVo;
import com.zzk.dockLock.vo.params.StationParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 站接口
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/21 22:17
 */
@RestController
@RequestMapping("/station")
@Api(tags = "StationController 站接口")
public class StationController  {

    @Autowired
    private IStationService stationService;


    /**
     * @Description: 分页获取所有站的
     * @Title: getAllStation
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Station>
     * @author: zzk
     * @Date: 2022/1/5 22:00
     */
    @ApiOperation ("分页获取所有站的")
    @GetMapping("/getAllStationPage")
    public Page<Station> getAllStationPage(@RequestParam(required = false,defaultValue = "1")  Long currentPage,
                                           @RequestParam(required = false,defaultValue = "10") Long pageSize){
        Page<Station> stationPage=new Page<> (currentPage,pageSize);
        QueryWrapper<Station>queryWrapper=new QueryWrapper<> ();
        Page<Station> page = stationService.page (stationPage, queryWrapper);

        return page;
    }

    @ApiOperation ("获取所有的站信息")
    @GetMapping("/getAllStation")
    public List<Station> getAllStation(){
        LambdaQueryWrapper<Station> queryWrapper=new LambdaQueryWrapper<> ();
        queryWrapper.eq (Station::getStationStatus,1);
        return stationService.list (queryWrapper);
    }

    /**
     * @Description: 添加站
     * @Title: addStation
     * @param stationParams
     * @return: boolean
     * @author: zzk
     * @Date: 2022/1/5 22:01
     */
    @ApiOperation ("添加站")
    @PostMapping("/addStation")
    public ResBean addStation(@RequestBody StationParams stationParams){
        Station station=new Station ();
        BeanUtils.copyProperties (stationParams,station);

        if (stationService.save (station)){
            return ResBean.success ("添加成功");
        }
        return ResBean.fail ("添加失败");
    }

    /**
     * @Description: 更新单前站的所有信息
     * @Title: updateAllStationByStationId
     * @param stationParams
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 10:34
     */
    @ApiOperation ("更新站的信息")
    @PostMapping("/updateStation")
    public ResBean updateAllStationByStationId(@RequestBody StationParams stationParams){
        Station station=new Station ();
        BeanUtils.copyProperties (stationParams,station);
        if (stationService.updateById (station)){
            return ResBean.success ("更新成功");
        }
        return ResBean.fail ("更新失败");
    }

    /**
     * @Description: 通过id查询站的消息
     * @Title: findStationById
     * @param stationId
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 11:22
     */
    @ApiOperation ("通过id查询站的信息")
    @PostMapping("/findStationById")
    public ResBean findStationById(Integer stationId){
        try {
            Station station = stationService.getById (stationId);
            StationVo stationVo =new StationVo ();
            BeanUtils.copyProperties (station,stationVo);
            return ResBean.success (stationVo);
        } catch (BeansException e) {
            e.printStackTrace ();
            return ResBean.fail ("查询失败");
        }

    }
    /**
     * @Description: 删除站
     * @Title: deleteUser
     * @param id
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 11:22
     */
    @ApiOperation ("删除站")
    @DeleteMapping("/deleteStation/{id}")
    public ResBean deleteStation(@PathVariable(value = "id") Integer id){
        try {
            if (stationService.removeById (id)){
                return ResBean.success ("删除成功");
            }
            return ResBean.fail ("删除失败，该站还有其他的柜门");
        } catch (Exception e) {
            e.printStackTrace ();
            return ResBean.fail ("删除失败，该站还有柜门请删除柜门后再删除");
        }
    }

}