package com.zzk.dockLock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzk.dockLock.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * @Description: 根据用户id获取用户角色
     * @Title: getRoles
     * @param userId
     * @return: java.util.List<com.zzk.dockLock.pojo.Role>
     * @author: zzk
     * @Date: 2021/11/15 17:35
     */
    List<Role> getRoles(Long userId);
}
