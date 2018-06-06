package com.yit.gitprd.pojo.gitlab;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
public class PushCommit {

    private String branchName;
    private String commitMessage;
    private List<CommitAction> actions = new ArrayList<>();

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public List<CommitAction> getActions() {
        return actions;
    }

    public void setActions(List<CommitAction> actions) {
        this.actions = actions;
    }
    public void addAction(CommitAction action) {
        this.actions.add(action);
    }
}
