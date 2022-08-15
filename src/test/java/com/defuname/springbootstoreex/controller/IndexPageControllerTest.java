package com.defuname.springbootstoreex.controller;

import com.defuname.springbootstoreex.domain.Product;
import com.defuname.springbootstoreex.service.CategoryService;
import com.defuname.springbootstoreex.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = IndexPageController.class)
@MockBean(UserDetailsService.class)
@WithMockUser(authorities = "USER")
class IndexPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @Test
    void homepage() throws Exception {
        int page = 1, pageSize = 3;
        when(productService.getPaginatedProducts(page, pageSize)).thenReturn(Page.empty()); // Mock страницы (Page)

        this.mockMvc.perform(get("/"))
                .andExpectAll(
                        status().isOk(),
                        view().name("index")
                );

        //verify(v, times(1)).getAllByCategoryAndTitle(category, title);
    }

    @Test
    void homepageSearchByTitle() throws Exception {
        String title = "title", category = "category";

        this.mockMvc.perform(get("/search")
                        .param("category", category)
                        .param("title", title))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("productListSearched", List.of()),
                        view().name("search")
                );

        verify(productService, times(1)).getAllByCategoryAndTitle(category, title);
    }

    @Test
    void viewPaginated() throws Exception {
        int page = 1, pageSize = 3;
        when(productService.getPaginatedProducts(page, pageSize)).thenReturn(Page.empty()); // Mock страницы (Page)
        Page<Product> paginatedProducts = productService.getPaginatedProducts(page, pageSize);

        this.mockMvc.perform(get("/" + page))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("pageNo", page),
                        model().attribute("totalPages", paginatedProducts.getTotalPages()),
                        model().attribute("productPaginated", paginatedProducts.getContent()),
                        model().attribute("categoryList", categoryService.getAllCategories()),
                        view().name("index")
                );

    }
}