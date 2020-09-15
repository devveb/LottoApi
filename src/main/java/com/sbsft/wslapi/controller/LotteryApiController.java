package com.sbsft.wslapi.controller;

import com.sbsft.wslapi.model.DreamStory;
import com.sbsft.wslapi.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/lotto")
public class LotteryApiController {

    @Autowired LotteryService lotteryService;


    @CrossOrigin
    @PostMapping("/wtn")
    @ResponseBody
    public String storyToNum(@RequestParam String story,@RequestParam int iss) {
        return lotteryService.getWinNumbers(story,iss);
    }

    /**
     * Dream to Number result list
     * @return
     */
    @CrossOrigin
    @PostMapping("/rl")
    @ResponseBody
    public List<DreamStory> recentList(@RequestParam("p") int page) {
        return lotteryService.dlist(page);
    }

    /**
     * Dream to Number result list and detail modal
     * @return
     */
    @CrossOrigin
    @PostMapping("/lm")
    @ResponseBody
    public String recentListAndModal(@RequestParam("p") int page) {

        return lotteryService.getListAndModal(page);
    }


    /**
     * Dream to Number result list and detail modal
     * @return
     */
    @CrossOrigin
    @PostMapping("/nd")
    @ResponseBody
    public String getSuggestionNumberDetail(@RequestParam("nid") int nid) {
        return lotteryService.getSuggestionNumberDetailHtml(nid);
    }


    /**
     * Dream to Number result list and detail modal
     * @return
     */
    @CrossOrigin
    @PostMapping("/sd")
    @ResponseBody
    public String getSuggestionStoryDetail(@RequestParam("sid") int sid) {
        return lotteryService.getSuggestionStoryDetail(sid);
    }

    /**
     * Dream to Number result list and detail modal
     * @return
     */
    @CrossOrigin
    @PostMapping("/rp")
    @ResponseBody
    public String postReply(@RequestParam("t") String type,@RequestParam("i") int idx,@RequestParam("s") String story) {
        return lotteryService.postNumberSuggestionReply(type,idx,story);
    }


    /**
     * Dream to Number result list and detail modal
     * @return
     */
    @CrossOrigin
    @PostMapping("/drp")
    @ResponseBody
    public String getSuggestionReply(@RequestParam("t") String type,@RequestParam("i") int idx) {
        return lotteryService.getReplyById(type,idx);
    }

    /**
     * Dream to Number result list and detail modal
     * @return
     */
    @CrossOrigin
    @PostMapping("/ch")
    @ResponseBody
    public String getWinHistoryAndNumberValue(@RequestParam("r") String result) {
        return lotteryService.getWinHistoryAndNumberValue(result);
    }

    /**
     * Dream to Number result list and detail modal
     * @return
     */
    @CrossOrigin
    @PostMapping("/dh")
    @ResponseBody
    public String getDrawHistoryHtml(@RequestParam("d") int draw) {
        return lotteryService.getDrawHistoryHtml(draw);
    }


}
