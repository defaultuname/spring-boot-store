package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public void saveCategory(Category category) {
        repository.save(category);
    }

    public Category getCategoryById(long id) {
        return repository.getById(id);
    }

    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    public void updateCategory(long id, Category newCategory) {
        Category oldCategory = getCategoryById(id);
        if (oldCategory != null) {
            oldCategory.setCategoryName(newCategory.getCategoryName());
            saveCategory(oldCategory);
        }
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
