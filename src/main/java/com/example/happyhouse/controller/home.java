package com.example.happyhouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class home {

    @RequestMapping("/")
    public String home() {
        return "index.html";
    }

    @RequestMapping("/login")
    public String login() {
        return "views/login.html";
    }
}
