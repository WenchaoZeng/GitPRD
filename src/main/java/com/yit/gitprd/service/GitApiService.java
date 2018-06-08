package com.yit.gitprd.service;

import com.yit.gitprd.exception.GitRuntimeException;
import com.yit.gitprd.pojo.git.Branch;
import com.yit.gitprd.pojo.git.GitStatus;
import com.yit.gitprd.utils.SystemUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
     * 本地分支
     *
     * @return
     * @throws GitAPIException
     */
    public List<Branch> localBranches() throws GitAPIException {
        return branches(null);
    }

    /**
     * 所有分支
     *
     * @return
     * @throws GitAPIException
     */
    public List<Branch> allBranches() throws GitAPIException {
        return branches(ListBranchCommand.ListMode.ALL);
    }

    /**
     * 创建本地分支
     *
     * @param branchName
     * @throws GitAPIException
     */
    public void createLocalBranch(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.checkout()
                    .setCreateBranch(true)
                    .setName(branchName)
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
            gitStatus.setExistUnCommit(status.hasUncommittedChanges());
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
    public void push(String branchName) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.push().setCredentialsProvider(gitHelper.getCredentialsProvider()).call();
        }
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


    //-- private methods

    private List<Branch> branches(ListBranchCommand.ListMode listMode) throws GitAPIException {
        List<Branch> list = new ArrayList<>();
        try (Git git = gitMaster()) {
            List<Ref> refs = git.branchList()
                    .setListMode(listMode)
                    .call();
            for (Ref ref : refs) {
                Branch branch = new Branch();
                branch.setName(ref.getName());
                branch.setRef(ref);
                list.add(branch);
            }
        }

        return list;
    }

    //master分支
    private Git gitMaster() {
        return git(gitHelper.getMasterPath());
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
            throw new GitRuntimeException("打开仓库异常");
        }
    }

    //添加
    private void add(String branchName, boolean update) throws GitAPIException {
        try (Git git = gitBranch(branchName)) {
            git.add().setUpdate(update).call();
        }
    }
}
