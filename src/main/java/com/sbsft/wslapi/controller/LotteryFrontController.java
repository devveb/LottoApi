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
    String mainPage(Model model, @RequestParam("p") int page){

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
    String contentPage(Model model,@ModelAttribute Paging p){

        Paging paging = new Paging(p.getCurrentPageNo(),15);
        paging.setIdx(p.getIdx());

        lotteryService.getContentReply(paging);
        lotteryService.getList(model,paging);

        return "views/content";
    }

    @RequestMapping("/demo")
    String demoPage(Model model){
        return "demo";
    }
}


