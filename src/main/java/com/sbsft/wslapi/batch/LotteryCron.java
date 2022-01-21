package com.sbsft.wslapi.batch;

import com.sbsft.wslapi.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LotteryCron {

    @Autowired
    LotteryService lotteryService;


    @Scheduled(cron = "30 5 12 * * *")
//    @Scheduled(cron = "0 * * * * mon")
    private void getWinningNumbers(){
        // 당첨번호 수집
        Map m = new HashMap();
        m.put("jobNo",1);
        lotteryService.insertCronlog(m);
        lotteryService.getWeeklyWinningNumbers();
    }



//    @Scheduled(cron = "30 * * * * *")
//    private void getThisWeekWinResult(){
//        // 당첨 여부 조회
//        lotteryService.getWeeklyWinResult(0);
//    }

    @Scheduled(cron = "50 10 12 * * *")
    private void getPastWeekWinResult(){
        // 이전 주 당첨 여부 업데이트
        Map m = new HashMap();
        m.put("jobNo",2);
        lotteryService.insertCronlog(m);
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
