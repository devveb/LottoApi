package com.sbsft.wslapi.service;

import com.sbsft.wslapi.mapper.LotteryMapper;
import com.sbsft.wslapi.model.NumSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class LotteryService {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    private LotteryMapper lotteryMapper;



    private Map getDrawNumberBasedOnStandardDate() {
        String standardDate = "2002-12-07 23:59:59";
        long nextEpi = 0;
        long presentEpi = 0;
        Map<String,Object> map = new HashMap<>();

        try {

            Date cDate = new Date();
            Date sDate = dateFormat.parse(standardDate);
            long diff = cDate.getTime() - sDate.getTime();


            nextEpi = (diff / (86400 * 1000 * 7))+2;
            presentEpi = nextEpi -1;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        map.put("nxt",nextEpi);
        map.put("present",presentEpi);
        return map;

    }

    private NumSet callWinNumApi(int nextEpisode) throws Exception {

        NumSet ns = new NumSet();
        Document google1 = Jsoup.connect("https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo="+nextEpisode).get();
        Elements eles = google1.select(".ball_645");

        ns.setDraw(nextEpisode);
        ns.setFirst(eles.get(0).text());
        ns.setSecond(eles.get(1).text());
        ns.setThird(eles.get(2).text());
        ns.setFourth(eles.get(3).text());
        ns.setFifth(eles.get(4).text());
        ns.setSixth(eles.get(5).text());
        ns.setBonus(eles.get(6).text());

        return ns;
    }

    private void insertWinningNumbers(NumSet numSet){
        lotteryMapper.insertWinningNumbers(numSet);
    }
}



	
