package com.yit.gitprd.pojo.git;

import org.eclipse.jgit.api.Status;

/**
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
public class GitStatus {

    private Status status;

    private String branchName;
    /**
     * 存在未提交的改变
     */
    private Boolean hasUncommittedChanges;


    //--

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Boolean getHasUncommittedChanges() {
        return hasUncommittedChanges;
    }

    public void setHasUncommittedChanges(Boolean hasUncommittedChanges) {
        this.hasUncommittedChanges = hasUncommittedChanges;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
