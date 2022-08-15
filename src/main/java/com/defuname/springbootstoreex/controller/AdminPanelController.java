package com.defuname.springbootstoreex.controller;

import com.defuname.springbootstoreex.domain.Category;
import com.defuname.springbootstoreex.domain.Product;
import com.defuname.springbootstoreex.domain.User;
import com.defuname.springbootstoreex.service.CategoryService;
import com.defuname.springbootstoreex.service.ProductService;
import com.defuname.springbootstoreex.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(AdminPanelController.class);
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

        logger.info("Redirect to admin panel");
        return "admin";
    }
}
