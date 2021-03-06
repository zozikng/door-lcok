package com.zzk.dockLock.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.zzk.dockLock.dto.TrendPoint;
import com.zzk.dockLock.pojo.Cabinet;
import com.zzk.dockLock.service.ICabinetService;
import com.zzk.dockLock.service.IInfluxdbService;
import com.zzk.dockLock.vo.LineVo;
import com.zzk.dockLock.vo.ResBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @description: 柜门接口
 * @program: door-lock
 * @author: zzk
 * @created: 2022/01/06 12:54
 */
@RestController
@RequestMapping("/cabinet")
@Api(tags = "CabinetController 柜门接口")
public class CabinetController {
    @Autowired
    private IInfluxdbService influxdbService;
    @Autowired
    private ICabinetService cabinetService;


    /**
     * @Description: 添加柜门
     * @Title: addCanbinet
     * @param cabinet
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 14:55
     */
    @ApiOperation ("添加柜门")
    @PostMapping("/addCabinet")
    public ResBean addCanbinet(@RequestBody Cabinet cabinet){

        try {
            if (cabinetService.save (cabinet)){
                return ResBean.success ("添加成功");
            }
            return ResBean.fail ("添加失败");
        } catch (Exception e) {
            e.printStackTrace ();
            return ResBean.fail ("没有对应的站,请添加站");
        }

    }

    /**
     * @Description: 删除柜门
     * @Title: deleteUser
     * @param id
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 14:56
     */
    @ApiOperation ("删除柜门")
    @DeleteMapping("/deleteCabinet/{cabinetId}")
    public ResBean deleteUser(@PathVariable("cabinetId") Integer id){

            if (cabinetService.removeById (id)){
                return ResBean.success ("删除成功");
            }
            return ResBean.fail ("删除失败，该站还有其他的柜门");
    }

    /**
     * @Description: 修改柜门信息
     * @Title: updateAllStationByStationId
     * @param cabinet
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 14:57
     */
    @ApiOperation ("更新柜门信息")
    @PostMapping("/updateCabinet")
    public ResBean updateAllStationByStationId(@RequestBody Cabinet cabinet){
        try {
            if (cabinetService.updateById (cabinet)){
                return ResBean.success ("更新成功");
            }
            return ResBean.fail ("更新失败,您更新的站不存在或柜门id不存在");
        } catch (Exception e) {
            e.printStackTrace ();
            return ResBean.fail ("更新失败,您更新的站不存在或柜门id不存在");
        }
    }


    /**
     * @Description: 分页获取所有柜门信息
     * @Title: getAllCabinet
     * @param currentPage  @param pageSize
     * @return: java.util.List<com.zzk.dockLock.pojo.Cabinet>
     * @author: zzk
     * @Date: 2022/1/6 15:23
     */
    @ApiOperation ("分页获取所有柜门")
    @PostMapping("/getAllCabinet")
    public ResBean getAllCabinet(Long currentPage, Long pageSize){
        try {
            Page<Cabinet> stationPage=new Page<> (currentPage,pageSize);
            QueryWrapper<Cabinet> queryWrapper=new QueryWrapper<> ();
            Page<Cabinet> page = cabinetService.page (stationPage, queryWrapper);
            return ResBean.success (page);
        } catch (Exception e) {
            return ResBean.fail ("获取信息失败");
        }
    }

    @ApiOperation ("通过id查询站的消息")
    @PostMapping("/findCabinetById")
    public ResBean findCabinetById(Integer cabinetId){
        try {
            Cabinet cabinet = cabinetService.getById (cabinetId);
            if (cabinet!=null) {
                return ResBean.success (cabinet);
            }
            return ResBean.fail ("查询失败，没有该柜门");
        } catch (BeansException e) {
            e.printStackTrace ();
            return ResBean.fail ("查询失败");
        }

    }

    @ApiOperation ("获取某个站（Ip）下的柜门")
    @GetMapping("/findCabinetByStationIp")
    public ResBean findCabinetByStationIp(String stationIp){
        try {
            return ResBean.success (cabinetService.selectCabinetByIp (stationIp));
        } catch (Exception e) {
            return ResBean.fail ("获取信息失败");
        }
    }


    /**
     * @Description: 获取柜门某段时间的状态
     * @Title: getCabinetStateLog
     * @param start 开始时间
     * @param end 结束时间
     * @param door 门状态类型（fontDoorState:，backDoorState，fontDoorExce，backDoorExce: ）
     * @param type 状态（true，false）
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/3/2 21:24
     */
    @ApiOperation ("获取柜门某段时间的状态日志")
    @PostMapping("/getCabinetStateLog/{cabinetId}/{stationIp}/{start}/{end}/{door}/{state}/{type}")
    public LineVo getCabinetStateLog(@PathVariable Integer cabinetId,@PathVariable String stationIp,@PathVariable String start,@PathVariable String end,
                                      @PathVariable String door,@PathVariable boolean state, @PathVariable Integer type){
        try {
            List<TrendPoint> trendPointList = influxdbService.getCabinetStateLog(cabinetId,stationIp,start,end,door,state,type);

            LineVo lineVo=new LineVo ();
            lineVo.setXdata (Lists.newArrayList ());
            lineVo.setSeriess (Lists.newArrayList ());

            trendPointList.forEach (trendPoint -> {
                lineVo.getXdata ().add (formatTime (trendPoint.getTime (),type));
                lineVo.getSeriess ().add (trendPoint.getPointValue ().longValue ());

            });


            return lineVo;
        } catch (Exception e) {
            e.printStackTrace ();
            return null;
        }

    }

    private String formatTime(String time,Integer type){

        LocalDateTime localDateTime = LocalDateTime.parse (time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);


        if (type==1){
            return localDateTime.plusHours (8).getMinute ()+"";
        }else if (type==2){
            return  localDateTime.plusHours (8).getDayOfMonth ()+"日"+localDateTime.plusHours (8).getHour ()+":00";
        }else{
            return localDateTime.plusHours (8).getMonthValue ()+"月"+localDateTime.plusHours (8).getDayOfMonth ()+"日";
        }

    }





}