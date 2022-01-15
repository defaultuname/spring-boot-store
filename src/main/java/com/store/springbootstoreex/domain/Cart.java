package com.store.springbootstoreex.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "CART")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @ManyToMany(mappedBy = "carts")
    private List<Product> products;

    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice = BigDecimal.valueOf(0);

}
