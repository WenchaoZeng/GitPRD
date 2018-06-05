package com.yit.gitprd.controller;

import com.yit.gitprd.pojo.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: clive
 * @date: 2018/06/05
 * @since: 1.0
 */

@RestController
@RequestMapping("api")
public class DemoApiController {

    @PostMapping("test")
    public String test() {
        return "hello world!";
    }

    @PostMapping("data")
    public ApiResponse data(@RequestBody String type) {
        if ("fail".equals(type))
        return ApiResponse.newFailInstance("请求失败");
        else return ApiResponse.newSuccessInstance("请求成功");
    }

}
