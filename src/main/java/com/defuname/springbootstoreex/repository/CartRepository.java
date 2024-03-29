package com.defuname.springbootstoreex.repository;

import com.defuname.springbootstoreex.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartByUserId(Long id);
}
