package com.somacode.celmybell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {
    @GetMapping("/login")
    public ModelAndView getLogin() {
        return new ModelAndView("login");
    }

    @GetMapping("/welcome")
    public ModelAndView getWelcome() {
        return new ModelAndView("welcome");
    }
}
