package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
/*
    Контроллер отвечает за секьюрити: авторизация и регистрация
 */
@Controller
public class SecurityController {

    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(value = "error", required = false) Boolean error, Model model) {
        if (error != null) { // Если пользователь сделал что-то не так при попытке логина, мы получим параметр error
            model.addAttribute("msg", "Неверный email или пароль, либо такой аккаунт не существует");
        } // Добавляем аттрибут с сообщением об ошибке, перенаправляем пользователя обратно на login.html, но уже с аттрибутом
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "login"; // При логауте перекидываем пользователя на login.html
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.saveUser(user); // При регистрации сохраняем нового юзера в БД
        return "redirect:/login";
    }
}
