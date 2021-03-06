package com.zzk.dockLock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzk.dockLock.pojo.MenuRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zzk
 * @since 2021-11-10
 */
@Repository
@Mapper
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * @Description: 更新角色菜单（权限）
     * @Title: insertRecord
     * @param rid  @param mids
     * @return: java.lang.Integer
     * @author: zzk
     * @Date: 2021/11/20 20:07
     */
    Integer insertRecord(Integer rid, Integer[] mids);
}
