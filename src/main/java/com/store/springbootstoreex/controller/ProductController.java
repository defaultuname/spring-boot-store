package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import com.store.springbootstoreex.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ReviewService reviewService;

    @Autowired
    public ProductController(CategoryService categoryService, ProductService productService, ReviewService reviewService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping("/new")
    public String createProduct(Model model) {
        List<Category> categoryList = categoryService.getAllCategories(); // Получаем список всех категорий,
        model.addAttribute("categoryList", categoryList);     // чтобы присвоить одну из них будущему продукту
        return "createProd";
    }

    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createProd";
        }

        productService.saveProduct(product);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Category> categoryList = categoryService.getAllCategories(); // Получаем список всех категорий, чтобы изменить
                                                                          // её у продукта
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productForm", product);

        return "editProd";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editProd";
        }

        productService.saveProduct(product);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')") // Получение информации о продукте могут выполнить и обычные пользователи
    public String getOneProduct(@PathVariable Long id, Model model) { // Также на странице есть возможность оставлять и просматривать отзывы
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("reviews", reviewService.getCommentsByProductId(id)); // Получаем отзывы о конкретном товаре
        return "product";
    }
}
