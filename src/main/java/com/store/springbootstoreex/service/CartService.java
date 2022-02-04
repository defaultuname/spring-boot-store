package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Cart;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.exception.CartNotFoundException;
import com.store.springbootstoreex.exception.UserNotFoundException;
import com.store.springbootstoreex.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    @Autowired
    public CartService(CartRepository cartRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new CartNotFoundException(id));
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Cart getCartByUserId(Long id) {
        return cartRepository.findCartByUserId(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public BigDecimal getTotalPriceCart() { // Получение итоговой цены корзины реализовано силами Stream API
        return getLoggedUserCart().getProducts().stream()
                .map(Product::getPrice)                    // Получаем сведения о цене каждого товара
                .reduce(BigDecimal.ZERO, BigDecimal::add); // и суммируем их, отталкиваясь от цены пустой корзины - нуля
    }

    public Cart getLoggedUserCart() {
        return getCartByUserId(userService.getLoggedUser().getId());
    }

    public void addProductToCart(Product product) {
        getLoggedUserCart().getProducts().add(product); // Получаем список товаров из корзины пользователя, добавляем туда товар
        cartRepository.save(getLoggedUserCart()); // и сохраняем изменённую информацию
    }

    public void deleteProductFromCart(Product product) {
        getLoggedUserCart().getProducts().remove(product); // Получаем список товаров из корзины пользователя, удаляем оттуда товар
        cartRepository.save(getLoggedUserCart()); // и сохраняем изменённую информацию
    }
}
