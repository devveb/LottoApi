package com.sbsft.wslapi.controller;

import com.sbsft.wslapi.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LotteryFrontController {

    @Autowired
    LotteryService lotteryService;

    @GetMapping("/")
    String indexPage(Model model, Map<String,Object> req){
        req.put("listType",1);

        model.addAttribute("list",lotteryService.getList(req));
        return "views/index";
    }

    @RequestMapping("/demo")
    String demoPage(Model model){
        return "demo";
    }
}
