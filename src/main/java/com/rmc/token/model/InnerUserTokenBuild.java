package com.rmc.token.model;

import com.google.gson.Gson;
import com.rmc.token.interfaces.InnerUserTokenBuilder;
import io.lettuce.core.api.async.RedisAsyncCommands;

public class InnerUserTokenBuild implements InnerUserTokenBuilder {
    int userId;
    String userName;
    int albumId;
    protected transient String token;
    protected transient Gson gson;
    transient final int lifeCycle = 60 * 60 * 2; // second to hours, 2 hours
    transient RedisAsyncCommands<String, String> redisCommand;
    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }
}
