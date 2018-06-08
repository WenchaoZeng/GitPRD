package com.yit.gitprd.service;

import com.yit.gitprd.cons.BranchListType;
import com.yit.gitprd.pojo.git.Branch;
import com.yit.gitprd.pojo.git.GitStatus;

import java.util.List;

/**
 * 实现gitPRD核心功能
 *
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
public class GitPrdServiceImpl implements GitPrdService {

    @Override
    public List<Branch> branches(BranchListType type) {
        return null;
    }

    @Override
    public void createBranch(String branchName, String refBranchName) {

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
