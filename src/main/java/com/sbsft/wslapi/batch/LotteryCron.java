package com.sbsft.wslapi.batch;

import com.sbsft.wslapi.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LotteryCron {

    @Autowired
    LotteryService lotteryService;


    @Scheduled(cron = "0 21 * * * *")
//    @Scheduled(cron = "0 * * * * mon")
    private void getWinningNumbers(){
        // 당첨번호 수집
        lotteryService.getWeeklyWinningNumbers();
    }



//    @Scheduled(cron = "30 * * * * *")
//    private void getThisWeekWinResult(){
//        // 당첨 여부 조회
//        lotteryService.getWeeklyWinResult(0);
//    }

    @Scheduled(cron = "5 21 * * * *")
    private void getPastWeekWinResult(){
        // 이전 주 당첨 여부 업데이트
        lotteryService.getPastWeeklyWinResult();
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
