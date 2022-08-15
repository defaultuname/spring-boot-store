package com.defuname.springbootstoreex.controller;

import com.defuname.springbootstoreex.domain.Category;
import com.defuname.springbootstoreex.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@MockBean(UserDetailsService.class)
@WithMockUser(authorities = "ADMIN") // Контроллер доступен только для админов, поэтому мокаем юзера с правами admin
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private final Long id = 1L;

    @Test
    void createCategory() throws Exception {
        Category category = new Category("smart");
        // Тест GET-запроса
        this.mockMvc.perform(get("/category/new"))
                .andExpectAll(
                        status().isOk(),
                        view().name("createCategory")
                );
        // Тесты POST-запросов
        this.mockMvc.perform(post("/category/new")
                        .flashAttr("category", category))
                .andExpect(view().name("redirect:/admin"));

        verify(categoryService, times(1)).saveCategory(category);

        category.setCategoryName(null); // Ставим наименование категории на некорректное (т.е. на null)
        this.mockMvc.perform(post("/category/new")
                        .flashAttr("category", category))
                .andExpect(model().hasErrors()); // И получаем ошибку в Binding Result
    }

    @Test
    void editCategory() throws Exception {
        Category category = new Category("smart");
        when(categoryService.getCategoryById(id)).thenReturn(category);
        // Тест GET-запроса
        this.mockMvc.perform(get("/category/edit/1"))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("categoryForm", category),
                        view().name("editCategory")
                );

        verify(categoryService, times(1)).getCategoryById(id);
        // Тесты POST-запросов
        this.mockMvc.perform(post("/category/edit/1")
                        .flashAttr("category", category))
                .andExpect(view().name("redirect:/admin"));

        verify(categoryService, times(1)).saveCategory(category);

        category.setCategoryName(null); // Ставим наименование категории на некорректное (т.е. на null)
        this.mockMvc.perform(post("/category/edit/1")
                        .flashAttr("category", category))
                .andExpect(model().hasErrors()); // И получаем ошибку в Binding Result
    }

    @Test
    void deleteCategory() throws Exception {
        this.mockMvc.perform(post("/category/delete/1"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/admin")
                );

        verify(categoryService, times(1)).deleteById(id);
    }
}