package com.store.springbootstoreex.controller;

import com.store.springbootstoreex.domain.Product;
import com.store.springbootstoreex.domain.Review;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReviewController.class)
@MockBean(UserDetailsService.class)
@WithMockUser(authorities = "USER")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Test
    void addComment() throws Exception {
        Review review = new Review("test", new User(), new Product(), 4);
        // Тесты POST-запросов
        this.mockMvc.perform(post("/comments/new")
                        .flashAttr("review", review))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/index"));

        verify(reviewService, times(1)).saveReview(review);

        review.setRating(-1); // Делаем отзыв некорректным (отрицательная оценка)
        this.mockMvc.perform(post("/comments/new")
                        .flashAttr("review", review))
                .andExpect(model().hasErrors()); // И получаем ошибку в Binding Result
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteComment() throws Exception {
        this.mockMvc.perform(post("/comments/delete/1"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/index")
                );

        Long id = 1L;
        verify(reviewService, times(1)).deleteReviewById(id);
    }
}