package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Cart;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.exception.CartNotFoundException;
import com.store.springbootstoreex.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public void deleteCartById(Long id) {
        cartRepository.deleteById(id);
    }

    public Cart getCartByUserId(Long id) {
        return cartRepository.findCartByUserId(id).orElseThrow(() -> new UsernameNotFoundException("User with id" + id + "not found"));
    }

    public BigDecimal getTotalPriceCart() {
        return getLoggedUserCart().getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Cart getLoggedUserCart() {
        return getCartByUserId(userService.getLoggedUser().getId()
        );
    }
}
