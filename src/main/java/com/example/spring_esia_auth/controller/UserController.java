package com.example.spring_esia_auth.controller;

import com.example.spring_esia_auth.entity.User;
import com.example.spring_esia_auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/sign-in")
    String signIn() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    String signUp() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(User user) {
        userService.signUp(user);
        return "redirect:/sign-in";
    }
}
