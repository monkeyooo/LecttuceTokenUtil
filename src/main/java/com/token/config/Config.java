package com.token.config;


import com.token.interfaces.ConfigInterface;

import java.io.InputStream;
import java.util.Properties;

public class Config implements ConfigInterface {
    static String host;
    static String password;
    static int port;
    public Config() {
        if (host == null) {
            if (System.getProperty("RMC_REDIS_HOST") != null) {
                host = System.getProperty("RMC_REDIS_HOST");
                port = Integer.parseInt(System.getProperty("RMC_REDIS_PORT", "6379"));
            } else {
                try {
                    InputStream inputStream = getClass().getResourceAsStream("/property.properties");
                    Properties appProps = new Properties();
                    appProps.load(inputStream);
                    if (appProps.getProperty("redisHost") != null)
                        host = appProps.getProperty("redisHost");
                    else
                        host = "localhost";
                    password = appProps.getProperty("redisHostPassword");
                    if (appProps.getProperty("redisPort") != null)
                        port = Integer.parseInt(appProps.getProperty("redisPort"));
                    else
                        port = 6379;
                } catch (Exception e) {
                    host = "localhost";
                    port = 6379;
                }
                System.setProperty("RMC_REDIS_HOST", host);
                System.setProperty("RMC_REDIS_PORT", String.valueOf(port));
            }
            System.out.println("Redis Host : " + host);
            System.out.println("Redis Port : " + port);
        }
    }
    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

}
