package com.zzk.dockLock.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzk
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)//开启链式编程
@TableName("z_sys_user")
@ApiModel(value="User对象", description="")
public class SysUser implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "姓名（昵称）")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "是否启用")
    @Getter(AccessLevel.NONE)//不需要getter方法
    private Boolean enabled;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "角色")
    @TableField(exist = false)
    private List<Role> roles;

    /*
    * 1.用户登录时，不仅要查询用户基本信息，还需要查询拥有的权限信息
    * 2.查询的所有权限需要封装成UserDetails指定的的权限集合
    * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //map就是一个加工方法，将stream中的每一个元素按照一定规则加工
        //collect就是能将stream转换为集合
        List<GrantedAuthority> authorities =roles
                .stream ()
                //角色名字转为SimpleGrantAuthority
                .map (role -> new SimpleGrantedAuthority (role.getName ()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
