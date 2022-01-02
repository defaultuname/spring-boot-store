package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/index", "", "/"})
    public String homepage(Model model) {
        List<Product> productList = productService.getAllProducts();
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("productList", productList);
        model.addAttribute("categoryList", categoryList);
        return "index";
    }

    @RequestMapping("/category")
    public String homepageSortedByCategory(@RequestParam("id") long categoryId, Model model) {
        List<Product> productListByCat = productService.getAllProductsByCategoryId(categoryId);
        model.addAttribute("productList", productListByCat);
        return "index";
    }
}
