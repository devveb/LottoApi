package com.sbsft.wslapi.utils;

import com.sbsft.wslapi.domain.NumberSet;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DtnUtil {

    public int getEpisode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int weeks=0;
        int targetEpi=0,lastEpi=0;
        try {
            Date initDate = sdf.parse("20021207");
            Date presentDate = new Date();
            long diff = presentDate.getTime() - initDate.getTime();
            int days = (int) (diff/(86400 * 1000));
            weeks = days/7;
            lastEpi = weeks +1;
            targetEpi = weeks+2;

            Calendar c = Calendar.getInstance();

            c.setTime(presentDate);
            int dayofweek = c.get(Calendar.DAY_OF_WEEK);
            int hourofDay = c.get(Calendar.HOUR);

            if(dayofweek == 7 && hourofDay >= 20){
                targetEpi =+1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return targetEpi;

    }

    public String textCheck(NumberSet dream) {
        return dream.getStory().replaceAll("\n", " ").replaceAll("(?m)^[ \t]*\r?\n", " ").replaceAll("\\s+",
                " ").replaceAll("[가|을|를|이|들|은|에|와|과|까|고|서|는|서]\\s", " ");
    }
}
