package com.sbsft.wslapi.domain;

import lombok.Data;

@Data
public class Episode {
	private int episode;
	private int startEpisode;
	private int endEpisode;
	private long firstPrize;
	private long secondPrize;
	private long thirdPrize;
	private long fourthPrize;
	private long fifthPrize;
	private int firstWinCnt;
	private int secondWinCnt;
	private int thirdWinCnt;
	private int fourthWinCnt;
	private int fifthWinCnt;
	private int sixthWinCnt;

	private String pickDate;
	private int numberType;

	


}
