package com.defuname.springbootstoreex.repository;

import com.defuname.springbootstoreex.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p JOIN Category c ON (p.category.id = c.id) " + // Запрос для поиска товаров по категории и наименованию:
            "WHERE UPPER(p.title) LIKE UPPER(CONCAT('%', :param1, '%')) " + // если оба поля не заполнены, выводятся все товары, если заполнено одно -
            "AND (:param2 LIKE '' OR c.categoryName = :param2)")
        // поиск по одному (конкретно если поле title пустое, SQL-запрос ищет по LIKE ''), если оба - по обоим
    List<Product> findProductsByCategoryNameAndContainsTitle(@Param("param2") String catName, @Param("param1") String title);

    Page<Product> findAll(Pageable pageable);
}