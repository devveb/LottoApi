package com.sbsft.wslapi.model;

import lombok.Data;

@Data
public class DrawInfo {

    private int draw;

    private String firstTotalPrize;
    private String secondTotalPrize;
    private String thirdTotalPrize;
    private String fourthTotalPrize;
    private String fifthTotalPrize;

    private int firstWinCnt;
    private int secondWinCnt;
    private int thirdWinCnt;
    private int fourthWinCnt;
    private int fifthWinCnt;
    private int sixthWinCnt;

    private String firstPrize;
    private String secondPrize;
    private String thirdPrize;
    private String fourthPrize;
    private String fifthPrize;

    private long firstPrizeInt;
    private long secondPrizeInt;
    private long thirdPrizeInt;
    private int fourthPrizeInt;
    private int fifthPrizeInt;

    private int place;
    private String pickDate;
    private String drawDate;
    private String sugDate;
    private String regDate;

    private String type;

    private int sugCount;

}
