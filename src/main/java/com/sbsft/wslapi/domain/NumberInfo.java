package com.sbsft.wslapi.domain;

import lombok.Data;

@Data
public class NumberInfo extends Episode {
	private int result;
	private int rank;
	private int sid;
	private int idx;
	private String name;
	private String meaning;
	private String story;
	private int privacy;
	private int number;
	private int count;
	private int suggestType;

}
