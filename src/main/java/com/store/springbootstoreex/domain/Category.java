package com.store.springbootstoreex.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CATEGORY_NAME", nullable = false, length = 32)
    @Size(min = 1, max = 32, message = "name of category cant be empty or >32 characters long!")
    private String categoryName;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
//    private Set<Product> products = new HashSet<>();

    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }

//    public Set<Product> getProducts() {
//        return products;
//    }
//
//    public void setProducts(Set<Product> products) {
//        this.products = products;
//    }
}
