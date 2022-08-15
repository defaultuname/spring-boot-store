package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class SecurityController {
    private final static Logger logger = LoggerFactory.getLogger(SecurityController.class);
    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) Boolean error, Model model) {
        if (error != null) {
            logger.warn("Error during authorization");
            model.addAttribute("msg", "Invalid email address or password, or this account does not exist");
        }
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            logger.warn("Error during authorization");
            model.addAttribute("msg", "You are already logged in");
            return "error/500";
        }
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            logger.warn("Error during authorization");
            model.addAttribute("msg", "Вы уже авторизованы");
            return "error/500";
        }

        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error! " + bindingResult.getFieldError());
            return "register";
        }

        userService.saveUser(user);
        logger.info("Register new user with email {} to database", user.getEmail());
        return "redirect:/login";
    }
}
