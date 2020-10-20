package com.sbsft.wslapi.batch;

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


    @Scheduled(cron = "0 0 21 * * SAT")
//    @Scheduled(cron = "0 * * * * mon")
    private void getWinningNumbers(){
        // TODO: 2020/08/20
        // 1. get last draw and present draw num
        // 2. get draw result and insert
        int maxDraw = lotteryService.getMaxDraw(); /* get latest draw from history */
        int presentDraw = lotteryService.getPresentDraw().get("nxt");
        if(maxDraw < presentDraw){
            for(int i = maxDraw+1;i<presentDraw;i++ ){
                NumSet ns = lotteryService.callWinNumApi(i);
                lotteryService.insertDrawHistory(ns);
            }
        }
    }

    @Scheduled(cron = "10 10 21 * * SAT")
//    @Scheduled(cron = "10 * * * * mon")
    private void getTargetDraw(){
        // TODO: 2020/08/20
        // 1. get last draw and present draw num
        // 2. get draw result and insert

        int presentDraw = lotteryService.getPresentDraw().get("present");
        NumSet thisWeekPickNumSet = lotteryService.getDrawNumSet(presentDraw);
        List<NumSet> thisWeekSuggestionNumSet = lotteryService.getSuggestionNumSet(presentDraw);

        for(NumSet ns :thisWeekSuggestionNumSet){
            int place = lotteryService.winChecker(thisWeekPickNumSet,ns);
            if(place <= 5){
                ns.setPlace(place);
                ns.setDraw(presentDraw);
                if(place == 1){
                    ns.setPrize(ns.getFifthPrize());
                }else if(place == 2){
                    ns.setPrize(ns.getSecondPrize());
                }else if(place == 3){
                    ns.setPrize(ns.getThirdPrize());
                }else if(place == 4){
                    ns.setPrize(ns.getFourthPrize());
                }else{
                    ns.setPrize("5000");
                }
                lotteryService.insertWeeklyDrawResult(ns);
            }
        }


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