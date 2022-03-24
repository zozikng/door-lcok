package com.zzk.dockLock.vo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 站的前端交互类
 * @program: door-lock
 * @author: zzk
 * @created: 2022/01/06 10:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "StationParams对象",description = "站参数对象")
public class StationParams implements Serializable {



    private static final long serialVersionUID = 1L;
    /**
     * 站id
     */
    @ApiModelProperty(value = "id",required = true)
    private Integer id;
    /**
     * 站名字
     */
    @ApiModelProperty(value = "站名称",required = true)
    private String stationName;
    /**
     * 站ip
     */
    @ApiModelProperty(value = "站ip",required = true)
    private String stationIp;
    /**
     * 站地址
     */
    @ApiModelProperty(value = "站地址",required = true)
    private String stationAddress;

    /**
     * 状态（是否在线）
     */
    @ApiModelProperty(value = "站状态")
    private Integer stationStatus;


}