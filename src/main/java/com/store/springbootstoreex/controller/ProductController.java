package com.store.springbootstoreex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
    @GetMapping({"/index", "", "/"})
    public String homepage() {
        return null;
    }
}
