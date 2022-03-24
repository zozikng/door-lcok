package com.zzk.dockLock.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzk.dockLock.pojo.Cabinet;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * (Cabinet)表数据库访问层
 *
 * @author makejava
 * @since 2021-12-18 15:32:11
 */
@Mapper
@Repository
public interface CabinetMapper extends BaseMapper<Cabinet> {


    /**
     * 根据站的ip查询该站下有多少个柜
     * @param ip
     * @return
     */
    int selectCountByIp(String ip);

    List<Cabinet> selectCabinetByIp(String stationIp);
}

