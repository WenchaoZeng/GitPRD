package com.yit.gitprd.exception;

import com.yit.gitprd.cons.GitLabReturnCode;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
public class GitlabServiceException extends Exception {

    public GitlabServiceException(String message) {
        super(message);
    }

    public GitlabServiceException(GitLabReturnCode gitLabReturnCode) {
        super(gitLabReturnCode.getText());
    }

    public GitlabServiceException(HttpException e) {
        super("接口异常: " + e.getMessage());
    }
}
