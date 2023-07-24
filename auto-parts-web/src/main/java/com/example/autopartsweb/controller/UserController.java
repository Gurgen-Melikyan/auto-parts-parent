package com.example.autopartsweb.controller;

import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.UserRepository;
import com.example.autopartsweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final UserRepository userRepository;


    @GetMapping("/register")
    public String registerPage() {
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
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            return "redirect:/";
        }
        if (byEmail.get().getEnabled()) {
            return "redirect:/";
        }
        if (byEmail.get().getToken().equals(token)) {
            User user = byEmail.get();
            user.setEnabled(true);
            user.setToken(null);
            userRepository.save(user);
        }
        return "redirect:/";
    }

//    @GetMapping("/admin")
//    public String adminPage() {
//        return "admin";
//    }
}
