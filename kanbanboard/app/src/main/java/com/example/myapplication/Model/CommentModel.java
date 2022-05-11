package com.example.myapplication.Model;

import java.io.Serializable;

public class CommentModel implements Serializable {
    private int cid;
    private int taskId;
    private String comment;
    private String timestamp;
    private String author;

    public CommentModel(int taskId, String comment, String timestamp, String author) {
        this.taskId = taskId;
        this.comment = comment;
        this.timestamp = timestamp;
        this.author = author;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
