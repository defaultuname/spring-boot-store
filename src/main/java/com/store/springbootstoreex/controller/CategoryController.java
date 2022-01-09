package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
@PreAuthorize("hasAuthority('user:write')")
public class CategoryController {
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
    public String createCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("categoryForm", category);
        return "editCategory";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin";
    }

}
