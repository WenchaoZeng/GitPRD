package com.yit.gitprd.pojo.git;

import java.util.Date;

/**
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
public class Branch {

    private String name;
    private Type type;
    private Date lastModifiedTime;
    private String lastCommitAuthor;
    private String lastComment;

    public enum Type {
        LOCAL, REMOTE
    }

    //-----


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastCommitAuthor() {
        return lastCommitAuthor;
    }

    public void setLastCommitAuthor(String lastCommitAuthor) {
        this.lastCommitAuthor = lastCommitAuthor;
    }

    public String getLastComment() {
        return lastComment;
    }

    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }
}
