package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.exception.ProductNotFoundException;
import com.store.springbootstoreex.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CartService cartService;

    @Autowired
    public ProductService(ProductRepository repository, CartService cartService) {
        this.repository = repository;
        this.cartService = cartService;
    }

    public void saveProduct(Product product) {
        repository.save(product);
    }

    public Product getProductById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Page<Product> getPaginatedProducts(int page, int pageSize) {
        return repository.findAll(PageRequest.of(page - 1, pageSize));
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

    public void deleteProductById(Long id) {
        cartService.getAllCarts().forEach(cart -> cart.getProducts().remove(getProductById(id))); // Перед удалением самого товара мы удаляем его из корзин всех пользователей
        repository.deleteById(id);
    }
}
