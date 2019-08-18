package com.sbsft.wslapi.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApiMapper {

    void insertToWordList(String word);

    List<Map<String, Object>> selectRecentList();
}
