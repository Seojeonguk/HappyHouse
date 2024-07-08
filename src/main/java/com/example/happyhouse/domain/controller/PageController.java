package com.example.happyhouse.domain.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/")
    public String home(HttpServletRequest request) {
        return "index";
    }

    @GetMapping(value = "/index")
    public String index(HttpServletRequest request) {
        return "index";
    }
}
