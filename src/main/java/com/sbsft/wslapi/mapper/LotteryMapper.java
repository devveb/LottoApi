package com.sbsft.wslapi.mapper;

import com.sbsft.wslapi.model.NumSet;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LotteryMapper {


    void insertWinningNumbers(NumSet numSet);
}
