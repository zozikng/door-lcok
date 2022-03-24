package com.zzk.dockLock.handler;

import com.zzk.dockLock.vo.ResBean;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 全局异常处理器
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/16 19:36
 */
@RestControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json数据
    public ResBean doException(Exception e){

        //打印异常
        e.printStackTrace ();
        //返回异常信息
        return ResBean.fail (-999,"系统异常");

    }


}