package com.token.factory;

import io.lettuce.core.api.async.RedisAsyncCommands;

public class LettuceFactory {

    public static RedisAsyncCommands<String, String> getRedisCmd() {
        return ConnectionFactory.getConnection().async();
    }
}
