package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import com.store.springbootstoreex.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@MockBean(UserDetailsService.class)
@WithMockUser(authorities = "ADMIN") // Контроллер доступен только для админов, поэтому мокаем юзера с правами admin
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ReviewService reviewService;

    private final Long id = 1L;

    @Test
    void createProduct() throws Exception {
        Product product = new Product("LG", "test", new Category("smart"), new BigDecimal(1000), 0);
        // Тест GET-запроса
        this.mockMvc.perform(get("/products/new"))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("categoryList", categoryService.getAllCategories()),
                        view().name("createProd")
                );
        // Тесты POST-запросов
        this.mockMvc.perform(post("/products/new")
                        .flashAttr("product", product))
                .andExpect(view().name("redirect:/admin"));

        verify(productService, times(1)).saveProduct(product);

        product.setPrice(BigDecimal.valueOf(-1)); // Ставим товару некорректные данные (например, цена < 0)
        this.mockMvc.perform(post("/products/new")
                        .flashAttr("product", product))
                .andExpect(model().hasErrors()); // И получаем ошибку в Binding Result

    }

    @Test
    void editProduct() throws Exception {
        Product product = new Product("LG", "test", new Category("smart"), new BigDecimal(1000), 0);
        when(productService.getProductById(id)).thenReturn(product);
        // Тест GET-запроса
        this.mockMvc.perform(get("/products/edit/1"))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("categoryList", categoryService.getAllCategories()),
                        model().attribute("productForm", product),
                        view().name("editProd")
                );

        verify(productService, times(1)).getProductById(id);
        // Тесты POST-запросов
        this.mockMvc.perform(post("/products/edit/1")
                        .flashAttr("product", product))
                .andExpect(view().name("redirect:/admin"));

        verify(productService, times(1)).saveProduct(product);

        product.setTitle(""); // Ставим товару некорректные данные (например, пустое имя)
        this.mockMvc.perform(post("/products/edit/1")
                        .flashAttr("product", product))
                .andExpect(model().hasErrors()); // И получаем ошибку в Binding Result
    }

    @Test
    void deleteProduct() throws Exception {
        this.mockMvc.perform(post("/products/delete/1"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/admin")
                );

        verify(productService, times(1)).deleteProductById(id);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getOneProduct() throws Exception {
        Product product = new Product("LG", "test", new Category("smart"), new BigDecimal(1000), 0);
        when(productService.getProductById(id)).thenReturn(product);

        this.mockMvc.perform(get("/products/1"))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("product", product),
                        model().attribute("reviews", reviewService.getReviewsByProductId(id)),
                        view().name("product")
                );

        verify(productService, times(1)).getProductById(id);
    }
}