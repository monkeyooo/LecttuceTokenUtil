package com.rmc.token.factory;


import com.rmc.token.config.Config;
import com.rmc.token.interfaces.ConfigInterface;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class RmcConnectionFactory {

    static Config config = new Config();

    private static class SingletonClient {
        public static RedisClient redisClient;
    }
    private static RedisClient getSingletonRedisClient() {
        if (SingletonClient.redisClient == null) {
            synchronized (SingletonClient.class) {
                RedisURI redisUri = RedisURI.builder()
                        .withHost(config.getHost())
                        .withPort(config.getPort())
                        .withTimeout(Duration.of(30, ChronoUnit.SECONDS))
                        .build();
                SingletonClient.redisClient = RedisClient.create(redisUri);
                SingletonClient.redisClient.setDefaultTimeout(Duration.ofSeconds(60));
            }
        }
        return SingletonClient.redisClient;
    }

    private static class SingletonConnection {
        public static StatefulRedisConnection<String, String> connection;
    }

    private static StatefulRedisConnection<String, String> getSingletonConnection() {
        if (SingletonConnection.connection == null) {
            synchronized (SingletonConnection.class) {
                 SingletonConnection.connection = getSingletonRedisClient().connect();
            }
        }
        return SingletonConnection.connection;
    }


    public static StatefulRedisConnection<String, String> getConnection() {
        return getSingletonConnection();
    }

    public static void shutdownConnection() {
        getSingletonConnection().close();
    }

}
