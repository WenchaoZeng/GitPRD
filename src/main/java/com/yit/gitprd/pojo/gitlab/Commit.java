package com.yit.gitprd.pojo.gitlab;

import java.util.Date;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
public class Commit {
    private String id;
    private String shortId;
    private String title;
    private String message;
    private String authorEmail;
    private String authorName;
    private Date authoredDate;
    private Date committedDate;
    private String committerEmail;
    private String committerName;
    private String[] parentIds;
    private Stats stats;
    private String status;

    private class Stats {
        private Integer additions;
        private Integer deletions;
        private Integer total;

        public Integer getAdditions() {
            return additions;
        }

        public void setAdditions(Integer additions) {
            this.additions = additions;
        }

        public Integer getDeletions() {
            return deletions;
        }

        public void setDeletions(Integer deletions) {
            this.deletions = deletions;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getAuthoredDate() {
        return authoredDate;
    }

    public void setAuthoredDate(Date authoredDate) {
        this.authoredDate = authoredDate;
    }

    public Date getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(Date committedDate) {
        this.committedDate = committedDate;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public void setCommitterEmail(String committerEmail) {
        this.committerEmail = committerEmail;
    }

    public String getCommitterName() {
        return committerName;
    }

    public void setCommitterName(String committerName) {
        this.committerName = committerName;
    }

    public String[] getParentIds() {
        return parentIds;
    }

    public void setParentIds(String[] parentIds) {
        this.parentIds = parentIds;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
