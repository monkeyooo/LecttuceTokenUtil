package com.rmc.token.interfaces;

public interface InnerUserToken {
    int getUserId();
    String getUserName();
    int getAlbumId();
    boolean extend();
    boolean save();
}
