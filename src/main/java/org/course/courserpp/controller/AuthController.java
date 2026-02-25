package org.course.courserpp.controller;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.Role;
import org.course.courserpp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("roles", Role.values());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String login,
                           @RequestParam String password,
                           @RequestParam Role role) {
        userService.register(login, password, role);
        return "redirect:/auth/login?registered";
    }
}
