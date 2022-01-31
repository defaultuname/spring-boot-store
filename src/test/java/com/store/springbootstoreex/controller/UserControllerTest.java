package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Role;
import com.store.springbootstoreex.domain.Status;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@MockBean(UserDetailsService.class)
@WithMockUser(authorities = "ADMIN") // Контроллер доступен только для админов, поэтому мокаем юзера с правами admin
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final Long id = 1L;

    @Test
    void createUser() throws Exception {
        User user = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);
        // Тесты GET-запроса
        this.mockMvc.perform(get("/users/new"))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("roles", List.of(Role.values())),
                        model().attribute("statuses", List.of(Status.values())),
                        view().name("createUser")
                );
        // Тесты POST-запросов
        this.mockMvc.perform(post("/users/new")
                        .flashAttr("user", user))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/admin"));

        verify(userService, times(1)).saveUser(user);

        user.setFirstname(""); // Делаем юзера некорректным (например, пустое имя)
        this.mockMvc.perform(post("/users/new")
                        .flashAttr("user", user))
                .andExpect(model().hasErrors()); // И получаем ошибку в Binding Result
    }

    @Test
    void editUser() throws Exception {
        User user = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);
        when(userService.getUserById(id)).thenReturn(user);
        // Тест GET-запроса
        this.mockMvc.perform(get("/users/edit/1"))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("userForm", user),
                        model().attribute("roles", List.of(Role.values())),
                        model().attribute("statuses", List.of(Status.values())),
                        view().name("editUser")
                );

        verify(userService, times(1)).getUserById(id);
        // Тесты POST-запросов
        this.mockMvc.perform(post("/users/edit/1")
                        .flashAttr("user", user))
                .andExpect(view().name("redirect:/admin"));

        verify(userService, times(1)).editUser(user);

        user.setLastname(""); // Делаем юзера некорректным (например, пустая фамилия)
        this.mockMvc.perform(post("/users/edit/1")
                        .flashAttr("user", user))
                .andExpect(model().hasErrors()); // И получаем ошибку в Binding Result
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc.perform(post("/users/delete/1"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/admin")
                );

        verify(userService, times(1)).deleteUserById(id);
    }
}