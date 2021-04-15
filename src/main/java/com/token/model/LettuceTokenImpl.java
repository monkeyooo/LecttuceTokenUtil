package com.token.model;

import com.google.gson.Gson;
import com.token.interfaces.LabelToken;
import com.token.constant.Constant;
import io.lettuce.core.api.async.RedisAsyncCommands;


public class LettuceTokenImpl extends TokenModel implements LabelToken {

    protected transient String token;
    protected transient Gson gson;
    transient final int lifeCycle = 60 * 60 * 2; // second to hours, 2 hours
    transient RedisAsyncCommands<String, String> redisCommand;


    public LettuceTokenImpl() {
        this.labelId = 0;
    }

    @Override
    public int getLabelId() {
        return labelId;
    }

    @Override
    public LabelToken setYourData(String data) {
        return this;
    }

    @Override
    public String getLabelName() {
        return labelName;
    }

    @Override
    public String getLabelEmail() {
        return labelEmail;
    }

    @Override
    public boolean save() {
        if (redisCommand == null || this.labelId == 0 || gson == null)
            return false;
        redisCommand.set(Constant.LABEL_TOKEN_PREFIX + token, gson.toJson(this));
        return true;
    }

    @Override
    public boolean extend() {
        if (redisCommand == null || this.labelId == 0 || gson == null)
            return false;
        redisCommand.expire(Constant.LABEL_TOKEN_PREFIX + token, lifeCycle);
        return true;
    }

    public LabelToken setRequire(String token, Gson gson, RedisAsyncCommands<String, String> redisCommand) {
        this.token = token;
        this.gson = gson;
        this.redisCommand = redisCommand;
        return this;
    }
}
