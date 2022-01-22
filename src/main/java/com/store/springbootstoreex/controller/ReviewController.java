package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Review;
import com.store.springbootstoreex.service.ReviewService;
import com.store.springbootstoreex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/comments")
@PreAuthorize("hasAuthority('USER')")
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @PostMapping("/new")
    public String addComment(@Valid @ModelAttribute Review review) {
        review.setAuthor(userService.getLoggedUser());
        reviewService.saveComment(review);
        return "redirect:/index";
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        reviewService.deleteCommentById(id);
        return "redirect:/index";
    }
}
