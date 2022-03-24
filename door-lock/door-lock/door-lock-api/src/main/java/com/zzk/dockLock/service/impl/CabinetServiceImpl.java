package com.zzk.dockLock.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.dockLock.mapper.CabinetMapper;
import com.zzk.dockLock.pojo.Cabinet;
import com.zzk.dockLock.service.ICabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Cabinet)表服务实现类
 *
 * @author makejava
 * @since 2021-12-18 15:32:11
 */
@Service
public class CabinetServiceImpl extends ServiceImpl<CabinetMapper, Cabinet> implements ICabinetService {

    @Autowired
    private CabinetMapper cabinetMapper;

    /**
     * 查询某个站下有多少个柜
     * @param ip
     * @return
     */
    @Override
    public int selectCountByIp(String ip) {
        return  cabinetMapper.selectCountByIp(ip);
    }

    /**
     * 查询某个站下的柜门信息
     * @param stationIp
     * @return
     */
    @Override
    public List<Cabinet> selectCabinetByIp(String stationIp) {
        return cabinetMapper.selectCabinetByIp(stationIp);
    }
}
