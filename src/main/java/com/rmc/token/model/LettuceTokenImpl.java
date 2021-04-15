package com.rmc.token.model;

import com.google.gson.Gson;
import com.rmc.token.interfaces.LabelToken;
import io.lettuce.core.api.async.RedisAsyncCommands;

import static com.rmc.token.constant.Constant.LABEL_TOKEN_PREFIX;


public class LettuceTokenImpl extends TokenModel implements LabelToken {

    protected transient String token;
    protected transient Gson gson;
    transient final int lifeCycle = 60 * 60 * 2; // second to hours, 2 hours
    transient RedisAsyncCommands<String, String> redisCommand;


    public LettuceTokenImpl() {
        this.labelId = 0;
        this.albumId = 0;
    }

    @Override
    public int getLabelId() {
        return labelId;
    }

    @Override
    public LabelToken setAlbumId(int albumId) {
        this.albumId = albumId;
        return this;
    }

    @Override
    public LabelToken setAlbumToken(String data) {
        this.albumToken = data;
        return this;
    }

    @Override
    public String getMusicBoxData() {
        return musicBoxData;
    }

    @Override
    public String getAnalysisData() {
        return analysisData;
    }

    @Override
    public String getAlbumToken() {
        return albumToken;
    }

    @Override
    public int getAlbumId() {
        return this.albumId;
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
    public LabelToken setMusicBoxData(String data) {
        this.musicBoxData = data;
        return this;
    }

    @Override
    public LabelToken setAnalysisData(String data) {
        this.analysisData = data;
        return this;
    }

    @Override
    public boolean save() {
        if (redisCommand == null || this.labelId == 0 || gson == null)
            return false;
        redisCommand.set(LABEL_TOKEN_PREFIX + token, gson.toJson(this));
        return true;
    }

    @Override
    public boolean extend() {
        if (redisCommand == null || this.labelId == 0 || gson == null)
            return false;
        redisCommand.expire(LABEL_TOKEN_PREFIX + token, lifeCycle);
        return true;
    }

    public LabelToken setRequire(String token, Gson gson, RedisAsyncCommands<String, String> redisCommand) {
        this.token = token;
        this.gson = gson;
        this.redisCommand = redisCommand;
        return this;
    }
}
