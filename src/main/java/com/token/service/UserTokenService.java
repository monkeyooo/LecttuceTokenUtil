package com.token.service;

import com.google.gson.Gson;
import com.token.factory.LettuceFactory;
import com.token.interfaces.UserToken;
import com.token.interfaces.UserTokenBuilder;
import com.token.model.LettuceTokenImpl;
import com.token.constant.Constant;
import io.lettuce.core.api.async.RedisAsyncCommands;


import java.util.concurrent.ExecutionException;


public class UserTokenService {
    final static Gson gson = new Gson();
    static RedisAsyncCommands<String, String> redisCommand;
    /**
     *
     * @param token - The label token , when token not found return label id = 1
     * @return LabelToken - label ID, label name,
     */
    public static UserToken getLabelToken(String token) {
        if (redisCommand == null) redisCommand = LettuceFactory.getRedisCmd();
        try {
            String tokenData = redisCommand.get(Constant.USER_TOKEN_PREFIX + token).get();
            if (tokenData == null) return new LettuceTokenImpl();
            return gson.fromJson(tokenData, LettuceTokenImpl.class).setRequire(token, gson, redisCommand);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception 35 ");
            return new LettuceTokenImpl();
        }
    }

    /**
     *
     * @param token Generated Label Token
     * @param labelToken LabelToken Object
     * @return true or false
     */
    public static boolean save(String token, UserTokenBuilder labelToken) {
        if (redisCommand == null) redisCommand = LettuceFactory.getRedisCmd();
        String data;
        try {
            data = gson.toJson(labelToken);
        } catch (Exception e) {
            return false;
        }
        redisCommand.set(Constant.USER_TOKEN_PREFIX + token, data);
        redisCommand.expire(Constant.USER_TOKEN_PREFIX + token, 7200 * 4);
        return true;
    }
}
