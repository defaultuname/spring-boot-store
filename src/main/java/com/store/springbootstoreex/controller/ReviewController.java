package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Review;
import com.store.springbootstoreex.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/comments")
@PreAuthorize("hasAuthority('USER')")
public class ReviewController {
    private final static Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/new")
    public String addComment(@Valid @ModelAttribute Review review, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Binding result has error! " + bindingResult.getFieldError());
            model.addAttribute("msg", "An error occurred while adding a review");
            return "error/500";
        }

        reviewService.saveReview(review);
        logger.info("Save new review to product {} to database", review.getProduct());
        return "redirect:/index";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        reviewService.deleteReviewById(id);
        logger.info("Delete review with id {} from database", id);
        return "redirect:/index";
    }
}
