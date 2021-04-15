package com.token.service;

import com.google.gson.Gson;
import com.token.factory.LettuceFactory;
import com.token.interfaces.LabelToken;
import com.token.interfaces.LabelTokenBuilder;
import com.token.model.LettuceTokenImpl;
import com.token.constant.Constant;
import io.lettuce.core.api.async.RedisAsyncCommands;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class LabelTokenService {
    final static Gson gson = new Gson();
    static RedisAsyncCommands<String, String> redisCommand;
    /**
     *
     * @param token - The label token , when token not found return label id = 1
     * @return LabelToken - label ID, label name,
     */
    public static LabelToken getLabelToken(String token) {
        if (redisCommand == null) redisCommand = LettuceFactory.getRedisCmd();
        try {
            String tokenData = redisCommand.get(Constant.LABEL_TOKEN_PREFIX + token).get();
            if (tokenData == null) return new LettuceTokenImpl();
            return gson.fromJson(tokenData, LettuceTokenImpl.class).setRequire(token, gson, redisCommand);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception 35 ");
            return new LettuceTokenImpl();
        }
    }

    /**
     *
     * @param albumToken AlbumToken
     * @return List<LabelToken> Labels who is using this album
     */
    public static List<LabelToken> getLabelTokenListByAlbumToken(String albumToken) {
        if (redisCommand == null) redisCommand = LettuceFactory.getRedisCmd();
        List<String> labelTokenList = null;
        try {
            labelTokenList = redisCommand.keys(Constant.LABEL_TOKEN_PREFIX + "*").get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception 46");
        }

        if (labelTokenList != null && !labelTokenList.isEmpty()) {
            Map<String, String> labelTokenMap = labelTokenList.parallelStream().collect(Collectors.toConcurrentMap(data -> data, data -> {
                try {
                    return redisCommand.get(data).get();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Exception 55");
                }
                return null;
            }));
            return labelTokenMap.entrySet()
                    .parallelStream()
                    .filter(data -> data.getValue() != null)
                    .map(data -> gson.fromJson(data.getValue(), LettuceTokenImpl.class))
                    .filter(data -> data.getAlbumToken().equals(albumToken)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     *
     * @param token Generated Label Token
     * @param labelToken LabelToken Object
     * @return true or false
     */
    public static boolean save(String token, LabelTokenBuilder labelToken) {
        if (redisCommand == null) redisCommand = LettuceFactory.getRedisCmd();
        String data;
        try {
            data = gson.toJson(labelToken);
        } catch (Exception e) {
            return false;
        }
        redisCommand.set(Constant.LABEL_TOKEN_PREFIX + token, data);
        redisCommand.expire(Constant.LABEL_TOKEN_PREFIX + token, 7200 * 4);
        return true;
    }
}
