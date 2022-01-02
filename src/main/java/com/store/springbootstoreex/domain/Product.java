package com.store.springbootstoreex.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    private Long id;

    private String title;

    private Category category;

    private BigDecimal price;


}
