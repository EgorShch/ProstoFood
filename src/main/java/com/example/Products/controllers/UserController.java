package com.example.Products.controllers;

import com.example.Products.models.User;
import com.example.Products.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/registration")
    public String reg(){
        return "registration";
    }

    @GetMapping("/login")
    public String log(){
        return "login";
    }

    @GetMapping("/hello")
    public String bog(){
        return "hello";
    }

    public String createUser(@RequestBody User user, Model model){
        if (!userService.createUser(user)){
            model.addAttribute("errorMessage", "Пользоваетль с логином: " + user.getLogin() + " уже занят");
            return "registration";
        }
        return "redirect:/login";
    }


}
