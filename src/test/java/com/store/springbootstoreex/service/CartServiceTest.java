package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.*;
import com.store.springbootstoreex.exception.CartNotFoundException;
import com.store.springbootstoreex.exception.UserNotFoundException;
import com.store.springbootstoreex.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    UserService userService;

    @InjectMocks
    CartService cartService;

    @Test
    void saveCart() {
        Cart cart = new Cart();

        when(cartRepository.save(cart)).thenReturn(cart);
        cartService.saveCart(cart);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        Cart found = cartService.getCartById(1L);

        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(cart);
    }

    @Test
    void getCartById() {
        Cart cart = new Cart();

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        cartService.getCartById(1L);
        assertDoesNotThrow(() -> new CartNotFoundException(1L));

        when(cartRepository.findById(2L)).thenThrow(new CartNotFoundException(2L));
        assertThrows(CartNotFoundException.class, () -> cartService.getCartById(2L));
    }

    @Test
    void getAllCarts() {
        Cart cart = new Cart();
        List<Cart> carts = new ArrayList<>();
        carts.add(cart);

        when(cartRepository.findAll()).thenReturn(carts);
        List<Cart> found = cartService.getAllCarts();
        verify(cartRepository).findAll();

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0)).isEqualTo(cart);
    }

    @Test
    void getCartByUserId() {
        Cart cart = new Cart();

        when(cartRepository.findCartByUserId(1L)).thenReturn(Optional.of(cart));
        cartService.getCartByUserId(1L);
        assertDoesNotThrow(() -> new UserNotFoundException(1L));

        when(cartRepository.findCartByUserId(2L)).thenThrow(new UserNotFoundException(2L));
        assertThrows(UserNotFoundException.class, () -> cartService.getCartByUserId(2L));
    }

    @Test
    void getTotalPriceCart() {
        User user = new User();
        user.setId(1L);

        Product product1 = new Product("LG", "test", new Category(), new BigDecimal(1000), 0);
        Product product2 = new Product("LG", "test", new Category(), new BigDecimal(1300), 0);
        Product product3 = new Product("LG", "test", new Category(), new BigDecimal(650), 0);
        List<Product> products = List.of(product1, product2, product3);
        Cart cart = new Cart(); // Тестовая корзина с товарами с разными ценами
        cart.setProducts(products);

        when(userService.getLoggedUser()).thenReturn(user); // Mock для getLoggedUser(), так как он косвенно вызывается в getTotalPriceCart()
        when(cartRepository.findCartByUserId(any())).thenReturn(Optional.of(cart));

        assertEquals(BigDecimal.valueOf(2950), cartService.getTotalPriceCart());
    }

    @Test
    void getLoggedUserCart() {
        User user = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);
        user.setId(1L);
        when(userService.getLoggedUser()).thenReturn(user);

        Cart cart = new Cart();

        when(cartRepository.findCartByUserId(1L)).thenReturn(Optional.of(cart));

        cartService.getLoggedUserCart();
        assertDoesNotThrow(() -> new CartNotFoundException(1L));
    }

    @Test
    void addProductToCart() {
        Product product = new Product("LG", "test", new Category(), new BigDecimal(1000), 0);
        Cart cart = new Cart();
        User user = new User();
        user.setId(1L);
        user.setCart(cart);

        when(userService.getLoggedUser()).thenReturn(user); // Mock для getLoggedUser(), так как он косвенно вызывается в addProductToCart()
        when(cartRepository.findCartByUserId(any())).thenReturn(Optional.of(cart));

        assertThat(user.getCart().getProducts().size()).isEqualTo(0); // Перед добавлением в корзину
        cartService.addProductToCart(product);
        assertThat(user.getCart().getProducts().size()).isEqualTo(1); // После
    }

    @Test
    void deleteProductFromCart() {
        Product product = new Product("LG", "test", new Category(), new BigDecimal(1000), 0);
        Cart cart = new Cart();
        cart.getProducts().add(product);

        User user = new User();
        user.setId(1L);
        user.setCart(cart);

        when(userService.getLoggedUser()).thenReturn(user); // Mock для getLoggedUser(), так как он косвенно вызывается в
        when(cartRepository.findCartByUserId(any())).thenReturn(Optional.of(cart));             // deleteProductFromCart()

        assertThat(user.getCart().getProducts().size()).isEqualTo(1); // Перед удалением из корзины
        cartService.deleteProductFromCart(product);
        assertThat(user.getCart().getProducts().size()).isEqualTo(0); // После
    }
}