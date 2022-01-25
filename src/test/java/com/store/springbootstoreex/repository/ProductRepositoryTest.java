package com.store.springbootstoreex.repository;

import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void creating_product_test() {
        Product product = new Product("LG", "test", new Category("smart"), new BigDecimal(1000), 0);
        productRepository.save(product);
    }

    @Test
    void find_product_by_id() {
        Product product = new Product("LG", "test", new Category("smart"), new BigDecimal(1000), 0);
        productRepository.save(product);
        assertTrue(productRepository.findById(1L).isPresent());
        assertFalse(productRepository.findById(999L).isPresent());
    }


    @Test
    void find_product_by_title_and_category() {
        Category category = new Category("smart");
        Product product = new Product("LG", "test", category, new BigDecimal(1000), 0);

        categoryRepository.save(category);
        productRepository.save(product);

        assertFalse(productRepository.findProductsByCategoryNameAndContainsTitle(category.getCategoryName(), "LG").isEmpty());
        assertFalse(productRepository.findProductsByCategoryNameAndContainsTitle(category.getCategoryName(), "lg").isEmpty());
    }
}