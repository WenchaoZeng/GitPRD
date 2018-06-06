package com.yit.gitprd.pojo.gitlab;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
public class CommitAction {

    public enum Action {
        CREATE,
        DELETE,
        MOVE,
        UPDATE
    }

    private Action action;
    private String filePath;
    private String conent;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getConent() {
        return conent;
    }

    public void setConent(String conent) {
        this.conent = conent;
    }
}
