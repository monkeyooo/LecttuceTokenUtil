package com.token.model;


import com.token.interfaces.UserTokenBuilder;

public class UserTokenBuild extends TokenModel implements UserTokenBuilder {

    public UserTokenBuild(int id, String name, String email) {
        this.userId = id;
        this.userName = name;
        this.email = email;
    }

    @Override
    public void setName(String labelName) {
        this.userName = labelName;
    }

    @Override
    public void setId(int labelId) {
        this.userId = labelId;
    }

    @Override
    public void setEmail(String labelEmail) {
        this.email = labelEmail;
    }
}
