package com.sbsft.wslapi.batch;

import com.sbsft.wslapi.model.DreamStory;
import com.sbsft.wslapi.model.NumSet;
import com.sbsft.wslapi.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryCron {

    @Autowired
    LotteryService lotteryService;

    @Scheduled(cron = "0 30 21 * * sat")
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

    @Scheduled(cron = "0 0 22 * * *")
    private void generateDrawPlaceResult(){
        // TODO: 2020/08/20
        // 1. get suggestions that not matched yet
        List<DreamStory> suggestionList = lotteryService.getUnpackedSuggestionList();
        for(DreamStory ds:suggestionList){
            lotteryService.checkHistory(ds);
        }
    }
}