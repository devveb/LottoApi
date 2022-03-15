package com.sbsft.wslapi.mapper;

import com.sbsft.wslapi.model.DrawInfo;
import com.sbsft.wslapi.model.DreamStory;
import com.sbsft.wslapi.model.NumSet;
import com.sbsft.wslapi.model.Paging;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface LotteryMapper {


    void insertWinningNumbers(NumSet numSet);

    int getMaxDraw();

    void insertDrawHistory(NumSet ns);

    String selectDreamNumber(DreamStory ds);

    void insertDreamResult (DreamStory ds);

    List<DreamStory> selectDreamResultList(int page);

    List<NumSet> getDrawHistory(NumSet draw);

    void insertDrawSimulation(NumSet dreamnum);

    List<DreamStory> seleteUnpackedSuggestionList();

    void insertNumberCombiTotalPrize(NumSet ns);

    int countNumberSet(DreamStory ds);

    void insertNumberSet(DreamStory ds);

    void updateSuggestionCount(DreamStory ds);

    NumSet getNumberSet(DreamStory ds);

    void updateNumberCombiTotalPrize(NumSet ns);

    NumSet getNumberSetById(DreamStory ds);

    List<DreamStory> getSimHistory(DreamStory ds);

    void postReply(DreamStory ds);

    List<DreamStory> getReplyById(DreamStory ds);

    DreamStory getSuggestionById(int snid);

    int getReplyCnt(DreamStory ds);

    NumSet getDrawNumSet(int presentDraw);

    List<DreamStory> getTargetDrawSuggestion(int presentDraw);

    void insertWeeklyDrawResult(NumSet ns);

    List<DreamStory> getWeeklyResult(NumSet ns);

    List<DrawInfo> getUnIdentifiedEpis(DrawInfo di);

    void insertAutoReplyResult(NumSet ns);

    List<DreamStory> getList(Paging paging);

    int getTotalPage();

    List<Map<String, String>> getContentReply(Paging paging);

    int getReplyCount(HashMap param);

    DreamStory getContent(HashMap param);
}
