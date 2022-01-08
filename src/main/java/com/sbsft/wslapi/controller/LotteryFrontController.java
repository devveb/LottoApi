package com.sbsft.wslapi.controller;

import com.sbsft.wslapi.model.DreamStory;
import com.sbsft.wslapi.model.Paging;
import com.sbsft.wslapi.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LotteryFrontController {

    @Autowired
    LotteryService lotteryService;

    @RequestMapping("/")
    String indexPage(Model model, @RequestParam int page){

        Paging paging = null;
        if(page == 0){
            paging = new Paging(1,30);
        }else{
            paging = new Paging(page,30);
        }
        model.addAttribute("list",lotteryService.getList(model,paging));
        model.addAttribute("paging",paging);

        return "views/index";
    }

    @RequestMapping(value = "/content")
    String contentPage(Model model,@RequestParam int page){
        Paging paging = null;
        if(page == 0){
            paging = new Paging(1,30);
        }else{
            paging = new Paging(page,30);
        }
        model.addAttribute("list",lotteryService.getList(model,paging));
        model.addAttribute("paging",paging);
        return "views/content";
    }

    @RequestMapping("/demo")
    String demoPage(Model model){
        return "demo";
    }
}


