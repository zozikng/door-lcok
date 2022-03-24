package com.zzk.dockLock.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * (Station)站实体类
 *
 * @author makejava
 * @since 2021-12-18 15:43:40
 */
@Data
@AllArgsConstructor//全参构造
@NoArgsConstructor//无参构造
@Accessors(chain = true)
@TableName("z_station")
@ApiModel(value="Station对象", description="")
public class Station implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 站id
     */
    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 站名字
     */
    @ApiModelProperty(value = "站名称")
    private String stationName;
    /**
     * 站ip
     */
    @ApiModelProperty(value = "站ip")
    private String stationIp;
    /**
     * 站地址
     */
    @ApiModelProperty(value = "站地址")
    private String stationAddress;
    /**
     * 状态（是否在线）
     */
    @ApiModelProperty(value = "站状态")
    private Integer stationStatus;

    /**
     *
     */
    @TableField(exist = false)//该属性不在数据库表中
    private List<Cabinet> cabinets;
}
