package com.example.mello;

import java.util.ArrayList;

public class GroupModel {


public     String groupName;
public     String groupId;
public ArrayList<GroupModel> groupModelArrayList;

    public ArrayList<GroupModel> getGroupModelArrayList() {
        return groupModelArrayList;
    }

    public void setGroupModelArrayList(ArrayList<GroupModel> groupModelArrayList) {
        this.groupModelArrayList = groupModelArrayList;
    }

    public GroupModel(){

}

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public GroupModel(String groupName, String groupId) {
        this.groupName = groupName;
        this.groupId = groupId;
    }
}
