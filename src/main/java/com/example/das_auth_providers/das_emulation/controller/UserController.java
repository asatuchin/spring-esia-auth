package com.example.das_auth_providers.das_emulation.controller;

import com.example.das_auth_providers.das_emulation.entity.domain.User;
import com.example.das_auth_providers.das_emulation.service.UserService;
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
