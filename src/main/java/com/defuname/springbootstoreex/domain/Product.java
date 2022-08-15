package com.defuname.springbootstoreex.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    @Size(min = 1, max = 255, message = "title can not be empty or >255 characters long!")
    @NotNull
    private String title;

    @Column(name = "IMAGE_LOCATION", length = 384)
    @Size(max = 384, message = "title can not be empty or >384 characters long!")
    private String imageLocation; // Картинки в БД хранятся в виде ссылок из интернета

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    private Category category;

    @Column(name = "PRICE", nullable = false)
    @Min(value = 0, message = "price cant be less than 0!")
    @NotNull
    private BigDecimal price;

    @Column(name = "QUANTITY")
    @Min(value = 0, message = "quantity cant be less than 0!")
    private int quantity = 0;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private List<Cart> productInCartsList;

    public Product() {
    }

    public Product(String title, String imageLocation, Category category, BigDecimal price, int quantity) {
        this.title = title;
        this.imageLocation = imageLocation;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public void addCommentToProduct(Review review) {
        reviews.add(review);
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

    public String getCategoryName() {
        return category.getCategoryName();
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Cart> getProductInCartsList() {
        return productInCartsList;
    }

    public void setProductInCartsList(List<Cart> productInCartsList) {
        this.productInCartsList = productInCartsList;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return title.equals(product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
