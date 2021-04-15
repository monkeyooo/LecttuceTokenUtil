package com.rmc.token.interfaces;

public interface LabelToken {
    String getLabelName();
    String getLabelEmail();
    String getMusicBoxData();
    String getAnalysisData();
    String getAlbumToken();
    int getAlbumId();
    int getLabelId();
    LabelToken setAlbumId(int albumId);
    LabelToken setAlbumToken(String data);
    LabelToken setMusicBoxData(String data);
    LabelToken setAnalysisData(String data);
    boolean save();
    boolean extend();
}
