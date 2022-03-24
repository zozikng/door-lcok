package com.zzk.dockLock.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 站前端交互类
 * @program: door-lock
 * @author: zzk
 * @created: 2022/01/06 10:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站id
     */
    @ApiModelProperty(value = "id")
    private Integer id;
    /**
     * 站名字
     */
    @ApiModelProperty(value = "站名字")
    private String stationName;
    /**
     * 站ip
     */
    @ApiModelProperty(value = "站Ip")
    private String stationIp;
    /**
     * 站地址
     */
    @ApiModelProperty(value = "站地址")
    private String stationAddress;
}