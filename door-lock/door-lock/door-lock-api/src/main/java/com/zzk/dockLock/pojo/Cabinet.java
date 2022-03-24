package com.zzk.dockLock.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (ZCabinet)柜门实体类
 *
 * @author makejava
 * @since 2021-12-18 15:32:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("z_cabinet")
@ApiModel(value="Cabinet对象", description="")
public class Cabinet implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 柜id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer cabinetId;
    /**
     * 柜名称
     */
    @ApiModelProperty(value = "柜名")
    private String cabinetName;
    /**
     * 站id
     */
    @ApiModelProperty(value = "站id")
    private Integer stationId;

}
