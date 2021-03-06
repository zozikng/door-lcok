package com.zzk.dockLock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzk.dockLock.pojo.SysUser;
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
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * @Description: 获取所有用户(可以有关键字)
     * @Title: getAllUsers
     * @param id  @param keyWords
     * @return: java.util.List<com.zzk.dockLock.pojo.SysUser>
     * @author: zzk
     * @Date: 2021/11/20 21:05
     */
    List<SysUser> getAllUsers(Long id, String keyWords);

    IPage<SysUser> getAllUsersPage(Page<SysUser> page, SysUser sysUser,String keyWords);
}
