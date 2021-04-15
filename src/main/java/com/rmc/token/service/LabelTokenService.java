package com.rmc.token.service;

import com.google.gson.Gson;
import com.rmc.token.factory.LettuceFactory;
import com.rmc.token.interfaces.LabelToken;
import com.rmc.token.interfaces.LabelTokenBuilder;
import com.rmc.token.model.LettuceTokenImpl;
import io.lettuce.core.api.async.RedisAsyncCommands;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.rmc.token.constant.Constant.LABEL_TOKEN_PREFIX;


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
            String tokenData = redisCommand.get(LABEL_TOKEN_PREFIX + token).get();
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
            labelTokenList = redisCommand.keys(LABEL_TOKEN_PREFIX + "*").get();
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
        redisCommand.set(LABEL_TOKEN_PREFIX + token, data);
        redisCommand.expire(LABEL_TOKEN_PREFIX + token, 7200 * 4);
        return true;
    }
}
