package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/category")
@PreAuthorize("hasAuthority('ADMIN')")
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
    public String createCategory(@Valid @ModelAttribute Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "createCategory";
        }

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
    public String editCategory(@Valid @ModelAttribute Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editCategory";
        }

        categoryService.saveCategory(category);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin";
    }
}
