package com.example.mello;

import java.util.ArrayList;

public class GroupUsers {

    String name;
    String userID;
    ArrayList <GroupUsers> groupUsers;

    public ArrayList<GroupUsers> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(ArrayList<GroupUsers> groupUsers) {
        this.groupUsers = groupUsers;
    }
public GroupUsers(){

}
    public GroupUsers(String name, String userID) {
        this.name = name;
        this.userID = userID;
    }
}
