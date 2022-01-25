package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Review;
import com.store.springbootstoreex.exception.CommentNotFoundException;
import com.store.springbootstoreex.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }

    public void saveComment(Review review) {
        review.setAuthor(userService.getLoggedUser()); // Чтобы указать автора отзыва, получим его из сессии
        reviewRepository.save(review);
    }

    public Review getCommentById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
    }

    public List<Review> getCommentsByProductId(Long id) {
        return reviewRepository.findAllByProductId(id);
    }

    public List<Review> getAllComments() {
        return reviewRepository.findAll();
    }

    public void deleteCommentById(Long id) {
        reviewRepository.deleteById(id);
    }
}
