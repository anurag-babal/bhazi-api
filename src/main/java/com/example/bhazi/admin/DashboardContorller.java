package com.example.bhazi.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardContorller {
    @GetMapping("")
    public String home() {
        return "index";
    }

    @GetMapping("/privacypolicy")
    public String privacyPolicy() {
        return "privacypolicy";
    }
}
