package com.rmc.token.service;

import com.google.gson.Gson;
import com.rmc.token.factory.LettuceFactory;
import com.rmc.token.interfaces.InnerUserToken;
import com.rmc.token.interfaces.InnerUserTokenBuilder;
import com.rmc.token.model.InnerUserTokenImpl;

import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.concurrent.ExecutionException;

import static com.rmc.token.constant.Constant.RMC_TOKEN_PREFIX;


public class RmcUserTokenService {
    final static Gson gson = new Gson();
    static RedisAsyncCommands<String, String> redisCommand;

    public static InnerUserToken getUserToken(String token) {
        if (redisCommand == null) redisCommand = LettuceFactory.getRedisCmd();
        try {
            String tokenData = redisCommand.get(RMC_TOKEN_PREFIX + token).get();
            return gson.fromJson(tokenData, InnerUserTokenImpl.class).setRequire(token, gson, redisCommand);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception 35 ");
        }
        return new InnerUserTokenImpl();
    }

    public static boolean save(String token, InnerUserTokenBuilder labelToken) {
        if (redisCommand == null) redisCommand = LettuceFactory.getRedisCmd();
        String data;
        try {
            data = gson.toJson(labelToken);
        } catch (Exception e) {
            return false;
        }
        redisCommand.set(RMC_TOKEN_PREFIX + token, data);
        redisCommand.expire(RMC_TOKEN_PREFIX + token, 7200 * 4);
        return true;
    }
}
