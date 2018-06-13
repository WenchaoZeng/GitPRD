package com.yit.gitprd.pojo;

import com.yit.gitprd.cons.BranchListType;

/**
 * @author: clive
 * @date: 2018/06/11
 * @since: 1.0
 */
public class BranchParam {

    private BranchListType branchListType;
    private String branchName;
    private String comment;
    private String refBranchName;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRefBranchName() {
        return refBranchName;
    }

    public void setRefBranchName(String refBranchName) {
        this.refBranchName = refBranchName;
    }

    public BranchListType getBranchListType() {
        return branchListType;
    }

    public void setBranchListType(BranchListType branchListType) {
        this.branchListType = branchListType;
    }
}
