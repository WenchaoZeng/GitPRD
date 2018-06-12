package com.yit.gitprd.test;

import com.yit.gitprd.pojo.git.Branch;
import com.yit.gitprd.pojo.git.GitStatus;
import com.yit.gitprd.service.GitApiService;
import com.yit.gitprd.service.GitHelper;
import org.eclipse.jgit.api.Status;
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

    @Test
    public void remoteBranches() throws GitAPIException {
        List<Branch> list = gitApiService.remoteBranches();
        list.forEach(b-> System.out.println(b.getName()));
    }
    @Test
    public void deleteBranch() throws GitAPIException {
        gitApiService.deleteRemoteBranch("on_sale_1");
    }
    @Test
    public void addTag() throws GitAPIException {
        gitApiService.addTag("onSale");
    }
    @Test
    public void status() throws GitAPIException {
        GitStatus gitStatus = gitApiService.status("onSale");
        System.out.println(gitStatus.getHasUncommittedChanges());
        Status status = gitStatus.getStatus();
        System.out.println("Added: " + status.getAdded());
        System.out.println("Changed: " + status.getChanged());//变动
        System.out.println("Conflicting: " + status.getConflicting());//冲突
        System.out.println("ConflictingStageState: " + status.getConflictingStageState());
        System.out.println("IgnoredNotInIndex: " + status.getIgnoredNotInIndex());
        System.out.println("Missing: " + status.getMissing());
        System.out.println("Modified: " + status.getModified()); //修改->与远程有不同的
        System.out.println("Removed: " + status.getRemoved());
        System.out.println("Untracked: " + status.getUntracked());//未添加
        System.out.println("UntrackedFolders: " + status.getUntrackedFolders());
        System.out.println("hasUncommittedChanges: " + status.hasUncommittedChanges());
    }
    @Test
    public void add() throws GitAPIException {
        gitApiService.add("onSale");
    }
    @Test
    public void commit() throws GitAPIException {
        gitApiService.commit("onSale", "test");
    }
    @Test
    public void push() throws GitAPIException {
        boolean pushResult = gitApiService.push("onSale");
        System.out.println("pushResult: "+pushResult);
    }
    @Test
    public void reset() throws GitAPIException {
        gitApiService.reset("onSale");
    }
}
