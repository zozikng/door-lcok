package com.zzk.dockLock.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzk.dockLock.pojo.Menu;
import com.zzk.dockLock.pojo.MenuRole;
import com.zzk.dockLock.pojo.Role;
import com.zzk.dockLock.service.IMenuRoleService;
import com.zzk.dockLock.service.IMenuService;
import com.zzk.dockLock.service.IRoleService;
import com.zzk.dockLock.vo.ResBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 权限管理
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/20 15:09
 */
@RestController
@RequestMapping("system/permiss")
@Api(tags = "PermissController 权限管理接口")
public class PermissController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IMenuRoleService menuRoleService;

    /**
     * @Description: 获取所有角色
     * @Title: getAllRoles
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Role>
     * @author: zzk
     * @Date: 2021/11/20 16:25
     */
    @ApiOperation ("获取所有角色")
    @GetMapping("/getAllRoles")
    public List<Role> getAllRoles(){
        return roleService.list ();
    }

    /**
     * @Description: 添加角色
     * @Title: addRole
     * @param role
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/20 16:33
     */
    @ApiOperation ("添加角色")
    @PostMapping("/addRole")
    public ResBean addRole(@RequestBody Role role){
        if (!role.getName ().startsWith ("ROLE_")){
            role.setName ("ROLE_"+role.getName ());
        }
        if (roleService.save (role)){
            return ResBean.success ("添加成功！");
        }
        return ResBean.fail ("添加失败！");
    }


    /**
     * @Description: 删除所有角色
     * @Title: deleteRole
     * @param rid
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/20 18:58
     */
    @ApiOperation ("删除角色")
    @DeleteMapping("/deleteRole/{rid}")
    public ResBean deleteRole(@PathVariable Integer rid){
        if (roleService.removeById (rid)){
           return ResBean.success ("删除成功！");
        }
        return ResBean.fail ("删除失败");
    }

    /**
     * @Description: 获取所有菜单
     * @Title: getAllMenus
     * @param
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/20 18:58
     */
    @ApiOperation ("获取所有菜单")
    @GetMapping("/getAllMenus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    /**
     * @Description: 根据用户id获取菜单id
     * @Title: getMidByRid
     * @param rid
     * @return: java.util.List<java.lang.Integer>
     * @author: zzk
     * @Date: 2021/11/20 19:37
     */
    @ApiOperation ("根据角色id获取菜单id")
    @GetMapping("/getMidByRid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid){
        return menuRoleService
                .list (new QueryWrapper<MenuRole> ()
                        //根据rid查询出所有
                        .eq ("role_id",rid)).stream ()
                //转为map集合后获取mid变为list集合
                .map (MenuRole::getMenuId).collect (Collectors.toList ());
    }

    /**
     * @Description: 更新角色菜单
     * @Title: updateMenuRole
     * @param rid  @param mids
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2021/11/20 19:43
     */
    @ApiOperation ("更新角色菜单")
    @PutMapping("/updateMenuRole")
    public  ResBean updateMenuRole(Integer rid ,Integer[] mids){

        return ResBean.success ("更新成功",menuRoleService.updateMenuRole(rid,mids));
    }



}