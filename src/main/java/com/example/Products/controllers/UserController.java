package com.example.Products.controllers;

import com.example.Products.models.User;
import com.example.Products.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/registration")
    public String reg(){
        return "registration";
    }

    @GetMapping("/login")
    public String log(Principal principal){
        if (principal != null){
            return "redirect:/profile";
        }
        return "login";
    }


    @GetMapping("/hello")
    public String bog(){
        return "hello";
    }

    @PostMapping("/registration")
    public String createUser(User user, Model model){
        if (!userService.createUser(user)){
            model.addAttribute("errorMessage", "Пользоваетль с логином: " + user.getLogin() + " уже занят");
            return "registration";
        }
        return "redirect:/login";
    }

}
