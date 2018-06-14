package com.yit.gitprd.service;

import com.yit.gitprd.cons.BranchListType;
import com.yit.gitprd.cons.GitPrdCons;
import com.yit.gitprd.pojo.Branch;
import com.yit.gitprd.pojo.GitStatus;
import com.yit.gitprd.utils.FileUtil;
import com.yit.gitprd.utils.StringUtil;
import com.yit.gitprd.utils.SystemUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.TrackingRefUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        Assert.notNull(type, "列表类型不能为空");
        switch (type) {
            case LOCAL: return this.onlyLocalBranches();
            case REMOTE: return this.onlyRemoteBranches();
            case ALL: {
                List<Branch> all = this.onlyLocalBranches();
                all.addAll(this.onlyRemoteBranches());
                return all;
            }
            default: return new ArrayList<>();
        }
    }

    private List<Branch> onlyLocalBranches() {
        List<Branch> list = new ArrayList<>();
        List<File> branchList = getLocalBranchedFiles();
        if (branchList == null) return list;
        for (File file : branchList) {
            Branch branch = new Branch();
            branch.setType(Branch.Type.LOCAL);
            branch.setName(file.getName());
            list.add(branch);
        }
        return list;
    }


    private List<Branch> onlyRemoteBranches() throws GitAPIException {
        List<Branch> list = new ArrayList<>();
        List<Branch> remotes = gitApiService.remoteBranches();
        if (remotes == null) return list;
        List<Branch> locals = this.onlyLocalBranches();
        for (Branch remote : remotes) {
            if (!branchContains(locals, remote)) list.add(remote);
        }
        return list;
    }

    private boolean branchContains(List<Branch> list, Branch branch) {
        return list.stream().anyMatch(b -> b.getName().equals(branch.getName()));
    }

    @Override
    public void createBranch(String branchName, String refBranchName) throws GitAPIException, IOException {
        Assert.isTrue(StringUtil.isNotBlank(branchName), GitPrdCons.BRANCH_NAME_NULL_MSG);
        Assert.isTrue(branchName.contains("/"), "名PRD称不能包含斜杠");
        Assert.isTrue(StringUtil.isNotBlank(refBranchName), "依赖PRD不能为空");
        //将项目拉下来命名为${branchName}
        //切换到分支${refBranchName} -> 如果依赖分支不是master
        //创建本地分支${branchName}
        //推送到远程

        String localBranchPath = gitHelper.getBranchesPath() + "/" + branchName;
        Assert.isTrue(!FileUtil.exist(localBranchPath), "该PRD名称已存在,请换个名称");
        gitApiService.cloneBranch(localBranchPath);
        try {
            gitApiService.createBranch(branchName, refBranchName);
        } catch (InvalidRefNameException e) {
            //回滚
            FileUtil.deleteDirectory(new File(gitHelper.getBranchesPath() + "/" + branchName));
            throw new IllegalArgumentException(e.getMessage());
        }
        //删除重新克隆 (因为jGit不支持 push -u origin branch_name)
        FileUtil.deleteDirectory(new File(gitHelper.getBranchesPath() + "/" + branchName));
        this.cloneBranch(branchName);


    }

    @Override
    public void commitModify(String branchName, String comment) throws GitAPIException {
        Assert.isTrue(StringUtil.isNotBlank(branchName), GitPrdCons.BRANCH_NAME_NULL_MSG);
        Assert.isTrue(StringUtil.isNotBlank(comment), "备注不能为空");
        //(如果有远程有更新则提示先撤销本地改动)

        //add
        //commit
        //push
        gitApiService.add(branchName);
        gitApiService.commit(branchName, comment);
        boolean pushResult = gitApiService.push(branchName);
        Assert.isTrue(pushResult, "提交失败");
    }

    @Override
    public void resetModify(String branchName) throws GitAPIException {
        Assert.isTrue(StringUtil.isNotBlank(branchName), GitPrdCons.BRANCH_NAME_NULL_MSG);
        gitApiService.reset(branchName);  //撤销本地删除和修改
        gitApiService.clean(branchName);  //清除本地新增文件(夹)
    }

    @Override
    public List<GitStatus> getAllBranchStatus() throws GitAPIException {
        List<GitStatus> statuses = new ArrayList<>();
        List<File> branchList = getLocalBranchedFiles();
        if (branchList == null) return statuses;
        FetchResult fetchResult = gitApiService.fetch(true);

        for (File file : branchList) {
            String branchName = file.getName();
            GitStatus status = gitApiService.status(branchName);
            status.setHasUnPulledChanges(hasUpdates(fetchResult, branchName));
            statuses.add(status);
        }
        return statuses;
    }

    private boolean hasUpdates(FetchResult fetchResult, String branchName) {
        if (fetchResult == null) return false;
        TrackingRefUpdate trackingRefUpdate = fetchResult.getTrackingRefUpdate("refs/remotes/origin/" + branchName);
        return trackingRefUpdate != null;
    }

    private List<File> getLocalBranchedFiles() {
        File branches = new File(gitHelper.getBranchesPath());
        File[] files = branches.listFiles();
        return Arrays.stream(files).filter(file -> file.isDirectory()).collect(Collectors.toList());
    }

    @Override
    public void cloneBranch(String branchName) throws GitAPIException {
        Assert.isTrue(StringUtil.isNotBlank(branchName), GitPrdCons.BRANCH_NAME_NULL_MSG);
        gitApiService.cloneBranch(gitHelper.getBranchesPath() + "/" + branchName);
        gitApiService.checkout(branchName);
    }

    @Override
    public void pull(String branchName) throws GitAPIException {
        Assert.isTrue(StringUtil.isNotBlank(branchName), GitPrdCons.BRANCH_NAME_NULL_MSG);
        //拉取更新 (先撤销改动再拉取,防止冲突)
        this.resetModify(branchName);
        gitApiService.fetch(false);
        gitApiService.pull(branchName);
    }

    @Override
    public void delete(String branchName) throws GitAPIException, IOException {
        Assert.isTrue(StringUtil.isNotBlank(branchName), GitPrdCons.BRANCH_NAME_NULL_MSG);
        Assert.isTrue(StringUtil.isNotBlank(branchName), GitPrdCons.BRANCH_NAME_NULL_MSG);
        //打标签
        //删除远程分支
        //删除本地文件夹
        gitApiService.addTag(branchName);
        gitApiService.deleteRemoteBranch(branchName);
        FileUtil.deleteDirectory(new File(gitHelper.getBranchesPath() + "/" + branchName));
    }

    @Override
    public void openInFinder(String branchName) {
        Assert.isTrue(StringUtil.isNotBlank(branchName), GitPrdCons.BRANCH_NAME_NULL_MSG);
        String path = gitHelper.getBranchesPath() + "/" + branchName;
        SystemUtils.openInFinder(path);
    }

    @Override
    public String getOnlineUrl(String branchName) {
        Assert.isTrue(StringUtil.isNotBlank(branchName), GitPrdCons.BRANCH_NAME_NULL_MSG);
        String url = gitHelper.getPrdServiceURI() + "/" + branchName;
        SystemUtils.browserUrl(url);
        return url;
    }
}
