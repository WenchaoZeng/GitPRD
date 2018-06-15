package com.yit.gitprd.pojo;

/**
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
public class Branch {

    private String name;
    private Type type;
    private GitStatus status;
//    private Date lastModifiedTime;
//    private String lastCommitAuthor;
//    private String lastComment;

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

    public GitStatus getStatus() {
        return status;
    }

    public void setStatus(GitStatus status) {
        this.status = status;
    }
}
