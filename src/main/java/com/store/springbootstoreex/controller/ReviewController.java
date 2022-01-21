package com.store.springbootstoreex.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@PreAuthorize("hasAuthority('USER')")
public class ReviewController {

    @GetMapping
    public String addCommentRedirect() {
        return null;
    }

    @PostMapping
    public String addCommentToProduct() {
        return null;
    }
}
