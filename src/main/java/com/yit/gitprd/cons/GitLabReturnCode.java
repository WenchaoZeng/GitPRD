package com.yit.gitprd.cons;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
public enum GitLabReturnCode {

    BRANCH_ALREADY_EXISTS("Branch already exists", "PRD名字重复"),
    CANNOT_REMOVE_HEAD_BRANCH("Cannot remove HEAD branch", "该PRD不能删除");

    GitLabReturnCode(String originalMsg, String text) {
        this.originalMsg = originalMsg;
        this.text = text;
    }

    private String originalMsg;
    private String text;

    public String getOriginalMsg() {
        return originalMsg;
    }

    public String getText() {
        return text;
    }

    /**
     * 解析gitlab原始消息
     *
     * @param originalMsg
     * @return
     */
    public static Optional<GitLabReturnCode> parseByGitLabOriginalMsg(String originalMsg) {
        return Arrays.stream(GitLabReturnCode.values())
                .filter(c -> c.originalMsg.equals(originalMsg)).findAny();
    }
}
