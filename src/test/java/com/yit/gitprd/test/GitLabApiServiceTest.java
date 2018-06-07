package com.yit.gitprd.test;

import com.alibaba.fastjson.JSON;
import com.yit.gitprd.exception.GitlabServiceException;
import com.yit.gitprd.pojo.gitlab.Branch;
import com.yit.gitprd.pojo.gitlab.Commit;
import com.yit.gitprd.pojo.gitlab.CommitAction;
import com.yit.gitprd.pojo.gitlab.PushCommit;
import com.yit.gitprd.service.GitLabApiService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GitLabApiServiceTest {

    @Autowired
    private GitLabApiService gitLabApiService;

    @Test
    public void a_createBranch() throws GitlabServiceException {
        String branchName = "on_sale_2";
        String ref = "master";
        Branch branch = gitLabApiService.createBranch(branchName, ref);
        System.out.println("test_start");
        System.out.println(JSON.toJSON(branch).toString());
        System.out.println("test_end");
    }

    @Test
    public void b_branchList() throws GitlabServiceException {
        List<Branch> branches = gitLabApiService.branchList();
        System.out.println("test_start");
        branches.forEach(branch -> System.out.println(JSON.toJSON(branch).toString()));
        System.out.println("test_end");
    }
    @Test
    public void c_getBranch() throws GitlabServiceException {
        String branchName = "on_sale_2";
        Branch branch = gitLabApiService.getBranch(branchName);
        System.out.println("test_start");
        System.out.println(JSON.toJSON(branch).toString());
        System.out.println("test_end");
    }

    @Test
    public void d_commit() throws GitlabServiceException {
        String branchName = "on_sale_2";
        System.out.println("test_start");
        PushCommit pushCommit = new PushCommit();
        pushCommit.setBranchName(branchName);
        pushCommit.setCommitMessage("api push");
        CommitAction action = new CommitAction();
        action.setAction(CommitAction.Action.create);
        action.setFilePath("/");
        action.setContent("test context");
        action.setEncoding(CommitAction.Encoding.text);
        pushCommit.addAction(action);
        Commit commit = gitLabApiService.commit(pushCommit);
        System.out.println(JSON.toJSON(commit).toString());
        System.out.println("test_end");
    }
    @Test
    public void e_pull() throws GitlabServiceException {
        String branchName = "on_sale_2";
        System.out.println("test_start");
        Commit commit = gitLabApiService.pull(branchName);
        System.out.println(JSON.toJSON(commit).toString());
        System.out.println("test_end");
    }

    @Test
    public void f_addTag() throws GitlabServiceException {
        String ref = "on_sale_2";
        String tagName = "base_on_" + ref + "_" + System.currentTimeMillis();
        System.out.println("test_start");
        gitLabApiService.addTag(tagName, ref);
        System.out.println("打标签成功");
        System.out.println("test_end");
    }
    @Test
    public void g_deleteRemoteBranch() throws GitlabServiceException {
        String branchName = "on_sale_2";
        System.out.println("test_start");
        gitLabApiService.deleteRemoteBranch(branchName);
        System.out.println("删除成功");
        System.out.println("test_end");
    }


}
