package com.example.autopartsweb.controller;

import com.example.autopartscommon.entity.User;
import com.example.autopartsweb.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;



    @GetMapping("/register")
    public String registerPage() {
        log.info("registration page opened");
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        if (user != null) {
            userService.save(user);
        }
        return "redirect:/";
    }

    @GetMapping("/verify")
    private String verifyUser(@RequestParam("email") String email,
                              @RequestParam("token") String token) {
        userService.verifyUser(email, token);
        return "redirect:/";
    }

}
