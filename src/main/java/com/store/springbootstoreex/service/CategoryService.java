package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.exception.CategoryNotFoundException;
import com.store.springbootstoreex.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Category getCategoryById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public List<Category> getAllCategories() {
        return repository.findAll();
    }

//    public Optional<Category> getCategoryByName(String name) {
//        return repository.findByCategoryName(name);
//    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
