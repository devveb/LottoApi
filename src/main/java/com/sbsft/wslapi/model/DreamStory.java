package com.sbsft.wslapi.model;

import lombok.Data;

@Data
public class DreamStory extends DrawInfo {

    private int idx;
    private String story;
    private int iss;
    private String result;
    private String timer;
    private long targetDraw;
    private int snid;


}
