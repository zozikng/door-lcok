package com.zzk.dockLock.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 曲线图视图对象类
 * @program: door-lock
 * @author: zzk
 * @created: 2022/03/03 15:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineVo  implements Serializable {

    @ApiModelProperty(value = "横轴数据")
    private List<String> xdata;

    @ApiModelProperty(value = "纵轴数据")
    private List<Long> seriess;

}