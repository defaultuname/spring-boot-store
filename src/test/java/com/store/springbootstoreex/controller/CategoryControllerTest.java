package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = CategoryController.class)
@MockBean(UserDetailsService.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void createCategory() throws Exception {
        this.mockMvc.perform(get("/category/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("createCategory"));

//        this.mockMvc.perform(post("/category/new"))
//                .
    }

    @Test
    void testCreateCategory() {
    }

    @Test
    void editCategory() {
    }

    @Test
    void testEditCategory() {
    }

    @Test
    void deleteCategory() {
    }
}