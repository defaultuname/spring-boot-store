package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Cart;
import com.store.springbootstoreex.domain.Category;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.exception.ProductNotFoundException;
import com.store.springbootstoreex.repository.ProductRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void saveProduct() {
        Category category = new Category("smart");
        Product product = new Product("LG", "test", category, new BigDecimal(1000), 0);

        when(productRepository.save(any(Product.class))).thenReturn(product);
        productService.saveProduct(product);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product found = productService.getProductById(1L);

        assertThat(found).isNotNull(); // Сохраняем объект, а потом получаем его. Проверяем на null и равенство полей
        assertThat(found).isEqualTo(product);
    }

    @Test
    public void getProductById() {
        Category category = new Category("smart");
        Product product = new Product("LG", "test", category, new BigDecimal(1000), 0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.getProductById(1L);
        assertDoesNotThrow(() -> new ProductNotFoundException(1L));

        when(productRepository.findById(2L)).thenThrow(new ProductNotFoundException(2L)); // Если товар не найден, кидаем exception
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(2L));
    }

    @Test
    void getAllProducts() {
        Category category = new Category("smart");
        List<Product> products = new ArrayList<>();
        products.add(new Product("LG", "test", category, new BigDecimal(1000), 0));

        when(productRepository.findAll()).thenReturn(products);
        List<Product> found = productService.getAllProducts();
        verify(productRepository).findAll();

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0)).isEqualTo(products.get(0));
    }

    @Test
    void getPaginatedProducts() {
        Category category = new Category("smart");
        List<Product> products = new ArrayList<>();
        products.add(new Product("LG", "test", category, new BigDecimal(1000), 0));
        Page<Product> paginatedProducts = new PageImpl<>(products);

        when(productRepository.findAll(isA(Pageable.class))).thenReturn(paginatedProducts);
        Page<Product> found = productService.getPaginatedProducts(1, 1);

        assertThat(found).isNotNull();
        assertThat(found.getTotalElements()).isEqualTo(1);
        assertThat(found.getContent().get(0)).isEqualTo(products.get(0));
    }

    @Test
    void getAllByCategoryAndTitle() {
        Category category = new Category("smart");
        Product product = new Product("LG", "test", category, new BigDecimal(1000), 0);
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(productRepository.findProductsByCategoryNameAndContainsTitle("smart", "LG")).thenReturn(products);
        List<Product> found = productService.getAllByCategoryAndTitle("smart", "LG");

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0)).isEqualTo(products.get(0));
    }

    @Test
    @Disabled("В процессе")
    void deleteProductById() {
        Category category = new Category("smart");
        Product product = new Product("LG", "test", category, new BigDecimal(1000), 0);
        product.setId(1L);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Cart cart = new Cart(new User(), new LinkedList<>());
        cart.setProducts(products);

        List<Cart> carts = new LinkedList<>();
        carts.add(cart);

        product.setProductInCartsList(carts); // Теперь товар находится в чьей-то корзине

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.deleteProductById(1L);

//        assertThat(cart.getProducts()).isEmpty();
        assertThat(product.getProductInCartsList()).isEmpty();
    }
}