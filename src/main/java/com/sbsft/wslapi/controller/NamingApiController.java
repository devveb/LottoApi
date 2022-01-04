package com.sbsft.wslapi.controller;

import com.sbsft.wslapi.service.AppService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/api")
public class NamingApiController {

    private final AppService appService;

    public NamingApiController(AppService appService) {
        this.appService = appService;
    }
    /*
    * Controller for Naming slot machine(Wordman:wordman.kickit.top)
    * */

    @CrossOrigin
    @PostMapping("/save")
    @ResponseBody
    public int save(HttpServletRequest req) {
        //save word slot combination
        return appService.save(req);
    }

    @CrossOrigin
    @GetMapping("/list")
    @ResponseBody
    public List<Map<String,Object>> list() {
        // recent list of words
        return appService.list();
    }




}
