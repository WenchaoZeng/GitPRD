package com.yit.gitprd.test;

import com.alibaba.fastjson.JSON;
import com.yit.gitprd.exception.GitlabServiceException;
import com.yit.gitprd.pojo.gitlab.Branch;
import com.yit.gitprd.service.GitLabApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GitLabApiServiceTest {

    @Autowired
    private GitLabApiService gitLabApiService;

    @Test
    public void branchList() throws GitlabServiceException {
        List<Branch> branches = gitLabApiService.branchList();
        System.out.println("test_start");
        branches.forEach(branch -> System.out.println(JSON.toJSON(branch).toString()));
        System.out.println("test_end");
    }
}
