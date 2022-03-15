package com.sbsft.wslapi.controller;

import com.sbsft.wslapi.model.DreamStory;
import com.sbsft.wslapi.model.Paging;
import com.sbsft.wslapi.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LotteryFrontController {

    @Autowired
    LotteryService lotteryService;

    @GetMapping("/")
    String indexPage(RedirectAttributes ra){


        ra.addAttribute("p",1);


        return "redirect:/main";
    }

    @RequestMapping("/main")
    String mainPage(Model model, @RequestParam HashMap param){

        Paging paging = null;
        int page = Integer.parseInt(param.get("p").toString());

        if(page == 0){
            paging = new Paging(1,30);
        }else{
            paging = new Paging(page,30);
        }
        lotteryService.getList(model,paging);
        model.addAttribute("paging",paging);

        return "views/index";
    }

    @PostMapping(value = "/content")
    String contentPage(Model model,@RequestParam HashMap param){

        Paging paging = new Paging(Integer.parseInt(param.get("currentPageNo").toString()),30);
        lotteryService.getList(model,paging);
        lotteryService.getContent(model,param);

        return "views/content";
    }

    @RequestMapping("/demo")
    String demoPage(Model model){
        return "views/demo";
    }
}


