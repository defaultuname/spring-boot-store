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

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void saveComment(Review review) {
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
