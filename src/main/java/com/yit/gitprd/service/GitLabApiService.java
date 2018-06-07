package com.yit.gitprd.service;

import com.yit.gitprd.exception.GitlabServiceException;
import com.yit.gitprd.pojo.gitlab.Branch;
import com.yit.gitprd.pojo.gitlab.Commit;
import com.yit.gitprd.pojo.gitlab.PushCommit;

import java.util.List;

/**
 * gitlab 接口
 *
 * @author: clive
 * @date: 2018/06/05
 * @since: 1.0
 */
public interface GitLabApiService {

    //分支列表
    List<Branch> branchList() throws GitlabServiceException;

    Branch getBranch(String branchName) throws GitlabServiceException;

    //创建分支
    Branch createBranch(String branchName, String ref) throws GitlabServiceException;

    //删除分支
    void deleteRemoteBranch(String branchName) throws GitlabServiceException;

    //打tag
    void addTag(String tagName, String ref, String msg) throws GitlabServiceException;

    //提交更改
    void commit(PushCommit pushCommit) throws GitlabServiceException;

    //拉取更新
    Commit pull(String branch);


}
