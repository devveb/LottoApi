package com.sbsft.wslapi.controller;

import com.sbsft.wslapi.domain.NumberSet;
import com.sbsft.wslapi.service.AppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/api")
public class ApiController {

    private final AppService appService;

    public ApiController(AppService appService) {
        this.appService = appService;
    }


    @CrossOrigin
    @PostMapping("/save")
    @ResponseBody
    public int save(HttpServletRequest req) {
        return appService.save(req);
    }

    @CrossOrigin
    @GetMapping("/list")
    @ResponseBody
    public List<Map<String,Object>> list() {
        return appService.list();
    }
}
