package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Role;
import com.store.springbootstoreex.domain.Status;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('user:write')")
public class UserController {

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
    public String createUser(@ModelAttribute User user) {
        userService.saveUser(user);
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
    public String editUser(@ModelAttribute User user) {
        user.setPassword(userService.getUserById(user.getId()).getPassword());
        userService.editUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
