package com.sbsft.wslapi.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LotteryCron {

    @Scheduled(cron = "0 0 21 * * SAT")
    private void getWinningNumbers(){
        // TODO: 2020/08/20
        // 1. get last draw and present draw num
        // 2. get draw result and insert

    }


}
