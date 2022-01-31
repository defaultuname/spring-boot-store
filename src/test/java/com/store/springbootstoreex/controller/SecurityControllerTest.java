package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Role;
import com.store.springbootstoreex.domain.Status;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import com.store.springbootstoreex.service.ReviewService;
import com.store.springbootstoreex.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SecurityController.class)

@MockBeans({@MockBean(UserDetailsService.class), @MockBean(ProductService.class),
        @MockBean(CategoryService.class), @MockBean(ReviewService.class)}) // Mock необходимых зависимостей
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getLoginPage() throws Exception {
        this.mockMvc.perform(get("/login")) // Тест успешного логина
                .andExpectAll(
                        status().isOk(),
                        view().name("login")
                );

        this.mockMvc.perform(get("/login?error=true")) // Тест ошибочного логина
                .andExpectAll(
                        status().isOk(),
                        model().attribute("msg", "Неверный email или пароль, либо такой аккаунт не существует"),
                        view().name("login")
                );
    }

    @Test
    void logout() throws Exception {
        this.mockMvc.perform(post("/logout"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/login")
                );
    }

    @Test
    void register() throws Exception {
        User user = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);
        // Тест GET-запроса
        this.mockMvc.perform(get("/register"))
                .andExpectAll(
                        status().isOk(),
                        view().name("register")
                );
        // Тесты POST-запросов
        this.mockMvc.perform(post("/register")
                        .flashAttr("user", user))
                .andExpect(view().name("redirect:/login"));

        verify(userService, times(1)).saveUser(user);

        user.setEmail(null); // Делаем юзера невалидным
        this.mockMvc.perform(post("/register")
                        .flashAttr("user", user))
                .andExpect(model().hasErrors()); // И получаем ошибку в Binding Result
    }
}