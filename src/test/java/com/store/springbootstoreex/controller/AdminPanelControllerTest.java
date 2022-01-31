package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import com.store.springbootstoreex.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminPanelController.class)
@MockBean(UserDetailsService.class)
@WithMockUser(authorities = "ADMIN") // Контроллер доступен только для админов, поэтому мокаем юзера с правами admin
class AdminPanelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserService userService;

    @Test
    void adminPageController() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("categoryList", categoryService.getAllCategories()),
                        model().attribute("productList", productService.getAllProducts()),
                        model().attribute("userList", userService.getAllUsers()),
                        view().name("admin")

                );
    }
}