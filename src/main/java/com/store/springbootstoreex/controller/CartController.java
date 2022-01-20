package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Cart;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.service.CartService;
import com.store.springbootstoreex.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/cart")
@PreAuthorize("hasAuthority('USER')")
public class CartController {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public CartController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping
    public String cartView(Model model) {
        Cart cart = cartService.getLoggedUserCart();
        BigDecimal totalPrice = cartService.getTotalPriceCart();

        model.addAttribute("cart", cart.getProducts());
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

    @PostMapping("/{id}")
    public String addProdToCart(@PathVariable("id") Long id) {
        Cart cart = cartService.getLoggedUserCart();
        Product product = productService.getProductById(id);

        cart.addProductToCart(product);
        cartService.saveCart(cart);
        return "redirect:/index";
    }
}