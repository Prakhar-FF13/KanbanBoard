package com.example.myapplication;

public class collabListModel {

    private String collabname;
    private String designation;
    private int wid;

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getCollabname() {
        return collabname;
    }

    public void setCollabname(String collabname) {
        this.collabname = collabname;
    }

    public String getDesignation() {
        return designation;
    }

    public collabListModel(String collabname, String designation, int wid) {
        this.collabname = collabname;
        this.designation = designation;
        this.wid = wid;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
