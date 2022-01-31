package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.service.CategoryService;
import com.store.springbootstoreex.service.ProductService;
import com.store.springbootstoreex.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(ProductController.class);

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
    public String createProduct(@Valid @ModelAttribute Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error! " + bindingResult.getFieldError());
            return "createProd";
        }

        productService.saveProduct(product);
        logger.info("Save new product with name {} to database", product.getTitle());
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
    public String editProduct(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error! " + bindingResult.getFieldError());
            model.addAttribute("productForm", product);
            return "editProd";
        }

        productService.saveProduct(product);
        logger.info("Edit product with id {}", product.getId());
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        logger.info("Delete product with id {} from database", id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')") // Получение информации о товаре могут выполнить и обычные пользователи
    public String getOneProduct(@PathVariable Long id, Model model) { // Также на странице есть возможность оставлять и просматривать отзывы
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("reviews", reviewService.getReviewsByProductId(id)); // Получаем отзывы о конкретном товаре
        logger.info("Get product with id {} and it reviews from database", id);
        return "product";
    }
}
