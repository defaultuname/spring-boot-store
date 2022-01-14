package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import com.store.springbootstoreex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminPanelController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public AdminPanelController(CategoryService categoryService, ProductService productService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String adminPageController(Model model) {
        List<Category> categoryList = categoryService.getAllCategories();
        List<Product> productList = productService.getAllProducts();
        List<User> userList = userService.getAllUsers();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
        model.addAttribute("userList", userList);
        return "admin";
    }
}
