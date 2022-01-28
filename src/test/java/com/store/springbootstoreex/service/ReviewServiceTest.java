package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.*;
import com.store.springbootstoreex.exception.ReviewNotFoundException;
import com.store.springbootstoreex.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void saveReview() {
        User user = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);
        Review review = new Review("test", user, new Product(), 4);

        when(reviewRepository.save(review)).thenReturn(review);
        when(userService.getLoggedUser()).thenReturn(user); // Так как в сервисе нужно получить авторизованного юзера, замокаем
        reviewService.saveReview(review);                                                 // метод userService.getLoggedUser()

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        Review found = reviewService.getReviewById(1L);

        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(review);
    }

    @Test
    void getReviewById() {
        Review review = new Review("test", new User(), new Product(), 4);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        reviewService.getReviewById(1L);
        assertDoesNotThrow(() -> new ReviewNotFoundException(1L));

        when(reviewRepository.findById(2L)).thenThrow(new ReviewNotFoundException(2L));
        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReviewById(2L));
    }

    @Test
    void getReviewByProductId() {
        Review review = new Review("test", new User(), new Product(), 4);

        when(reviewRepository.findAllByProductId(1L)).thenReturn(List.of(review));
        List<Review> found = reviewService.getReviewsByProductId(1L);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0)).isEqualTo(review);
    }

    @Test
    void getAllReviews() {
        Review review = new Review("test", new User(), new Product(), 4);
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);

        when(reviewRepository.findAll()).thenReturn(reviews);
        List<Review> found = reviewService.getAllReview();
        verify(reviewRepository).findAll();

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0)).isEqualTo(review);
    }

    @Test
    void deleteReviewById() {
        reviewService.deleteReviewById(1L);
        verify(reviewRepository).deleteById(1L);

        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReviewById(1L));
    }
}