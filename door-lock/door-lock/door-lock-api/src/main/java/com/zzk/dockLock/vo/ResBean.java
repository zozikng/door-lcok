package com.zzk.dockLock.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 统一返回结果的类
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/07 19:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("统一返回结果类")
public  class ResBean {

    @ApiModelProperty(value = "状态",example = "true或false")
   private boolean status;

    @ApiModelProperty(value = "状态码",example = "200（成功）")
   private int code;

    @ApiModelProperty(value = "状态信息",example = "success")
   private String msg;

    @ApiModelProperty(value = "数据")
   private Object data;


    //成功访问数据
    public static ResBean success(Object data){
        return new ResBean (true,200,"success",data);
    }
    //成功访问数据
    public static ResBean success(String msg){
        return new ResBean (true,200,msg,"");
    }
    //成功访问数据
    public static ResBean success(String msg,Object data){
        return new ResBean (true,200,msg,data);
    }
    //不成功访问数据
    public  static ResBean fail(int code, String msg){
        return new ResBean (false, code, msg,null);
    }
    //不成功访问数据
    public  static ResBean fail( String msg){
        return new ResBean (false, 404, msg,null);
    }


}