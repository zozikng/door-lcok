package com.zzk.dockLock.vo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 添加用户参数
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/21 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "SysUserParam对象",description = "添加用户参数对象")
public class SysUserParam {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "姓名（昵称）")
    private String nickname;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled=true;

    @ApiModelProperty(value = "性别")
    private String gender;
}