package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @Autowired
    public ProductController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/new")
    public String createProduct(Model model) {
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        return "createProd";
    }

    @PostMapping("/new")
    public String createProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productForm", product);
        return "editProd";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin";
    }
}
