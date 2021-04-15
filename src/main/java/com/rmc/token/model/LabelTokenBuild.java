package com.rmc.token.model;


import com.rmc.token.interfaces.LabelTokenBuilder;

public class LabelTokenBuild extends TokenModel implements LabelTokenBuilder {

    public LabelTokenBuild(int labelId, String labelName, String labelEmail) {
        this.labelId = labelId;
        this.labelName = labelName;
        this.labelEmail = labelEmail;
    }

    public String getMusicBoxData() {
        return musicBoxData;
    }

    public void setMusicBoxData(String musicBoxData) {
        this.musicBoxData = musicBoxData;
    }

    public String getLabelName() {
        return labelName;
    }
    @Override
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public int getLabelId() {
        return labelId;
    }
    @Override
    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public String getLabelEmail() {
        return labelEmail;
    }
    @Override
    public void setLabelEmail(String labelEmail) {
        this.labelEmail = labelEmail;
    }

    public String getAnalysisData() {
        return analysisData;
    }

    public void setAnalysisData(String analysisData) {
        this.analysisData = analysisData;
    }

    public String getAlbumToken() {
        return albumToken;
    }

    public void setAlbumToken(String albumToken) {
        this.albumToken = albumToken;
    }
}
