package com.example.chatter;

class User {
    private String userName, userID;

    public User(String un, String id) {
        userName = un;
        userID = id;
    }
    public User() {

    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }
}
