package com.yit.gitprd.test;

import com.yit.gitprd.pojo.git.Branch;
import com.yit.gitprd.service.GitApiService;
import com.yit.gitprd.service.GitHelper;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GitApiServiceTest {

    @Autowired
    private GitApiService gitApiService;
    @Autowired
    private GitHelper gitHelper;

    @Test
    public void localBranches() throws GitAPIException {
        List<Branch> branches = gitApiService.localBranches();
        branches.forEach(b -> System.out.println(b.getName()));
    }
}
