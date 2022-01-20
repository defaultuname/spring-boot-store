package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('USER')")
public class IndexPageController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public IndexPageController(ProductService productService, CategoryService categoryService) {
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

    @GetMapping("/search")
    public String homepageSearchByTitle(@RequestParam("title") String title, @RequestParam("category") String category, Model model) {
        List<Product> productList = productService.getAllByCategoryAndTitle(category.trim(), title.trim());
        model.addAttribute("productListSearched", productList);
        return "search";
    }
}
