package com.yit.gitprd.service;

import com.yit.gitprd.cons.BranchListType;
import com.yit.gitprd.pojo.git.Branch;
import com.yit.gitprd.pojo.git.GitStatus;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.List;

/**
 * gitPRD核心业务接口
 *
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
public interface GitPrdService {


    /**
     * prd列表 (本地, 远程, 所有)
     *
     * @param type 列表类型
     * @return
     */
    List<Branch> branches(BranchListType type) throws GitAPIException;


    /**
     * 新建prd (基于某个分支创建 本地&远程)
     *
     * @param branchName 新分支名称
     * @param refBranchName 依赖的分支名称
     */
    void createBranch(String branchName, String refBranchName)throws GitAPIException;

    /**
     * 提交改动
     * (如果有远程有更新则提示先撤销本地改动)
     * @param branchName 分支名
     */
    void commitModify(String branchName)throws GitAPIException;

    /**
     * 撤销本地改动
     *
     * @param branchName 分支名
     */
    void resetModify(String branchName)throws GitAPIException;

    /**
     * 获取所有分支更新状态
     *
     * @return
     */
    List<GitStatus> getAllBranchStatus()throws GitAPIException;

    /**
     * 拉取更新 (先撤销改动再拉取,防止冲突)
     *
     * @param branchName 分支名
     */
    void pull(String branchName)throws GitAPIException;

    /**
     * 删除prd (本地和远程)
     *
     * @param branchName 分支名
     */
    void delete(String branchName)throws GitAPIException;

    /**
     * 在finder中打开
     *
     * @param branchName 分支名
     */
    void openInFinder(String branchName)throws GitAPIException;

    /**
     * 获取在线prd的地址
     *
     * @param branchName 分支名
     * @return
     */
    String getOnlineUrl(String branchName)throws GitAPIException;

}
