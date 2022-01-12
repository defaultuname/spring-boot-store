package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.exception.ProductNotFoundException;
import com.store.springbootstoreex.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public void saveProduct(Product product) {
        repository.save(product);
    }

    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = repository.findById(id);
        Product product;

        if (optionalProduct.isPresent()) {
            product = optionalProduct.get();
        } else {
            throw new ProductNotFoundException(id);
        }

        return product;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

//    public List<Product> getProductsByCategoryId(long id) {
//        return repository.findAllByCategoryId(id);
//    }
//
//    public List<Product> getProductsByTitle(String name) {
//        return repository.findAllByTitleContainsIgnoreCase(name);
//    }
//
//    public List<Product> getProductsByCategoryName(String name) {
//        return repository.findAllByCategory_CategoryName(name);
//    }

    public List<Product> getAllByCategoryAndTitle(String category, String title) {
        return repository.findProductsByCategoryNameAndContainsTitle(category, title);
    }

    public void deleteProductById(long id) {
        repository.deleteById(id);
    }
}
