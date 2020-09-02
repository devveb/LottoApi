package com.sbsft.wslapi.batch;

import com.sbsft.wslapi.model.DreamStory;
import com.sbsft.wslapi.model.NumSet;
import com.sbsft.wslapi.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LotteryCron {

    @Autowired
    LotteryService lotteryService;

    @Scheduled(cron = "15 15 * * * *")
    private void getWinningNumbers(){
        // TODO: 2020/08/20
        // 1. get last draw and present draw num
        // 2. get draw result and insert

        int maxDraw = lotteryService.getMaxDraw();
        int presentDraw = lotteryService.getPresentDraw();
        if(maxDraw < presentDraw){
            for(int i = maxDraw+1;i<=presentDraw;i++ ){
                NumSet ns = lotteryService.callWinNumApi(i);
                lotteryService.insertDrawHistory(ns);
            }
        }
    }

    @Scheduled(cron = "59 23 * * * *")
    private void generateDrawPlaceResult(){
        // TODO: 2020/08/20
        // 1. get suggestions that not matched yet
        List<DreamStory> suggestionList = lotteryService.getUnpackedSuggestionList();
        // 2. trans into numset
        List<NumSet> suggestionNumsetList = new ArrayList<>();
        for(DreamStory ds:suggestionList){
            NumSet numset = new NumSet();
            String[] arr = ds.getResult().split(",");

            numset.setIdx(ds.getIdx());
            numset.setFirst(arr[0]);
            numset.setSecond(arr[1]);
            numset.setThird(arr[2]);
            numset.setFourth(arr[3]);
            numset.setFifth(arr[4]);
            numset.setSixth(arr[5]);
            suggestionNumsetList.add(numset);
        }

        List<NumSet> historyNums = lotteryService.getDrawHistory();

        for(NumSet dreamnum:suggestionNumsetList){
            for(NumSet historyNum:historyNums){
                int place = lotteryService.winChecker(historyNum,dreamnum);
                if(place <= 5){
                    dreamnum.setPlace(place);
                    dreamnum.setDraw(historyNum.getDraw());
                    lotteryService.insertDrawSimmulation(dreamnum);
                };
            }
        }


    }
}