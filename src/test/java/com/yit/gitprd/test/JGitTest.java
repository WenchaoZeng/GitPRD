package com.yit.gitprd.test;

import com.yit.gitprd.utils.SystemUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author: clive
 * @date: 2018/06/07
 * @since: 1.0
 */
public class JGitTest {

    private static final String GIT_URI = "http://gitlab.yit.com/javayuanjian/prd.git";
    private static final String PRD_ROOT_NAME = "gitPRD";
    private static final String USER_NAME = "javayuanjian";
    private static final String PASSWORD = "cliveyuan";

    private static final UsernamePasswordCredentialsProvider upcp = new UsernamePasswordCredentialsProvider(USER_NAME, PASSWORD);

    private static Git git;
    private static String rootPath;
    private static String masterPath;
    private static File repoFile;

    @BeforeClass
    public static void before() throws IOException {
        String userHome = SystemUtils.getUserHome();
        rootPath = userHome + "/" + PRD_ROOT_NAME;
        repoFile = new File(rootPath);
        masterPath = rootPath + "/master" ;
        if (!repoFile.exists()) {
            repoFile.mkdirs();
        }
        git = Git.open(new File(masterPath));
    }

    @Test
    public void cloneTest() throws GitAPIException {
        String localFolderName = "master";
       Git.cloneRepository()
                .setURI(GIT_URI)
                .setDirectory(new File(rootPath + "/" + localFolderName))
                .setCredentialsProvider(upcp)
                .call();
    }

    @Test
    public void branchList() throws GitAPIException {
        List<Ref> refs = git.branchList()
                .setListMode(ListBranchCommand.ListMode.ALL)
                .call();
        for (Ref ref : refs) {
            System.out.println("Branch: " + ref + " " + ref.getName() + " "
                    + ref.getObjectId().getName());
        }

    }

    @Test
    public void createLocalBranch() throws GitAPIException {
        git.checkout()
                .setCreateBranch(true)
                .setName("onSale")
                .call();
    }
    @Test
    public void switchExistBranch() throws GitAPIException {
        git.checkout()
                .setName("onSale")
                .call();
    }
    @Test
    public void createRemoteBranch() throws GitAPIException {

        Iterable<PushResult> iterable = git.push()
                .setCredentialsProvider(upcp)
                .call();
        PushResult pushResult = iterable.iterator().next();
        RemoteRefUpdate.Status status
                = pushResult.getRemoteUpdate( "refs/heads/jGit" ).getStatus();
        System.out.println(status.toString());
    }

    @Test
    public void getStatus() throws GitAPIException {
        Status status = git.status().call();
        Set<String> untracked = status.getUntracked();//未添加
        Set<String> changed = status.getChanged(); //变动
        Set<String> modified = status.getModified(); //修改->与远程有不同的
        Set<String> conflicting = status.getConflicting();//冲突
        System.out.println("untracked: " + untracked);
        System.out.println("changed: " + changed);
        System.out.println("modified: " + modified);
        System.out.println("conflicting: " + conflicting);
    }

    @Test
    public void add()throws GitAPIException {
        git.add().addFilepattern(".").call();
    }
    @Test
    public void commit()throws GitAPIException {
        String commitMsg = "Using jGit";
        git.commit().setMessage(SystemUtils.getUserName() + " :" +commitMsg).call();
    }
    @Test
    public void push() throws GitAPIException {

        Iterable<PushResult> iterable = git.push()
                .setCredentialsProvider(upcp)
                .call();
        iterable.forEach(pushResult -> System.out.println(pushResult.getRemoteUpdate("refs/heads/jGit")));
    }

    @Test
    public void createTag() throws GitAPIException {
        String tagName = "v1.0.2-jGit";
        String msg = "tag by " + SystemUtils.getUserName();
        Ref tag = git.tag().setName(tagName).setMessage(msg).call();

        Iterable<PushResult> iterable = git.push()
                .add(tag)
                .setCredentialsProvider(upcp)
                .call();
        PushResult pushResult = iterable.iterator().next();
        RemoteRefUpdate.Status status
                = pushResult.getRemoteUpdate( "refs/tags/" + tagName ).getStatus();
        System.out.println(status.toString());
    }

    @Test
    public void deleteLocalBranch() throws GitAPIException {
        String branchName = "onSale";
        List<String> result = git.branchDelete().setBranchNames(branchName).call();
        System.out.println(result);
    }
    @Test
    public void deleteRemoteBranch() throws GitAPIException {
        String branchName = "onSale";
        List<String> result = git.branchDelete().setBranchNames(branchName).call();
        System.out.println(result);
        git.push().call();
    }
    @Test
    public void pull() throws GitAPIException {
        PullResult pullResult = git.pull()
                .setCredentialsProvider(upcp)
                .setRebase(true)
                .call();
        System.out.println(pullResult);
    }
    @Test
    public void reset() throws GitAPIException {
        Ref ref = git.reset()
                .setMode(ResetCommand.ResetType.HARD)
                .call();
        System.out.println(ref);

    }

}
