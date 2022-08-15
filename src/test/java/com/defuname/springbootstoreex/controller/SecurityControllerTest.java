package com.defuname.springbootstoreex.controller;

import com.defuname.springbootstoreex.domain.Role;
import com.defuname.springbootstoreex.domain.Status;
import com.defuname.springbootstoreex.domain.User;
import com.defuname.springbootstoreex.service.CategoryService;
import com.defuname.springbootstoreex.service.ProductService;
import com.defuname.springbootstoreex.service.ReviewService;
import com.defuname.springbootstoreex.service.UserService;
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
        @MockBean(CategoryService.class), @MockBean(ReviewService.class)})
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getLoginPage() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpectAll(
                        status().isOk(),
                        view().name("login")
                );

        this.mockMvc.perform(get("/login?error=true"))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("msg", "Invalid email address or password, or this account does not exist"),
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

        this.mockMvc.perform(get("/register"))
                .andExpectAll(
                        status().isOk(),
                        view().name("register")
                );

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