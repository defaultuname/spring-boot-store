package com.defuname.springbootstoreex.service;

import com.defuname.springbootstoreex.domain.Category;
import com.defuname.springbootstoreex.exception.CategoryIsNotEmptyException;
import com.defuname.springbootstoreex.exception.CategoryNotFoundException;
import com.defuname.springbootstoreex.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteById(Long id) {
        if (!getCategoryById(id).getProducts().isEmpty())
            throw new CategoryIsNotEmptyException(id); // Категория не может быть удалена, если в ней есть товары
        categoryRepository.deleteById(id);
    }
}
