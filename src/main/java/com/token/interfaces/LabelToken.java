package com.token.interfaces;

public interface LabelToken {
    String getLabelName();
    String getLabelEmail();
    int getLabelId();
    LabelToken setYourData(String data);
    boolean save();
    boolean extend();
}
