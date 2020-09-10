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
    public String getSuggestionNumberDetail(@RequestParam("snid") int snid) {
        return lotteryService.getSuggestionNumberDetailHtml(snid);
    }

    /**
     * Dream to Number result list and detail modal
     * @return
     */
    @CrossOrigin
    @PostMapping("/nrp")
    @ResponseBody
    public String postNumberSuggestionReply(@RequestParam("i") int snid,@RequestParam("s") String story) {
        return lotteryService.postNumberSuggestionReply(snid,story);
    }


    /**
     * Dream to Number result list and detail modal
     * @return
     */
    @CrossOrigin
    @PostMapping("/drp")
    @ResponseBody
    public String getSuggestionReply(@RequestParam("i") int snid) {
        return lotteryService.getSuggestionNumberReplyById(snid);
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


}
