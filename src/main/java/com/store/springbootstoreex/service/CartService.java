package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Cart;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.exception.CartNotFoundException;
import com.store.springbootstoreex.repository.CartRepository;
import com.store.springbootstoreex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Service
//@Scope(value = WebApplicationContext.SCOPE_SESSION)
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

//    public void saveProductToCart(Product product) {
//        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
//        String username = loggedInUser.getName();
//        Cart cart = getCartByUserId(userService.getUserByEmail(username).getId());
//        List<Product> productsOfCart = cart.getProducts();
//        productsOfCart.add(product);
//    }

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
        return cartRepository.findByUserId(id).orElse(cartRepository.save(new Cart()));
    }

    public Cart getCart() {
        Cart cart = new Cart();
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        userService.getUserByEmail(username).setCart(cart);
        return cart;
    }
}
