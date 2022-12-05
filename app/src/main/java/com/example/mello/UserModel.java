package com.example.mello;

import java.util.ArrayList;

public class UserModel {
    String userID;
    String userName;
    ArrayList<UserModel> userModelArrayList;

    public UserModel(String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public UserModel() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<UserModel> getUserModelArrayList() {
        return userModelArrayList;
    }

    public void setUserModelArrayList(ArrayList<UserModel> userModelArrayList) {
        this.userModelArrayList = userModelArrayList;
    }
}
