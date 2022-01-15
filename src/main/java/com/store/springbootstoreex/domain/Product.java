package com.store.springbootstoreex.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "IMAGE_LOCATION")
    private String imageLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    private Category category;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "QUANTITY")
    private int quantity = 0;

    @ManyToMany
    @JoinTable(name = "PRODUCT_CART",
    joinColumns = @JoinColumn(name = "PRODUCT_ID"),
    inverseJoinColumns = @JoinColumn(name = "CART_ID"))
    private Set<Cart> carts;

    public Product() {
    }

    public Product(String title, String imageLocation, Category category, BigDecimal price, int quantity) {
        this.title = title;
        this.imageLocation = imageLocation;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageLocation='" + imageLocation + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
