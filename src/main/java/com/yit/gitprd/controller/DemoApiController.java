package com.yit.gitprd.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;

import com.yit.gitprd.cons.BranchListType;
import com.yit.gitprd.pojo.ApiResponse;
import com.yit.gitprd.pojo.Branch;
import com.yit.gitprd.service.GitPrdService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: clive
 * @date: 2018/06/05
 * @since: 1.0
 */

@RestController
@RequestMapping("api")
public class DemoApiController {

    @Autowired
    GitPrdService gitPrdService;

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

    @PostMapping("getBranchList")
    @ResponseBody
    public ApiResponse getBranchList(@RequestParam(name = "type") BranchListType type) throws GitAPIException {
        List<Branch> branches = gitPrdService.branches(type);
         return new ApiResponse(true, "success", JSON.toJSONString(branches));
    }

    @RequestMapping("delBranch")
    @ResponseBody
    public ApiResponse delBranch(String name) {
        return new ApiResponse(true, "success", name);
    }
}
