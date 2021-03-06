package com.zzk.dockLock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzk.dockLock.pojo.UserRole;
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
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * @Description: 更新用户角色
     * @Title: insertRecord
     * @param userId  @param rids
     * @return: java.lang.Integer
     * @author: zzk
     * @Date: 2021/11/21 13:33
     */
    Integer insertRecord(Long userId, Integer[] rids);
}
