package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @Autowired
    public AdminPanelController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public String adminPageController(Model model) {
        List<Category> categoryList = categoryService.getAllCategories();
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
        return "admin";
    }
}
