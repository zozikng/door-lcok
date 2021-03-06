package com.zzk.dockLock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.dockLock.mapper.UserRoleMapper;
import com.zzk.dockLock.pojo.UserRole;
import com.zzk.dockLock.service.IUserRoleService;
import com.zzk.dockLock.vo.ResBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zzk
 * @since 2021-11-10
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * @Description: 更新用户角色
     * @Title: updateUserRole
     * @param userId  @param rids
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/21 13:15
     */
    @Override
    @Transactional
    public ResBean updateUserRole(Long userId, Integer[] rids) {
        try {
            userRoleMapper.delete (new QueryWrapper<UserRole> ().eq ("user_id",userId));
            if (null==rids||0==rids.length){
                return ResBean.success ("更新成功");
            }
            Integer result=userRoleMapper.insertRecord(userId,rids);
            if (result==rids.length){
                return ResBean.success ("更新成功");
            }

        } catch (Exception e) {
            e.printStackTrace ();
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus()
                    .setRollbackOnly();
        }
        return ResBean.fail ("更新失败");

    }
}
