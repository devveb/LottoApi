package com.sbsft.wslapi.service;

import com.sbsft.wslapi.mapper.LotteryMapper;
import com.sbsft.wslapi.model.DreamStory;
import com.sbsft.wslapi.model.NumSet;
import com.sbsft.wslapi.utils.DtnUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class LotteryService {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    private LotteryMapper lotteryMapper;
    @Autowired
    private DtnUtil dtnUtil;

    public String getWinNumbers(String story, int iss) {
//        Map map;
//        map = new HashMap<String,Object>();


        DreamStory ds = new DreamStory();


        String result = lotteryMapper.selectDreamNumber(dtnUtil.textCheck(story));

        ds.setStory(story);
        ds.setResult(result);
        ds.setIss(iss);

        int numsetCnt = lotteryMapper.countNumberSet(ds);

        if (numsetCnt < 1) {
            lotteryMapper.insertNumberSet(ds);
        } else {
            lotteryMapper.updateSuggestionCount(ds);
        }

        NumSet ns = lotteryMapper.getNumberSet(ds);
        ds.setSnid(ns.getIdx());
        ds.setSugDate(ns.getSugDate());
        lotteryMapper.insertDreamResult(ds);
        checkHistory(ds);
        return result;
    }

    public List<String> checkHistory(DreamStory ds) {
        NumSet dreamnum = new NumSet();
        int totalPrize = 0;
        List<String> resultStrs = new ArrayList<>();

        String[] arr = ds.getResult().split(",");
        dreamnum.setFirst(arr[0]);
        dreamnum.setSecond(arr[1]);
        dreamnum.setThird(arr[2]);
        dreamnum.setFourth(arr[3]);
        dreamnum.setFifth(arr[4]);
        dreamnum.setSixth(arr[5]);
        dreamnum.setIdx(ds.getSnid());

        List<NumSet> historyNums = lotteryMapper.getDrawHistory();

        for (NumSet historyNum : historyNums) {
            int place = winChecker(historyNum, dreamnum);
            if (place <= 3) {
                if (place == 1) {
                    totalPrize += Integer.parseInt(historyNum.getFirstPrize());
                } else if (place == 2) {
                    totalPrize += Integer.parseInt(historyNum.getSecondPrize());
                } else {
                    totalPrize += Integer.parseInt(historyNum.getThirdPrize());
                }

                dreamnum.setPlace(place);
                dreamnum.setDraw(historyNum.getDraw());
                dreamnum.setDrawDate(historyNum.getDrawDate());
                dreamnum.setSugDate(ds.getRegDate());
                lotteryMapper.insertDrawSimulation(dreamnum);
            }
        }
        NumSet ns = new NumSet();
        //ns.setIdx(dreamnum.getIdx());
        ns.setIdx(ds.getSnid());
        ns.setTotalPrize(totalPrize);
        //lotteryMapper.insertNumberCombiTotalPrize(ns);
        lotteryMapper.updateNumberCombiTotalPrize(ns);

        return resultStrs;
    }

    public List<NumSet> getDrawHistory() {
        return lotteryMapper.getDrawHistory();
    }

    public int winChecker(NumSet winnum, NumSet dreamnum) {


        int result = 0;
        List<Integer> winnumsarr = new ArrayList<Integer>(Arrays.asList(new Integer[]{Integer.valueOf(winnum.getFirst()),
                Integer.valueOf(winnum.getSecond()), Integer.valueOf(winnum.getThird()), Integer.valueOf(winnum.getFourth()),
                Integer.valueOf(winnum.getFifth()), Integer.valueOf(winnum.getSixth())}));

        List<Integer> bnumsarr = new ArrayList<Integer>(
                Arrays.asList(new Integer[]{Integer.valueOf(winnum.getBonus())}));

        List<Integer> dreamnumsarr = new ArrayList<Integer>(Arrays.asList(new Integer[]{Integer.valueOf(dreamnum.getFirst()),
                Integer.valueOf(dreamnum.getSecond()), Integer.valueOf(dreamnum.getThird()), Integer.valueOf(dreamnum.getFourth()),
                Integer.valueOf(dreamnum.getFifth()), Integer.valueOf(dreamnum.getSixth())}));
        dreamnumsarr.removeAll(winnumsarr);
        if (dreamnumsarr.size() == 0) {
            result = 1;
        }
        if (dreamnumsarr.size() == 1) {
            dreamnumsarr.removeAll(bnumsarr);
            if (dreamnumsarr.size() == 0) {
                result = 2;
            }
            result = 3;
        }
        if (dreamnumsarr.size() == 2) {
            result = 4;
        }
        if (dreamnumsarr.size() == 3) {
            result = 5;
        }
        if (dreamnumsarr.size() == 4) {
            result = 6;
        }

        if (dreamnumsarr.size() == 5) {
            result = 7;
        }

        if (dreamnumsarr.size() == 6) {
            result = 8;
        }
        return result;

    }

    public List<DreamStory> dlist(int page) {
        return lotteryMapper.selectDreamResultList(page);
    }


    public String getListAndModal(int page) {
        List<DreamStory> dreamList = lotteryMapper.selectDreamResultList(page);
        String historyHtml = "";
        int idx = 1;
        for (DreamStory ds : dreamList) {
            historyHtml = historyHtml + "<div class=\"m-timeline-3__item m-timeline-3__item--info\">" +
                    "<span class=\"m-timeline-3__item-time\">" + ds.getTimer() + "</span>" + "<div class=\"m-timeline-3__item-desc\">"
                    + "<span class=\"m-timeline-3__item-text\">" + ds.getStory().replaceAll("<","&lt;").replaceAll(">","&gt;")+ "</span>" + "<br>"
                    + "<span class=\"m-timeline-3__item-user-name\">"
                    + "<a href=\"#\" data-toggle=\"modal\" data-target=\"#m_modal_"+String.valueOf(idx)+"\"  onclick=\"javascript:getModalInfo("+idx+","+ds.getSnid()+");\">" + ds.getResult() + "</a>"
                    + "</span>" + "</div>" + "</div>"
                    +makeModal(ds.getResult(),String.valueOf(idx));
                    idx++;
        }
        return historyHtml;
    }

    public String makeModal(String title,String id){
        return "<div class=\"modal fade\" id=\"m_modal_"+id+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" style=\"display: none;\" aria-hidden=\"true\">\n" +
                "\t\t\t\t\t\t\t<div class=\"modal-dialog\" role=\"document\">\n" +
                "\t\t\t\t\t\t\t\t<div class=\"modal-content\">\n" +
                "\t\t\t\t\t\t\t\t\t<div class=\"modal-header\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<h5 class=\"modal-title\" id=\"exampleModalLabel\">\n" +
                 title+
                "\t\t\t\t\t\t\t\t\t\t</h5>\n" +
                "\t\t\t\t\t\t\t\t\t\t<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<span aria-hidden=\"true\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t×\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</span>\n" +
                "\t\t\t\t\t\t\t\t\t\t</button>\n" +
                "\t\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t\t\t<div class=\"modal-body\">\n" +
                "\t\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t\t\t<div class=\"modal-footer\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\tClose\n" +
                "\t\t\t\t\t\t\t\t\t\t</button>\n" +
                "\t\t\t\t\t\t\t\t\t\t<button type=\"button\" class=\"btn btn-primary\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\tSave changes\n" +
                "\t\t\t\t\t\t\t\t\t\t</button>\n" +
                "\t\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t</div>";
    }


    public Map getDrawNumberBasedOnStandardDate() {
        String standardDate = "2002-12-07 23:59:59";
        long nextEpi = 0;
        long presentEpi = 0;
        Map<String, Object> map = new HashMap<>();

        try {

            Date cDate = new Date();
            Date sDate = dateFormat.parse(standardDate);
            long diff = cDate.getTime() - sDate.getTime();


            nextEpi = (diff / (86400 * 1000 * 7)) + 2;
            presentEpi = nextEpi - 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        map.put("nxt", nextEpi);
        map.put("present", presentEpi);
        return map;

    }

    public NumSet callWinNumApi(int nextEpisode) {
        NumSet ns = new NumSet();

        try {

            Document google1 = Jsoup.connect("https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=" + nextEpisode).get();
            String targetText = google1.select(".win_result > .desc").text();
            ns.setDrawDate(targetText.substring(1, targetText.indexOf("일")).replace(" ", "").replaceAll("[^0-9]", "-"));

            Elements eles = google1.select(".ball_645");

            ns.setDraw(nextEpisode);
            ns.setFirst(eles.get(0).text());
            ns.setSecond(eles.get(1).text());
            ns.setThird(eles.get(2).text());
            ns.setFourth(eles.get(3).text());
            ns.setFifth(eles.get(4).text());
            ns.setSixth(eles.get(5).text());
            ns.setBonus(eles.get(6).text());

            Elements prizeEles = google1.select(".tar");
            ns.setFirstPrize(prizeEles.eq(1).toString().replaceAll("[^0-9]", ""));
            ns.setSecondPrize(prizeEles.eq(3).toString().replaceAll("[^0-9]", ""));
            ns.setThirdPrize(prizeEles.eq(5).toString().replaceAll("[^0-9]", ""));
            ns.setFourthPrize(prizeEles.eq(7).toString().replaceAll("[^0-9]", ""));
            //ns.setFifthPrize(prizeEles.eq(9).toString().replaceAll("[^0-9]", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }


        return ns;
    }

    public int getMaxDraw() {
        return lotteryMapper.getMaxDraw();
    }

    public int getPresentDraw() {
        String standardDate = "2002-12-07 23:59:59";
        long nextEpi = 0;
        long presentEpi = 0;
        Map<String, Object> map = new HashMap<>();

        try {

            Date cDate = new Date();
            Date sDate = dateFormat.parse(standardDate);
            long diff = cDate.getTime() - sDate.getTime();


            nextEpi = (diff / (86400 * 1000 * 7)) + 2;
            presentEpi = nextEpi - 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        map.put("nxt", nextEpi);
        map.put("present", presentEpi);
        return Integer.parseInt(String.valueOf(presentEpi));

    }

    private void insertWinningNumbers(NumSet numSet) {
        lotteryMapper.insertWinningNumbers(numSet);
    }


    public void insertDrawHistory(NumSet ns) {
        lotteryMapper.insertDrawHistory(ns);
    }

    public List<DreamStory> getSuggestionList() {
        return lotteryMapper.selectDreamResultList(0);
    }

    public void insertDrawSimmulation(NumSet dreamnum) {
        lotteryMapper.insertDrawSimulation(dreamnum);
    }

    public List<DreamStory> getUnpackedSuggestionList() {
        return lotteryMapper.seleteUnpackedSuggestionList();
    }


    public String getSuggestionNumberDetailHtml(int snid) {
        DreamStory ds = new DreamStory();
        ds.setSnid(snid);
        NumSet ns = lotteryMapper.getNumberSetById(ds);
        List<DreamStory> history = lotteryMapper.getSimHistory(ds);

        String html = "<h6>추천횟수: "+ns.getSugCount()+"</h6>";



        if(history.size() > 0){
            html = html+"<div style=\"overflow:scroll;height:200px;\">";
            for(DreamStory dream : history){
                html=html+"<div class=\"m-timeline-3__item m-timeline-3__item--info\"><span class=\"m-timeline-3__item-time\">"+dream.getDraw()+"</span><div class=\"m-timeline-3__item-desc\"><span class=\"m-timeline-3__item-text\">"+dream.getPlace()+"</span><br></div></div>";
            }
            html=html+"</div>";
        }else{
            html=html+"<h6>당첨내역 없음</h6>";
        }



        html=html+"<h6>총 획득상금: "+ns.getTotalPrize()+"</h6>";

        return html;
    }
}



	
