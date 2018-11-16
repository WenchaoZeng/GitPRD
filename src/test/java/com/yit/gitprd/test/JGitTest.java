package com.yit.gitprd.test;

import com.yit.gitprd.utils.SystemUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.transport.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author: clive
 * @date: 2018/06/07
 * @since: 1.0
 */
public class JGitTest {

    private static final String GIT_URI = "";
    private static final String PRD_ROOT_NAME = "gitPRD";
    private static final String USER_NAME = "";
    private static final String PASSWORD = "";

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
        masterPath = rootPath + "/master";
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
                .setName("master")
                .call();
    }

    @Test
    public void createRemoteBranch() throws GitAPIException {

        Iterable<PushResult> iterable = git.push()
                .setCredentialsProvider(upcp)
                .call();
        PushResult pushResult = iterable.iterator().next();
        RemoteRefUpdate.Status status
                = pushResult.getRemoteUpdate("refs/heads/jGit").getStatus();
        System.out.println(status.toString());
    }

    @Test
    public void getStatus() throws GitAPIException {
        Status status = git.status().call();
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
        git.add().addFilepattern(".")
                //.setUpdate(true)
                .call();
    }

    @Test
    public void commit() throws GitAPIException {
        String commitMsg = "test author";
        git.commit()
                .setAuthor(SystemUtils.getUserName(), SystemUtils.getUserName() + "@yit.com")
                .setMessage(SystemUtils.getUserName() + " :" + commitMsg).call();
    }

    @Test
    public void push() throws GitAPIException {

        Iterable<PushResult> iterable = git.push()
                .setCredentialsProvider(upcp)
                .call();
        //iterable.forEach(pushResult -> System.out.println(pushResult.getRemoteUpdate("refs/heads/jGit")));
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
                = pushResult.getRemoteUpdate("refs/tags/" + tagName).getStatus();
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

    @Test
    public void fetch() throws GitAPIException {
        git.fetch()
                .setRemoveDeletedRefs(true)
                .setCredentialsProvider(upcp)
//                .setDryRun(true)
                .call();
    }

    @Test
    public void log() throws GitAPIException {
        RevFilter revFilter;
        Iterable<RevCommit> revCommits = git.log()
                .setMaxCount(1).call();
        System.out.println(revCommits);
        Iterator<RevCommit> iterator = revCommits.iterator();
        if (iterator.hasNext()) {
            RevCommit rev = iterator.next();
            System.out.println("Commit: " + rev + ", name: " + rev.getName() + ", id: " + rev.getId().getName());
        }
    }

}
