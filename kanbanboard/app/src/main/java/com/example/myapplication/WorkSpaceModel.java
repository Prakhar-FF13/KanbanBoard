package com.example.myapplication;

public class WorkSpaceModel {
    private String workspace_name;
    private int tworkspace_member_count;


    public WorkSpaceModel(String workspace_name, int tworkspace_member_count) {
        this.workspace_name = workspace_name;
        this.tworkspace_member_count = tworkspace_member_count;
    }

    public String getWorkspace_name() {
        return workspace_name;
    }

    public void setWorkspace_name(String workspace_name) {
        this.workspace_name = workspace_name;
    }

    public int getWorkspace_member_count() {
        return tworkspace_member_count;
    }

    public void setWorkspace_member_count(int tworksapce_team_count) {
        this.tworkspace_member_count = tworksapce_team_count;
    }
}
