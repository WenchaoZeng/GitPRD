package com.yit.gitprd.pojo.gitlab;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
public class CommitAction {

    public enum Action {
        create,
        delete,
        move,
        update
    }
    public enum Encoding {
        text, base64
    }

    private Action action;
    @JSONField(name="file_path")
    private String filePath;
    private String content;
    @JSONField(name="previous_path")
    private String previousPath;
    private Encoding encoding;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPreviousPath() {
        return previousPath;
    }

    public void setPreviousPath(String previousPath) {
        this.previousPath = previousPath;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }
}
