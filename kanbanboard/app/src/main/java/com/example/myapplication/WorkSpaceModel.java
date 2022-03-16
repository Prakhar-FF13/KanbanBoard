package com.example.myapplication;

public class WorkSpaceModel {
    private String workspace_name;
    private int wid;
    private int tworkspace_member_count;
    private String createdBy;

    public WorkSpaceModel(int wid, String workspace_name, int tworkspace_member_count, String createdBy) {
        this.wid = wid;
        this.workspace_name = workspace_name;
        this.tworkspace_member_count = tworkspace_member_count;
        this.createdBy = createdBy;
    }

    public String getWorkspace_name() {
        return workspace_name;
    }

    public int getWorkspace_member_count() {
        return tworkspace_member_count;
    }

    public int getWid() {
        return this.wid;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }
}
