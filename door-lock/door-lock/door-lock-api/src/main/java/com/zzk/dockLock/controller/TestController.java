package com.zzk.dockLock.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 测试接口
 * @program: door-lock
 * @author: zzk
 * @created: 2021/11/11 12:42
 */
@RestController
@Api(tags = "TestController 测试接口")
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "你好啊";
    }

    @GetMapping("/test/basic/hello")
    public String testAuthcation1(){
        return "/test/basic/hello";
    }


    @GetMapping("/test/advanced/hello")
    public String testAuthcation2(){
        return "/test/advanced/hello";
    }
    @GetMapping("/test/advanced/add")
    public String testAuthcation3(){
        return "test/advance/hello";
    }

}