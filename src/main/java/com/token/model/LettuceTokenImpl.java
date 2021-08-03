package com.token.model;

import com.google.gson.Gson;
import com.token.interfaces.UserToken;
import com.token.constant.Constant;
import io.lettuce.core.api.async.RedisAsyncCommands;


public class LettuceTokenImpl extends TokenModel implements UserToken {

    protected transient String token;
    protected transient Gson gson;
    transient final int lifeCycle = 60 * 60 * 2; // second to hours, 2 hours
    transient RedisAsyncCommands<String, String> redisCommand;


    public LettuceTokenImpl() {
        this.userId = 0;
    }

    @Override
    public int getId() {
        return userId;
    }

    @Override
    public UserToken setYourData(String data) {
        return this;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public boolean save() {
        if (redisCommand == null || this.userId == 0 || gson == null)
            return false;
        redisCommand.set(Constant.USER_TOKEN_PREFIX + token, gson.toJson(this));
        return true;
    }

    @Override
    public boolean extend() {
        if (redisCommand == null || this.userId == 0 || gson == null)
            return false;
        redisCommand.expire(Constant.USER_TOKEN_PREFIX + token, lifeCycle);
        return true;
    }

    public UserToken setRequire(String token, Gson gson, RedisAsyncCommands<String, String> redisCommand) {
        this.token = token;
        this.gson = gson;
        this.redisCommand = redisCommand;
        return this;
    }
}
