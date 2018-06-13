package com.yit.gitprd.pojo;

/**
 * @author: clive
 * @date: 2018/06/13
 * @since: 1.0
 */
public class Notice {

    public enum Type {
        MSG,
        BRANCH_STATUS
    }

    private Type type;
    private Object content;

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Notice(Type type, Object content) {
        this.type = type;
        this.content = content;
    }

    public static Notice newMsgNotice(Object content) {
        return new Notice(Type.MSG, content);
    }
    public static Notice newBranchStatusNotice(Object content) {
        return new Notice(Type.BRANCH_STATUS, content);
    }
}
