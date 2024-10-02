package com.example.projectx.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController extends UserModelController{

    @GetMapping("/")
    public String loginRedirect() {
        return "redirect:/userlogin";
    }

    @GetMapping("home")
    public String home(Model model) {
        addUserToModel(model);
        model.addAttribute("activeFunction", "home");
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
