package com.yit.gitprd.service;

import com.yit.gitprd.pojo.Branch;
import com.yit.gitprd.pojo.GitStatus;
import com.yit.gitprd.utils.SystemUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 操作git by jGit
 *
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
@Service
public class GitApiService {

    private static final Logger logger = LoggerFactory.getLogger(GitApiService.class);

    @Autowired
    private GitHelper gitHelper;

    /**
     * 克隆远程分支到本地
     *
     * @param localPath
     * @throws GitAPIException
     */
    public void cloneBranch(String localPath) throws GitAPIException {
        try (Git git = Git.cloneRepository()
                .setURI(gitHelper.getUri())
                .setDirectory(new File(localPath))
                .setCredentialsProvider(gitHelper.getCredentialsProvider())
                .call()) {
            logger.info("cloneBranch success: localPath={}, repository={}", localPath, git.getRepository().getDirectory());
        }
    }

    /**
     * 获取远程分支
     *
     * @return
     * @throws GitAPIException
     */
    public List<Branch> remoteBranches() throws GitAPIException {
        return branches(ListBranchCommand.ListMode.REMOTE);
    }

    /**
     * 创建本地分支
     *
     * @param branchName
     * @param refBranchName
     * @throws GitAPIException
     */
    public void createBranch(String branchName, String refBranchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.checkout()
                    .setCreateBranch(true)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
                    .setName(branchName)
                    .setStartPoint("origin/" + refBranchName)
                    .call();
            git.push()
                    .setCredentialsProvider(gitHelper.getCredentialsProvider())
                    .setRemote("origin")
                    .call();
        }
    }

    /**
     * 切换分支
     *
     * @param branchName
     * @throws GitAPIException
     */
    public void checkout(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.checkout()
                    .setCreateBranch(true)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                    .setName(branchName)
                    .setStartPoint("origin/" + branchName)
                    .call();
        }
    }

    /**
     * 分支状态
     *
     * @param branchName
     * @return
     * @throws GitAPIException
     */
    public GitStatus status(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            Status status = git.status().call();
            GitStatus gitStatus = new GitStatus();
            int missing = status.getMissing().size(); //本地删除
            int modified = status.getModified().size();//本地修改
            int untracked = status.getUntracked().size();//本地新增文件
            int untrackedFolders = status.getUntrackedFolders().size();//本地新增文件夹
            int uncommittedChanges = status.getUncommittedChanges().size();//未提交修改
            int sum = missing + modified + untracked + untrackedFolders + uncommittedChanges;
            gitStatus.setHasUncommittedChanges(status.hasUncommittedChanges() || sum > 0);
            gitStatus.setBranchName(branchName);
            gitStatus.setStatus(status);
            return gitStatus;
        }

    }

    /**
     * 添加到本地缓存区
     *
     * @param branchName
     * @throws GitAPIException
     */
    public void add(String branchName) throws GitAPIException {
        //获取分支状态, 判断是否添加
        //有新增文件 => update=false [默认]
        //有删除文件 => update=true
        this.add(branchName, false);
        GitStatus status = this.status(branchName);
        int missingSize = status.getStatus().getMissing().size();
        if (missingSize > 0) {
            this.add(branchName, true);
        }
    }

    /**
     * 拉取更新
     *
     * @param branchName 分支名
     * @throws GitAPIException
     */
    public void pull(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.pull().setRebase(true)
                    .setCredentialsProvider(gitHelper.getCredentialsProvider())
                    .call();
        }
    }

    /**
     * 提交到本地
     *
     * @param branchName
     * @param msg
     * @throws GitAPIException
     */
    public void commit(String branchName, String msg) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.commit().setAll(true)
                    .setMessage(SystemUtils.getUserName() + " :" + msg).call();
        }
    }

    /**
     * 推送到远程分支
     *
     * @param branchName
     * @throws GitAPIException
     */
    public boolean push(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            Iterable<PushResult> results = git.push()
                    .setCredentialsProvider(gitHelper.getCredentialsProvider())
                    .call();
            Iterator<PushResult> iterator = results.iterator();
            if (iterator.hasNext()) {
                PushResult push = iterator.next();
                RemoteRefUpdate remoteUpdate = push.getRemoteUpdate("refs/heads/" + branchName);
                logger.info("push==> branchName={},remoteUpdate.status={}", branchName, remoteUpdate.getStatus());
                return RemoteRefUpdate.Status.OK.equals(remoteUpdate.getStatus() )
                        || RemoteRefUpdate.Status.UP_TO_DATE.equals(remoteUpdate.getStatus());
            }
        }
        return false;
    }

    /**
     * git仓库是否存在
     *
     * @param branchPath
     * @return
     */
    public boolean respExist(String branchPath) {
        File file = new File(branchPath + "/.git");
        return file.exists();
    }

    /**
     * 重置
     *
     * @param branchName
     * @throws GitAPIException
     */
    public void reset(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.reset()
                    .setMode(ResetCommand.ResetType.HARD)
                    .call();
        }
    }

    /**
     * 清除本地添加
     *
     * @param branchName
     * @throws GitAPIException
     */
    public void clean(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.clean()
                    .setCleanDirectories(true)
                    .setForce(true)
                    .call();
        }
    }

    /**
     * 添加标签
     *
     * @param branchName
     * @throws GitAPIException
     */
    public void addTag(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            String tagName = branchName +"_"+ System.currentTimeMillis();
            String msg = String.format("deleted by %s, based on %s", SystemUtils.getUserName(), branchName);
            //create local tag
            git.tag()
                    .setName(tagName)
                    .setMessage(msg)
                    .call();
            //push to remote
            git.push().setPushTags()
                    .setCredentialsProvider(gitHelper.getCredentialsProvider())
                    .call();
        }
    }

    /**
     * 删除远程分支
     *
     * @param branchName
     * @throws GitAPIException
     */
    public void deleteRemoteBranch(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            RefSpec refSpec = new RefSpec()
                    .setSource(null)
                    .setDestination("refs/heads/" + branchName);
            git.push()
                    .setCredentialsProvider(gitHelper.getCredentialsProvider())
                    .setRefSpecs(refSpec).setRemote("origin").call();
        }
    }


    //-- private methods

    private List<Branch> branches(ListBranchCommand.ListMode listMode) throws GitAPIException {
        List<Branch> list = new ArrayList<>();
        try (Git git = gitRemote()) {
            List<Ref> refs = git.branchList()
                    .setListMode(listMode)
                    .call();
            if (refs == null) return list;
            String localPrefix = "refs/heads/";
            String remotePrefix = "refs/remotes/origin/";
            for (Ref ref : refs) {
                Branch branch = new Branch();
                String refName = ref.getName();

                if (refName.startsWith(localPrefix)) {
                    branch.setType(Branch.Type.LOCAL);
                    branch.setName(refName.replace(localPrefix, ""));
                } else if (refName.startsWith(remotePrefix)) {
                    branch.setType(Branch.Type.REMOTE);
                    branch.setName(refName.replace(remotePrefix, ""));
                }

                list.add(branch);
            }
        }

        return list;
    }


    /**
     * 获取更新
     *
     * @param dryRun 干跑 -> 白跑一趟
     *               Show what would be done, without making any changes.
     * @return
     * @throws GitAPIException
     */
    public FetchResult fetch(String branchPath, Boolean dryRun, Boolean removeDeletedRefs) throws GitAPIException {
        try (Git git = git(branchPath)) {
            return git.fetch()
                    .setRemoveDeletedRefs(removeDeletedRefs) //删除远程已经不存在的分支缓存
                    .setDryRun(dryRun)
                    .setCredentialsProvider(gitHelper.getCredentialsProvider())
                    .call();
        }

    }

    //master分支
    private Git gitMaster() {
        return git(gitHelper.getMasterPath());
    }
    //remote分支
    private Git gitRemote() {
        return git(gitHelper.getRemotePath());
    }

    //本地分支
    private Git gitBranch(String branchName) {
        return git(gitHelper.getBranchesPath() + "/" + branchName);
    }

    private Git git(String gitPath) {
        try {
            return Git.open(new File(gitPath));
        } catch (IOException e) {
            logger.error("get git error", e);
            throw new IllegalArgumentException("分支不存在");
        }
    }

    //添加
    private void add(String branchName, boolean update) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.add().addFilepattern(".").setUpdate(update).call();
        }
    }
}
