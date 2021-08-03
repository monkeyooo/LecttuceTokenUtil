package com.token.interfaces;

public interface UserToken {
    String getUserName();
    String getEmail();
    int getId();
    UserToken setYourData(String data);
    boolean save();
    boolean extend();
}
