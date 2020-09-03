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


}
