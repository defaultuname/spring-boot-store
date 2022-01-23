package com.store.springbootstoreex.repository;

import com.store.springbootstoreex.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findAllByTitleContainsIgnoreCase(String name);
//
//    List<Product> findAllByCategoryId(Long id);
//
//    List<Product> findAllByCategory_CategoryName(String name);

    @Query("SELECT p FROM Product p JOIN Category c ON (p.category.id = c.id) " +
            "WHERE UPPER(p.title) LIKE UPPER(CONCAT('%', :param1, '%')) " +
            "AND (:param2 like '' or c.categoryName = :param2)")
    List<Product> findProductsByCategoryNameAndContainsTitle(@Param("param2") String catName, @Param("param1") String title);

    Page<Product> findAll(Pageable pageable);
}