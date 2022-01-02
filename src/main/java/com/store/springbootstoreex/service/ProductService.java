package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Product getByIdProduct(long id) {
        return repository.getById(id);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public List<Product> getAllProductsByCategoryId(long id) {
        return repository.findAllByCategoryId(id);
    }

    public List<Product> getAllProductsByCategoryName(String name) {
        return repository.findAllByCategory_CategoryName(name);
    }

    public void editProduct(long id, Product newProduct) {
        Product oldProduct = getByIdProduct(id);
        if (oldProduct != null) {
            oldProduct.setTitle(newProduct.getTitle());
            oldProduct.setPrice(newProduct.getPrice());
            oldProduct.setCategory(oldProduct.getCategory());
            saveProduct(oldProduct);
        }
    }

    public void deleteProductById(long id) {
        repository.deleteById(id);
    }
}
