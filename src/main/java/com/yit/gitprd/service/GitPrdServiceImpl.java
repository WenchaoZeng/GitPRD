package com.yit.gitprd.service;

import com.yit.gitprd.cons.BranchListType;
import com.yit.gitprd.pojo.git.Branch;
import com.yit.gitprd.pojo.git.GitStatus;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现gitPRD核心功能
 *
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
@Service
public class GitPrdServiceImpl implements GitPrdService {

    @Autowired
    private GitApiService gitApiService;
    @Autowired
    private GitHelper gitHelper;


    @Override
    public List<Branch> branches(BranchListType type) throws GitAPIException {
        if (BranchListType.LOCAL.equals(type)) return gitApiService.localBranches();
        else return gitApiService.allBranches();
    }

    @Override
    public void createBranch(String branchName, String refBranchName) throws GitAPIException {
        //将项目拉下来命名为${branchName}
        //切换到分支${refBranchName}
        //创建本地分支${branchName}
        //提交分支到远程

        String localBranchPath = gitHelper.getBranchesPath() + "/" + branchName;
        gitApiService.cloneBranch(localBranchPath);
//        gitApiService.switchBranch();
//        gitApiService.createLocalBranch();
    }

    @Override
    public void commitModify(String branchName) {

    }

    @Override
    public void resetModify(String branchName) {

    }

    @Override
    public List<GitStatus> getAllBranchStatus() {
        return null;
    }

    @Override
    public void pull(String branchName) {

    }

    @Override
    public void delete(String branchName) {

    }

    @Override
    public void openInFinder(String branchName) {

    }

    @Override
    public String getOnlineUrl(String branchName) {
        return null;
    }
}
