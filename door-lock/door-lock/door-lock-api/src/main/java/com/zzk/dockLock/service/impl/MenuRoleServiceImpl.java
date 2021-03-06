package com.zzk.dockLock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.dockLock.mapper.MenuRoleMapper;
import com.zzk.dockLock.pojo.Menu;
import com.zzk.dockLock.pojo.MenuRole;
import com.zzk.dockLock.service.IMenuRoleService;
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
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {


    @Autowired
    private  MenuRoleMapper menuRoleMapper;

    /**
     * @Description: 更新角色菜单
     * @Title: updateMenuRole
     * @param rid  @param mids
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/20 19:45
     */
    @Override
    @Transactional
    public ResBean updateMenuRole(Integer rid, Integer[] mids) {

        try {
            menuRoleMapper.delete (new QueryWrapper<MenuRole> ().eq ("role_id",rid));
            if (null==mids||0==mids.length){
                return ResBean.success ("更新成功");
            }
            Integer result=menuRoleMapper.insertRecord(rid,mids);

            if (result==mids.length){
                return ResBean.success ("更新成功");
            }
        } catch (Exception e) {
            e.printStackTrace ();
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus()
                    .setRollbackOnly();
            return ResBean.fail ("更新失败");
        }
        return ResBean.fail ("更新失败");
    }
}
