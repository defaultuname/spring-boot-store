package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Role;
import com.store.springbootstoreex.domain.Status;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String createUser(Model model) {
        List<Role> roles = List.of(Role.values());
        List<Status> statuses = List.of(Status.values());

        model.addAttribute("roles", roles);
        model.addAttribute("statuses", statuses);

        return "createUser";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error! " + bindingResult.getFieldError());
            return "createUser";
        }

        userService.saveUser(user);
        logger.info("Register new user with email {} to database", user.getEmail());
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        List<Role> roles = List.of(Role.values());
        List<Status> statuses = List.of(Status.values());

        model.addAttribute("userForm", user);
        model.addAttribute("roles", roles);
        model.addAttribute("statuses", statuses);

        return "editUser";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error! " + bindingResult.getFieldError());
            model.addAttribute("userForm", user);
            model.addAttribute("roles", List.of(Role.values()));
            model.addAttribute("statuses", List.of(Status.values()));
            return "editUser";
        }

        userService.editUser(user);
        logger.info("Edit user with email {}", user.getEmail());
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        logger.warn("Delete user with id {} from database", id);
        return "redirect:/admin";
    }
}
