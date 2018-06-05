package com.yit.gitprd.controller;

import org.springframework.web.bind.annotation.PostMapping;
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

}
