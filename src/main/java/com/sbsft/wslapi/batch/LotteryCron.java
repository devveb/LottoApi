package com.sbsft.wslapi.batch;

import com.sbsft.wslapi.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LotteryCron {

    @Autowired
    LotteryService lotteryService;


    @Scheduled(cron = "30 14 * * * *")
//    @Scheduled(cron = "0 * * * * mon")
    private void getWinningNumbers(){
        // TODO: 2020/08/20
        // 1. get last draw and present draw num
        // 2. get draw result and insert
        lotteryService.getWeeklyWinningNumbers();
    }



    @Scheduled(cron = "30 * * * * *")
//    @Scheduled(cron = "10 * * * * mon")
    private void getTargetDraw(){
        // TODO: 2020/08/20
        // 1. get last draw and present draw num
        // 2. get draw result and insert
        lotteryService.getWeeklyWinResult(0);


    }



//    @Scheduled(cron = "0 0 22 * * *")
//    private void generateDrawPlaceResult(){
//        // TODO: 2020/08/20
//        // 1. get suggestions that not matched yet
//        List<DreamStory> suggestionList = lotteryService.getUnpackedSuggestionList();
//        for(DreamStory ds:suggestionList){
//            lotteryService.checkHistory(ds);
//        }
//    }
}