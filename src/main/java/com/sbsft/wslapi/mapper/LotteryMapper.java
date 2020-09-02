package com.sbsft.wslapi.mapper;

import com.sbsft.wslapi.model.DreamStory;
import com.sbsft.wslapi.model.NumSet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LotteryMapper {


    void insertWinningNumbers(NumSet numSet);

    int getMaxDraw();

    void insertDrawHistory(NumSet ns);

    String selectDreamNumber(String textCheck);

    void insertDreamResult(DreamStory ds);

    List<DreamStory> selectDreamResultList(int page);

    List<NumSet> getDrawHistory();

    void insertDrawSimulation(NumSet dreamnum);

    List<DreamStory> seleteUnpackedSuggestionList();
}
