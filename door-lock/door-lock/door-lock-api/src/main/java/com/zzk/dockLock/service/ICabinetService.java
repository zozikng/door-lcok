package com.zzk.dockLock.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.dockLock.pojo.Cabinet;

import java.util.List;

/**
 * (ZCabinet)表服务接口
 *
 * @author makejava
 * @since 2021-12-18 15:32:11
 */
public interface ICabinetService extends IService<Cabinet> {

    /**
     * 查询某个站下有多少个柜
     * @param ip
     * @return
     */
    int selectCountByIp(String ip);

    /**
     * 查询某个站下的柜门信息
     * @param stationIp
     * @return
     */
    List<Cabinet> selectCabinetByIp(String stationIp);
}
