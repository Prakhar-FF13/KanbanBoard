package com.example.myapplication.Model;

import java.io.Serializable;

public class TaskModel implements Serializable {
    private final int id;
    private String title;
    private String description;
    private String priority;
    private String assignee;
    private String status;
    private String date;

    public TaskModel(int id, String title, String description, String priority, String assignee, String status, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.assignee = assignee;
        this.status = status;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() { return this.id; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
