package com.defuname.springbootstoreex.controller;

import com.defuname.springbootstoreex.domain.Cart;
import com.defuname.springbootstoreex.domain.Product;
import com.defuname.springbootstoreex.service.CartService;
import com.defuname.springbootstoreex.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(CartController.class);
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

        logger.info("Redirect to /cart page");
        return "cart";
    }

    @PostMapping("/new/{id}")
    public String addProdToCart(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        cartService.addProductToCart(product);

        logger.info("Add product with id {} to cart", product.getId());
        return "redirect:/index";
    }

    @PostMapping("/delete/{id}")
    public String deleteProductFromCart(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        cartService.deleteProductFromCart(product);

        logger.info("Delete product with id {} from cart", product.getId());
        return "redirect:/cart";
    }
}