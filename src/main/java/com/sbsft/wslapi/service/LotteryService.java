package com.sbsft.wslapi.service;

import com.sbsft.wslapi.mapper.LotteryMapper;
import com.sbsft.wslapi.model.DrawInfo;
import com.sbsft.wslapi.model.DreamStory;
import com.sbsft.wslapi.model.NumSet;
import com.sbsft.wslapi.model.Paging;
import com.sbsft.wslapi.utils.DtnUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    public String getWinNumbers(String story, int iss , int digit) {
        String result = null;
        DreamStory ds = new DreamStory();
        ds.setStory(dtnUtil.textCheck(story));
        ds.setDigit(digit);
        try{
            result = lotteryMapper.selectDreamNumber(ds);

            ds.setStory(story);
            ds.setResult(result);
            ds.setIss(iss);

            getNumberSet(ds);
            ds.setTargetDraw((long) this.getPresentDraw().get("nxt"));
            lotteryMapper.insertDreamResult(ds);
            checkHistory(ds);
        }catch (Exception e){
            e.printStackTrace();
        }


        return result;
    }

    public String getWinHistoryAndNumberValue(String result) {
        String html ="";
        DreamStory ds = new DreamStory();
        ds.setResult(result);
        getNumberSet(ds);
        checkHistory(ds);

        List<DreamStory> history = lotteryMapper.getSimHistory(ds);
        NumSet ns = lotteryMapper.getNumberSetById(ds);

        html = html+"<div class=\"m-demo__preview\">\n" +
                "<div class=\"m-stack m-stack--hor m-stack--general m-stack--demo\" style=\"height: 20px\">\n" +
                "<div class=\"m-stack__item m-stack__item--center\">\n" +
                "<div class=\"m-stack__demo-item\">\n" +
                ns.getNumberUnit()+
                "</div>\n" +
                "</div>\n" +
                "</div>"+

                "<div class=\"m--space-10\"></div>\n" +
                "<div class=\"m-stack m-stack--ver m-stack--tablet m-stack--demo\">\n" +
                "<div class=\"m-stack__item m-stack__item--center m-stack__item--top\">\n" +
                "<h6>추천횟수: "+ns.getSugCount()+"</h6>" +
                "</div>\n" +
                "<div class=\"m-stack__item m-stack__item--center m-stack__item--middle\">\n";

        html = getHistoryHtmlString(html, history, ns);


        return html;
    }

    private String getHistoryHtmlString(String html, List<DreamStory> history, NumSet ns) {
        if(history.size() > 0){
            html = html+"<div class=\"m-list-timeline\" style=\"overflow:scroll;height:100px;\">";
            html = html+"<div class=\"m-list-timeline__items\">";
            for(DreamStory dr : history){
                html=html + "<div class=\"m-list-timeline__item\">\n" +
                        "<span class=\"m-list-timeline__badge\"></span>\n" +
                        "<span class=\"m-list-timeline__time\">\n" +
                        dr.getDraw()+"회</br>"+dr.getDrawDate()+
                        "</span>\n" +
                        "<span class=\"m-list-timeline__badge\"></span>\n" +
                        "<span class=\"m-list-timeline__text\" style\"font-size:22px;\">\n" +
                        dr.getPlace()+"등 당첨 상금 : "+getPrize(dr)+
                        "</span>\n" +
                        "</div>";;
            }
            html=html+"</div></div>";
        }else{
            html=html+"<h6>당첨내역 없음</h6>";
        }

        html=html+"</div>\n" +
                "<div class=\"m-stack__item m-stack__item--center m-stack__item--bottom\">\n" +
                "<h6>총 획득상금: "+
                String.format("%,d",ns.getTotalPrize())+"원</h6>" +
                "</div>\n" +
                "</div>\n" +
                "</div>";
        return html;
    }

    private String getPrize(DreamStory dr) {
        String rt ="";
        if(dr.getPlace() == 1){
            rt= String.format("%,d",dr.getFirstPrizeInt())+"원";
        }else if(dr.getPlace() == 2){
            rt= String.format("%,d",dr.getSecondPrizeInt())+"원";
        }else if(dr.getPlace() == 3){
            rt= String.format("%,d",dr.getThirdPrizeInt())+"원";
        }else if(dr.getPlace() == 4){
            rt= String.format("%,d",dr.getFourthPrizeInt())+"원";
        }else if(dr.getPlace() == 5){
            rt= String.format("%,d",5000)+"원";
        }
        return rt;
    }

    private void getNumberSet(DreamStory ds) {
        int numsetCnt = lotteryMapper.countNumberSet(ds);

        if (numsetCnt < 1) lotteryMapper.insertNumberSet(ds);
        else {
            lotteryMapper.updateSuggestionCount(ds);
        }
        NumSet ns = lotteryMapper.getNumberSet(ds);
        ds.setSnid(ns.getIdx());
        ds.setSugDate(ns.getSugDate());
    }

    public void checkHistory(DreamStory ds) {
        NumSet dreamnum = new NumSet();
        long totalPrize = 0;
        List<String> resultStrs = new ArrayList<>();

        stringToNumSet(ds, dreamnum);
        dreamnum.setIdx(ds.getSnid());

        List<NumSet> historyNums = lotteryMapper.getDrawHistory(dreamnum);

        for (NumSet historyNum : historyNums) {
            int place = winChecker(historyNum, dreamnum);
            if (place <= 5) {
                if (place == 1) {
                    totalPrize += Long.parseLong(historyNum.getFirstPrize());
                } else if (place == 2) {
                    totalPrize += Long.parseLong(historyNum.getSecondPrize());
                } else if(place == 3){
                    totalPrize += Long.parseLong(historyNum.getThirdPrize());
                }else if(place == 4) {
                    totalPrize += Long.parseLong(historyNum.getFourthPrize());
                }else if(place == 5){
                    totalPrize += 5000;
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
                    + "<span class=\"m-timeline-3__item-text\">"
                    + "<a href=\"#\" data-toggle=\"modal\" data-target=\"#s_modal_"+String.valueOf(ds.getIdx())+"\"  onclick=\"javascript:getSModalInfo("+ds.getIdx()+");\">" + ds.getStory().replaceAll("<","&lt;")+"&nbsp;["+getReplyCnt("s",ds.getIdx()) + "]</a>"
                    +  "</span><br>"
                    + "<span class=\"m-timeline-3__item-user-name\">"
                    + "<a href=\"#\" data-toggle=\"modal\" data-target=\"#n_modal_"+String.valueOf(ds.getSnid())+"\"  onclick=\"javascript:getModalInfo("+ds.getSnid()+");\">" + ds.getResult() +"&nbsp;["+getReplyCnt("n",ds.getSnid()) + "]</a>"
                    + "</span>" + "</div>" + "</div>"
                    +makeModal("n",ds.getResult(),String.valueOf(ds.getSnid()))
                    +makeModal("s",ds.getStory(),String.valueOf(ds.getIdx()));
                    idx++;
        }
        return historyHtml;
    }

    private String getReplyCnt(String type, int idx) {
        DreamStory ds = new DreamStory();
        ds.setType(type);
        ds.setIdx(idx);
        int cnt = lotteryMapper.getReplyCnt(ds);
        return String.valueOf(cnt);
    }

    public String makeModal(String type,String title,String id){
        return "<div class=\"modal fade\" id=\""+type+"_modal_"+id+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" style=\"display: none;\" aria-hidden=\"true\">\n" +
                "<div class=\"modal-dialog\" role=\"document\">\n" +
                "<div class=\"modal-content\">\n" +
                "<div class=\"modal-header\">\n" +
                "<h5 class=\"modal-title\" id=\"exampleModalLabel\">\n" +
                "토론방"+
                "</h5>\n" +
                "<button type=\"button\" class=\"close close-refresh\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                "<span aria-hidden=\"true\">\n" +
                "×\n" +
                "</span>\n" +
                "</button>\n" +
                "</div>\n" +
                "<div class=\"modal-body\">\n" +
                "</div>\n" +
                "<div class=\"modal-footer\">\n" +
                "<button type=\"button\" class=\"btn btn-secondary  close-refresh\" data-dismiss=\"modal\">\n" +
                "Close\n" +
                "</button>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>";
    }


//    public Map getDrawNumberBasedOnStandardDate() {
//        String standardDate = "2002-12-07 23:59:59";
//        long nextEpi = 0;
//        long presentEpi = 0;
//        Map<String, Long> map = new HashMap<>();
//
//        try{
//
//            Date cDate = new Date();
//            Date sDate = dateFormat.parse(standardDate);
//            long diff = cDate.getTime() - sDate.getTime();
//
//            nextEpi = (diff / (86400 * 1000 * 7)) + 2;
//            presentEpi = nextEpi - 1;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        map.put("nxt", nextEpi);
//        map.put("present", presentEpi);
//        return map;
//
//    }

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

    public Map<String, Integer> getPresentDraw() {

        String standardDate = "2002-12-07 23:59:59";
        int nextEpi = 0;
        int presentEpi = 0;
        Map<String, Integer> map = new HashMap<>();

        try {

            Date cDate = new Date();
            Date sDate = dateFormat.parse(standardDate);
            long diff = cDate.getTime() - sDate.getTime();


            nextEpi = (int) ((diff / (86400 * 1000 * 7)) + 2);
            presentEpi = nextEpi - 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        map.put("nxt", nextEpi);
        map.put("present", presentEpi);
        return map;
//        return Integer.parseInt(String.valueOf(presentEpi));

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

    public String getReplyById(String type,int idx){

        DreamStory ds = new DreamStory();
        ds.setIdx(idx);
        ds.setType(type);

        List<DreamStory> replyList = lotteryMapper.getReplyById(ds);

        String html ="<div class=\"m-section\">\n" +
                "<div class=\"m-section__content\">\n" +
                "<!--begin::Preview-->\n" +
                "<div class=\"m-demo\">\n" +
                "<div class=\"m-demo__preview\">\n" +
                "<div class=\"m-list-timeline\">\n";



        if(replyList.size() > 0){
            for(DreamStory drs : replyList){
                html=html+"<div class=\"m-list-timeline__items\">\n" +
                        "<div class=\"m-list-timeline__item\">\n" +
                        "<span class=\"m-list-timeline__badge\"></span>\n" +
                        "<span class=\"m-list-timeline__text\">\n" +
                        drs.getStory()+
                        "</span>\n" +
                        "<span class=\"m-list-timeline__time\">\n" +
                        drs.getTimer()+
                        "</span>\n" +
                        "</div>\n" +
                        "</div>\n";
            }
        }else{
            html=html+"<div class=\"m-list-timeline__items\">\n" +
                    "<div class=\"m-list-timeline__item\">\n" +
                    "<span class=\"m-list-timeline__badge\"></span>\n" +
                    "<span class=\"m-list-timeline__text\">\n" +
                    "no reply"+
                    "</span>\n" +
                    "</div>\n" +
                    "</div>\n";
        }
        html=html+"</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--end::Preview-->\n" +
                "</div>\n" +
                "</div>";
        return html;
    }


    public String getSuggestionStoryDetail(int sid) {
        DreamStory ds = lotteryMapper.getSuggestionById(sid);

        String html = "";
        html = html+"<div class=\"m-demo__preview\">\n" +

                "<div class=\"m-stack m-stack--hor m-stack--general m-stack--demo\" style=\"height: 20px\">\n" +
                "<div class=\"m-stack__item m-stack__item--center\">\n" +
                "<div class=\"m-stack__demo-item\">\n" +
                ds.getStory()+
                "</div>\n" +
                "</div>\n" +
                "</div>"+
                "<div class=\"form-group m-form__group\" style=\"margin-top:33px;\">\n" +
                "<div class=\"input-group\">\n" +
                "<input type=\"text\" class=\"form-control\" id=\"reptxt\" placeholder=\"Reply...\">\n" +
                "<div class=\"input-group-append\">\n" +
                "<button class=\"btn btn-secondary\" style=\"background-color:darkblue;\" type=\"button\" onclick=\"nrep('s',"+ds.getIdx()+")\">\n" +
                "<i class=\"flaticon-edit-1\" style=\"color:white;\"></i>" +
                "</button>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>";

        html=html+"<div class=\"m-section\" id=\"drep\" style=\"height:200px; overflow:scroll; margin-top:33px;\">\n"+getReplyById("s",ds.getIdx());

        return html;
    }


    public String getSuggestionNumberDetailHtml(int nid) {
        //DreamStory ds = lotteryMapper.getSuggestionById(nid);
        //ds.setnid(nid);
        DreamStory ds = new DreamStory();
        ds.setSnid(nid);
        NumSet ns = lotteryMapper.getNumberSetById(ds);
        List<DreamStory> history = lotteryMapper.getSimHistory(ds);

        String html = "";
        html = html+"<div class=\"m-demo__preview\">\n" +

                "<div class=\"m-stack m-stack--hor m-stack--general m-stack--demo\" style=\"height: 20px\">\n" +
                "<div class=\"m-stack__item m-stack__item--center\">\n" +
                "<div class=\"m-stack__demo-item\">\n" +
                ns.getNumberUnit()+
                "</div>\n" +
                "</div>\n" +
                "</div>"+

                "<div class=\"m--space-10\"></div>\n" +
                "<div class=\"m-stack m-stack--ver m-stack--tablet m-stack--demo\">\n" +
                "<div class=\"m-stack__item m-stack__item--center m-stack__item--top\">\n" +
                "<h6>추천횟수: "+ns.getSugCount()+"</h6>" +
                "</div>\n" +
                "<div class=\"m-stack__item m-stack__item--center m-stack__item--middle\">\n";

        html = getHistoryHtmlString(html, history, ns);

        html=html+"<div class=\"form-group m-form__group\" style=\"margin-top:33px;\">\n" +
                        "<div class=\"input-group\">\n" +
                        "<input type=\"text\" class=\"form-control\" id=\"reptxt\" placeholder=\"Reply...\">\n" +
                        "<div class=\"input-group-append\">\n" +
                        "<button class=\"btn btn-secondary\" style=\"background-color:darkblue;\" type=\"button\" onclick=\"nrep('n',"+ds.getSnid()+")\">\n" +
                        "<i class=\"flaticon-edit-1\" style=\"color:white;\"></i>" +
                        "</button>\n" +
                        "</div>\n" +
                        "</div>\n" +
                        "</div>";

                        html=html+"<div class=\"m-section\" id=\"drep\" style=\"height:200px; overflow:scroll; margin-top:33px;\">\n"+getReplyById("n",ds.getSnid());



        return html;
    }

    public String postNumberSuggestionReply(String type,int idx, String story) {
        DreamStory ds = new DreamStory();
        String code ="200";
        try{
            ds.setType(type);
            ds.setStory(story.replaceAll("<","&lt;"));
            ds.setIdx(idx);

            lotteryMapper.postReply(ds);
        }catch (Exception e){
            code="999";
        }
        return code;


    }


    public String getDrawHistoryHtml(int draw) {
        this.getWeeklyWinningNumbers();
        String html ="";
        NumSet ns = new NumSet();
        ns.setDraw(draw);
        List<NumSet> drawHistory = lotteryMapper.getDrawHistory(ns);


        for(NumSet dr : drawHistory){
            html = html + "<div class=\"m-list-timeline__item\">\n" +
                    "<span class=\"m-list-timeline__badge\"></span>\n" +
                    "<span class=\"m-list-timeline__time\">\n" +
                    dr.getDraw()+"회</br>"+dr.getDrawDate()+
                    "</span>\n" +
                    "<span class=\"m-list-timeline__badge\"></span>\n" +
                    "<span class=\"m-list-timeline__text\" style\"font-size:22px;\">\n" +
                    dr.getFirst()+" , "+dr.getSecond()+" , "+dr.getThird()+" , "+dr.getFourth()+" , "+dr.getFifth()+" , "+dr.getSixth()+" + 보너스: "+dr.getBonus()+
                    "</span>\n" +
                    "</div>";
        }
        return html;
    }

    public NumSet getDrawNumSet(int presentDraw) {
        return lotteryMapper.getDrawNumSet(presentDraw);
    }

    public List<NumSet> getSuggestionNumSet(int presentDraw) {

        List<NumSet> list = new ArrayList<>();
        List<DreamStory> dsList = lotteryMapper.getTargetDrawSuggestion(presentDraw);

        for(DreamStory ds:dsList){
            NumSet ns = new NumSet();
            stringToNumSet(ds, ns);
            ns.setIdx(ds.getSnid());
            list.add(ns);
        }

        return list;

    }

    private void stringToNumSet(DreamStory ds, NumSet ns) {
        String[] digit = ds.getResult().split(",");
        ns.setFirst(digit[0]);
        ns.setSecond(digit[1]);
        ns.setThird(digit[2]);
        ns.setFourth(digit[3]);
        ns.setFifth(digit[4]);
        ns.setSixth(digit[5]);
    }

    public void insertWeeklyDrawResult(NumSet ns) {
        lotteryMapper.insertWeeklyDrawResult(ns);
    }

    public String getWeeklyResult(int draw) {


        if(draw == 0){
            draw = this.getMaxDraw();
        }

        int maxDraw = lotteryMapper.getMaxDraw();
        this.getWeeklyWinResult(draw);
        NumSet ns = lotteryMapper.getDrawNumSet(draw);
        List<DreamStory> weeklyResult = lotteryMapper.getWeeklyResult(ns);

        String html ="<div class=\"m-portlet\">\n" +
                "<div class=\"m-portlet__head\">\n" +
                "<div class=\"m-portlet__head-caption\">\n" +
                "<div class=\"m-portlet__head-title\" style=\"width:110%;\">\n" +
                "<h3 class=\"m-portlet__head-text\">\n" +
                ns.getDraw()+"회 추첨일: "+ns.getDrawDate()+
                "</h3>\n" +
                "<div style=\"margin-top:18px; margin-right:30px; float:right;\">\n" +
                "<select class=\"form-control\" id=\"wkld\" style=\"width: 60px;display: inline; margin-left:15px;\">\n";
                for(int i = maxDraw; 1 <= i; i--){
                    html=html+"<option value=\""+i+"\"";
                    if(i == draw) html=html+"selected=\"selected\"";
                    html=html+">" +i+ "</option>\n";
                }

               html = html+
                "</select>\n" +
                "<button type=\"reset\" class=\"btn btn-primary\" onclick=\"javscript:wkll();\">\n" +
                "\t조회\n" +
                "</button>\n" +
                "</div>"+
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"m-portlet__body\">\n" +
                "<div class=\"m-section\">\n" +

                        "<div class=\"m-section__content\">\n" +
                        "<table class=\"table\">\n" +
                        "<thead>\n" +
                        "<tr>\n" +
                        "<th>\n" +
                        "#\n" +
                        "</th>\n" +
                        "<th>\n" +
                        "\tNumber\n" +
                        "</th>\n" +
                        "<th>\n" +
                        "\tPrize\n" +
                        "</th>\n" +
                        "</tr>\n" +
                        "</thead>\n" +
                        "<tbody>\n";
                        if(weeklyResult.size() == 0) {
                            html = html + "<tr><td colspan=\"3\" style=\"text-align:center;\">당첨결과가 없습니다.</td></tr>";
                        }else{
                            for(DreamStory result:weeklyResult){
                                html=html+"<tr><td>"+result.getPlace()+"등</td><td>"+result.getResult()+"</td><td>"+result.getPrize()+"원</td></tr>";
                            }
                        }

                        html = html+"</tbody>\n" +
                        "</table>\n" +
                        "</div>\n" +
                        "</div>"+
                "</div>\n" +
                "</div>";



        return  html;
    }

    public void getWeeklyWinningNumbers() {
        int maxDraw = this.getMaxDraw(); /* get latest draw from history */
        int presentDraw = this.getPresentDraw().get("nxt");
        if(maxDraw < presentDraw){
            for(int i = maxDraw+1;i<presentDraw;i++ ){
                NumSet ns = this.callWinNumApi(i);
                this.insertDrawHistory(ns);
            }
        }
    }

    public void getWeeklyWinResult(int draw) {

        int presentDraw=0;

            if(draw == 0){
                presentDraw = getPresentDraw().get("present");
            }else{
                presentDraw = draw;
            }

            //이번 주 당첨 번호
            NumSet thisWeekPickNumSet = getDrawNumSet(presentDraw);
            //이번 주에 추천된 번호들
            List<NumSet> thisWeekSuggestionNumSet = getSuggestionNumSet(presentDraw);

            for(NumSet ns : thisWeekSuggestionNumSet){

                int place = this.winChecker(thisWeekPickNumSet,ns);
                ns.setPlace(place);
                ns.setDraw(presentDraw);

                if(place <= 5){
                    if(place == 1){
                        ns.setPrize(ns.getFifthPrize());
                    }else if(place == 2){
                        ns.setPrize(ns.getSecondPrize());
                    }else if(place == 3){
                        ns.setPrize(ns.getThirdPrize());
                    }else if(place == 4){
                        ns.setPrize(ns.getFourthPrize());
                    }else if(place == 5){
                        ns.setPrize("5000");
                    }

                }else{
                    ns.setPrize("0");
                }

                this.insertWeeklyDrawResult(ns);
                lotteryMapper.insertAutoReplyResult(ns);
            }

    }

    public void getPastWeeklyWinResult() {
        //기준 회차
        DrawInfo di = new DrawInfo();
        di.setDraw(getPresentDraw().get("present"));


        // 당첨확인 안한 회차 리스트
        List<DrawInfo> unIdentifiedEpi = lotteryMapper.getUnIdentifiedEpis(di);

        for(DrawInfo  draw : unIdentifiedEpi){
            getWeeklyWinResult(draw.getDraw());
        }
    }

    public List<DreamStory> getList(Map<String,Object> req) {
        Paging paging = new Paging(1,"2");
        List<DreamStory> list = lotteryMapper.getList(paging);
        return list;
    }
}



	
