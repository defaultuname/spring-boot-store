package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Cart;
import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.service.CartService;
import com.store.springbootstoreex.service.ProductService;
import com.store.springbootstoreex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductService productService;
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(ProductService productService, CartService cartService, UserService userService) {
        this.productService = productService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String createCart() {
        return "createCategory";
    }

    @GetMapping
    public String cartView(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUserByEmail(username);
        Cart cart = cartService.getCartByUserId(user.getId());
        model.addAttribute("cart", cart.getProducts());
        System.out.println(cart);

        return "cart";
    }


    @PostMapping("/{id}")
    public String addProdToCart(@PathVariable("id") Long id) {
        Long userId = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        Cart cart = cartService.getCartByUserId(userId);
        Product product = productService.getProductById(id);

        System.out.println("USERUSERUSER " + cart);

        List<Product> productsOfCart = cart.getProducts();
        cart.addProductToCart(product);
        cartService.saveCart(cart);

        System.out.println(productsOfCart);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        cartService.deleteCartById(id);
        return "redirect:/index";
    }


}