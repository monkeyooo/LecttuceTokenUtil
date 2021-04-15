package com.token.model;

import com.google.gson.Gson;

import com.token.interfaces.InnerUserToken;
import io.lettuce.core.api.async.RedisAsyncCommands;

import static com.token.constant.Constant.INNER_TOKEN_PREFIX;


public class InnerUserTokenImpl implements InnerUserToken {

    int userId;
    String userName;
    transient final int lifeCycle = 60 * 60 * 9;
    protected transient String token;
    protected transient Gson gson;
    transient RedisAsyncCommands<String, String> redisCommand;
    public InnerUserTokenImpl() {
        this.userId = 0;
    }

    @Override
    public int getUserId() {
        return this.userId;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public boolean save() {
        if (redisCommand == null || this.userId == 0 || gson == null)
            return false;
        redisCommand.set(INNER_TOKEN_PREFIX + token, gson.toJson(this));
        return true;
    }

    @Override
    public boolean extend() {
        if (redisCommand == null || this.userId == 0 || gson == null)
            return false;
        redisCommand.expire(INNER_TOKEN_PREFIX + token, lifeCycle);
        return true;
    }

    public InnerUserToken setRequire(String token, Gson gson, RedisAsyncCommands<String, String> redisCommand) {
        this.token = token;
        this.gson = gson;
        this.redisCommand = redisCommand;
        return this;
    }
}
