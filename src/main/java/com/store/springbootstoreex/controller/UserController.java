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

    @GetMapping("/new") // Админ может самостоятельно создавать (регистрировать) юзеров
    public String createUser(Model model) {        // Создание нового пользователя. Отличается от обычной регистрации тем,
        List<Role> roles = List.of(Role.values()); // что админ при создании может указать пользователю конкретные статус и роль
        List<Status> statuses = List.of(Status.values());

        model.addAttribute("roles", roles);       // Отправим на createUser.html список всех ролей и статусов, чтобы
        model.addAttribute("statuses", statuses); // админ смог их задать новому юзеру

        return "createUser";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error!");
            return "createUser";
        }

        logger.info("Register new user with email {} to database", user.getEmail());
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        List<Role> roles = List.of(Role.values());        // Отправим на editUser.html список всех ролей и статусов, чтобы
        List<Status> statuses = List.of(Status.values()); // админ мог при желании поменять их юзеру

        model.addAttribute("userForm", user);
        model.addAttribute("roles", roles);
        model.addAttribute("statuses", statuses);

        return "editUser";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error!");
            return "editUser";
        }

        userService.editUser(user);
        logger.info("Edit user with email {}", user.getEmail());
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        logger.warn("Delete user with id {} from database", id);
        return "redirect:/admin";
    }
}
