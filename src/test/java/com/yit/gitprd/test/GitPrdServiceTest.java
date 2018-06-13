package com.yit.gitprd.test;

import com.yit.gitprd.cons.BranchListType;
import com.yit.gitprd.pojo.Branch;
import com.yit.gitprd.pojo.GitStatus;
import com.yit.gitprd.service.GitPrdService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * @author: clive
 * @date: 2018/06/11
 * @since: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GitPrdServiceTest {

    @Autowired
    private GitPrdService gitPrdService;

    private static final String TEST_BRANCH_NAME = "on_sale";

    @Test
    public void cloneBranch() throws GitAPIException {
        gitPrdService.cloneBranch("gift_3");
    }
    @Test
    public void branches() throws GitAPIException {
        List<Branch> branches = gitPrdService.branches(BranchListType.ALL);
        branches.forEach(b-> {
            System.out.println(b.getType() + ", " + b.getName());
        });
    }
    @Test
    public void createBranch() throws GitAPIException, IOException {
        gitPrdService.createBranch("gift_40", "gift");
    }
    @Test
    public void status() throws GitAPIException {
        List<GitStatus> allBranchStatus = gitPrdService.getAllBranchStatus();
        allBranchStatus.forEach(b-> System.out.printf("name: %s, hasCommit: %s \n", b.getBranchName(),
                b.getHasUncommittedChanges().toString()));
    }
    @Test
    public void commit() throws GitAPIException {
        gitPrdService.commitModify("gift", "prd test by clive");
    }
    @Test
    public void pull() throws GitAPIException {
        gitPrdService.pull("gift_40");
    }
    @Test
    public void delete() throws GitAPIException, IOException {
        gitPrdService.delete("gift_3");
    }
    @Test
    public void openInFinder() throws GitAPIException {
        gitPrdService.openInFinder("gift_40");
    }
    @Test
    public void getOnlineUrl() throws GitAPIException {
        String onlineUrl = gitPrdService.getOnlineUrl("gift_40");
        System.out.println(onlineUrl);
    }
}
