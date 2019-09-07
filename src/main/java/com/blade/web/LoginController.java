package com.blade.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(){
        System.out.println("xxxxxxxxx");
        return "redirect:login.jsp";
    }

}
