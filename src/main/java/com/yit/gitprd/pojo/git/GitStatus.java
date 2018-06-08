package com.yit.gitprd.pojo.git;

import org.eclipse.jgit.api.Status;

/**
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
public class GitStatus {

    private Status status;
    /**
     * 存在未提交文件: 新添加,新改动
     */
    private Boolean existUnCommit;

    //--

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getExistUnCommit() {
        return existUnCommit;
    }

    public void setExistUnCommit(Boolean existUnCommit) {
        this.existUnCommit = existUnCommit;
    }
}
