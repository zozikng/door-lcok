package com.zzk.dockLock.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzk.dockLock.pojo.Menu;
import com.zzk.dockLock.service.IMenuService;
import com.zzk.dockLock.vo.ResBean;
import com.zzk.dockLock.vo.params.MenuParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 菜单接口类
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/14 14:14
 */
@RestController
@RequestMapping("/system/menu")
@Api(tags = "MenuController 菜单管理接口")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    /**
     * @Description: 通过当前用户id获取菜单列表
     * @Title: getMenuByUserId
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2021/11/14 14:14
     */
    @ApiOperation (value = "通过当前用户id获取菜单列表")
    @GetMapping("/getMenuBycurrentUser")
    public List<Menu> getMenuByUserId(){
        return menuService.getMenuByUserId();
    }


  /**
     * @Description: 获取一级子菜单
     * @Title: getAllFirstChildMenu
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2022/1/6 16:27
     */
    @ApiOperation ("获取一级子菜单")
    @GetMapping("/getAllFirstChildMenu")
    public ResBean getAllFirstChildMenu(){
        try {
            LambdaQueryWrapper<Menu> lambdaQueryWrapper=new LambdaQueryWrapper<> ();
            lambdaQueryWrapper.eq (Menu::getParentId,1);
            return ResBean.success (menuService.list (lambdaQueryWrapper));
        } catch (Exception e) {
            return ResBean.fail ("获取信息失败");
        }
    }

    /**
     * @Description: 添加子菜单
     * @Title: addMenu
     * @param menuParams
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 18:54
     */
    @ApiOperation ("添加子菜单")
    @PostMapping("/addMenu")
    public ResBean addMenu(@RequestBody MenuParams menuParams){
        Menu menu=new Menu ();
        BeanUtils.copyProperties (menuParams,menu);
        if (menuService.save (menu)){
                return ResBean.success ("添加成功");
            }
            return ResBean.fail ("添加失败");

    }

    /**
     * @Description: 分页获取所有站
     * @Title: getAllStation
     * @param currentPage  @param pageSize
     * @return: com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2022/1/6 18:55
     */
    @ApiOperation ("分页获取所有站的（没有父子关系）")
    @PostMapping("/getAllMenusNullFZ")
    public ResBean getAllMenus(Long currentPage, Long pageSize){
        try {
            Page<Menu> menuPage=new Page<> (currentPage,pageSize);
            QueryWrapper<Menu> queryWrapper=new QueryWrapper<> ();
            Page<Menu> page = menuService.page (menuPage, queryWrapper);
            return ResBean.success (page);
        } catch (Exception e) {
            return ResBean.fail ("获取信息失败");
        }
    }


    /**
     * @Description: 获取所有的菜单有父子关系且详细
     * @Title: getAllMenus
     * @param
     * @return: java.util.List<com.zzk.dockLock.pojo.Menu>
     * @author: zzk
     * @Date: 2022/1/6 19:04
     */
    @ApiOperation ("获取所有的菜单有父子关系且详细")
    @GetMapping("/getAllMenusDetail")
    public ResBean getAllMenus(){
        try {
            return ResBean.success (menuService.getAllMenusDetail());
        } catch (Exception e) {
            return ResBean.fail ("获取信息失败");
        }
    }

    /**
     * @Description: 修改菜单的信息
     * @Title: updateAllStationByStationId
     * @param menuParams
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 18:55
     */
    @ApiOperation ("修改菜单的信息")
    @PostMapping("/updateMenu")
    public ResBean updateAllMenuByMenuId(@RequestBody MenuParams menuParams){
        Menu menu=new Menu ();
        BeanUtils.copyProperties (menuParams,menu);
        if (menuService.updateById (menu)){
            return ResBean.success ("更新成功");
        }
        return ResBean.fail ("更新失败");
    }

    /**
     * @Description: 删除菜单
     * @Title: deleteMenu
     * @param id
     * @return: com.zzk.dockLock.vo.ResBean
     * @author: zzk
     * @Date: 2022/1/6 18:57
     */
    @ApiOperation ("删除菜单")
    @PostMapping("/deleteMenu")
    public ResBean deleteMenu(Integer id){
        try {
            if (menuService.removeById (id)){
                return ResBean.success ("删除成功");
            }
            return ResBean.fail ("删除失败，该站还有其他的柜门");
        } catch (Exception e) {
            e.printStackTrace ();
            return ResBean.fail ("删除失败，该站还有其他的站");
        }
    }


}