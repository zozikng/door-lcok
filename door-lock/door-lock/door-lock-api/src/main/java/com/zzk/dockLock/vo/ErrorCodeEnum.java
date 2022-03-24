package com.zzk.dockLock.vo;

/**
 * @Description: 错误枚举类
 * @author: zzk
 * @Date: 2021/11/10 15:23
 */

public enum ErrorCodeEnum {

    PARAMS_ERROR(10001,"参数有误"),
    ACCOUNT_PWD_NOT_EXIST(10002,"用户名或密码不正确"),
    TOKEN_ERROR(10003,"token不合法"),
    ACCOUNT_EXIST(10004,"账号已存在"),
    CODE_ERROR(10005,"验证码错误"),
    ACCOUNT_IS_NO_ENABLED(10006,"用户已被禁用，请联系管理员"),
    NO_PERMISSION(70001,"无访问权限"),
    SESSION_TIME_OUT(90001,"会话超时"),
    NO_LOGIN(90002,"尚未登录，请登录"),;

    private int code;
    private String msg;

    ErrorCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
