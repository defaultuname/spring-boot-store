package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/*
    Контроллер отвечает за управление категориями товаров
 */
@Controller
@RequestMapping("/category")
@PreAuthorize("hasAuthority('ADMIN')")
public class CategoryController {
    private final static Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/new")
    public String createCategory() {
        return "createCategory";
    }

    @PostMapping("/new")
    public String createCategory(@Valid @ModelAttribute Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error!");
            return "createCategory";
        }

        categoryService.saveCategory(category);
        logger.info("Save new category with name {} to database", category.getCategoryName());
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("categoryForm", category);
        return "editCategory";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@Valid @ModelAttribute Category category, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error!");
            model.addAttribute("categoryForm", category);
            return "editCategory";
        }

        categoryService.saveCategory(category);
        logger.info("Edit category with id {}", category.getId());
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        logger.info("Delete category with id {} from database", id);
        return "redirect:/admin";
    }
}
